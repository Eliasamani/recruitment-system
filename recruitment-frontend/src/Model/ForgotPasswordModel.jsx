/**
 * ForgotPasswordModel.js
 *
 * This module provides the data model and helper functions for the Forgot Password functionality.
 * It includes a form model, form validation, and API calls for requesting a reset code and resetting a password.
 */

// Static form model for forgot password
export const ForgotPasswordFormModel = {
    email: '',
    username: '',
    newPassword: '',
    resetCode: ''
};

/**
 * Validates the forgot password form data.
 *
 * @param {Object} formData - The form data to validate.
 * @returns {Object} An object containing any errors and a boolean flag indicating validity.
 */
export const validateForgotPasswordForm = (formData) => {
    const errors = {};

    // Check required fields
    if (!formData.email.trim()) {
        errors.email = 'Email is required.';
    }
    if (!formData.resetCode.trim()) {
        errors.resetCode = 'Reset code is required.';
    }

    // Validate email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (formData.email && !emailRegex.test(formData.email)) {
        errors.email = 'Invalid email format';
    }

    return {
        errors: errors,
        isValid: Object.keys(errors).length === 0
    };
};

/**
 * Requests a reset code to be sent to the specified email.
 *
 * @param {string} email - The email address to send the reset code to.
 * @returns {Promise<Object>} The JSON response from the server.
 * @throws {Error} If the request fails.
 */
export const requestResetCode = async (email) => {
    const response = await fetch(process.env.REACT_APP_API_URL + '/api/reset/code', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to send reset code.');
    }

    return await response.json();
};

/**
 * Resets the user's password using the provided user data.
 *
 * @param {Object} userData - An object containing email, username, new password, and reset code.
 * @returns {Promise<Object>} The JSON response from the server.
 * @throws {Error} If the password reset fails.
 */
export const resetPassword = async (userData) => {
    const response = await fetch(process.env.REACT_APP_API_URL + '/api/reset/password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(userData)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Reset failed.');
    }

    return await response.json();
};
