package org.spoonner.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.spoonner.model.AttributePair;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.*;

public class AttributeEditorController implements Initializable {

    private final double TEXT_FIELD_WIDTH = 175.0;
    private final double COMPONENT_MARGIN = 10.0;
    private final double NAME_LABEL_WIDTH = 50.0;
    private final double VALUE_LABEL_WIDTH = 90.0;
    private final double TEXT_FIELD_HEIGHT = 20.0;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button addRowButton;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;

    private VBox contentVBox;

    private Map<String, String> attributes = new HashMap<>();
    private Map<TextField, TextField> attributeComponentMap = new HashMap<>(5);
    private Map<TextField, TextField> initialAttributeComponentMap;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        contentVBox = new VBox(5);
        scrollPane.setContent(contentVBox);
        scrollPane.setFitToWidth(true);

        okButton.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        cancelButton.setOnAction(event -> {
            this.attributeComponentMap = initialAttributeComponentMap;
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

        addRowButton.setOnAction(event -> {

            addNewRow(contentVBox, null, null);
        });

    }

    private void addNewRow(VBox content, String nameValue, String textValue) {
        Label nameLabel = new Label("Имя:");
        Label valueLabel = new Label("Значение:");

        nameLabel.setPrefWidth(NAME_LABEL_WIDTH);
        valueLabel.setPrefWidth(VALUE_LABEL_WIDTH);

        AnchorPane anchorPane = new AnchorPane();
        TextField nameField = new TextField();
        TextField valueField = new TextField();

        nameField.setPrefWidth(TEXT_FIELD_WIDTH);
        valueField.setPrefWidth(TEXT_FIELD_WIDTH);
        nameField.setPrefHeight(TEXT_FIELD_HEIGHT);
        valueField.setPrefHeight(TEXT_FIELD_HEIGHT);

        AnchorPane.setLeftAnchor(nameLabel, 5.0);
        AnchorPane.setTopAnchor(nameLabel, TEXT_FIELD_HEIGHT / 2);

        AnchorPane.setTopAnchor(nameField, 5.0);
        AnchorPane.setLeftAnchor(nameField, NAME_LABEL_WIDTH + COMPONENT_MARGIN);

        AnchorPane.setLeftAnchor(valueLabel, NAME_LABEL_WIDTH + TEXT_FIELD_WIDTH + 3 * COMPONENT_MARGIN);
        AnchorPane.setTopAnchor(valueLabel, TEXT_FIELD_HEIGHT / 2);

        AnchorPane.setTopAnchor(valueField, 5.0);
        AnchorPane.setLeftAnchor(valueField, NAME_LABEL_WIDTH + TEXT_FIELD_WIDTH + VALUE_LABEL_WIDTH + 4 * COMPONENT_MARGIN);

        Button remBtn = new Button("R");

        AnchorPane.setTopAnchor(remBtn, 5.0);
        AnchorPane.setBottomAnchor(remBtn, 5.0);
        AnchorPane.setRightAnchor(remBtn, 5.0);

        attributeComponentMap.put(nameField, valueField);

        remBtn.setOnAction(e -> {
            attributeComponentMap.remove(nameField);
            content.getChildren().remove(anchorPane);
        });

        if (textValue != null && !textValue.isEmpty()) {
            valueField.setText(textValue);
        }

        if (nameValue != null && !nameValue.isEmpty()) {
            nameField.setText(nameValue);
        }

        anchorPane.getChildren().addAll(nameField, valueField, remBtn, nameLabel, valueLabel);
        content.getChildren().addAll(anchorPane);
    }

    public void initData(Map<String, String> attrs) {
        if (attrs == null) {
            addNewRow(contentVBox, null, null);
            return;
        }
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (name != null && !name.isEmpty()) {
                addNewRow(contentVBox, name, value);
            }
        }
        initialAttributeComponentMap = new HashMap<>(attributeComponentMap);
    }

    public List<AttributePair> getData() {
        //loop over every pair of textEdits and form an attribute
        List<AttributePair> pairs = new ArrayList<>(10);
        for (Map.Entry<TextField, TextField> entry : attributeComponentMap.entrySet()) {
            String name = entry.getKey().getText();
            String value = entry.getValue().getText();
            if (name == null || name.isEmpty()) {
                continue;
            }
            AttributePair pair = new AttributePair(name, value);
            pairs.add(pair);
        }
        return pairs;
    }
}
