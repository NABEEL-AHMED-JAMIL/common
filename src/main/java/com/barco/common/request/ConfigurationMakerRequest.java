package com.barco.common.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationMakerRequest {

    private List<TagInfo> xmlTagsInfo;
    private List<TagInfo> jsonTagsInfo;

    public ConfigurationMakerRequest() {}

    public List<TagInfo> getXmlTagsInfo() {
        return xmlTagsInfo;
    }

    public void setXmlTagsInfo(List<TagInfo> xmlTagsInfo) {
        this.xmlTagsInfo = xmlTagsInfo;
    }

    public List<TagInfo> getJsonTagsInfo() {
        return jsonTagsInfo;
    }

    public void setJsonTagsInfo(List<TagInfo> jsonTagsInfo) {
        this.jsonTagsInfo = jsonTagsInfo;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
