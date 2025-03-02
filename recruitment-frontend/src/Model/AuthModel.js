export const SignInFormModel = {
    username: '',
    password: ''
};

export const validateSignInForm = (formData) => {
    const errors = {};
    if (!formData.username.trim()) errors.username = 'Username is required';
    if (!formData.password.trim()) errors.password = 'Password is required';
    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};

export const getRedirectPathByRole = (role) => {
    if (role === 2) return '/applicant/dashboard';
    if (role === 1) return '/recruiter/dashboard';
    return '/'; // Default path
};