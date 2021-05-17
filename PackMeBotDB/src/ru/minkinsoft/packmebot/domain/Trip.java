package ru.minkinsoft.packmebot.domain;
import java.util.Objects;

//Класс для описания поездки (по умолчанию)
public class Trip {
	private String direction; 	// Основное направление поездки, "куда"
	private String correction; 	// Уточнение направления поездки, "куда именно"
	private int useCount; 		// Количество использований направления

	public Trip() {
	}

	public Trip(String direction, String correctionLevelOne) {
		this.direction = direction;
		this.correction = correctionLevelOne;
	}

	@Override
	public String toString() {
		return "Trip: " + direction + "/" + correction;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getCorrection() {
		return correction;
	}

	public void setCorrection(String correction) {
		this.correction = correction;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Trip))
			return false;
		Trip trip = (Trip) o;
		return getDirection().equals(trip.getDirection()) && getCorrection().equals(trip.getCorrection());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getDirection(), getCorrection());
	}

}
