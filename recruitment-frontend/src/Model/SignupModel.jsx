export const SignupFormModel = {
    firstname: '',
    lastname: '',
    personNumber: '',
    username: '',
    email: '',
    password: ''
};

export const validateSignupForm = (formData) => {
    const errors = {};
    
    if (!formData.firstname.trim()) errors.firstname = 'First name is required.';
    if (!formData.lastname.trim()) errors.lastname = 'Last name is required.';
    if (!formData.personNumber.trim()) errors.personNumber = 'Personnumber is required.';
    if (!formData.username.trim()) errors.username = 'Username is required.';
    if (!formData.email.trim()) errors.email = 'Email is required.';
    if (!formData.password.trim()) errors.password = 'Password is required.';

    // Personnumber validation
    const personNumberRegex = /^\d{8}-\d{4}$/;
    if (formData.personNumber && !personNumberRegex.test(formData.personNumber.trim())) {
        errors.personNumber = 'Invalid format (expected YYYYMMDD-XXXX)';
    }

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