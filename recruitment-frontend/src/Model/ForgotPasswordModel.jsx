export const ForgotPasswordFormModel = {
    email: '',
    username: '',
    newPassword: '',
    resetCode: ''
};

export const validateForgotPasswordForm = (formData) => {
    const errors = {};

    if (!formData.email.trim()) errors.email = 'Email is required.';
    if (!formData.resetCode.trim()) errors.resetCode = 'Reset code is required.';

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (formData.email && !emailRegex.test(formData.email)) {
        errors.email = 'Invalid email format';
    }

    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};

export const requestResetCode = async (email) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/reset/code`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email })
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to send reset code.');
    }

    return await response.json();
};

export const resetPassword = async (userData) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/reset/password`, {
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
