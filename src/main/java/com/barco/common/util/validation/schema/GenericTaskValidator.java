package com.barco.common.util.validation.schema;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * @author Nabeel.amd
 */
public class GenericTaskValidator implements ITaskValidator {

    private final JsonSchema schema;

    GenericTaskValidator(String schemaResource) {
        try {
            JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonNode jsonNode = JsonLoader.fromResource(schemaResource);
            schema = factory.getJsonSchema(jsonNode);
        } catch (IOException | ProcessingException e) {
            throw new IllegalStateException("could not initialize validator due to previous exception", e);
        }
    }

    @Override
    public final boolean isValid(String json) {
        try {
            return validate(json).isValid();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public final boolean isValid(JsonNode jsonNode) {
        try {
            return validate(jsonNode).isValid();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public final boolean isValid(File file) {
        try {
            return validate(file).isValid();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public final boolean isValid(Reader reader) {
        try {
            return validate(reader).isValid();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public final TaskValidationResult validate(String json) throws IOException {
        JsonNode jsonNode = JsonLoader.fromString(json);
        return getValidationResult(jsonNode);
    }

    @Override
    public final TaskValidationResult validate(JsonNode jsonNode) throws IOException {
        return getValidationResult(jsonNode);
    }

    @Override
    public final TaskValidationResult validate(File file) throws IOException {
        JsonNode jsonNode = JsonLoader.fromFile(file);
        return getValidationResult(jsonNode);
    }

    @Override
    public final TaskValidationResult validate(Reader reader) throws IOException {
        JsonNode jsonNode = JsonLoader.fromReader(reader);
        return getValidationResult(jsonNode);
    }

    private final TaskValidationResult getValidationResult(JsonNode jsonNode) throws IOException {
        try {
            ProcessingReport processingReport = schema.validate(jsonNode);
            if (processingReport != null) {
                return new TaskValidationResult(processingReport.isSuccess(), processingReport.toString());
            } else {
                return new TaskValidationResult(false, null);
            }
        } catch (ProcessingException e) {
            throw new IOException(e.getMessage());
        }
    }
}
