/**
 * ProfileModel.js
 *
 * This module provides the data model and helper functions for the Profile functionality.
 * It includes a form model, form validation, and API calls for updating profile data.
 */

/**
 * Static form model for the profile.
 *
 * @constant
 */
export const ProfileFormModel = {
  firstName: '',
  lastName: '',
  email: '',
  username: '',
  personNumber: ''
};

/**
 * Validates the profile form data for email format only (if provided).
 *
 * @param {Object} formData - The profile form data.
 * @returns {Object} An object containing any errors.
 */
export const validateProfileForm = (formData) => {
  const errors = {};

  // Only validate email format if the user typed something
  if (formData.email.trim()) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(formData.email)) {
      errors.email = 'Invalid email format.';
    }
  }

  return { errors };
};

/**
 * Updates the user's profile.
 *
 * @param {Object} profileData - The profile data to update.
 * @returns {Promise<Object>} The JSON response from the server.
 * @throws {Error} If the update fails.
 */
export const updateProfile = async (profileData) => {
  const response = await fetch(
    process.env.REACT_APP_API_URL + '/api/users/edit',
    {
      credentials: "include",
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(profileData),
    }
  );

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Failed to update profile.');
  }

  return await response.json();
};
