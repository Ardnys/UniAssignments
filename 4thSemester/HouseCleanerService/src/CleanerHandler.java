import java.util.*;

public class CleanerHandler {
    private final List<Cleaner> cleaners;
    private final Map<String, List<Integer>> locationMap = new HashMap<>();
    private final Map<String, List<Integer>> cleaningTypeMap = new HashMap<>();
    private final Map<Integer, List<Integer>> experienceMap = new HashMap<>();
    private final Map<Integer, List<Integer>> ratingMap = new HashMap<>(); // based on trust stat because people value that more
    private final Map<String, List<Integer>> nameMap = new HashMap<>();

    public CleanerHandler(List<Cleaner> cleaners) {
        this.cleaners = cleaners;
        makeMaps();
    }

    private void makeMaps() {
        int ctr = 0;
        for (Cleaner cleaner : cleaners) {
            String name = cleaner.getPerson().name();
            String location = cleaner.getPerson().location();
            List<String> types = cleaner.getCleaningTypes();
            int experience = cleaner.getExperience();
            int trustRate = cleaner.getStats().get("Trustworthiness");

            nameMap.computeIfAbsent(name, k -> new ArrayList<>()).add(ctr);
            locationMap.computeIfAbsent(location, k -> new ArrayList<>()).add(ctr);
            experienceMap.computeIfAbsent(experience, k -> new ArrayList<>()).add(ctr);
            ratingMap.computeIfAbsent(trustRate, k -> new ArrayList<>()).add(ctr);

            for (String type : types) {
                cleaningTypeMap.computeIfAbsent(type, k -> new ArrayList<>()).add(ctr);
            }
            ++ctr;
        }
        System.out.println("maps created");
    }

    private void printMaps() {
        String s = "name map: " + nameMap + "\nlocation map: " + locationMap +
                "\nexperience map: " + experienceMap + "\nrating map: " + ratingMap + "\ncleaning type map: " + cleaningTypeMap;
        System.out.println(s);
    }

    public void test() {
        List<String> llist = new ArrayList<>();
        llist.add("Tokyo");
        llist.add("Paris");
        llist.add("Chicago");

        List<String> tlist = new ArrayList<>();
        tlist.add("Cooking and Dishes");
        tlist.add("Housework");

        List<Integer> rlist = new ArrayList<>();
        rlist.add(4);
        rlist.add(5);

        List<String> nlist = new ArrayList<>();

        CommandUI.insertFilter(0, Arrays.asList(llist));
        CommandUI.insertFilter(1, Arrays.asList(tlist));
        CommandUI.insertFilter(3, Arrays.asList(rlist));
        CommandUI.insertFilter(4, Arrays.asList(nlist));

        List<Cleaner> clist = filtering(4);
        for (Cleaner c : clist)
            System.out.println(c.shortDetails());

    }

    public List<Cleaner> filtering(int idx) {
        List<?> list = CommandUI.filterMap.get(CommandUI.filterKeys[idx]);
        return switch (idx) {
            case 0 -> fiveIndentation(list, cleaningTypeMap);
            case 1 -> fiveIndentation(list, experienceMap);
            case 2 -> fiveIndentation(list, locationMap);
            case 3 -> fiveIndentation(list, nameMap);
            case 4 -> fiveIndentation(list, ratingMap);
            default -> new ArrayList<>();
        };
    }

    /*
    The monstrosity you see below is ensuring type safety somehow.
    Since the filter maps can have integers or strings
    we don't know what we'll get. well i do but the compiler doesn't
    we know that it's Object, so using that we check if it's a List<?>
    because that object is actually a List<?> at that point.
    then, we check for ? types, which can be integers or strings
    and check each of them in a loop.
    then bla bla do the rest
     */
    // TODO the logic here is kinda messed up remove the list thingy
    private List<Cleaner> fiveIndentation(List<?> list, Map<?, List<Integer>> map) {
        List<Cleaner> filteredCleaners = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof List<?> subList) {
                for (Object subItem : subList) {
                    if (subItem instanceof Integer) {
                        List<Integer> idxList = map.get(subItem);
                        for (Integer i : idxList) {
                            if (!filteredCleaners.contains(cleaners.get(i))) {
                                filteredCleaners.add(cleaners.get(i));
                            }
                        }
                    } else if (subItem instanceof String) {
                        List<Integer> idxList = map.get(subItem);
                        for (Integer i : idxList) {
                            if (!filteredCleaners.contains(cleaners.get(i))) {
                                filteredCleaners.add(cleaners.get(i));
                            }
                        }
                    }
                }
                if (subList.size() == 0) return cleaners;
            }
        }
        if (list.size() == 0) return cleaners;
        return filteredCleaners;
    }

    public int checkName(String name) {
        return nameMap.containsKey(name) ? nameMap.get(name).size() : 0;
    }
    public int checkRating(Integer rating) {
        return ratingMap.containsKey(rating) ? ratingMap.get(rating).size() : 0;
    }

    public int checkExperiences(Integer experience) {
        return experienceMap.containsKey(experience) ? experienceMap.get(experience).size() : 0;
    }

    public int checkCleaningTypes(String type) {
        return cleaningTypeMap.containsKey(type) ? cleaningTypeMap.get(type).size() : 0;
    }
}
