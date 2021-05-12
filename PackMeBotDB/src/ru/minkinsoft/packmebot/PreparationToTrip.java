package ru.minkinsoft.packmebot;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import ru.minkinsoft.packmebot.db.DatabaseFacade;


public class PreparationToTrip {

    //Переменные класса
    private List<Thing> selectedThingsList = new ArrayList<>();     //Список выбранных вещей
    private List<Thing> tookThingsList = new ArrayList<>();         //Список взятых вещей
    //Пока компаратор отдельный, на случай если понадобится делать разные
    private Comparator<Thing> categoryComparator = new Comparator<>() {   //TODO: Здесь или отдельный файл?
        @Override
        public int compare(Thing o1, Thing o2) {
            return o1.getCategoryThing().compareTo(o2.getCategoryThing());
        }
    };

    public Stage stage;                                             //Этапы работы с ботом
    private StringBuilder requestString = new StringBuilder();      //Строка запроса направления
    private List<String> nextList;                                  //Список для последующего выбора
    private TripsData tripsData;
    private DatabaseFacade databaseFacade;
    private UserTrip userTrip;
    private Map<String, Command> commandList = new HashMap<>();         //Список управляющих команд
    
    private String answerErrorDB =  "Есть проблема :( Не удалось получить данные. " +
									"\nРаботаю над исправлением. Приходи через минутку";

    //Конструктор класса
    public PreparationToTrip(Integer userID) {
        this.nextList = new ArrayList<>();
        this.stage = Stage.DEFAULT_ANSWER;
//        String tripHistoryPath = "C:\\Java\\Progwards\\PackMe\\src\\TripHistory.txt";
        fillCommandList();
//        checkConnectDataFile(tripHistoryPath);
        checkConnectDB();
        userTrip = new UserTrip(userID);
    }

    //Этапы (стадии) сборов
    public enum Stage {
        DEFAULT_ANSWER,
        CHOOSE_DEFAULT_DIRECTION,   //Стадия выбора первоначального направления
        CHOOSE_CORRECTION,          //Стадия выбора уточнения по направлению
        CHOOSE_THINGS,              //Стадия выбора вещей для направления
        PACK_CONTROL,               //Стадия контроля сбора вещей
        ERROR_DB                    //Ошибка работы с базой данных TODO: нужна ли?
    }

    //Метод для проверки соединения с базой данных
    private void checkConnectDB() {
        try {
            databaseFacade= new DatabaseFacade();
        } catch (SQLException exception) {
            stage = Stage.ERROR_DB;
        }
    }
    
    //Метод для проверки соединения с файлом данных
    private void checkConnectDataFile(String tripHistoryPath) {
        try {
            tripsData = new TripsData(tripHistoryPath);
        } catch (FileNotFoundException exception) {
            stage = Stage.ERROR_DB;
        }
    }

    private void fillCommandList() {
        commandList.put("/start",new Command(this::doStart));
        commandList.put("/stop",new Command(this::doStop));
        commandList.put("/new",new Command(this::createNewDirection));
        commandList.put("/help",new Command(this::doHelp));
        commandList.put("/things",new Command(this::goToChooseThings));
        commandList.put("/list",new Command(this::showSelectedThingsList));
        commandList.put("/pack",new Command(this::goToPack));
        commandList.put("/menu",new Command(this::showMenu));
    }

    //Метод для организации взаимодействия с классом бота
    public String getBotAnswer(String text) {
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
    }

    private String getAnswerFromStage(String text) {
        switch (stage) {
            case CHOOSE_DEFAULT_DIRECTION -> {
                return doChooseDirectionStage(text);
            }
            case CHOOSE_CORRECTION -> {
                return doChooseCorrectionStage(text);
            }
            case CHOOSE_THINGS -> {
                return checkMenuSymbol(text);
            }
            case PACK_CONTROL -> {
                return control(text);
            }
            case ERROR_DB -> {
                return "Прошу простить, но что-то не так с базой данных :(" +
                		"\nПопробуй еще раз через некоторое время.";
            }
            case DEFAULT_ANSWER -> {return "Что-то пошло не так." +
                                    "\nПопробуй воспользоваться командами.\n" + doHelp();}
        }
//        }
        return "Не понял тебя. Попробуй еще раз или используй команды:" +
                doHelp();
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
        if(selectedThingsList != null && requestString.length() > 0){
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
            userTrip.setUserTripThings(selectedThingsList);
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
        	nextList = getDirectionList();
        	stage = Stage.CHOOSE_DEFAULT_DIRECTION;
	        return "Привет! Куда собираешься? Предлагаю популярные варианты:\n" +
	                getStringFromList(nextList) +
	                "\nЕсли варианты не подходят, можешь ввести свой," +
	                "написав \"+Куда\" (Например, \"+ К бабушке\")";
        }catch(SQLException exc) {
        	return answerErrorDB;
        }catch(NullPointerException exc) {
        	return answerErrorDB;
        }
    }

    //Сброс всех данных по поездке
    private void toStart() {
        stage = Stage.DEFAULT_ANSWER;
        requestString = new StringBuilder();    //TODO: или удалять символы???
        nextList.clear();
        selectedThingsList.clear();
        tookThingsList.clear();
        userTrip = new UserTrip();
    }

    private String doChooseDirectionStage(String text) {	//TODO: Разделить!
        if (nextList.contains(text)) {
        	try {
	            nextList = getCorrectionList(text);
	            stage = Stage.CHOOSE_CORRECTION;
	            requestString.append(text).append("/");
	            userTrip.setDirection(text);
	            return "Давай уточним. Предлагаю варианты:\n" + getStringFromList(nextList) +
	                    "\nНо можешь ввести свой.";
        	}catch(SQLException exc) {
            	return answerErrorDB;
            }catch(NullPointerException exc) {
            	return answerErrorDB;
            }
        } else {
            if (!text.isBlank()) {
                StringBuilder addDirection = new StringBuilder(text);
                if (addDirection.charAt(0) == "+".charAt(0)) {        //TODO: Такой вариант сравнения???
                    addDirection.deleteCharAt(0);
                    String direction = addDirection.toString().trim();
                    if (!nextList.contains(direction)) {
                        requestString.append(direction).append("/");
                        stage = Stage.CHOOSE_CORRECTION;
                        userTrip.setDirection(direction);
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
            requestString.append(text);
            userTrip.setCorrection(text);
        } else {
            requestString.append("-");
            userTrip.setCorrection("-");
        }
        //getSelectedThingsList(requestString.toString().toLowerCase());
        try{
        	getSelectedThingsList(userTrip);
	        nextList.clear();
	        return "Отлично! Давай перейдем к вещам. Вот мой совет:\n" +
	                getStringFromList(selectedThingsList) + "\n" +
	                showMenu();
        }catch(SQLException exc) {
        	return answerErrorDB;			// + exc.getMessage();
        }catch(NullPointerException exc) {
        	return answerErrorDB;			// + exc.getMessage();
        }
    }

    //Метод для контроля за сбором
    private String control(String text) {
        if (!text.isBlank()) {
            for (Thing thing : selectedThingsList) {
                if (thing.getNameThing().trim().equalsIgnoreCase(text.trim())) {
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
            toStart();
            try {
                tripsData.writeTrip(userTrip);
            } catch (IOException exc) {
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
        String menuSymbol = String.valueOf(text.charAt(0));     //TODO: использовать equals или ==
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
        StringTokenizer tokenizer = new StringTokenizer(str.toString().trim(), "(");
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
                selectedThingsList.add(new Thing(textParts.get(0).trim(), "без категории"));
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
                if (thing.getNameThing().trim().equalsIgnoreCase(deleteThingName)) {
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

    //Множество вещей соответствующих запросу
    private void getSelectedThingsList(String requestTrip) {
        List<Thing> thingsList = tripsData.getThingsList();
        selectedThingsList.clear();
        for (Thing thing : thingsList) {
            if (thing.tagsMap.containsKey(requestTrip)) {
                selectedThingsList.add(thing);
            }
        }
        selectedThingsList.sort(new Comparator<Thing>() {   //Сортировка по количеству использований в поездках
            @Override
            public int compare(Thing o1, Thing o2) {
                if (!o1.getCategoryThing().equals(o2.getCategoryThing())) {
                    return o1.getCategoryThing().compareTo(o2.getCategoryThing());
                } else {
                    return o2.tagsMap.get(requestTrip).compareTo(o1.tagsMap.get(requestTrip));
                }
            }
        });
    }

    //Множество вещей соответствующих запросу
    private void getSelectedThingsList(UserTrip userTrip) throws SQLException {
//        List<Thing> thingsList = databaseFacade.getThingsList(userTrip.getDirection(), 
//        													userTrip.getCorrection());
        selectedThingsList.clear();
        selectedThingsList = databaseFacade.getThingsList(userTrip.getDirection(), 
															userTrip.getCorrection());
        
//        for (Thing thing : thingsList) {
//            if (thing.tagsMap.containsKey(requestTrip)) {
//                selectedThingsList.add(thing);
//            }
//        }
        selectedThingsList.sort(new Comparator<Thing>() {   //Сортировка по количеству использований в поездках
            @Override
            public int compare(Thing o1, Thing o2) {
                if (!o1.getCategoryThing().equals(o2.getCategoryThing())) {
                    return o1.getCategoryThing().compareTo(o2.getCategoryThing());
                } else {
//                    return o2.tagsMap.get(requestTrip).compareTo(o1.tagsMap.get(requestTrip));
                    return Integer.compare(o2.usesCount, o1.usesCount);
                }
            }
        });
    }
    
    
    
    //Перечень начальных вариантов поездок
    //public для использования в классе Test
    public List<String> getDirectionList() throws SQLException, NullPointerException {
        List<String> directionList = new ArrayList<>();
//        List<Trip> tripList = new ArrayList<>(tripsData.getFrequentTripsList(null, 3));
//        List<Trip> tripList = new ArrayList<>(databaseFacade.getFrequentTripsList(null, 3));
        List<Trip> tripList = new ArrayList<>(databaseFacade.getFrequentDirection(3));
        tripList.forEach(dt -> {if (!directionList.contains(dt.getDirection())) {
                				directionList.add(dt.getDirection());
            					}
        				});
        return directionList;
    }

    //Формирование списка уточнений. Зависит от выбранной поездки
    //public для использования в классе Test
    public List<String> getCorrectionList(String direction) throws SQLException, NullPointerException {
        List<String> correctionList = new ArrayList<>();
//        List<Trip> tripList = new ArrayList<>(tripsData.getFrequentTripsList(direction, 5));
        List<Trip> tripList = new ArrayList<>(databaseFacade.getFrequentCorrection(direction, 3));
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

    //Метод для автотеста
    public List<Thing> readSelectedThingsList() {
        return selectedThingsList;
    }


    //Класс для обработки команд
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



