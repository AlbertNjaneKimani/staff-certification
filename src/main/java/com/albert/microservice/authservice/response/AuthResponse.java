package com.albert.microservice.authservice.response;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;

public class AuthResponse implements Serializable {

    private Header header;
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static class Header implements Serializable {
        private String requestRefId;
        private int responseCode;
        private HttpStatus responseMessage;
        private String customerMessage;
        private Instant timestamp;

        public String getRequestRefId() {
            return requestRefId;
        }

        public void setRequestRefId(String requestRefId) {
            this.requestRefId = requestRefId;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public HttpStatus getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(HttpStatus responseMessage) {
            this.responseMessage = responseMessage;
        }

        public String getCustomerMessage() {
            return customerMessage;
        }

        public void setCustomerMessage(String customerMessage) {
            this.customerMessage = customerMessage;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
        }
    }
}
