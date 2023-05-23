import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class UIEngine {
    private final CleanerHandler cleanerHandler;
    private final Scanner scanner = new Scanner(System.in);

    public UIEngine(CleanerHandler cleanerHandler) {
        this.cleanerHandler = cleanerHandler;
    }

    public void welcomeWindow() {
        getFilterInput();
    }

    private void getFilterInput() {
        System.out.println(CommandUI.startWindow());
        int choice = -1;
        while (choice != 0) {
            System.out.println(CommandUI.filters() + "Actions:\n" + "\t6- Search\n" + "\t0- Exit");
            System.out.print('>');
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 0 -> System.out.println("Have a nice day!");
                case 1 -> enterCleaningTypeFilters(choice);
                case 2 -> enterExperienceFilters(choice);
                case 3 -> enterLocationFilters(choice);
                case 4 -> enterNameFilters(choice);
                case 5 -> enterRatingFilters(choice);
                case 6 -> searchFunction();
                default -> System.out.println("Unsupported input. Enter again");
            }
        }
    }

    private void enterNameFilters(int idx) {
        System.out.println("Enter -1 to exit");
        System.out.print("Enter names (whitespace separated): >");

        String[] names = scanner.nextLine().split(" ");
        if (names[0].equals("-1")) return;
        for (String name : names) {
            int count = cleanerHandler.checkName(name);
            System.out.println("Found " + count + " instances of " + name);
        }
        CommandUI.insertFilter(idx, Arrays.asList(names));
    }

    private void enterRatingFilters(int idx) {
        System.out.println("Enter -1 to exit");
        System.out.print("Enter ratings between 1-5 (whitespace separated): >");
        String[] ratings = scanner.nextLine().split(" ");
        if (ratings[0].equals("-1")) return;
        List<Integer> intRatings = new ArrayList<>();
        for (String rating : ratings) {
            intRatings.add(Integer.parseInt(rating));
            int count = cleanerHandler.checkRating(Integer.parseInt(rating));
            System.out.println("Found " + count + " instances of " + rating);
        }
        CommandUI.insertFilter(idx, Arrays.asList(intRatings));
    }

    private void enterExperienceFilters(int idx) {
        System.out.println("Enter -1 to exit");
        System.out.print("Enter experience between 1-10 (whitespace separated): >");
        String[] exps = scanner.nextLine().split(" ");
        if (exps[0].equals("-1")) return;
        List<Integer> intExperiences = new ArrayList<>();
        for (String exp : exps) {
            intExperiences.add(Integer.parseInt(exp));
            int count = cleanerHandler.checkExperiences(Integer.parseInt(exp));
            System.out.println("Found " + count + " instances of " + exp);
        }
        CommandUI.insertFilter(idx, Arrays.asList(intExperiences));

    }

    private void enterCleaningTypeFilters(int idx) {
        String prompt = "Available cleaning services are:\n" +
                "1- Housework\n" + "2- Loundry and Ironing\n" +
                "3- Cooking and Dishes\n" + "4- Window Cleaning\n";
        System.out.println(prompt + "\nEnter -1 to exit");
        System.out.print("Enter cleaning types' numbers (whitespace separated): >");
        String[] types = scanner.nextLine().split(" ");
        List<String> actualList = new ArrayList<>();
        if (types[0].equals("-1")) return;
        for (String type : types) {
            type = type.replace("1", "Housework");
            type = type.replace("2", "Loundry and Ironing");
            type = type.replace("3", "Cooking and Dishes");
            type = type.replace("4", "Window Cleaning");
            System.out.println("type is " + type);
            int count = cleanerHandler.checkCleaningTypes(type);
            System.out.println("Found " + count + " instances of " + type);
            actualList.add(type);
        }
        CommandUI.insertFilter(idx, Arrays.asList(actualList));
    }

    private void enterLocationFilters(int idx) {
        System.out.println("Enter -1 to exit");
        System.out.print("Enter locations (whitespace separated): >");
        String[] locations = scanner.nextLine().split(" ");
        if (locations[0].equals("-1")) return;
        for (String location : locations) {
            int count = cleanerHandler.checkName(location);
            System.out.println("Found " + count + " instances of " + location);
        }
        CommandUI.insertFilter(idx, Arrays.asList(locations));

    }

    private void searchFunction() {
        List<Cleaner> list1 = new ArrayList<>();
        List<Cleaner> list2 = new ArrayList<>();
        List<Cleaner> availableCleaners = new ArrayList<>();
        for (int i = 0; i < CommandUI.filterKeys.length; i++) {
            if (list1.isEmpty()) {
                list1 = cleanerHandler.filtering(i);
                continue;
            } else {
                if (!list2.isEmpty())
                    list1 = list2;
                list2 = cleanerHandler.filtering(i);
            }
            if (availableCleaners.isEmpty()) {
                availableCleaners = bro(list1, list2);
            } else {
                List<Cleaner> tempList = bro(list1, list2);
                List<Cleaner> tempList2 = new ArrayList<>(availableCleaners);
                availableCleaners = bro(tempList2, tempList);
            }
        }
        for (Cleaner cleaner : availableCleaners)
            System.out.println(cleaner.shortDetails());
    }

//    private List<Cleaner> intersection(List<Cleaner> list1, List<Cleaner> list2) {
//        if (list1.size() >= list2.size()) {
//            return bro(list2, list1);
//        } else {
//            return bro(list1, list2);
//        }
//    }

    private List<Cleaner> bro(List<Cleaner> small, List<Cleaner> large) {
        small.removeIf(c -> !large.contains(c));
        return small;
    }
}
