package com.barco.common.utility.validation;

import com.barco.common.utility.BarcoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * This RPPValidation validate the information of the sheet
 * if the date not valid its stop the process and through the valid msg
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RPPValidation {

    private final String REGEX = "^[-a-zA-Z0-9@\\.+_]+$";
    private Integer rowCounter = 0;
    private String errorMsg;
    // business filed
    private String name;
    private String description;

    public RPPValidation() {
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This isValidJobDetail use to validate the
     * job detail of the job valid return true
     * if non-valid return false
     * @return boolean true|false
     * */
    public void isValidLookup() {
        if (BarcoUtil.isNull(this.name)) {
            this.setErrorMsg(String.format("Name should not be empty at row %s.<br>", rowCounter));
        } else if (!this.name.matches(this.REGEX)) {
            this.setErrorMsg(String.format("Name should not be non space latter at row %s.<br>", rowCounter));
        }
        if (BarcoUtil.isNull(this.description)) {
            this.setErrorMsg(String.format("Description should not be empty at row %s.<br>", rowCounter));
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
