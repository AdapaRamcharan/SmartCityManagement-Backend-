package com.smartcity.backend.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FeedbackResponseDTO {
    @JsonProperty("feedbackId")
    private Long id;

    private String message;
    private Integer rating;
    private Long userId;
    private String userName;

    @JsonProperty("createdAt")
    private LocalDateTime timestamp;

    public FeedbackResponseDTO() {}

    public FeedbackResponseDTO(Long id, String message, Integer rating, Long userId, String userName, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.rating = rating;
        this.userId = userId;
        this.userName = userName;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
