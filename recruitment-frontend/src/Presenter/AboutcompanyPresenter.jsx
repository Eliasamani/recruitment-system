/**
 * AboutCompanyPresenter.jsx
 *
 * This component presents the "About HireMe" page.
 * It uses a reusable Header component and displays static information about the company.
 *
 * Following Oracle’s Java coding conventions for naming, formatting, and documentation.
 */
import React from 'react';
import Header from '../Components/Header'; // Import the reusable Header

export default function AboutcompanyPresenter() {
    return (
        <div className="about-page">
            <Header />
            <div style={{ padding: '2rem' }}>
                <h1>About HireMe</h1>
                <p>Welcome to HireMe! We are dedicated to helping you unlock your professional potential.</p>
            </div>
        </div>
    );
}