package org.spoonner.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.spoonner.controller.AttributeEditorController;
import org.spoonner.controller.MainGUIController;
import org.spoonner.controller.TagNameDialogController;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class WindowDisplay {

    private static final String TAG_NAME_FXML_PATH = "/resources/tagNameDialog.fxml";
    private static final String ATTRIBUTE_FXML_PATH = "/resources/attributeDialog.fxml";

    public static TagNameDialogController showTagNameWindowAndHideText() {
        return showTagEditorWithSettings(true, null, null);
    }

    public static TagNameDialogController showTagNameWindowAndShowText() {
        return showTagEditorWithSettings(false, null, null);
    }

    public static TagNameDialogController showTagNameWindowsAndFillEdits(String tagName, String textValue) {
        return showTagEditorWithSettings(false, tagName, textValue);
    }

    private static TagNameDialogController showTagEditorWithSettings(boolean hiddenText, String tagName, String value) {
        URL url = MainGUIController.class.getResource(TAG_NAME_FXML_PATH);
        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = null;

        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        TagNameDialogController controller = loader.getController();

        if (hiddenText)
            controller.hideText();

        if (tagName != null)
            controller.setTagText(tagName);

        if (value != null)
            controller.setTextValue(value);

        stage.showAndWait();

        return controller;
    }

    public static AttributeEditorController showAttrbitesWindow(Map<String, String> data) {

        URL url = MainGUIController.class.getResource(ATTRIBUTE_FXML_PATH);
        FXMLLoader loader = new FXMLLoader(url);
        Scene scene = null;

        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        AttributeEditorController controller = loader.getController();
        controller.initData(data);
        stage.showAndWait();

        return controller;

    }

}
