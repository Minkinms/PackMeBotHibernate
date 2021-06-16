package datasource;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserTripID implements Serializable{
	
	@Column
	private int userTripID;
	
	@Column
	private int userID;
	
//	@Column
	//private int tripID;
	
//	@Column
//	private int thingID;

	public UserTripID() {
	}

	public UserTripID(int userTripID, int userID) {
		this.userTripID = userTripID;
		this.userID = userID;
		//this.tripID = tripID;
		//this.thingID = thingID;
	}

	public int getUserTripID() {
		return userTripID;
	}

	public void setUserTripID(int userTripID) {
		this.userTripID = userTripID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

//	public int getTripID() {
//		return tripID;
//	}
//
//	public void setTripID(int tripID) {
//		this.tripID = tripID;
//	}

//	public int getThingID() {
//		return thingID;
//	}
//
//	public void setThingID(int thingID) {
//		this.thingID = thingID;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + thingID;
		//result = prime * result + tripID;
		result = prime * result + userID;
		result = prime * result + userTripID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())	return false;
		UserTripID other = (UserTripID) obj;
		//if (thingID != other.thingID)		return false;
		//if (tripID != other.tripID)			return false;
		if (userID != other.userID)			return false;
		if (userTripID != other.userTripID)	return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserTrip [userTripID=" + userTripID + 
				", userID=" + userID + "]";
				//", tripID=" + tripID + 
//				", thingID="
//				+ thingID + 
	}


	
	

}
