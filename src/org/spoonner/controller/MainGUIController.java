package org.spoonner.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.spoonner.model.AttributePair;
import org.spoonner.model.ModelElement;
import org.spoonner.utils.WindowDisplay;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.text.normalizer.NormalizerBase;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainGUIController implements Initializable {

    //use default size to draw rectangles
    //TODO: deprecated, remove later
 /*   private double RECTANGLE_WIDTH = 200;
    private double RECTANGLE_HEIGHT = 50;
    private Color RECTANGLE_FILL_COLOR = Color.TRANSPARENT;
    private double RECTANGLE_STROKE_WIDTH = 5;
    private Color RECTANGLE_STROKE_COLOR = Color.BLACK;
*/
    //TODO: remove this attribute

    private final String FILE_NAME = "testfile.xml";

    private Document doc;

    @FXML
    private Button btnGenerate;

    @FXML
    private Pane mainPane;

    @FXML
    private TreeView<ModelElement> treeView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ContextMenu menuNoRoot = new ContextMenu();
        MenuItem itemAddRoot = new MenuItem("Добавить корневой элемент");
        File existedFile = new File(FILE_NAME);

        //clear the file every time (for testing purposes)
        existedFile.delete();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            doc = docBuilder.newDocument();
            doc.setXmlStandalone(true);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        itemAddRoot.setOnAction(event -> {

            TagNameDialogController controller = WindowDisplay.showTagNameWindowAndHideText();
            String tagName = controller.getTagName();

            if (tagName == null)
                return;

            Element rootElem = doc.createElement(tagName);
            doc.appendChild(rootElem);

            ModelElement root = new ModelElement(rootElem);
            TreeItem<ModelElement> rootItem = new TreeItem<>(root);
            rootItem.setExpanded(true);
            treeView.setRoot(rootItem);
        });

        menuNoRoot.getItems().addAll(itemAddRoot);

        ContextMenu menu = new ContextMenu();
        MenuItem itemAddChild = new MenuItem("Добавить дочерний элемент");
        itemAddChild.setOnAction(event -> {
            TreeItem<ModelElement> currentSelection = treeView.getSelectionModel().getSelectedItem();
            if (currentSelection == null)
                return;
            TagNameDialogController controller = WindowDisplay.showTagNameWindowAndShowText();
            String tagName = controller.getTagName();
            if (tagName == null)
                return;
            String text = controller.getTextValue();

            Element child = doc.createElement(tagName);


//            child.setNodeValue(text);
//            child.setTextContent(text);
//            child.setNodeValue(text);


            child.appendChild(doc.createTextNode(text));

            currentSelection.getValue().addChild(child);

            ModelElement newElem = new ModelElement(child);

            TreeItem<ModelElement> treeItemToAdd = new TreeItem<>(newElem);
            currentSelection.getChildren().addAll(treeItemToAdd);
            currentSelection.setExpanded(true);
        });


        MenuItem itemAttributes = new MenuItem("Атрибуты...");
        itemAttributes.setOnAction(event -> {
            TreeItem<ModelElement> currentSelection = treeView.getSelectionModel().getSelectedItem();
            if (currentSelection == null)
                return;

            Map<String, String> attributeMap = currentSelection.getValue().getAttributes();
            AttributeEditorController controller = WindowDisplay.showAttrbitesWindow(attributeMap);
            List<AttributePair> attrList = controller.getData();

            for (AttributePair pair : attrList) {
                //form an attr
                Attr newAttr = doc.createAttribute(pair.getName());
                newAttr.setValue(pair.getValue());
                currentSelection.getValue().addAttribute(newAttr);
            }

        });

        MenuItem itemRemElement = new MenuItem("Удалить элемент");
        itemRemElement.setOnAction(event -> {
            TreeItem<ModelElement> currentSelection = treeView.getSelectionModel().getSelectedItem();

            if (currentSelection == null)
                return;

            currentSelection.getParent().getChildren().removeAll(currentSelection);
            currentSelection.getValue().removeChild();

        });


        MenuItem itemEditElement = new MenuItem("Изменить элемент");
        itemEditElement.setOnAction(event -> {
            TreeItem<ModelElement> currentSelection = treeView.getSelectionModel().getSelectedItem();

            if (currentSelection == null)
                return;

            String tagName = currentSelection.getValue().getName();
            String tagValue = currentSelection.getValue().getValue();
        });

        btnGenerate.setOnAction(event -> {
            //generate XML file
            try {
                File file = new File(FILE_NAME);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);
                transformer.transform(source, result);
            } catch (TransformerException tfe) {
                tfe.printStackTrace();
            }

        });

        menu.getItems().addAll(itemAddChild, itemAttributes, itemRemElement, itemEditElement);

        treeView.setOnContextMenuRequested(event -> {
            //request context menu

            for (MenuItem item : menu.getItems()) {
                item.setDisable(false);
            }

            for (MenuItem item : menuNoRoot.getItems()) {
                item.setDisable(false);
            }

            double x = event.getScreenX();
            double y = event.getScreenY();

            if (treeView.getRoot() == null) {
                //no root need to create one
                menuNoRoot.show(treeView, x, y);
                event.consume();
            } else {
                //there is root
                ObservableList<TreeItem<ModelElement>> selectedItems = treeView.getSelectionModel().getSelectedItems();
                if (selectedItems.isEmpty()) {
                    if (menu.isShowing()) {
                        menu.hide();
                    } else if (menuNoRoot.isShowing()) {
                        menuNoRoot.hide();
                    } else {
                        event.consume();
                        return;
                    }
                } else {
                    if (selectedItems.get(0).getParent() == null) {
                        //root is selected
                        menu.getItems().get(1).setDisable(true);
                        menu.getItems().get(2).setDisable(true);
                    }
                    menu.show(treeView, x, y);
                }
            }


        });

        treeView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                //on mouse clicked
                if (menu.isShowing()) {
                    menu.hide();
                    event.consume();
                }
                if (menuNoRoot.isShowing()) {
                    menuNoRoot.hide();
                    event.consume();
                }
            }
        });
    }

}
