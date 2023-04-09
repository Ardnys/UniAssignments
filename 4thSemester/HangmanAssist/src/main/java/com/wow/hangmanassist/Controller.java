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
    private ObservableList<String> suggestions = FXCollections.observableArrayList();
    @FXML
    private ListView<String> list = new ListView<>();
    @FXML
    private TextField textField;
    @FXML
    private TextArea textArea;
    private String guess;
    private final HangmanGame game = new HangmanGame();

    public void choose(ActionEvent event) {
        guess = textField.getText();
        System.out.println("guess is: " + guess);
//        textArea.setText(guess);
        if (!game.playFromGUI(guess)) {
            textArea.appendText(game.getGameOutcome());
        } else {
            String status = game.getGameStatus();
            textArea.appendText("Guessed: " + guess + "\n");
            textArea.appendText(status);
            String topSuggestions = game.displaySuggestions();
            String[] suggestionArr = topSuggestions.split("\n");
            suggestions.clear();
            suggestions.addAll(suggestionArr);
        }
//        textArea.appendText(guess + "\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        list.setItems(suggestions);
        String status = game.getGameStatus();
        textArea.appendText(status);
        System.out.println("does this execute");

        list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
//                System.out.println("observable value: " + observableValue + "\nnumber: " + number + "\nt1: " + t1);
                // t1 is useful. it's the index of the list. it becomes -1 when i click the button
                if (t1.intValue() >= 0){
                    guess = list.getSelectionModel().getSelectedItem();
                    System.out.println("init guess is " + guess);
                    textField.setText(guess.substring(0, 1));
                }
            }
        });
    }
}