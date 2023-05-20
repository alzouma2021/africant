package com.africanb.africanb.helper.contrat;

import com.africanb.africanb.helper.status.Status;

public class ResponseBase {

    protected Status status;
    protected boolean	hasError;
    protected String	sessionUser;
    protected Long		count;
    protected String  	filePath;

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
    public boolean isHasError() {
        return hasError;
    }
    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }
    public String getSessionUser() {
        return sessionUser;
    }
    public void setSessionUser(String sessionUser) {
        this.sessionUser = sessionUser;
    }
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "ResponseBase{" +
                "status=" + status +
                ", hasError=" + hasError +
                ", sessionUser='" + sessionUser + '\'' +
                ", count=" + count +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
