package ru.minkinsoft.packmebot;
import java.util.ArrayList;
import java.util.List;

//Класс для описания завершенных сборов к поездке пользователя
public class UserTrip extends Trip {
    private List<Thing> userTripThings;     //Список вещей

    public UserTrip() {
    }

    public UserTrip(String direction, String correction, List<Thing> userTripThings) {
        super(direction, correction);
        this.userTripThings = new ArrayList<>(userTripThings);;
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
