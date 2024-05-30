package com.barco.common.utility.validation;

import com.barco.common.utility.BarcoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * Abstract class use to give the batch validation detail to sub class
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class UploadValidator {

    private Integer rowCounter = 0;
    private String errorMsg;

    public UploadValidator() {
    }

    public abstract void isValidBatch();

    public Integer getRowCounter() {
        return rowCounter;
    }

    public void setRowCounter(Integer rowCounter) {
        this.rowCounter = rowCounter;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        if (BarcoUtil.isNull(this.errorMsg)) {
            this.errorMsg = errorMsg;
        } else {
            this.errorMsg += errorMsg;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
