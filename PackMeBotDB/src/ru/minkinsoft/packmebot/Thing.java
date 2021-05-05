package ru.minkinsoft.packmebot;
import java.util.*;

public class Thing {
    private String nameThing;       //Название вещи
    private String categoryThing;   //Категория
    public Map<String, Integer> tagsMap = new HashMap<>();

    public Thing(String nameThing, String categoryThing) {
        this.nameThing = nameThing;
        this.categoryThing = categoryThing;
    }

    public String getNameThing() {
        return nameThing;
    }

    public String getCategoryThing() {
        return categoryThing;
    }

    @Override
    public String toString() {
        return this.getNameThing() + " (" + this.getCategoryThing() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Thing)) return false;
        Thing thing = (Thing) o;
        return Objects.equals(getNameThing(), thing.getNameThing()) &&
                Objects.equals(getCategoryThing(), thing.getCategoryThing());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNameThing(), getCategoryThing());
    }
}
