package com.hng.stagetwo.response;

import com.hng.stagetwo.dto.response.OrganizationResponseDto;

import java.util.List;

public class OrganizationsResponse {
    private String status;
    private String message;
    private List<OrganizationResponseDto> data;

    public OrganizationsResponse(String status, String message, List<OrganizationResponseDto> data) {
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
    public List<OrganizationResponseDto> getData() {
        return data;
    }
    public void setData(List<OrganizationResponseDto> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "OrganizationsResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
