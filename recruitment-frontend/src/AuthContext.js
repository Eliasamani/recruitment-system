import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null); // null means not authenticated
  const [loading, setLoading] = useState(true);

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

export const useAuth = () => useContext(AuthContext);
