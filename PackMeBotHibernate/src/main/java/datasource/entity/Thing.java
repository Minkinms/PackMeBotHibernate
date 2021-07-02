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
	@JoinTable(name = "kit_thing",
				joinColumns = @JoinColumn(name = "thing_id"),
				inverseJoinColumns = @JoinColumn(name = "kit_id")
	)
	private List<Kit> kitList;

	public Thing() {
	}

	public Thing(String name, String category) {
		this.name = name;
		this.category = category;
	}

	
	public void addKit(Kit kit) {
		if(kitList == null) {
			kitList = new ArrayList<Kit>();
		}
		kitList.add(kit);
	}

	
	public List<Kit> getKitList() {
		return kitList;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
//		result = prime * result + id;		//TODO: Убрать из определения id и Kit???
//		result = prime * result + ((kitList == null) ? 0 : kitList.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())	return false;
		Thing other = (Thing) obj;
		if (category == null) {
			if (other.getCategory() != null)
				return false;
		} else if (!category.equals(other.getCategory()))
			return false;
//		if (id != other.getId())	return false;
//		if (kitList == null) {
//			if (other.kitList != null)
//				return false;
//		} else if (!kitList.equals(other.kitList))
//			return false;
		if (name == null) {
			if (other.getName() != null)
				return false;
		} else if (!name.equals(other.getName()))
			return false;
		return true;
	}
    
    
    
}
