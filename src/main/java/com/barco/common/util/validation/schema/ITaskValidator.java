package com.barco.common.util.validation.schema;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Nabeel.amd
 */
public interface ITaskValidator {

    public boolean isValid(String json);

    public boolean isValid(JsonNode jsonNode);

    public boolean isValid(File file);

    public boolean isValid(Reader reader);

    public TaskValidationResult validate(String json) throws IOException;

    public TaskValidationResult validate(JsonNode jsonNode) throws IOException;

    public TaskValidationResult validate(File file) throws IOException;

    public TaskValidationResult validate(Reader reader) throws IOException;
}
