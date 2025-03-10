/**
 * Model for the sign-in form.
 */
export const SignInFormModel = {
    username: '',
    password: ''
};


/**
 * Validates the sign-in form data.
 *
 * @param {Object} formData - The sign-in form data.
 * @returns {Object} An object containing any validation errors and an isValid flag.
 */
export const validateSignInForm = (formData) => {
    const errors = {};
    if (!formData.username.trim()) errors.username = 'Username is required';
    if (!formData.password.trim()) errors.password = 'Password is required';
    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};

/**
 * Determines the redirect path based on the user's role.
 *
 * @param {number} role - The role of the user.
 * @returns {string} The redirect URL.
 */
export const getRedirectPathByRole = (role) => {
    if (role === 2) return '/applicant/dashboard';
    if (role === 1) return '/recruiter/dashboard';
    return '/'; // Default path
};