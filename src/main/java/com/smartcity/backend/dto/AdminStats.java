package com.smartcity.backend.dto;
public class AdminStats {
    private long totalUsers;
    private long adminCount;
    private long userCount;
    private long totalIssues;
    private long totalFeedback;
    public AdminStats() {}
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getAdminCount() { return adminCount; }
    public void setAdminCount(long adminCount) { this.adminCount = adminCount; }
    public long getUserCount() { return userCount; }
    public void setUserCount(long userCount) { this.userCount = userCount; }
    public long getTotalIssues() { return totalIssues; }
    public void setTotalIssues(long totalIssues) { this.totalIssues = totalIssues; }
    public long getTotalFeedback() { return totalFeedback; }
    public void setTotalFeedback(long totalFeedback) { this.totalFeedback = totalFeedback; }
}
