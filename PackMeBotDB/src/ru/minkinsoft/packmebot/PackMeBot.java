package ru.minkinsoft.packmebot;

//import org.telegram.telegrambots.ApiContextInitializer;
import ru.progwards.java1.telegrambot.ProgwardsTelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import java.util.*;

public class PackMeBot extends ProgwardsTelegramBot {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		PackMeBot bot = new PackMeBot();
		bot.username = "Pack_Me_bot";
		bot.token = "1611064894:AAHe1K2-x6p_40WzBpeCCpRsKZqLz1WOFnc";
		// TODO: Считывать данные бота из файла для безопасности учетных данных бота

            bot.start();
//		bot.test();
	}

	// переменные класса
	Map<Integer, PreparationToTrip> users = new HashMap<>();

	@Override
	public String processMessage(Integer userid, String text) {
		if (!users.containsKey(userid)) {
			users.put(userid, new PreparationToTrip());
		}
		return users.get(userid).getBotAnswer(text.trim());
		// TODO: Добавить удаление пользователей? Ввести enum.END, проверять и удалять
	}

	void test() {
		Scanner in = new Scanner(System.in);
		String input;
		TUser user = new TUser();
		TestClass testClass = new TestClass();
		
//		  System.out.println("User: " + testClass.printHello());
//		  System.out.println(processMessage(user.userID, testClass.printHello()));
//		  
//		  // if(packMeToTrip.stage != PreparationToTrip.Stage.ERROR) {
//		  if(users.get(user.userID).stage != PreparationToTrip.Stage.ERROR_DB) { input =
//		  testClass.printDefDirection(); System.out.println("User: " + input);
//		  System.out.println(processMessage(user.userID, input));
//		  
//		  input = testClass.printCorrection(input); System.out.println("User: " +
//		  input); System.out.println(processMessage(user.userID, input));
//		  
//		  input = testClass.printOk(); System.out.println("User: " + input);
//		  System.out.println(processMessage(user.userID, input)); //
//		  System.out.println(processMessage(user.userID, "Стоп"));
//		  
//		  while (!users.get(user.userID).readSelectedThingsList().isEmpty()) { input =
//		  testClass.printThing(users.get(user.userID).readSelectedThingsList());
//		  System.out.println("\nUser: " + input);
//		  System.out.println(processMessage(user.userID, input)); }
//		 
//
//		do {
//			input = in.nextLine();
//			System.out.println(processMessage(user.userID, input));
//		} while (!input.equals("стоп"));
//        }

		in.close();
	}

}

// Условный пользователь
class TUser {
	Integer userID;

	public TUser() {
		this.userID = new Random().nextInt();
	}
}
