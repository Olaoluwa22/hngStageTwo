package com.hng.stagetwo.response;

import com.hng.stagetwo.model.Organization;

public class OrganizationResponse {
    private String status;
    private String message;
    private Organization data;

    public OrganizationResponse() {
    }

    public OrganizationResponse(String status, String message, Organization data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Organization getData() {
        return data;
    }
    public void setData(Organization data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OrganizationResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
