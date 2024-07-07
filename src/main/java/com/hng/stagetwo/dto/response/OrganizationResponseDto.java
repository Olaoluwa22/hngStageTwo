package com.hng.stagetwo.dto.response;

public class OrganizationResponseDto {
    private String orgId;
    private String name;
    private String description;

    public OrganizationResponseDto(String orgId, String name, String description) {
        this.orgId = orgId;
        this.name = name;
        this.description = description;
    }

    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
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

    @Override
    public String toString() {
        return "OrganizationResponseDto{" +
                "orgId='" + orgId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
