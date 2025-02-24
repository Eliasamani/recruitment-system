export const SignInFormModel = {
    username: '',
    password: ''
};

export const SignupFormModel = {
    firstname: '',
    lastname: '',
    personNumber: '',
    username: '',
    email: '',
    password: ''
};

// CandidateModel.js
export const fetchCompetences = async () => {
    try {
      const response = await fetch(`${process.env.REACT_APP_BACKEND_URL}/competences`)
      const data = await response.json();
      if (data.success) {
        return data.competences;
      } else {
        throw new Error('Error fetching competences');
      }
    } catch (err) {
      throw err;
    }
  };
  
  export const submitApplication = async (userId, expertise, availability) => {
    try {
      const response = await fetch(`${import.meta.env.VITE_BACKEND_URL}/applications/submit`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ userId, expertise, availability }),
      });
      const data = await response.json();
      if (response.ok) {
        return data;
      } else {
        throw new Error(data.message || 'Error submitting application');
      }
    } catch (err) {
      throw err;
    }
  };


export const fetchJobPosts = async () => {
    // Simulate a network delay of 500ms and return static job posts.
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve([
          {
            id: 1,
            title: "Fantastiska sommarjobb på Gröna Lund",
            company: "Parks and Resorts",
            location: "Stockholm",
            // No description provided – instead we list available roles:
            roles: ["Ticket sales", "Lotteries", "Roller coaster operation"]
          },
        ]);
      }, 500);
    });
  };
  