package com.wow.hangmanassist;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private ObservableList<String> suggestions = FXCollections.observableArrayList("i am", "testing", "this", "list view");
    @FXML
    private ListView<String> list = new ListView<>();
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    private String text;

    public void choose(ActionEvent event) {
        text = textField.getText();
//        textArea.setText(text);
        textArea.appendText(text + "\n");
        suggestions.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setItems(suggestions);

        list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                text = list.getSelectionModel().getSelectedItem();
                textField.setText(text);
            }
        });
    }
}