import java.util.*;

public class CommandUI {

    public static Map<String, List<?>> filterMap = new TreeMap<>();
    public static final String[] filterKeys = {"Cleaning Type", "Experience","Location", "Name", "Rating"};
    private static boolean init = false;
    public static String startWindow() {
        return """
                Welcome to Dirtbuster House Cleaner Service!
                Search a trustworthy and experienced house cleaner easily!
                Enter filters below and navigate the command line.
                Search will apply the filters automatically.
                Navigation is done by entering the numbers of the items.
                Hope you'll be satisfied!""";
    }
    public static String filters() {
        initializeFilters();
        int ctr = 1;
        StringBuilder filter = new StringBuilder();
        filter.append("Filters:\n");
        for (Map.Entry<String, List<?>> entry : filterMap.entrySet()) {
            if (entry.getValue().isEmpty())
                filter.append("\t").append(ctr).append("- ").append(entry.getKey()).append('\n');
            else filter.append("\t").append(ctr).append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append('\n');
            ++ctr;
        }
        return filter.toString();
    }

    private static void initializeFilters() {
        // filters are initialized to empty strings
        if (!init) {
            for (String s : filterKeys) {
                filterMap.put(s, new ArrayList<>());
            }
            init = true;
        }
    }

    public static void insertFilter(int idx, List<?> filters) {
        String key = filterKeys[idx-1];
        filterMap.put(key, filters);
    }
    public static void searchWindow() {

    }

    private static void availableHouseCleaners() {

    }


}
