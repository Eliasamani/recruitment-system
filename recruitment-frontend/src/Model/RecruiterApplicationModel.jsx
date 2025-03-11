/**
 * RecruiterApplicationModel - Manages the data and business logic for recruiter applications.
 * Handles API requests related to applications and authentication.
 */

/**
 * Fetches all recruiter applications.
 * 
 * @returns {Promise<Object[]>} Resolves to an array of application objects.
 * @throws {Error} If the request fails or the response is not OK.
 */
export const fetchApplications = async () => {
    try {
        const response = await fetch(
            `${process.env.REACT_APP_API_URL}/api/recruiter/applications`,
            { credentials: "include" }
        );
        
        // Check if the response is successful
        if (!response.ok) {
            throw new Error("Unable to retrieve applications.");
        }
        
        return await response.json();
    } catch (err) {
        console.error("Error fetching applications:", err);
        throw new Error("Could not fetch applications. Please try again later.");
    }
};

/**
 * Fetches the details of a specific application.
 * 
 * @param {string} applicationId - The ID of the application to retrieve.
 * @returns {Promise<Object>} Resolves to an application details object.
 * @throws {Error} If the request fails or the response is not OK.
 */
export const fetchApplicationDetails = async (applicationId) => {
    try {
        const response = await fetch(
            `${process.env.REACT_APP_API_URL}/api/recruiter/applications/${applicationId}`,
            { credentials: "include" }
        );
        
        // Check if the response is successful
        if (!response.ok) {
            throw new Error("Failed to fetch application details.");
        }
        
        return await response.json();
    } catch (error) {
        console.error("Error fetching application details:", error);
        throw error;
    }
};

/**
 * Updates the status of a specific application.
 * 
 * @param {string} id - The ID of the application to update.
 * @param {string} status - The new status to be set.
 * @returns {Promise<boolean>} Resolves to true if the update is successful.
 * @throws {Error} If the request fails or the response is not OK.
 */
export const updateApplicationStatus = async (id, status) => {
    try {
        const response = await fetch(
            `${process.env.REACT_APP_API_URL}/api/recruiter/applications/${id}/update-status`,
            {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ status }),
                credentials: "include",
            }
        );
        
        // Check if the response is successful
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Failed to update status");
        }
        
        return true;
    } catch (error) {
        console.error("Error updating application status:", error);
        throw error;
    }
};
