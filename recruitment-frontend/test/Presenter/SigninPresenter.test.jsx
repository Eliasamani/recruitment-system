/*
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import SigninPresenter from '../Presenter/SigninPresenter';
import { MemoryRouter } from 'react-router-dom';

// Mock API calls
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

describe('SigninPresenter', () => {
  const mockNavigate = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    (global as any).useNavigate = jest.fn(() => mockNavigate);
  });

  it('renders the sign-in form correctly', () => {
    render(
      <MemoryRouter>
        <SigninPresenter />
      </MemoryRouter>
    );

    // Check if the form elements are rendered
    expect(screen.getByText('Sign In')).toBeInTheDocument();
    expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
  });

  it('checks authentication status on mount and redirects if authenticated', async () => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      ok: true,
      json: jest.fn().mockResolvedValue({ role: 2 }), // Candidate role
    });

    render(
      <MemoryRouter>
        <SigninPresenter />
      </MemoryRouter>
    );

    // Wait for the API call and redirection
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        `${process.env.REACT_APP_API_URL}/api/session`,
        expect.objectContaining({ credentials: 'include' })
      );
      expect(mockNavigate).toHaveBeenCalledWith('/candidate'); // Redirects to candidate page
    });
  });

  it('handles successful login and redirects based on role', async () => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      ok: true,
      json: jest.fn().mockResolvedValue({ role: 1 }), // Recruiter role
    });

    render(
      <MemoryRouter>
        <SigninPresenter />
      </MemoryRouter>
    );

    // Fill in the form
    const usernameInput = screen.getByLabelText(/username/i);
    const passwordInput = screen.getByLabelText(/password/i);
    const submitButton = screen.getByRole('button', { name: /sign in/i });

    userEvent.type(usernameInput, 'testuser');
    userEvent.type(passwordInput, 'testpassword');
    fireEvent.click(submitButton);

    // Wait for the API call and redirection
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        `${process.env.REACT_APP_API_URL}/api/login`,
        expect.objectContaining({
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username: 'testuser', password: 'testpassword' }),
          credentials: 'include',
        })
      );
      expect(mockNavigate).toHaveBeenCalledWith('/recruiter'); // Redirects to recruiter page
    });
  });

  it('handles login failure and displays an error message', async () => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      ok: false,
      json: jest.fn().mockResolvedValue({ error: 'Invalid credentials' }),
    });

    render(
      <MemoryRouter>
        <SigninPresenter />
      </MemoryRouter>
    );

    // Fill in the form
    const usernameInput = screen.getByLabelText(/username/i);
    const passwordInput = screen.getByLabelText(/password/i);
    const submitButton = screen.getByRole('button', { name: /sign in/i });

    userEvent.type(usernameInput, 'testuser');
    userEvent.type(passwordInput, 'wrongpassword');
    fireEvent.click(submitButton);

    // Wait for the error message to appear
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        `${process.env.REACT_APP_API_URL}/api/login`,
        expect.objectContaining({
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username: 'testuser', password: 'wrongpassword' }),
          credentials: 'include',
        })
      );
      expect(screen.getByText('Login failed')).toBeInTheDocument();
    });
  });

  it('displays validation errors when fields are empty', async () => {
    render(
      <MemoryRouter>
        <SigninPresenter />
      </MemoryRouter>
    );

    // Submit the form without filling fields
    const submitButton = screen.getByRole('button', { name: /sign in/i });
    fireEvent.click(submitButton);

    // Check for error messages
    expect(await screen.findByText('Username is required')).toBeInTheDocument();
    expect(await screen.findByText('Password is required')).toBeInTheDocument();
  });
});
*/