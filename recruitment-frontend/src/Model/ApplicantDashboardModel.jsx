export const fetchCompetences = async () => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/competences`);
    if (!response.ok) {
        throw new Error('Failed to fetch competences');
    }
    return await response.json();
};

export const submitApplication = async (personId, expertise, availability) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/applications`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ person_id: personId, expertise, availability }),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to submit application.');
    }
};

export const validateApplication = (expertise, availability) => {
    const errors = {};

    if (expertise.length === 0) errors.expertise = 'At least one expertise is required.';
    if (availability.length === 0) errors.availability = 'At least one availability period is required.';

    return {
        errors,
        isValid: Object.keys(errors).length === 0
    };
};
