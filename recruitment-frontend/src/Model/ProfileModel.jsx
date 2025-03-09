/**
 * ProfileModel.js
 *
 * This module provides the data model and helper functions for the Profile functionality.
 * It includes a form model, form validation, and API calls for updating profile data.
 * For resetting the person number, it provides a helper to generate a mailto link.
 */

/**
 * Static form model for the profile.
 *
 * @constant
 */
export const ProfileFormModel = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: ''
  };
  
  /**
   * Validates the profile form data.
   *
   * @param {Object} formData - The profile form data.
   * @returns {Object} An object containing any errors and a flag indicating whether the data is valid.
   */
  export const validateProfileForm = (formData) => {
    const errors = {};
  
    if (!formData.firstName.trim()) {
      errors.firstName = 'First name is required.';
    }
    if (!formData.lastName.trim()) {
      errors.lastName = 'Last name is required.';
    }
    if (!formData.username.trim()) {
      errors.username = 'Username is required.';
    }
    if (!formData.email.trim()) {
      errors.email = 'Email is required.';
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (formData.email && !emailRegex.test(formData.email)) {
      errors.email = 'Invalid email format.';
    }
    if (!formData.password.trim()) {
      errors.password = 'Password is required.';
    }
  
    return {
      errors,
      isValid: Object.keys(errors).length === 0
    };
  };
  
  /**
   * Updates the user's profile.
   *
   * @param {Object} profileData - The profile data to update.
   * @returns {Promise<Object>} The JSON response from the server.
   * @throws {Error} If the update fails.
   */
  export const updateProfile = async (profileData) => {
    const response = await fetch(process.env.REACT_APP_API_URL + '/api/reset/password', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(profileData)
    });
  
    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to update profile.');
    }
  
    return await response.json();
  };
  
  /**
   * Generates a mailto link for requesting a person number reset.
   *
   * @param {string} email - The user's email address.
   * @returns {string} A mailto link that opens the user's email client.
   */
  export const getPersonNumberResetLink = (email) => {
    return `mailto:support@yourcompany.com?subject=PersonNumber Reset Request&body=Please reset my person number. My email: ${email}`;
  };
  