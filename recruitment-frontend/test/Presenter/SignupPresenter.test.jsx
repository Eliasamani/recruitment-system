import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import SignupPresenter from '../scr/Presenter/SignupPresenter';

// Mock useNavigate
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: jest.fn(),
}));

// Mock fetch
global.fetch = jest.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve({ message: 'Registration successful' }),
  })
);

test('renders signup presenter', () => {
  render(
    <MemoryRouter>
      <SignupPresenter />
    </MemoryRouter>
  );
  expect(screen.getByText(/Sign Up/i)).toBeInTheDocument();
});

test('submits signup form and redirects to login', async () => {
  render(
    <MemoryRouter>
      <SignupPresenter />
    </MemoryRouter>
  );

  fireEvent.change(screen.getByPlaceholderText(/First Name/i), { target: { value: 'John' } });
  fireEvent.change(screen.getByPlaceholderText(/Last Name/i), { target: { value: 'Doe' } });
  fireEvent.change(screen.getByPlaceholderText(/YYYYMMDD-XXXX/i), { target: { value: '19990101-1234' } });
  fireEvent.change(screen.getByPlaceholderText(/username/i), { target: { value: 'johndoe' } });
  fireEvent.change(screen.getByPlaceholderText(/name@mailservice.ex/i), { target: { value: 'john@example.com' } });
  fireEvent.change(screen.getByPlaceholderText(/password/i), { target: { value: 'securePass' } });

  fireEvent.submit(screen.getByRole('form'));

  await waitFor(() => expect(global.fetch).toHaveBeenCalled());
  expect(global.fetch).toHaveBeenCalledWith(expect.stringContaining('/api/register'), expect.any(Object));
});
