package com.wow.hangmanassist;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    @FXML
    private ListView<String> list;
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    private String[] listItems = {"i am", "testing", "this", "list view"};
    private String text;

    public void choose(ActionEvent event) {
        text = textField.getText();
//        textArea.setText(text);
        textArea.appendText(text + "\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.getItems().addAll(listItems);

        list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                text = list.getSelectionModel().getSelectedItem();
                textField.setText(text);
            }
        });
    }
}