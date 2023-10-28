package com.oodwj_assignment.Models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Sessions extends Model {
    private UUID userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration; // Duration in milliseconds
    private String ipAddress;
    private String userAgent;
    private String location;
    private String deviceInfo;
    private boolean isAuthenticated;
    private String referer;
    private String terminationReason;
    private boolean isActive;
    private UUID sessionToken;

    public Sessions(UUID sessionId, UUID userId, LocalDateTime startTime, LocalDateTime endTime, long duration,
                    String ipAddress, String userAgent, String location, String deviceInfo,
                    boolean isAuthenticated, String referer, String terminationReason,
                    boolean isActive, UUID sessionToken, LocalDateTime updatedAt, LocalDateTime createdAt) {

        super(sessionId, updatedAt, createdAt);
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.location = location;
        this.deviceInfo = deviceInfo;
        this.isAuthenticated = isAuthenticated;
        this.referer = referer;
        this.terminationReason = terminationReason;
        this.isActive = isActive;
        this.sessionToken = sessionToken;
    }

    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getLocation() {
        return location;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public String getReferer() {
        return referer;
    }

    public String getTerminationReason() {
        return terminationReason;
    }

    public UUID getSessionToken() {
        return sessionToken;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public void setTerminationReason(String terminationReason) {
        this.terminationReason = terminationReason;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setSessionToken(UUID sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String toString() {
        return getId() + ";" + getUserId() + ";" + getStartTime() + ";" + getEndTime() + ";" + getDuration() + ";" +
                getIpAddress() + ";" + getUserAgent() + ";" + getLocation() + ";" + getDeviceInfo() + ";" +
                isAuthenticated() + ";" + getReferer() + ";" + getTerminationReason() + ";" + isActive() + ";" +
                getSessionToken() + ";" + getUpdatedAt() + ";" + getCreatedAt();
    }
}

