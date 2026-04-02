package com.smartcity.backend.controller;
import com.smartcity.backend.dto.*;
import com.smartcity.backend.entity.*;
import com.smartcity.backend.security.JwtUtils;
import com.smartcity.backend.service.SmartCityService;
import com.smartcity.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class SmartCityController {
    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserRepository userRepository;
    @Autowired JwtUtils jwtUtils;
    @Autowired SmartCityService smartCityService;

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtUtils.generateJwtToken(auth.getName());
        User u = userRepository.findByUsername(auth.getName()).get();
        smartCityService.logLogin(u, request.getRemoteAddr());
        return ResponseEntity.ok().body(new JwtResponse(jwt, u.getUsername(), u.getRole()));
    }

    @GetMapping("/cities")
    public List<City> getCities() {
        return smartCityService.getAllCities();
    }

    @GetMapping("/amenities/search")
    public List<Amenity> searchAmenities(@RequestParam Long cityId, @RequestParam String name) {
        return smartCityService.searchAmenities(cityId, name);
    }

    @PostMapping("/issues")
    public Issue reportIssue(@RequestBody Issue issue) {
        return smartCityService.reportIssue(issue);
    }

    @GetMapping("/issues/user/{userId}")
    public List<Issue> getUserIssues(@PathVariable Long userId) {
        return smartCityService.getUserIssues(userId);
    }

    @PostMapping("/feedback")
    public Feedback submitFeedback(@RequestBody Feedback feedback) {
        return smartCityService.submitFeedback(feedback);
    }

    @GetMapping("/admin/stats")
    public AdminStats getStats() {
        return smartCityService.getAdminStats();
    }

    @GetMapping({"/admin/logs", "/admin/login-history"})
    public List<LoginLog> getLogs() {
        return smartCityService.getAllLoginLogs();
    }

    @GetMapping({"/users", "/admin/users"})
    public List<User> getUsers() {
        return smartCityService.getAllUsers();
    }

    @GetMapping({"/admin/issues", "/issues"})
    public List<Issue> getAllIssues() {
        return smartCityService.getAllIssues();
    }

    @PutMapping("/admin/issues/{id}")
    public Issue updateIssue(@PathVariable Long id, @RequestParam String status) {
        return smartCityService.updateIssueStatus(id, status);
    }

    @GetMapping({"/admin/feedback", "/feedback"})
    public List<Feedback> getAllFeedback() {
        return smartCityService.getAllFeedback();
    }
}
