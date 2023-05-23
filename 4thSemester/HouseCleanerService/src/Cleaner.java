import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Cleaner {
    private final Person person;
    private final String company;
    private final List<String> cleaningTypes;
    private final int pricePerDay;
    private final String availableTimes;
    private final int experience;
    private Map<String, Integer> stats;

    // TODO how to represent stats in a nice way? using maps

    public Cleaner(Person person, String company, List<String> cleaningTypes, int pricePerDay, String availableTimes, int experience) {
        this.person = person;
        this.company = company;
        this.cleaningTypes = cleaningTypes;
        this.pricePerDay = pricePerDay;
        this.availableTimes = availableTimes;
        this.experience = experience;
    }

    public Person getPerson() {
        return person;
    }

    public String getCompany() {
        return company;
    }

    public List<String> getCleaningTypes() {
        return cleaningTypes;
    }

    public int getPricePerDay() {
        return pricePerDay;
    }

    public String getAvailableTimes() {
        return availableTimes;
    }

    public int getExperience() {
        return experience;
    }

    public Map<String, Integer> getStats() {
        return stats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cleaner cleaner = (Cleaner) o;
        return pricePerDay == cleaner.pricePerDay && experience == cleaner.experience && Objects.equals(person, cleaner.person) && Objects.equals(company, cleaner.company) && Objects.equals(cleaningTypes, cleaner.cleaningTypes) && Objects.equals(availableTimes, cleaner.availableTimes) && Objects.equals(stats, cleaner.stats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(person, company, cleaningTypes, pricePerDay, availableTimes, experience, stats);
    }

    @Override
    public String toString() {
        return "Person: " + person +
                "Company: " + company +
                " | Experience: " + experience +
                " | Cleaning Types: " + cleaningTypes +
                " | Price Per Day: " + pricePerDay +
                " | Available Times: " + availableTimes +
                "\nStats: " + stats + '\n';
    }

    public String shortDetails() {
        return "Name: " + person.name() +
                " | Location: " + person.location() +
                " | Experience: " + experience +
                " | Price: " + pricePerDay +
                "\nStats: " + stats + '\n';
    }

    public void statsFromFile() {
        /*
        professionalism
        time management
        quality of cleaning
        price
        trustworthiness
        --- more can be added
         */
    }

    public void setStats(Map<String, Integer> stats) {
        this.stats = stats;
    }
}
