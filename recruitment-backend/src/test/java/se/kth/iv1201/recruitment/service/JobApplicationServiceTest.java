package se.kth.iv1201.recruitment.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.model.availability.Availability;
import se.kth.iv1201.recruitment.model.competence.Competence;
import se.kth.iv1201.recruitment.model.exception.ApplicationAlreadyExistsException;
import se.kth.iv1201.recruitment.model.exception.ApplicationNotFoundException;
import se.kth.iv1201.recruitment.model.jobApplication.JobApplication;
import se.kth.iv1201.recruitment.model.jobApplication.JobApplicationDTO;
import se.kth.iv1201.recruitment.model.person.Person;
import se.kth.iv1201.recruitment.repository.JobApplicationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the JobApplicationService class.
 * Uses Spring Boot's test framework with Mockito to mock the
 * JobApplicationRepository.
 */
@SpringBootTest
@Transactional
public class JobApplicationServiceTest {

    @Autowired
    private JobApplicationService jobApplicationService;

    @MockitoBean
    private JobApplicationRepository jobApplicationRepository;

    /**
     * Test retrieving all job applications.
     * Verifies that the service returns the full list of applications from the
     * repository.
     */
    @Test
    void testGetAllApplications() {
        JobApplication app1 = new JobApplication();
        JobApplication app2 = new JobApplication();
        List<JobApplication> applications = List.of(app1, app2);
        when(jobApplicationRepository.findAll()).thenReturn(applications);

        List<JobApplicationDTO> result = jobApplicationService.getAllApplications();

        assertEquals(2, result.size());
        assertTrue(result.contains(app1));
        assertTrue(result.contains(app2));
    }

    /**
     * Test saving a new job application.
     * Verifies that the application is saved with the correct fields and status
     * UNDETERMINED.
     */
    @Test
    void testSaveApplicationNew() {
        Person person = new Person();
        person.setId(1L); // Simulate a persisted person
        List<Competence> competences = List.of(new Competence());
        List<Availability> availabilities = List.of(new Availability());

        when(jobApplicationRepository.findByPerson(person)).thenReturn(null);
        when(jobApplicationRepository.save(any(JobApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JobApplication savedApplication = jobApplicationService.saveApplication(person, competences, availabilities);

        assertNotNull(savedApplication);
        assertEquals(person, savedApplication.getPerson());
        assertEquals(competences, savedApplication.getCompetences());
        assertEquals(availabilities, savedApplication.getAvailabilities());
        assertEquals(JobApplicationDTO.Status.UNDETERMINED, savedApplication.getStatus());
        verify(jobApplicationRepository).save(any(JobApplication.class));
    }

    /**
     * Test saving an application when one already exists for the person.
     * Verifies that an exception is thrown.
     */
    @Test
    void testSaveApplicationAlreadyExists() {
        Person person = new Person();
        person.setId(1L); // Simulate a persisted person
        JobApplication existingApplication = new JobApplication();
        when(jobApplicationRepository.findByPerson(person)).thenReturn(existingApplication);

        assertThrows(ApplicationAlreadyExistsException.class, () -> {
            jobApplicationService.saveApplication(person, List.of(), List.of());
        });
    }

    /**
     * Test finding an application by ID when it exists.
     * Verifies that the correct application is returned.
     */
    @Test
    void testFindApplicationByIdExists() {
        long id = 1L;
        JobApplication application = new JobApplication();
        when(jobApplicationRepository.findById(id)).thenReturn(Optional.of(application));

        JobApplicationDTO result = jobApplicationService.findApplicationById(id);

        assertEquals(application, result);
    }

    /**
     * Test finding an application by ID when it does not exist.
     * Verifies that an exception is thrown.
     */
    @Test
    void testFindApplicationByIdNotFound() {
        long id = 1L;
        when(jobApplicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class, () -> {
            jobApplicationService.findApplicationById(id);
        });
    }

    /**
     * Test updating the status of an existing application.
     * Verifies that the status is updated correctly.
     */
    @Test
    void testUpdateApplicationStatusExists() {
        long id = 1L;
        JobApplication application = new JobApplication();
        application.setStatus(JobApplicationDTO.Status.UNDETERMINED); // Initial status
        when(jobApplicationRepository.findById(id)).thenReturn(Optional.of(application));
        when(jobApplicationRepository.save(application)).thenReturn(application);

        jobApplicationService.updateApplicationStatus(id, JobApplicationDTO.Status.APPROVED);

        assertEquals(JobApplicationDTO.Status.APPROVED, application.getStatus());
        verify(jobApplicationRepository).save(application);
    }

    /**
     * Test updating the status of a non-existent application.
     * Verifies that an exception is thrown.
     */
    @Test
    void testUpdateApplicationStatusNotFound() {
        long id = 1L;
        when(jobApplicationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ApplicationNotFoundException.class, () -> {
            jobApplicationService.updateApplicationStatus(id, JobApplicationDTO.Status.APPROVED);
        });
    }
}