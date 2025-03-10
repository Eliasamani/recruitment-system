/**
 * fetches the applications from the backend
 * @returns 
 */
export const fetchApplications = async () => {
    const response = await fetch(process.env.REACT_APP_API_URL + '/api/applications');
    if (!response.ok) {
        throw new Error('Failed to fetch applications');
    }
    return await response.json();
};
/**
 * fetches the details of a specific application
 * @param {*} applicationId 
 * @returns 
 */
export const fetchApplicationDetails = async (applicationId) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/applications/${applicationId}`);
    if (!response.ok) {
        throw new Error('Failed to fetch application details');
    }
    return await response.json();
};
/**
 * Sends the updated application status to the backend.
 * @param {*} applicationId the application id
 * @param {*} status the status of the application
 */
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
