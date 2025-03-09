import React, { useState } from 'react';
import ProfileView from '../View/ProfileView';
import {
  ProfileFormModel,
  validateProfileForm,
  updateProfile,
  getPersonNumberResetLink
} from '../Model/ProfileModel';

/**
 * ProfilePresenter component.
 *
 * This component manages the state and event handling for the Profile functionality.
 * It allows the user (applicant or recruiter) to update their first name, last name,
 * username, email, and password. For resetting the person number, it provides a mailto link.
 *
 * @component
 */
const ProfilePresenter = () => {
  const [formData, setFormData] = useState(ProfileFormModel);
  const [updateError, setUpdateError] = useState('');
  const [updateSuccess, setUpdateSuccess] = useState('');

  /**
   * Handles the event to update the profile.
   *
   * @param {Object} event - The event object.
   */
  const handleUpdateProfile = async (event) => {
    event.preventDefault();
    setUpdateError('');
    setUpdateSuccess('');

    const validation = validateProfileForm(formData);
    if (!validation.isValid) {
      setUpdateError(Object.values(validation.errors).join(' '));
      return;
    }

    try {
      await updateProfile(formData);
      setUpdateSuccess('Profile updated successfully.');
    } catch (error) {
      setUpdateError(error.message);
    }
  };

  return (
    <ProfileView
      firstName={formData.firstName}
      setFirstName={(value) => setFormData({ ...formData, firstName: value })}
      lastName={formData.lastName}
      setLastName={(value) => setFormData({ ...formData, lastName: value })}
      username={formData.username}
      setUsername={(value) => setFormData({ ...formData, username: value })}
      email={formData.email}
      setEmail={(value) => setFormData({ ...formData, email: value })}
      password={formData.password}
      setPassword={(value) => setFormData({ ...formData, password: value })}
      updateError={updateError}
      updateSuccess={updateSuccess}
      handleUpdateProfile={handleUpdateProfile}
      personNumberResetLink={getPersonNumberResetLink(formData.email)}
    />
  );
};

export default ProfilePresenter;
