import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Initializer {
    private static final List<Cleaner> cleaners = new ArrayList<>();
    private static final List<String> cleanerStats = new ArrayList<>();
    public static void main(String[] args) {

        String houseCleanerInfoFile = "src/housecleanerlist.txt";
        String houseCleanerStatsFile = "src/stats.csv";
        readCleanerInfo(houseCleanerInfoFile);
        readCleanerStats(houseCleanerStatsFile);
        fun();
        CleanerHandler cleanerHandler = new CleanerHandler(cleaners);
//        cleanerHandler.test();
        UIEngine ui = new UIEngine(cleanerHandler);
        ui.welcomeWindow();
         /*
        professionalism
        time management
        quality of cleaning
        price
        trustworthiness
        --- more can be added
         */



    }

    public static List<Cleaner> getCleaners() { // TODO make this a better interface because it's so sketchy rn
        return cleaners;
    }

    private static void readCleanerInfo(String filePath) {
        // I apologize if this looks like an abomination
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String s = "";
            while ((s = br.readLine()) != null) {
                String[] things = s.split(", ");
                Person p = new Person(things[0], things[1], things[2], things[3], things[4], things[5]);
                String[] cleaning = things[7].split("-");
                List<String> cleaningList = Arrays.asList(cleaning);
                Cleaner c = new Cleaner(p, things[6], cleaningList, Integer.parseInt(things[8]), things[9], Integer.parseInt(things[10]));
                cleaners.add(c);
//                System.out.println(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while reading house cleaner file");
        }
    }

    private static void readCleanerStats(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String s = "";
            while ((s = br.readLine()) != null) {
                cleanerStats.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while reading stats csv file");
        }

    }

    private static void fun() {
        for (int i = 0; i < cleaners.size(); i++) {
            Cleaner cleaner = cleaners.get(i);
            String statString = cleanerStats.get(i);
            cleaner.setStats(statTable(statString));
//            System.out.println(cleaner.shortDetails());
        }
    }

    private static Map<String, Integer> statTable(String statsString) {
        String[] statsStrings = statsString.split(",");
        int[] stats = Arrays.stream(statsStrings).mapToInt(Integer::parseInt).toArray();
        Map<String, Integer> statMap = new HashMap<>();
        String[] keys = {"Professionalism", "Time Management", "Quality of Cleaning", "Price", "Trustworthiness"};
        for (int i = 0; i < keys.length; i++) {
            statMap.put(keys[i], stats[i]);
        }
        return statMap;
    }
}
