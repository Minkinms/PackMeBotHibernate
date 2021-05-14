package ru.minkinsoft.packmebot;

import ru.progwards.java1.telegrambot.ProgwardsTelegramBot;
import org.telegram.telegrambots.ApiContextInitializer;
import java.util.*;

public class PackMeBot extends ProgwardsTelegramBot {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		PackMeBot bot = new PackMeBot();
		bot.username = "Pack_Me_bot";
		bot.token = "1611064894:AAHe1K2-x6p_40WzBpeCCpRsKZqLz1WOFnc";

            bot.start();
//		bot.test();
	}

	//Переменные класса
	Map<Integer, PreparationToTrip> users = new HashMap<>();

	@Override
	public String processMessage(Integer userid, String text) {
		if (!users.containsKey(userid)) {
			users.put(userid, new PreparationToTrip(userid));
		}
		return users.get(userid).getBotAnswer(text.trim());
	}

	
	//TODO Определиться с классом
	void test() {
		Scanner in = new Scanner(System.in);
		String input;
		TUser user = new TUser();
		TestClass testClass = new TestClass();

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
