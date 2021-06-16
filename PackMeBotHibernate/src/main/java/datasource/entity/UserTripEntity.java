package datasource.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import datasource.UserTripID;

@Entity
@Table(name = "test_user_trips")
public class UserTripEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_trip_id")
	private int userTripID;


	@Column(name = "user_id")
	private int userID;
	
	@OneToOne
	@JoinColumn(name = "trip_id")
	private Trip trip;

	public UserTripEntity() {
	}
	
	
	

	public UserTripEntity(int userID, Trip tripID) {
		this.userID = userID;
		this.trip = tripID;
	}



	public int getUserTripID() {
		return userTripID;
	}



	public void setUserTripID(int userTripID) {
		this.userTripID = userTripID;
	}




	public Trip getTrip() {
		return trip;
	}




	public void setTrip(Trip trip) {
		this.trip = trip;
	}




	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public Trip getTripID() {
		return trip;
	}

	public void setTripID(Trip tripID) {
		this.trip = tripID;
	}



	@Override
	public String toString() {
		return "UserTripEntity [userTripID=" + userTripID + ", userID=" + userID + ", tripID=" + trip + "]";
	}




	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((trip == null) ? 0 : trip.hashCode());
		result = prime * result + userID;
		result = prime * result + userTripID;
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
		UserTripEntity other = (UserTripEntity) obj;
		if (trip == null) {
			if (other.trip != null)
				return false;
		} else if (!trip.equals(other.trip))
			return false;
		if (userID != other.userID)
			return false;
		if (userTripID != other.userTripID)
			return false;
		return true;
	}





	
	
	
}
