/**
 * ApplicantDashboardModel.js
 *
 * This module contains static data and helper functions for the Applicant Dashboard.
 */

/**
 * Static list of competences.
 * Note: Constant names are in uppercase.
 *
 * @constant
 */
export const competences = [
    { competence_id: 1, name: "ticket sales" },
    { competence_id: 2, name: "lotteries" },
    { competence_id: 3, name: "roller coaster operation" }
  ];
  

/**
 * In-memory JSON "file" that persists form data.
 *
 * @constant
 */
export let savedApplicationData = {
    selectedCompetence: '',
    yearsOfExperience: '',
    expertise: [],
    fromDate: '',
    toDate: '',
    availability: []
  };
  
  /**
   * Validates the application data.
   *
   * @param {Array} expertise - An array of expertise objects.
   * @param {Array} availability - An array of availability periods.
   * @returns {Object} An object containing any validation errors and a flag indicating if the data is valid.
   */
  export const validateApplication = (expertise, availability) => {
    const errors = {};
  
    if (expertise.length === 0) {
      errors.expertise = 'At least one expertise is required.';
    }
    if (availability.length === 0) {
      errors.availability = 'At least one availability period is required.';
    }
  
    return {
      errors,
      isValid: Object.keys(errors).length === 0
    };
  };
  