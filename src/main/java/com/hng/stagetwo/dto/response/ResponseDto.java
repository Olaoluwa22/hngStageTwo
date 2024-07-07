package com.hng.stagetwo.dto.response;

import com.hng.stagetwo.utils.Data;

public class ResponseDto {

    private String status;
    private String message;
    private Data data;

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
    public Data getData() {
        return data;
    }
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
