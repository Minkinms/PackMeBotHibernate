package domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import datasource.DAOException;
import datasource.DBConnection;
import datasource.TripData;
import datasource.UserTripData;
import datasource.entity.Thing;
import datasource.entity.Trip;
import presentation.PackMeBot;


public class PreparationToTrip {

    //Переменные класса
	private Integer userID;											//ID пользователя из Telegram
	private Stage stage;                                            //Этапы работы с ботом
	private Map<String, Command> commandList = new HashMap<>();     //Список управляющих команд
    private List<Thing> selectedThingsList = new ArrayList<>();     //Список выбранных вещей
    private List<Thing> tookThingsList = new ArrayList<>();         //Список взятых вещей
    private List<String> nextList = new ArrayList<>();              //Список для последующего выбора
    private String selectedDirection;
    private String selectedCorrection;
	
    private String answerErrorDB =  "Есть проблема :( Не удалось получить данные. " +
									"\nРаботаю над исправлением. Приходи через минутку";
    private Comparator<Thing> categoryComparator = new Comparator<Thing>() {   
        @Override
        public int compare(Thing o1, Thing o2) {
            return o1.getCategory().compareTo(o2.getCategory());
        }
    };

    //Конструктор класса
    public PreparationToTrip(Integer userID) {
    	this.userID = userID;
        toStart();
        fillCommandList();
    }

    //Этапы (стадии) сборов
    public enum Stage {
        DEFAULT_ANSWER,
        CHOOSE_DEFAULT_DIRECTION,   //Стадия выбора первоначального направления
        CHOOSE_CORRECTION,          //Стадия выбора уточнения по направлению
        CHOOSE_THINGS,              //Стадия выбора вещей для направления
        PACK_CONTROL,               //Стадия контроля сбора вещей
        ERROR_DB                    //Ошибка работы с базой данных 
    }

    //Метод для определения команд для работы с ботом
    private void fillCommandList() {
        commandList.put("/start", new Command(this::doStart));
        commandList.put("/stop",  new Command(this::doStop));
        commandList.put("/new",   new Command(this::createNewDirection));
        commandList.put("/help",  new Command(this::doHelp));
        commandList.put("/things",new Command(this::goToChooseThings));
        commandList.put("/list",  new Command(this::showSelectedThingsList));
        commandList.put("/pack",  new Command(this::goToPack));
        commandList.put("/menu",  new Command(this::showMenu));
    }

    //Метод для организации взаимодействия с классом бота
    public String getBotAnswer(String text) throws DomainException {
        try {
			String userText = text.trim().toLowerCase();
			if (userText.startsWith("/")) {
			    if(commandList.containsKey(userText)) {
			        return commandList.get(userText).answer.getAnswer();
			    }else {
			        return "Сожалею, но такой команды нет среди возможных :(\n" +
			                commandList.get("/help").answer.getAnswer();
			    }
			}
			return getAnswerFromStage(text);
		} catch (Exception e) {
			throw new DomainException("Error in PreparationToTrip.getBotAnswer()", e);
		}
    }

	//Метод формирования ответа в зависимости от стадии
    private String getAnswerFromStage(String text) {
        switch (stage) {
            case CHOOSE_DEFAULT_DIRECTION:	return doChooseDirectionStage(text);
            case CHOOSE_CORRECTION:		return doChooseCorrectionStage(text);
            case CHOOSE_THINGS:		return checkMenuSymbol(text);
            case PACK_CONTROL:	return control(text);
//            case ERROR_DB:	return "Прошу простить, но что-то не так с базой данных :(" +
//                					"\nПопробуй еще раз через некоторое время.";
            case DEFAULT_ANSWER:	return "Что-то пошло не так." +
                                    "\nПопробуй воспользоваться командами.\n" + doHelp();
            default: return "Не понял тебя. Попробуй еще раз или используй команды:" + doHelp();
        }
    }

    private String doHelp(){
        return "Перечень доступных команд:" +
                "\n/help   - показать перечень доступных команд;" +
                "\n/start  - приветствие;" +
                "\n/new    - начать сбор в новую поездку;" +
                "\n/stop   - остановить сбор;" +
                "\n/things - перейти к стадии выбора вещей (для стадии упаковывания);" +
                "\n/list   - показать список вещей (для стаий выбора вещей и упаковывания)" +
                "\n/pack   - перейти к стадии упаковывания (для стадии выбора вещей);" +
                "\n/menu   - список действий для корректировки списка вещей (для стадии выбора вещей).";
    }

    private String doStart(){
        toStart();
        return "Привет!\nЯ хочу помочь тебе собраться в поездку, " +
                "подобрать нужные вещи и не забыть их сложить.\n" +
                "Чтобы начать новый сбор введи /new, посмотреть все команды можно с помощью /help.";
    }

    private String doStop(){
        toStart();
        return "Подготовка к этой поездке завершена.\n" +
                "Чтобы начать новый сбор введи /new, посмотреть все команды можно с помощью /help.";
    }

    private String goToChooseThings(){
        if(stage == Stage.PACK_CONTROL || stage == Stage.CHOOSE_THINGS) {
            stage = Stage.CHOOSE_THINGS;
            return "Вы вернулись к дополнению списка вещей.\n" +
                    "Посмотреть список можно спомощью /list, команды для корректировки списка смотри в /menu";
        }else {
            return "Команда предназначена для перехода к стадии выбора вещей из стадии их укладывания";
        }
    }

    private String showSelectedThingsList(){
        if(selectedThingsList != null ){
            return "Осталось сложить:\n" + getStringFromList(selectedThingsList) +
                    "\nСложено:\n" + getStringFromList(tookThingsList);
        }else {
            return "Список пуст, так как еще не задано направление поездки.\n" +
                    "Начните новый сбор с помощью /new";
        }
    }

    private String goToPack(){
        if(stage == Stage.PACK_CONTROL || stage == Stage.CHOOSE_THINGS) {
            stage = Stage.PACK_CONTROL;
            return "Готово! Давай ничего не забудем.\nПиши что сложено, а я буду вычеркивать.";
        }else{
            return "Команда предназначена для перехода к стадии укладывания вещей из стадии их выбора";
        }
    }

    private String showMenu(){
        if(stage == Stage.CHOOSE_THINGS) {
            return "Список команд для корректирования списка вещей:\n" +
                    "Добавить - \"+ Название (Категория)\";\n" +
                    "Удалить - \"- Название\" или \"-номер в списке\"\n" +
                    "Если готово, переходи к укладыванию /pack.";
        }else{
            return "Команда доступна при работе со списком вещей";
        }
    }


    private String createNewDirection(){
        toStart();
        try{
        	PackMeBot.factory = DBConnection.getFactory();
        	nextList = getDirectionList();
        	stage = Stage.CHOOSE_DEFAULT_DIRECTION;
	        return "Привет! Куда собираешься? Предлагаю популярные варианты:\n" +
	                getStringFromList(nextList) +
	                "\nЕсли варианты не подходят, можешь ввести свой," +
	                "написав \"+Куда\" (Например, \"+ К бабушке\")";
        }catch(DAOException  exc) {
        	System.out.println(exc.getMessage());	// log message
        	return answerErrorDB;
        }
    }

    //Сброс всех данных по поездке
    private void toStart() {
        stage = Stage.DEFAULT_ANSWER;
        nextList.clear();
        selectedThingsList.clear();
        tookThingsList.clear();
    }

    private String doChooseDirectionStage(String text) {	
        if (nextList.contains(text)) {
        	try {
	            nextList = getCorrectionList(text);
	            stage = Stage.CHOOSE_CORRECTION;
	            selectedDirection = text;
	            return "Давай уточним. Предлагаю варианты:\n" + getStringFromList(nextList) +
	                    "\nНо можешь ввести свой.";
        	}catch(DAOException exc) {
        		System.out.println(exc.getMessage());
        		return answerErrorDB;
            }
        } else {
            if (!text.isBlank()) {
                StringBuilder addDirection = new StringBuilder(text);
                if (addDirection.charAt(0) == "+".charAt(0)) {  
                    addDirection.deleteCharAt(0);
                    String direction = addDirection.toString().trim();
                    if (!nextList.contains(direction)) {
                        stage = Stage.CHOOSE_CORRECTION;
                        selectedDirection = direction;
                        return "Давай уточним. Например: для \"Командировка\" можно написать \"Россия\"\n" +
                                "Введи уточнение для направления";
                    } else return "Такое уже есть в списке";
                }
            }
        }
        return "Не понял тебя. Попробуй еще раз";
    }

    private String doChooseCorrectionStage(String text) {
        stage = Stage.CHOOSE_THINGS;
        if (!text.isBlank()) {
        	selectedCorrection = text;
        } else {
        	selectedCorrection = "нет";
        }
        try{
        	if(selectedDirection != null && selectedCorrection != null) {
        		getSelectedThingsList();
        		nextList.clear();
		        return "Отлично! Давай перейдем к вещам. Вот мой совет:\n" +
		                getStringFromList(selectedThingsList) + "\n" +
		                showMenu();
	        }else {
	        	return "Невозможно определить список вещей, т.к. не выбрано направление поездки";
	        }
        }catch(DAOException exc) {		
        	System.out.println(exc.getMessage());
        	return answerErrorDB;			
        }
    }

    //Метод для контроля за сбором
    private String control(String text) {
        if (!text.isBlank()) {
            for (Thing thing : selectedThingsList) {
                if (thing.getName().trim().equalsIgnoreCase(text.trim())) {
                    moveTing(thing);
                    if (selectedThingsList.isEmpty()) {
                        return writeUserTrip();
                    } else {
                        return "Осталось сложить:\n" + getStringFromList(selectedThingsList) +
                                "\nСложено:\n" + getStringFromList(tookThingsList);
                    }
                }
            }
            return "Не нашел такой вещи. Попробуй еще раз";
        } else
            return "Ничего не введено. Впиши название сложенной вещи";
    }

    private String writeUserTrip(){
            try {
            	new UserTripData().writeUserTrip(userID, 
            									selectedDirection, 
            									selectedCorrection, 
            									tookThingsList);
            	toStart();
            	PackMeBot.factory.close();
            } catch (DAOException exc) {
            	System.out.println(exc.getMessage());
                return "Всё собрано! Хорошей поездки!\n" +
                        "Произошла ошибка записи\n" +
                        "Чтобы начать новую, напиши /new";
            }
            return "Всё собрано! Хорошей поездки!\n" +
                    "Чтобы начать новую, напиши /new";
    }

    //Метод для перемещения вещи из списка собираемых в список собранных
    private void moveTing(Thing thing) {
        selectedThingsList.remove(thing);
        selectedThingsList.sort(categoryComparator);
        tookThingsList.add(thing);
        tookThingsList.sort(categoryComparator);
    }

    //Метод для проверки специальных знаков и изменения списка вещей
    private String checkMenuSymbol(String text) {
        String menuSymbol = String.valueOf(text.charAt(0));     
        if (menuSymbol.equals("+")) {
            if (addThing(text)) {
                return "Добавлено.\nМожно продолжить добавлять или убирать (подсказки /menu).\n" +
                        "Осталось сложить:\n" + getStringFromList(selectedThingsList) +
                        "\nСложено:\n" + getStringFromList(tookThingsList);
            }
        }
        if (menuSymbol.equals("-")) {
            if (deleteThing(text)) {
                return "Убрал.\nМожно продолжить добавлять или убирать (подсказки /menu).\n" +
                        "Осталось сложить:\n" + getStringFromList(selectedThingsList) +
                        "\nСложено:\n" + getStringFromList(tookThingsList);
            }
        }
        return "Не понял тебя.\n" +
                showMenu();
    }

    //Метод для добавления вещи в список
    private boolean addThing(String text) {				
        StringBuilder str = new StringBuilder(text);
        str.deleteCharAt(0);
        StringTokenizer tokenizer = new StringTokenizer(str.toString().trim(), "()");
        List<String> textParts = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            textParts.add(tokenizer.nextToken());
        }
        if (textParts.size() == 2) {
            selectedThingsList.add(new Thing(textParts.get(0).trim(), textParts.get(1).trim()));
            selectedThingsList.sort(categoryComparator);
            return true;
        } else {
            if (textParts.size() == 1) {
                selectedThingsList.add(new Thing(textParts.get(0).trim(), "нет"));
                selectedThingsList.sort(categoryComparator);
                return true;
            }
        }
        return false;
    }

    //Метод для удаления вещи из списка выбранных
    private boolean deleteThing(String text) {
        StringBuilder str = new StringBuilder(text.toLowerCase());
        str.deleteCharAt(0);
        String deleteThingName = str.toString().trim();
        if (Character.isDigit(deleteThingName.charAt(0))) {           //Удаление по номеру
            int index = getIndex(deleteThingName) - 1;
            if (index < selectedThingsList.size()) {
                selectedThingsList.remove(selectedThingsList.get(index));
                return true;
            }
        } else {
            for (Thing thing : selectedThingsList) {                //Удаление по имени вещи
                if (thing.getName().trim().equalsIgnoreCase(deleteThingName)) {
                    selectedThingsList.remove(thing);
                    selectedThingsList.sort(categoryComparator);
                    return true;
                }
            }
        }
        return false;
    }

    private int getIndex(String text) {
        StringBuilder result = new StringBuilder();
        char[] chars = text.toCharArray();
        for (char aChar : chars) {
            if (Character.isDigit(aChar)) {
                result.append(aChar);
            } else break;
        }
        return Integer.parseInt(result.toString());
    }

    //Список вещей соответствующих запросу
    private void getSelectedThingsList() throws DAOException {
        selectedThingsList.clear();
        selectedThingsList = new TripData().getThingsList(selectedDirection, selectedCorrection);
    }
    
    //Список начальных вариантов поездок
    private List<String> getDirectionList() throws DAOException {
        List<String> directionList = new ArrayList<>();
        
        List<Trip> tripList = new ArrayList<Trip>(
        							new TripData().getFrequentDirection(10));

        tripList.forEach(dt -> {if (!directionList.contains(dt.getDirection())) {
                				directionList.add(dt.getDirection());
            					}
        				});
        return directionList;
    }

    //Формирование списка уточнений. Зависит от выбранной поездки
    private List<String> getCorrectionList(String direction) throws DAOException {
        List<String> correctionList = new ArrayList<>();
        
        List<Trip> tripList = new ArrayList<Trip>(
        		new TripData().getFrequentCorrection(direction, 2));
        
        tripList.forEach(dt -> {if (!correctionList.contains(dt.getCorrection())) {
                				correctionList.add(dt.getCorrection());
            					}
        				});
        return correctionList;
    }

    //Метод для формирования строки вывода из списка
    private String getStringFromList(List<?> list) {
        if (list != null && list.size() > 0) {
            StringBuilder outputString = new StringBuilder("");
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    outputString.append(i + 1).append(". ").append(list.get(i));
                } else {
                    outputString.append(i + 1).append(". ").append(list.get(i)).append("\n");
                }
            }
            return outputString.toString();
        } else return "Список пуст";
    }

    
    //Класс для подготовки команд
    class Command {
        private Answer answer;

        public Command(Answer answer) {
            this.answer = answer;
        }
    }

    interface Answer {
        String getAnswer();
    }

}
