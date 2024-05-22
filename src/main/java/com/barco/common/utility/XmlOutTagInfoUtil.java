package com.barco.common.utility;

import com.barco.common.request.ConfigurationMakerRequest;
import com.barco.common.request.TagInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * @author Nabeel Ahmed
 */
@Component
public class XmlOutTagInfoUtil {

    public Logger logger = LoggerFactory.getLogger(XmlOutTagInfoUtil.class);

    final String BLANK = "";
    final String SPACE = "";
    final String UTF8 ="UTF-8";
    final String YES = "yes";
    final String NAME = "{http://xml.apache.org/xslt}indent-amount";
    final String VALUE = "2";

    /**
     * Method use to make the xml
     * @param xmlMakerRequest
     * @return String
     * */
    public String makeXml(ConfigurationMakerRequest xmlMakerRequest) throws Exception {
        logger.info("Process For Xml Create Start");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        String xml = null;
        if (xmlMakerRequest.getXmlTagsInfo() != null) {
            Document xmlDoc = builder.newDocument();
            boolean isParent = true;
            for (TagInfo tagInfo:
                xmlMakerRequest.getXmlTagsInfo()) {
                String tagKey = tagInfo.getTagKey();
                String tagParent = tagInfo.getTagParent();
                String tagValue = tagInfo.getTagValue();
                Element child;
                // for first node
                if (isParent) {
                    // first time it's consider as root
                    child = xmlDoc.createElementNS(BLANK, tagKey);
                    addTagValue(xmlDoc, child, tagValue);
                    xmlDoc.appendChild(child);
                    isParent = false;
                } else {
                    // if parent not define then skip this one
                    if (tagParent != null && !tagParent.equals(BLANK)) {
                        // first check if parent exist get the old parent else create the new once
                        NodeList nodeList = xmlDoc.getElementsByTagName(tagParent);
                        if (nodeList != null && nodeList.getLength() > 0) {
                            // old tag which append value
                            Node node = nodeList.item(nodeList.getLength()-1);
                            if (node != null && (tagKey != null && !tagKey.equals(BLANK))) {
                                child = xmlDoc.createElement(tagKey);
                                addTagValue(xmlDoc, child, tagValue);
                                node.appendChild(child);
                            }
                        } else {
                            // main second level child
                            Element parent = xmlDoc.createElement(tagParent);
                            if (tagKey != null && !tagKey.equals(BLANK)) {
                                child = xmlDoc.createElement(tagKey);
                                addTagValue(xmlDoc, child, tagValue);
                                parent.appendChild(child);
                                xmlDoc.getDocumentElement().appendChild(parent);
                            }
                        }
                    } else {
                        // main root level child
                        child = xmlDoc.createElement(tagKey);
                        addTagValue(xmlDoc, child, tagValue);
                        xmlDoc.getDocumentElement().appendChild(child);
                    }
                }
            }
            // below line use after all root
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, UTF8);
            transformer.setOutputProperty(OutputKeys.INDENT, YES);
            transformer.setOutputProperty(NAME, VALUE);
            DOMSource source = new DOMSource(xmlDoc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            transformer.transform(source,result);
            xml = result.getWriter().toString();
        }
        logger.info("Process For Xml Create End");
        return xml;
    }

    private void addTagValue(Document xmlDoc, Element child, String tagValue) {
        if (!BarcoUtil.isNull(tagValue)) {
            child.appendChild(xmlDoc.createTextNode(tagValue));
        } else {
            child.appendChild(xmlDoc.createTextNode(SPACE));
        }
    }

    @Override
    public String toString() { return new Gson().toJson(this); }

}