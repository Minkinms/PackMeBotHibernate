package datasource.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity 
@Table(name="test_things")
public class Thing {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="thing_id")
	private int id;
	
	@Column(name="thing_name")
	private String name;
	
	@Column(name="category")
	private String category;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
			CascadeType.REFRESH, CascadeType.DETACH})
//	@ManyToMany(cascade = CascadeType.ALL)
	
//	@ManyToMany(cascade = {CascadeType.MERGE, 
//			CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(name = "trip_use_thing",
				joinColumns = @JoinColumn(name = "thing_id"),
				inverseJoinColumns = @JoinColumn(name = "trip_id")
	)
	private List<Trip> tripList;

	public Thing() {
	}

	public Thing(String name, String category) {
		this.name = name;
		this.category = category;
	}

	
	public void addTrip(Trip trip) {
		if(tripList == null) {
			tripList = new ArrayList<Trip>();
		}
		tripList.add(trip);
	}


	
	
	public List<Trip> getTripList() {
		return tripList;
	}

	public void setTripList(List<Trip> tripList) {
		this.tripList = tripList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
    @Override
    public String toString() {
        return this.getName() + " (" + this.getCategory() + ")"; 
    }
}
