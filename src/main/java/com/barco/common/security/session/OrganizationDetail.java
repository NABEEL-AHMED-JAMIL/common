package com.barco.common.security.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDetail implements Serializable {

    private Long id;
    private String uuid;
    private String name;

    public OrganizationDetail() {}

    public Long getId() {
        return id;
    }

    public OrganizationDetail setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public OrganizationDetail setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getName() {
        return name;
    }

    public OrganizationDetail setName(String name) {
        this.name = name;
        return this;
    }

}
