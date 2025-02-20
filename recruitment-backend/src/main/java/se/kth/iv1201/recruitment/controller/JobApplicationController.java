package se.kth.iv1201.recruitment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.iv1201.recruitment.model.JobApplication;
import se.kth.iv1201.recruitment.service.JobApplicationService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> getAllApplications() {
        return ResponseEntity.ok(jobApplicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getApplicationDetails(@PathVariable long id) {
        return ResponseEntity.ok(jobApplicationService.findApplicationById(id));
    }

    @PostMapping("/{id}/update-status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable long id,
            @RequestBody Map<String, String> requestBody) {
        try {
            String statusString = requestBody.get("status");
            JobApplication.Status status = JobApplication.Status.valueOf(statusString.toUpperCase());
            jobApplicationService.updateApplicationStatus(id, status);
            return ResponseEntity.ok().body(Map.of("message", "Application status updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status value"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Unexpected error updating application status"));
        }
    }

}
