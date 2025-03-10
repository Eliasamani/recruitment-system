package se.kth.iv1201.recruitment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.jobApplication.JobApplicationDTO;
import se.kth.iv1201.recruitment.RecruitmentBackendApplication;
import se.kth.iv1201.recruitment.model.exception.ApplicationNotFoundException;
import se.kth.iv1201.recruitment.model.jobApplication.JobApplication;
import se.kth.iv1201.recruitment.service.JobApplicationService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
@AutoConfigureMockMvc
@Transactional
public class JobApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobApplicationService jobApplicationService;

    /**
     * Test retrieving all job applications.
     * Verifies that the service returns the full list of applications from the
     * repository.
     */
    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testGetAllApplicationsReturnsList() throws Exception {
        JobApplicationDTO app1 = new JobApplication();
        JobApplicationDTO app2 = new JobApplication();
        List<JobApplicationDTO> applications = Arrays.asList(app1, app2);
        when(jobApplicationService.getAllApplications()).thenReturn(applications);
        mockMvc.perform(get("/api/recruiter/applications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    /**
     * Test retrieving all job applications when the repository is empty.
     * Verifies that the service returns an empty list and that the endpoint
     * returns an empty JSON array.
     */
    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testGetAllApplicationsReturnsEmptyList() throws Exception {
        when(jobApplicationService.getAllApplications()).thenReturn(Arrays.asList());
        mockMvc.perform(get("/api/recruiter/applications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    /**
     * Test retrieving the details of a job application when the ID is valid.
     * Verifies that the service returns the correct application and that the
     * endpoint returns the JSON representation of the application.
     */
    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testGetApplicationDetailsValidId() throws Exception {
        JobApplicationDTO application = new JobApplication();
        when(jobApplicationService.findApplicationById(1L)).thenReturn(application);
        mockMvc.perform(get("/api/recruiter/applications/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    /**
     * Test retrieving the details of a job application when the ID is invalid.
     * Verifies that the service returns null and that the endpoint returns an
     * empty JSON string.
     */
    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testGetApplicationDetailsInvalidId() throws Exception {
        when(jobApplicationService.findApplicationById(999L)).thenReturn(null);
        mockMvc.perform(get("/api/recruiter/applications/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    /**
     * Test updating the status of a job application with valid input.
     * Verifies that the service is called with the correct ID and status and that
     * the endpoint returns a JSON response with a success message.
     */
    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testUpdateApplicationStatusValidInput() throws Exception {
        doNothing().when(jobApplicationService).updateApplicationStatus(1L, JobApplicationDTO.Status.APPROVED);
        mockMvc.perform(post("/api/recruiter/applications/1/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \"APPROVED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application status updated successfully"));
        verify(jobApplicationService).updateApplicationStatus(1L, JobApplicationDTO.Status.APPROVED);
    }

    /**
     * Test updating the application status with a status string that has a
     * different
     * case than expected. Verifies that the status update is processed successfully
     * and is case-insensitive.
     */

    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testUpdateApplicationStatusCaseInsensitive() throws Exception {
        doNothing().when(jobApplicationService).updateApplicationStatus(1L, JobApplicationDTO.Status.APPROVED);
        mockMvc.perform(post("/api/recruiter/applications/1/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \"approved\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Application status updated successfully"));
        verify(jobApplicationService).updateApplicationStatus(1L, JobApplicationDTO.Status.APPROVED);
    }

    /**
     * Test updating the application status with an invalid status value.
     * Verifies that the endpoint returns a bad request status and an error message
     * indicating the invalid status value.
     */

    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testUpdateApplicationStatusInvalidStatus() throws Exception {
        mockMvc.perform(post("/api/recruiter/applications/1/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \"INVALID\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid status value: INVALID"));
    }

    /**
     * Test updating the application status with an invalid application ID.
     * Verifies that the endpoint returns a not found status and an error message
     * indicating that the application with the specified ID does not exist.
     */

    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testUpdateApplicationStatusInvalidId() throws Exception {
        doThrow(new ApplicationNotFoundException(999L))
                .when(jobApplicationService).updateApplicationStatus(999L, JobApplicationDTO.Status.APPROVED);

        mockMvc.perform(post("/api/recruiter/applications/999/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \"APPROVED\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Application not found with ID: 999"));
    }

    /**
     * Test updating the application status with a JSON object that does not
     * contain a "status" key. Verifies that the endpoint returns a bad request
     * status and an error message indicating that the "status" key is missing.
     */
    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testUpdateApplicationStatusMissingStatusKey() throws Exception {
        mockMvc.perform(post("/api/recruiter/applications/1/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid status value: null or empty"));
    }

    /**
     * Test updating the application status with invalid JSON input.
     * Verifies that the endpoint returns a bad request status when the request
     * body is not a valid JSON.
     */

    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testUpdateApplicationStatusInvalidJson() throws Exception {
        mockMvc.perform(post("/api/recruiter/applications/1/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("not-a-json"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test updating the application status with a status string that contains
     * leading
     * and trailing spaces. Verifies that the endpoint returns a bad request status
     * and an error message indicating the invalid status value.
     */

    @Test
    @WithMockUser(username = "recruiter", roles = { "RECRUITER" })
    public void testUpdateApplicationStatusWithSpaces() throws Exception {
        mockMvc.perform(post("/api/recruiter/applications/1/update-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\": \" APPROVED \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid status value:  APPROVED "));
    }

    /**
     * Test accessing the endpoint without the RECRUITER role.
     * Verifies that the endpoint returns a 403 Forbidden status when the user
     * does not have the RECRUITER role.
     */
    @Test
    @WithMockUser(username = "user", roles = { "APPLICANT" })
    public void testAccessWithoutRecruiterRole() throws Exception {
        // This endpoint should be forbidden for users not having the RECRUITER role
        mockMvc.perform(get("/api/recruiter/applications")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}