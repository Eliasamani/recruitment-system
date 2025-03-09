package se.kth.iv1201.recruitment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import se.kth.iv1201.recruitment.model.exception.InvalidStatusException;
import se.kth.iv1201.recruitment.model.jobApplication.JobApplicationDTO;
import se.kth.iv1201.recruitment.service.JobApplicationService;
import java.util.List;
import java.util.Map;

/**
 * Controller for job application related API operations.
 */
@RestController
@RequestMapping("/api/recruiter/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    /**
     * Retrieves all job applications from the system.
     * 
     * @return a ResponseEntity containing a list of all existing job applications
     */

    @GetMapping
    public ResponseEntity<List<JobApplicationDTO>> getAllApplications() {
        return ResponseEntity.ok(jobApplicationService.getAllApplications());
    }

    /**
     * fetch a job application by its id
     * 
     * @param id id of the job application to be fetched. Accessed via url
     *           path
     * @return a response entity with the job application that has the provided id
     */
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationDTO> getApplicationDetails(@PathVariable long id) {
        return ResponseEntity.ok(jobApplicationService.findApplicationById(id));
    }

    /**
     * update the status of the job application wiht the given id and status.
     * 
     * @param id          id of the job application to be updated. Accessed via url
     *                    path
     * @param requestBody the json object that provides the new status to be set
     * @return response entity with the success message or the error message if
     *         invalid status is given or other error occurs
     */
    @PostMapping("/{id}/update-status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable long id,
            @RequestBody Map<String, String> requestBody) {
        String statusString = requestBody.get("status");
        if (statusString == null || statusString.trim().isEmpty()) {
            throw new InvalidStatusException("null or empty");
        }
        try {
            JobApplicationDTO.Status status = JobApplicationDTO.Status.valueOf(statusString.toUpperCase());
            jobApplicationService.updateApplicationStatus(id, status);
            return ResponseEntity.ok().body(Map.of("message", "Application status updated successfully"));
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException(statusString);
        }
    }

}
