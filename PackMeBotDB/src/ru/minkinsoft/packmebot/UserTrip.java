package ru.minkinsoft.packmebot;
import java.util.ArrayList;
import java.util.List;

//Класс для описания завершенных сборов к поездке пользователя
public class UserTrip extends Trip {
    private List<Thing> userTripThings;     //Список вещей
    private Integer userID;

    public UserTrip() {
    }

    
    
    public UserTrip(Integer userID) {
		super();
		this.userID = userID;
	}

	public UserTrip(String direction, String correction, List<Thing> userTripThings) {
        super(direction, correction);
        this.userTripThings = new ArrayList<>(userTripThings);;
    }

    public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	@Override
    public String toString() {
        return super.toString() + " ___ Вещи в поездку" + userTripThings;
    }

    public List<Thing> getUserTripThings() {
        return userTripThings;
    }

    public void setUserTripThings(List<Thing> userTripThings) {
        this.userTripThings = new ArrayList<>(userTripThings);
    }
}
