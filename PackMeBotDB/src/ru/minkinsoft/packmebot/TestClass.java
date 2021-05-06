package ru.minkinsoft.packmebot;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import ru.minkinsoft.packmebot.PreparationToTrip.Stage;

public class TestClass {
    PreparationToTrip pmt = new PreparationToTrip();

    //TODO: Требует изменения для ввода команд
    public String printHello(){
        return "\\new";
    }

    public String printDefDirection(){
    try {
        List<String> list = pmt.getDirectionList();
        return list.get(new Random().nextInt(list.size()));
    }catch(SQLException exc) {
//    	stage = Stage.ERROR_DB;
    	return "Есть проблема :(\n";
    }
    }
//    public String printCorrection(String text){
////        List<String> list = pmt.getCorrectionList(text);
//        return list.get(new Random().nextInt(list.size()));
//    }

    public String printOk(){
        return "готово";
    }

    public String printThing(List<Thing> thingList){
        return thingList.get(new Random().nextInt(thingList.size())).getNameThing();
    }
}
