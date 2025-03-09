import React, { useState, useEffect } from 'react';
import ProfileView from '../View/ProfileView';
import { useAuth } from "../Model/AuthContext.jsx";
import {
  ProfileFormModel,
  validateProfileForm,
  updateProfile,
  getPersonNumberResetLink
} from '../Model/ProfileModel';

const ProfilePresenter = () => {
  const { user } = useAuth();
  const [formData, setFormData] = useState(ProfileFormModel);
  const [updateError, setUpdateError] = useState('');
  const [updateSuccess, setUpdateSuccess] = useState('');

  // When the user data is available from useAuth, initialize formData.
  useEffect(() => {
    if (user) {
      setFormData({
        ...ProfileFormModel,
        username: user.username,
        firstName: user.firstName,
        lastName: user.lastName,
        personNumber: user.personNumber,
        email: user.email,
      });
    }
  }, [user]);

  
  // If user.personNumber exists (from useAuth), then editing is disabled.
  const personNumberEditable = !user?.personNumber;
  const personNumberValue = user?.personNumber || formData.personNumber;
  // Only update personNumber in state if the field is editable.
  const setPersonNumberProp = personNumberEditable
    ? (value) => setFormData({ ...formData, personNumber: value })
    : () => {};

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
      username={formData.username}
      firstName={formData.firstName}
      setFirstName={(value) => setFormData({ ...formData, firstName: value })}
      lastName={formData.lastName}
      setLastName={(value) => setFormData({ ...formData, lastName: value })}
      email={formData.email}
      setEmail={(value) => setFormData({ ...formData, email: value })}
      personNumber={personNumberValue}
      setPersonNumber={setPersonNumberProp}
      personNumberEditable={personNumberEditable}
      updateError={updateError}
      updateSuccess={updateSuccess}
      handleUpdateProfile={handleUpdateProfile}
      personNumberResetLink={getPersonNumberResetLink(formData.email)}
    />
  );
};

export default ProfilePresenter;
