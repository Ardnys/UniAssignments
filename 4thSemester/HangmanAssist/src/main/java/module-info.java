module com.wow.hangmanassist {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.wow.hangmanassist to javafx.fxml;
    exports com.wow.hangmanassist;
}