package com.barco.common.utility.validation;

import com.barco.common.utility.BarcoUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;

/**
 * This LookupDataValidation validate the information of the sheet
 * if the date not valid its stop the process and through the valid msg
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LookupDataValidation extends UploadValidator {

    // validation filed
    private final String REGEX = "^[-a-zA-Z0-9@\\.+_]+$";
    // business filed
    private String lookupCode;
    private String lookupType;
    private String lookupValue;
    private String uiLookup;
    private String description;
    //
    private Long parentLookupId;

    public LookupDataValidation() {
    }

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupCode() {
        return lookupCode;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public String getLookupValue() {
        return lookupValue;
    }

    public void setLookupValue(String lookupValue) {
        this.lookupValue = lookupValue;
    }

    public String getUiLookup() {
        return uiLookup;
    }

    public void setUiLookup(String uiLookup) {
        this.uiLookup = uiLookup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getParentLookupId() {
        return parentLookupId;
    }

    public void setParentLookupId(Long parentLookupId) {
        this.parentLookupId = parentLookupId;
    }

    /**
     * This isValidJobDetail use to validate the
     * job detail of the job valid return true
     * if non-valid return false
     * @return boolean true|false
     * */
    public void isValidBatch() {
        if (!BarcoUtil.isNull(this.parentLookupId) && BarcoUtil.isNull(this.lookupCode)) {
            this.setErrorMsg(String.format("LookupCode should not be empty at row %s.<br>", this.getRowCounter()));
        }
        if (BarcoUtil.isNull(this.lookupType)) {
            this.setErrorMsg(String.format("LookupType should not be empty at row %s.<br>", this.getRowCounter()));
        } else if (!this.lookupType.matches(this.REGEX)) {
            this.setErrorMsg(String.format("LookupType should not be non space latter at row %s.<br>", this.getRowCounter()));
        }
        if (BarcoUtil.isNull(this.lookupValue)) {
            this.setErrorMsg(String.format("LookupValue should not be empty at row %s.<br>", this.getRowCounter()));
        }
        if (BarcoUtil.isNull(this.uiLookup)) {
            this.setErrorMsg(String.format("UILookup should not be empty at row %s.<br>", this.getRowCounter()));
        } else if (!BarcoUtil.isNull(this.uiLookup) && (!this.uiLookup.equals("TRUE") && !this.uiLookup.equals("FALSE"))) {
            this.setErrorMsg(String.format("UILookup should not be empty and should be (TRUE|FALSE) at row %s.<br>", this.getRowCounter()));
        }
        if (BarcoUtil.isNull(this.description)) {
            this.setErrorMsg(String.format("Description should not be empty at row %s.<br>", this.getRowCounter()));
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
