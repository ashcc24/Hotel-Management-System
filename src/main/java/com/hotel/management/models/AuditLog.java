package com.hotel.management.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String timestamp;
    private String performedBy;
    private String action;
    private String details;

    public AuditLog(String performedBy, String action, String details) {
        this.timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.performedBy = performedBy;
        this.action = action;
        this.details = details;
    }

    public String getTimestamp()   { return timestamp; }
    public String getPerformedBy() { return performedBy; }
    public String getAction()      { return action; }
    public String getDetails()     { return details; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + performedBy + " | " + action + " | " + details;
    }
}
