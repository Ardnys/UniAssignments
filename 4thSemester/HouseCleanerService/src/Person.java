public record Person(String name, String surname, String email, String password, String phone, String location) {
    // No need to explicitly define fields or constructor
    // Record implicitly generates equals(), hashCode(), and toString() methods

    @Override
    public String toString() {
        return "Name: " + name +
                " | Surname: " + surname +
                " | Email: " + email +
                " | Phone: " + phone +
                " | Location: " + location + '\n';
    }
}