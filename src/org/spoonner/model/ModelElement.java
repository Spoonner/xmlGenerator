package org.spoonner.model;

import org.w3c.dom.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelElement {

    private Element tagElem;

    public ModelElement(Element elem) {
        this.tagElem = elem;
    }

    @Override
    public String toString() {
        String tagValue = extractTextValue();

        if (!tagValue.isEmpty())
            return tagElem.getTagName() + " : " + tagValue;
        else
            return tagElem.getTagName();
    }

    private String extractTextValue() {

        NodeList childNodes = tagElem.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == Node.TEXT_NODE)
                return childNodes.item(i).getNodeValue();
        }
        return "";
    }

    public void addAttribute(Attr attr) {
        tagElem.setAttributeNode(attr);
    }

    public void removeAttribute(Attr attr) {
        tagElem.removeAttributeNode(attr);
    }

    public void addChild(Element childElem) {
        tagElem.appendChild(childElem);
    }

    //returns map of name-value or null otherwise
    public Map<String, String> getAttributes() {
        Map<String, String> attributeMap = new HashMap<>();
        NamedNodeMap attributes = tagElem.getAttributes();

        if (attributes == null) {
            return null;
        }

        for (int i = 0; i < attributes.getLength(); i++) {
            Node node = attributes.item(i);
            String name = node.getNodeName();
            String value = node.getNodeValue();
            attributeMap.put(name, value);
        }
        return attributeMap;
    }

    public void removeChild() {
        tagElem.getParentNode().removeChild(this.tagElem);
    }

    public String getName() {
        return tagElem.getTagName();
    }

    public String getValue() {
        return tagElem.getNodeValue();
    }
}
