package org.aigu;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.json.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class JsonToXml {

    private static final String DEFAULT_ARRAY_ELEMENT_NAME = "ARRAY";
    private static final String DEFAULT_ARRAY_ITEM_TAG_POSTFIX = "_ITEM";
    private static final String DEFAULT_OBJECT_ELEMENT_NAME = "OBJECT";

    private static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
    private static final String NIL_ATTR = "xsi:nil";

    private Document doc = null;
    private Object inputJson;

    public void printOutput(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(System.out);
        transformer.transform(source, result);
    }

    public void setInput(JsonStructure inputJson) {
        this.inputJson = inputJson;
    }

    public Document getOutput() {
        return doc;
    }

    public void run() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
        parseJson(inputJson);
    }

    private void parseJsonArray(JsonArray jsonArray, Node root) {
        if (root == null) {
            root = doc.createElement(DEFAULT_ARRAY_ELEMENT_NAME);
            doc.appendChild(root);
        }
        for (JsonValue value : jsonArray.getValuesAs(JsonValue.class)) {
            Element child = doc.createElement(root.getNodeName() + DEFAULT_ARRAY_ITEM_TAG_POSTFIX);
            root.appendChild(child);
            JsonValue.ValueType valueType = value.getValueType();
            switch (valueType) {
                case OBJECT:
                    parseJsonObject((JsonObject) value, child);
                    break;
                case ARRAY:
                    Node subRoot = doc.createElement(DEFAULT_ARRAY_ELEMENT_NAME);
                    child.appendChild(subRoot);
                    parseJsonArray((JsonArray) value, subRoot);
                    break;
                case NUMBER:
                case TRUE:
                case FALSE:
                    child.appendChild(doc.createTextNode(value.toString()));
                    break;
                case NULL:
                    Attr attribute = doc.createAttributeNS(XSI_URI, NIL_ATTR);
                    attribute.appendChild(doc.createTextNode("true"));
                    child.setAttributeNodeNS(attribute);
                    break;
                default:
                    child.appendChild(doc.createTextNode(((JsonString) value).getString()));
                    break;
            }
        }
    }

    private void parseJsonObject(JsonObject jsonObject, Node root) {
        for (String key : jsonObject.keySet()) {
            JsonValue value = key == null ? null : jsonObject.get(key);

            Element element = doc.createElement(key);
            root.appendChild(element);

            JsonValue.ValueType valueType = value.getValueType();
            switch (valueType) {
                case ARRAY:
                    parseJsonArray((JsonArray) value, element);
                    break;
                case OBJECT:
                    parseJsonObject((JsonObject) value, element);
                    break;
                case NUMBER:
                case TRUE:
                case FALSE:
                    element.appendChild(doc.createTextNode(value.toString()));
                    break;
                case NULL:
                    Attr attribute = doc.createAttributeNS(XSI_URI, NIL_ATTR);
                    attribute.appendChild(doc.createTextNode("true"));
                    element.setAttributeNodeNS(attribute);
                    break;
                default:
                    element.appendChild(doc.createTextNode(((JsonString) value).getString()));
            }
        }
    }

    private void parseJson(Object object) throws JsonException, IllegalStateException {
        if (object instanceof JsonObject) {
            Element root = doc.createElement(DEFAULT_OBJECT_ELEMENT_NAME);
            doc.appendChild(root);
            parseJsonObject((JsonObject) object, root);
        } else if (object instanceof JsonArray) {
            Element root = doc.createElement(DEFAULT_ARRAY_ELEMENT_NAME);
            doc.appendChild(root);
            parseJsonArray((JsonArray) object, root);
        } else {
            throw new IllegalStateException("Cannot parse JSON: input is neither object nor array");
        }
    }
}






