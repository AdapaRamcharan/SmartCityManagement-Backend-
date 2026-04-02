package com.smartcity.backend.service;
import com.smartcity.backend.entity.*;
import com.smartcity.backend.repository.*;
import com.smartcity.backend.dto.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmartCityService {
    @Autowired private UserRepository userRepository;
    @Autowired private CityRepository cityRepository;
    @Autowired private AmenityRepository amenityRepository;
    @Autowired private IssueRepository issueRepository;
    @Autowired private FeedbackRepository feedbackRepository;
    @Autowired private LoginLogRepository loginLogRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRole() == null) user.setRole("USER");
        return userRepository.save(user);
    }

    public void logLogin(User user, String ip) {
        LoginLog log = new LoginLog();
        log.setUser(user);
        log.setUserId(user.getId());
        log.setLoginTimestamp(LocalDateTime.now());
        log.setIpAddress(ip);
        loginLogRepository.save(log);
    }

    public List<City> getAllCities() { return cityRepository.findAll(); }
    public List<Amenity> searchAmenities(Long cityId, String name) {
        return amenityRepository.findByCityIdAndNameContainingIgnoreCase(cityId, name);
    }

    public Issue reportIssue(Issue issue) {
        issue.setCity(resolveCity(issue.getCity()));
        issue.setUser(resolveUser(issue.getUser()));
        if (issue.getPriority() == null || issue.getPriority().isBlank()) {
            issue.setPriority("medium");
        }
        issue.setStatus("pending");
        issue.setTimestamp(LocalDateTime.now());
        return issueRepository.save(issue);
    }

    public List<Issue> getUserIssues(Long userId) { return issueRepository.findByUserId(userId); }
    public Feedback submitFeedback(Feedback feedback) {
        feedback.setCity(resolveCity(feedback.getCity()));
        feedback.setUser(resolveUser(feedback.getUser()));
        feedback.setTimestamp(LocalDateTime.now());
        return feedbackRepository.save(feedback);
    }

    public AdminStats getAdminStats() {
        AdminStats stats = new AdminStats();
        stats.setTotalUsers(userRepository.count());
        stats.setAdminCount(userRepository.countByRole("ADMIN"));
        stats.setUserCount(userRepository.countByRole("USER"));
        stats.setTotalIssues(issueRepository.count());
        stats.setTotalFeedback(feedbackRepository.count());
        return stats;
    }

    public List<LoginLog> getAllLoginLogs() { return loginLogRepository.findAll(); }
    public List<Issue> getAllIssues() { return issueRepository.findAll(); }
    public List<Feedback> getAllFeedback() { return feedbackRepository.findAll(); }
    public List<User> getAllUsers() { return userRepository.findAll(); }

    public Issue updateIssueStatus(Long id, String status) {
        Issue issue = issueRepository.findById(id).orElseThrow();
        issue.setStatus(status);
        return issueRepository.save(issue);
    }

    private City resolveCity(City city) {
        if (city == null || city.getId() == null) {
            throw new DataIntegrityViolationException("City is required");
        }
        return cityRepository.findById(city.getId())
            .orElseThrow(() -> new DataIntegrityViolationException("Invalid city id: " + city.getId()));
    }

    private User resolveUser(User user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        return userRepository.findById(user.getId())
            .orElseThrow(() -> new DataIntegrityViolationException("Invalid user id: " + user.getId()));
    }
}
