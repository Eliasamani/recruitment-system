// /src/Presenter/JobPostPresenter.jsx
import React, { useState, useEffect } from 'react';
import JobpostView from '../View/JobpostView';
import { fetchJobPosts } from '../Model/fetchJobPosts';

export default function JobpostPresenter() {
  const [jobs, setJobs] = useState([]);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetchJobPosts()
      .then((data) => {
        setJobs(data);
        setIsLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setIsLoading(false);
      });
  }, []);

  if (isLoading) return <div>Loading job posts...</div>;
  if (error) return <div className="error-container">{error}</div>;

  return <JobpostView jobs={jobs} />;
}
