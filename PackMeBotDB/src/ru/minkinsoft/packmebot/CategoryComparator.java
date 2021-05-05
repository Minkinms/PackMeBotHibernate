package ru.minkinsoft.packmebot;
import java.util.Comparator;

public class CategoryComparator implements Comparator<Thing> {
	@Override
	public int compare(Thing o1, Thing o2) {
		return o1.getCategoryThing().compareTo(o2.getCategoryThing());
	}
}
