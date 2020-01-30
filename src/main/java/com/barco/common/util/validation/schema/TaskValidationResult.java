package com.barco.common.util.validation.schema;

import com.google.gson.Gson;

/**
 * @author Nabeel.amd
 */
public class TaskValidationResult {

    private final boolean valid;
    private final String result;

    TaskValidationResult(boolean valid, String result) {
        this.valid = valid;
        this.result = result;
    }

    public boolean isValid() { return valid; }

    public String getResult() { return result; }

    @Override
    public String toString() { return new Gson().toJson(this); }
}
