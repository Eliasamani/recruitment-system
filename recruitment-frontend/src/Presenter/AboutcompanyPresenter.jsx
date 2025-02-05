import React from 'react';
import Header from '../Reusablecomponent/Header'; // Import the reusable Header

export default function AboutcompanyPresenter () {
  return (
    <div className="about-page">
      {/* Use the reusable Header component */}
      <Header />

      <div style={{ padding: '2rem' }}>
        <h1>About HireMe</h1>
        <p>Welcome to HireMe! We are dedicated to helping you unlock your professional potential.</p>
      </div>
    </div>
  );
}