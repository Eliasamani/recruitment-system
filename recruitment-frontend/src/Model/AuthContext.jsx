/**
 * AuthContext.jsx
 *
 * This module provides the authentication context for the application.
 * It includes functionality for session checking, login, logout,
 * as well as form validation and redirect logic.
 * 
 */

import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();


/**
 * AuthProvider component.
 *
 * Provides authentication state and functions (checkSession, login, logout)
 * to its child components.
 *
 * @param {Object} props - The component props.
 * @param {React.ReactNode} props.children - Child components.
 */
export const AuthProvider = ({ children }) => {
 // State indicating the current user (null means not authenticated)
 const [user, setUser] = useState(null);
 // State indicating whether a session check is in progress
 const [loading, setLoading] = useState(true);

 /**
  * Checks the current user session from the back-end.
  * Updates the user state based on the server response.
  */
   const checkSession = async () => {
    setLoading(true);
    try {
      const response = await fetch(
        process.env.REACT_APP_API_URL + '/api/auth/session',
        { credentials: 'include' }
      );
      if (response.status === 200) {
        const data = await response.json();
        setUser(data);
      } else if (response.status === 400){
        // Bad request – we set user as null
        setUser(null);
      } else if (response.status === 401) {
        // Not authenticated – we set user as null
        setUser(null);
      } else {
        console.error('Unexpected response status:', response.status);
        setUser(null);
      }
    } catch (error) {
      console.error('Error checking session:', error);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  // Run checkSession once on mount
  useEffect(() => {
    checkSession();
  }, []);

  /**
     * Attempts to log in using the provided credentials.
     *
     * @param {string} username - The username.
     * @param {string} password - The password.
     * @returns {boolean} True if login is successful; otherwise, false.
     */
  const login = async (username, password) => {
    try {
      const response = await fetch(
        process.env.REACT_APP_API_URL + '/api/auth/login',
        {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username, password }),
          credentials: 'include',
        }
      );
      if (response.status === 200) {
        // After successful login, update the user state.
        await checkSession();
        return true;
      }
      return false;
    } catch (error) {
      console.error('Login error:', error);
      return false;
    }
  };

   /**
     * Logs out the current user.
     */
  const logout = async () => {
    try {
      const response = await fetch(
        process.env.REACT_APP_API_URL + '/api/auth/logout',
        { method: 'POST', credentials: 'include' }
      );
      if (response.status === 200) {
        setUser(null);
      }
    } catch (error) {
      console.error('Logout error:', error);
    }
  };

  return (
    <AuthContext.Provider value={{ user, loading, checkSession, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
/**
 * Custom hook to access the authentication context.
 *
 * @returns {Object} The authentication context.
 */
export const useAuth = () => useContext(AuthContext);