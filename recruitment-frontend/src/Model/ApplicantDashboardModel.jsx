/**
 * ApplicantDashboardModel.js:
 * This module contains static data and helper functions for the Applicant Dashboard.
 */

/**
 * Static list of competences.
 * Note: Constant names are in uppercase.
 */
 export const competences = [
    { competence_id: 1, name: "ticket sales" },
    { competence_id: 2, name: "lotteries" },
    { competence_id: 3, name: "roller coaster operation" }
  ];

  /**
 * Submits an application to the back-end.
 *
 * @param {number} personId - The ID of the applicant.
 * @param {Array} expertise - An array of expertise objects.
 * @param {Array} availability - An array of availability periods.
 * @returns {Promise<Object>} The JSON response.
 * @throws {Error} If the submission fails.
 */
export const submitApplication = async (personId, expertise, availability) => {
    const response = await fetch(process.env.REACT_APP_API_URL + '/api/applications', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ person_id: personId, expertise, availability }),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to submit application.');
    }
};

/**
 * Validates the application data.
 *
 * @param {Array} expertise - An array of expertise objects.
 * @param {Array} availability - An array of availability periods.
 * @returns {Object} An object containing errors and an isValid flag.
 */
export const validateApplication = (expertise, availability) => {
    const errors = {};

    if (expertise.length === 0) errors.expertise = 'At least one expertise is required.';
    if (availability.length === 0) errors.availability = 'At least one availability period is required.';

    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};
