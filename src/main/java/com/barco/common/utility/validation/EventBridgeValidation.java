package com.barco.common.utility.validation;

import com.barco.common.utility.BarcoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * This class use to validate the event-bridge
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventBridgeValidation extends UploadValidator {

    private final String URL_VALIDATION = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    private String name;
    private String bridgeUrl;
    private String description;
    private String bridgeType;

    public EventBridgeValidation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBridgeUrl() {
        return bridgeUrl;
    }

    public void setBridgeUrl(String bridgeUrl) {
        this.bridgeUrl = bridgeUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBridgeType() {
        return bridgeType;
    }

    public void setBridgeType(String bridgeType) {
        this.bridgeType = bridgeType;
    }

    /**
     * Method use to validate the batch
     * */
    @Override
    public void isValidBatch() {
        if (BarcoUtil.isNull(this.name)) {
            this.setErrorMsg(String.format("Name should not be empty at row %s.<br>", this.getRowCounter()));
        }
        if (BarcoUtil.isNull(this.bridgeUrl)) {
            this.setErrorMsg(String.format("Url should not be empty at row %s.<br>", this.getRowCounter()));
        } else if (!this.bridgeUrl.matches(this.URL_VALIDATION)) {
            this.setErrorMsg(String.format("Url not valid at row %s.<br>", this.getRowCounter()));
        }
        if (BarcoUtil.isNull(this.description)) {
            this.setErrorMsg(String.format("Description should not be empty at row %s.<br>", this.getRowCounter()));
        }
        if (BarcoUtil.isNull(this.bridgeType)) {
            this.setErrorMsg(String.format("Bridge Type should not be empty at row %s.<br>", this.getRowCounter()));
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
