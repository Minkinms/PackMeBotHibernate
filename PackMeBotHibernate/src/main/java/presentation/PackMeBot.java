package presentation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.telegram.telegrambots.ApiContextInitializer;

import datasource.DBConnection;
import ru.progwards.java1.telegrambot.ProgwardsTelegramBot;

import domain.PreparationToTrip;


public class PackMeBot extends ProgwardsTelegramBot {

	public static void main(String[] args) {
		ApiContextInitializer.init();
		PackMeBot bot = new PackMeBot();
		bot.username = "Pack_Me_bot";
		bot.token = "1611064894:AAHe1K2-x6p_40WzBpeCCpRsKZqLz1WOFnc";

//            bot.start();
		bot.test();
	}

	//Class variables
	Map<Integer, PreparationToTrip> users = new HashMap<Integer, PreparationToTrip>();
	
	DBConnection dbConnection = new DBConnection();
	public static SessionFactory factory;
	
	//Constructor
	public PackMeBot() {
		try{
			factory = dbConnection.getFactory();
		}catch(HibernateException exc) {
			System.out.println("Error connecting to the database" + "\n");	//log message
			System.out.println(exc.getMessage());
		}
	}
		
	@Override
	public String processMessage(Integer userid, String text) {
		if (!users.containsKey(userid)) {
			users.put(userid, new PreparationToTrip(userid));
		}
		return users.get(userid).getBotAnswer(text.trim());
	}

	//For test without telegram 
	//(Метод для тестирования без использования telegram)
	void test() {
		Scanner in = new Scanner(System.in);
		String input;
		TUser user = new TUser();
		do {
			input = in.nextLine();
			System.out.println(processMessage(user.userID, input));
		} while (!input.equals("стоп"));
		in.close();
	}

}

// Any User (Условный пользователь)
class TUser {
	Integer userID;

	public TUser() {
		this.userID = new Random().nextInt();
	}
}
