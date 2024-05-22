package com.barco.common.utility;

import com.barco.common.request.ConfigurationMakerRequest;
import com.barco.common.request.TagInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel Ahmed
 */
@Component
public class JsonOutTagInfoUtil {

    private final Logger logger = LoggerFactory.getLogger(JsonOutTagInfoUtil.class);

    public String makeJson(ConfigurationMakerRequest jsonMakerRequest) throws Exception {
        logger.info("Process For JSON Create Start");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonNode = objectMapper.createObjectNode();
        if (jsonMakerRequest.getJsonTagsInfo() != null) {
            boolean isParent = true;
            for (TagInfo tagInfo : jsonMakerRequest.getJsonTagsInfo()) {
                String tagKey = tagInfo.getTagKey();
                String tagParent = tagInfo.getTagParent();
                String tagValue = tagInfo.getTagValue();
                // for first node
                if (isParent) {
                    // first time it's considered as root
                    addTagValue(jsonNode, tagKey, tagValue);
                    isParent = false;
                } else {
                    // if parent not defined, then skip this one
                    if (tagParent != null && !tagParent.equals("")) {
                        JsonNode parentNode = jsonNode.path(tagParent);
                        if (!parentNode.isMissingNode()) {
                            if (parentNode.isObject()) {
                                ObjectNode parentObject = (ObjectNode) parentNode;
                                addTagValue(parentObject, tagKey, tagValue);
                            }
                        } else {
                            ObjectNode parentObject = jsonNode.putObject(tagParent);
                            addTagValue(parentObject, tagKey, tagValue);
                        }
                    } else {
                        addTagValue(jsonNode, tagKey, tagValue);
                    }
                }
            }
        }
        logger.info("Process For JSON Create End");
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
    }

    private void addTagValue(ObjectNode parentNode, String tagKey, String tagValue) {
        if (tagValue != null && !tagValue.equals("")) {
            parentNode.put(tagKey, tagValue);
        } else {
            parentNode.put(tagKey, "");
        }
    }

    @Override
    public String toString() { return new Gson().toJson(this); }
}
