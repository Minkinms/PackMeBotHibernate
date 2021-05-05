package ru.minkinsoft.packmebot;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TripsData {
    //Переменные класса
    String tripHistoryPath;                         //Путь к файлу истории поездок
    List<UserTrip> allTrips = new ArrayList<>();    //Полный список поездок
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS");

    //Конструктор
    public TripsData(String tripHistoryPath) throws FileNotFoundException {
        this.tripHistoryPath = tripHistoryPath;
        getAllTrips();
    }

    //Метод для получения множества всех поездок истории
    private void getAllTrips() throws FileNotFoundException {
        this.allTrips.clear();
        File tripHistoryFile = new File(tripHistoryPath);
        Scanner scanner = new Scanner(tripHistoryFile);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.isBlank() && line.startsWith("tr")) {
                String[] arrayTripLine = line.split(",");
                String[] arrayDirection = arrayTripLine[1].split("/");
                allTrips.add(new UserTrip(arrayDirection[0].trim(),             //Direction
                        arrayDirection[1].trim(),              //Correction
                        getTripThingsList(arrayTripLine)));    //UserTripThingsList
            }
        }
        scanner.close();
    }

    //Метод для получения списка вещей.
    // Источник - массив строк, полученный из строки поездки, считанной из файла всех поездок
    private List<Thing> getTripThingsList(String[] arrayTripLine) {
        List<Thing> thingsList = new ArrayList<>();
        for (int i = 3; i < arrayTripLine.length; i++) {
            StringTokenizer tokenizer = new StringTokenizer(arrayTripLine[i].trim(), "()");
            List<String> textParts = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                textParts.add(tokenizer.nextToken());
            }
            if (textParts.size() == 2) {
                thingsList.add(new Thing(textParts.get(0).trim(), textParts.get(1).trim()));
                thingsList.sort(new CategoryComparator());
            }
        }
        return thingsList;
    }


    //Метод для записи строки поездки в файл
    public void writeTrip(UserTrip userTrip) throws IOException {
        File tripHistoryFile = new File(tripHistoryPath);
        FileWriter tripWriter = new FileWriter(tripHistoryFile, true);
        StringBuilder stringToWrite = new StringBuilder();
        stringToWrite.append("\ntr").append(",");
        stringToWrite.append(userTrip.getDirection()).append("/");
        stringToWrite.append(userTrip.getCorrection()).append(",");
        //Дата записи не используется в работе. Сохранение необходимо для анализа истории при необходимости
        stringToWrite.append(dateTimeFormatter.format(LocalDateTime.now())).append(",");
        for (Thing thing : userTrip.getUserTripThings()) {
            stringToWrite.append(thing.toString()).append(",");
        }
        stringToWrite.deleteCharAt(stringToWrite.length() - 1);
        tripWriter.write(stringToWrite.toString());
        tripWriter.close();
    }

    //Метод для добавления поездки в список
    private void addTrip(List<Trip> tripList, Trip addTrip) {
        if (tripList.contains(addTrip)) {
            int count = tripList.get(tripList.indexOf(addTrip)).getUseCount() + 1;
            tripList.get(tripList.indexOf(addTrip)).setUseCount(count);
        } else {
            addTrip.setUseCount(1);
            tripList.add(addTrip);
        }
    }

    //Метод для получения частых направлений поездок
    public List<Trip> getFrequentTripsList(String direction, int numberOfFrequentTrips) {
        List<Trip> frequentTrips = new ArrayList<>();
        for (UserTrip userTrip : allTrips) {
            Trip trip = new Trip(userTrip.getDirection(), userTrip.getCorrection());
            if (direction == null) {
                addTrip(frequentTrips, trip);
            } else {
                if (userTrip.getDirection().equals(direction)) {
                    addTrip(frequentTrips, trip);
                }
            }
        }
        frequentTrips.sort((o1, o2) -> Integer.compare(o2.getUseCount(), o1.getUseCount()));

        if (numberOfFrequentTrips > 0 && numberOfFrequentTrips < frequentTrips.size()) {
            return frequentTrips.subList(0, numberOfFrequentTrips);
        } else return frequentTrips;
    }

    //Метод для получения полного списка вещей с определением количества раз использования в поездках
    public List<Thing> getThingsList() {
        List<Thing> thingsList = new ArrayList<>();
        for (UserTrip userTrip : allTrips) {
            for (Thing thing : userTrip.getUserTripThings()) {
                String key = (userTrip.getDirection() + "/" + userTrip.getCorrection()).toLowerCase();
                if (thingsList.contains(thing)) {
                    Thing extractedThing = thingsList.get(thingsList.indexOf(thing));
                    if (extractedThing.tagsMap.containsKey(key)) {
                        extractedThing.tagsMap.put(key, extractedThing.tagsMap.get(key) + 1);
                    } else {
                        extractedThing.tagsMap.put(key, 1);
                    }
                } else {
                    thing.tagsMap.put(key, 1);
                    thingsList.add(thing);
                }
            }
        }
        return thingsList;
    }
}
