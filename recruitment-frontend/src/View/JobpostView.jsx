// /src/View/JobPostView.jsx
import React from 'react';
import '../App.css';

export default function JobpostView({ jobs }) {
  return (
    <div className="job-posts">
      <h2>Available Job Posts</h2>
      {jobs.length === 0 ? (
        <p>No job posts available at the moment.</p>
      ) : (
        <ul className="job-list">
          {jobs.map((job) => (
            <li key={job.id} className="job-item">
              <h3>{job.title}</h3>
              <p><strong>Company:</strong> {job.company}</p>
              <p><strong>Location:</strong> {job.location}</p>
              {job.roles && job.roles.length > 0 ? (
                <>
                  <h4>Available Roles:</h4>
                  <ul>
                    {job.roles.map((role, idx) => (
                      <li key={idx}>{role}</li>
                    ))}
                  </ul>
                </>
              ) : (
                job.description && (
                  <>
                    <h4>Job Description:</h4>
                    <p>{job.description}</p>
                  </>
                )
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
