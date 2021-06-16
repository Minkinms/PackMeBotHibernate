package datasource.entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity 
@Table(name="test_trips")
public class Trip {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="trip_id")
	private int tripID; 		//ID направления в таблице
	
	@Column(name="direction")
	private String direction; 	// Основное направление поездки, "куда"
	
	@Column(name="correction")
	private String correction; 	// Уточнение направления поездки, "куда именно"
	
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
			CascadeType.REFRESH, CascadeType.DETACH})
//	@ManyToMany(cascade = CascadeType.ALL)
//	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
//			CascadeType.REFRESH, CascadeType.DETACH})
//	@ManyToMany(cascade = {CascadeType.MERGE, 
//			CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(name = "trip_use_thing",
				joinColumns = @JoinColumn(name = "trip_id"),
				inverseJoinColumns = @JoinColumn(name = "thing_id")
	)
	private List<Thing> thingList;
	
	
//	public int useCount; 		// Количество использований направления

	public Trip() {
	}

	public Trip(String direction, String correction) {
		this.direction = direction;
		this.correction = correction;
	}

	
	public void addThing(Thing thing) {
		if(thingList == null) {
			thingList = new ArrayList<Thing>();
		}
		thingList.add(thing);
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

//	public int getUseCount() {
//		return useCount;
//	}
//
//	public void setUseCount(int useCount) {
//		this.useCount = useCount;
//	}
	
	
	
	
	@Override
	public String toString() {
		return "Trip: " + direction + "/" + correction + " ID = " + tripID;
				
	}
	
	public List<Thing> getThingList() {
		return thingList;
	}

	public void setThingList(List<Thing> thingList) {
		this.thingList = thingList;
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
