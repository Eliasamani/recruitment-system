
export const fetchApplications = async () => {
    const response = await fetch(process.env.REACT_APP_API_URL + '/api/applications');
    if (!response.ok) {
        throw new Error('Failed to fetch applications');
    }
    return await response.json();
};

export const fetchApplicationDetails = async (applicationId) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/applications/${applicationId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch application details');
    }
    return await response.json();
};

export const updateApplicationStatus = async (applicationId, status) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/applications/${applicationId}/status`, {
        method: 'PATCH',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ status }),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to update application status.');
    }
};
