/**
 * SignupModel.js
 *
 * This module contains the data model and helper functions for user registration.
 * It provides a static form model, form validation, and an API call to register a new user.
 */

// Static form model for user registration.

export const SignupFormModel = {
    firstname: '',
    lastname: '',
    personNumber: '',
    username: '',
    email: '',
    password: ''
};

/**
 * Validates the signup form data.
 *
 * @param {Object} formData - The form data to validate.
 * @returns {Object} An object containing any errors and a boolean flag indicating if the form is valid.
 */
export const validateSignupForm = (formData) => {
    const errors = {};

    // Validate required fields.
    if (!formData.firstname.trim()) errors.firstname = 'First name is required.';
    if (!formData.lastname.trim()) errors.lastname = 'Last name is required.';
    if (!formData.personNumber.trim()) errors.personNumber = 'Personnumber is required.';
    if (!formData.username.trim()) errors.username = 'Username is required.';
    if (!formData.email.trim()) errors.email = 'Email is required.';
    if (!formData.password.trim()) errors.password = 'Password is required.';

    // Validate personnumber format (expected format: YYYYMMDD-XXXX).
    const personNumberRegex = /^\d{8}-\d{4}$/;
    if (formData.personNumber && !personNumberRegex.test(formData.personNumber.trim())) {
        errors.personNumber = 'Invalid format (expected YYYYMMDD-XXXX)';
    }

    // Validate email format.
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (formData.email && !emailRegex.test(formData.email)) {
        errors.email = 'Invalid email format';
    }

    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};

/**
 * Registers a user by sending the provided data to the registration API.
 *
 * @param {Object} userData - The registration data.
 * @returns {Promise<Object>} The JSON response from the server.
 * @throws {Error} If the registration fails.
 */
export const registerUser = async (userData) => {
    const response = await fetch(process.env.REACT_APP_API_URL + '/api/users/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    });
    
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Registration failed');
    }
    
    return await response.json();
};