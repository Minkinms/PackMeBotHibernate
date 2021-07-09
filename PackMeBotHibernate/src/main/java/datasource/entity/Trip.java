package datasource.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity 
@Table(name="trips")
public class Trip {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="trip_id")
	private int tripID; 		//ID направления в таблице
	
	@Column(name="direction")
	private String direction; 	// Основное направление поездки, "куда"
	
	@Column(name="correction")
	private String correction; 	// Уточнение направления поездки, "куда именно"
	
	@OneToOne
	@JoinColumn(name="kit_id")
	private Kit kit;	//Набор вещей в эту поездку
	
	@Column(name="use_count")		//Количество использований этого варианта направления
	private int useCount;

	public Trip() {
	}

	public Trip(String direction, String correction, Kit kitID) {
		this.direction = direction;
		this.correction = correction;
		this.kit = kitID;
	}

	public int getTripID() {
		return tripID;
	}

	public void setTripID(int tripID) {
		this.tripID = tripID;
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

	public Kit getKit() {
		return kit;
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public void upUseCount() {
		this.useCount++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((correction == null) ? 0 : correction.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((kit == null) ? 0 : kit.hashCode());
		result = prime * result + tripID;
		result = prime * result + useCount;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trip other = (Trip) obj;
		if (correction == null) {
			if (other.correction != null)
				return false;
		} else if (!correction.equals(other.correction))
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (kit == null) {
			if (other.kit != null)
				return false;
		} else if (!kit.equals(other.kit))
			return false;
		if (tripID != other.tripID)
			return false;
		if (useCount != other.useCount)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TripEntity [tripID=" + tripID + ", direction=" + direction + ", correction=" + correction + ", kit="
				+ kit + ", useCount=" + useCount + "]";
	}
	
	
	

}
