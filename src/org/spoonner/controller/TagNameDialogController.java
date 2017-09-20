package org.spoonner.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TagNameDialogController implements Initializable {

    @FXML
    private Label textLabel;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField tagName;
    @FXML
    private TextField textValue;

    private String tag;
    private String text;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        okButton.setOnAction(event -> {
            tag = tagName.getText();
            text = textValue.getText();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        cancelButton.setOnAction(event -> {
            tag = null;
            text = null;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    public String getTagName() {
        return tag;
    }

    public String getTextValue() {
        return text;
    }

    public void hideText() {
        textValue.setVisible(false);
        textLabel.setVisible(false);
    }

    public void setTagText(String tagName) {
        this.tagName.setText(tagName);
    }

    public void setTextValue(String textValue) {
        this.textValue.setText(textValue);
    }
}
