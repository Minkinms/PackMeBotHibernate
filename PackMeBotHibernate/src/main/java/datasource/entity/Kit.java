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
@Table(name="kits")
public class Kit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="kit_id")
	private int kitID;
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, 
			CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(name = "kit_thing",
	joinColumns = @JoinColumn(name = "kit_id"),
	inverseJoinColumns = @JoinColumn(name = "thing_id")
	)
	private List<Thing> thingList;

	public Kit() {
	}

	public Kit(List<Thing> thingList) {
		this.thingList = thingList;
	}

	public int getKitID() {
		return kitID;
	}

	public void setKitID(int kitID) {
		this.kitID = kitID;
	}

	public List<Thing> getThingList() {
		return thingList;
	}
	
	public void addThing(Thing thing) {
		if(thingList == null) {
			thingList = new ArrayList<Thing>();
		}
		thingList.add(thing);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + kitID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Kit other = (Kit) obj;
		if (kitID != other.getKitID())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KitThingsEntity [kitID=" + kitID + "]";
	}

}
