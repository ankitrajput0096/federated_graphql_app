import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import StudentsList from '../components/StudentsList';
import { getStudentsByUniversityMock, errorMock } from './mockData';

describe('StudentsList', () => {
  it('renders university search input', () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <StudentsList />
      </MockedProvider>
    );
    
    expect(screen.getByPlaceholderText(/university id/i)).toBeInTheDocument();
    expect(screen.getByText(/search/i)).toBeInTheDocument();
  });

  it('searches for students by university', async () => {
    render(
      <MockedProvider mocks={[getStudentsByUniversityMock]} addTypename={false}>
        <StudentsList />
      </MockedProvider>
    );
    
    // Enter university ID and search
    fireEvent.change(screen.getByPlaceholderText(/university id/i), {
      target: { value: 'uni1' }
    });
    fireEvent.click(screen.getByText(/search/i));
    
    // Wait for results
    expect(await screen.findByText('John Doe')).toBeInTheDocument();
  });

  it('displays error message when query fails', async () => {
    render(
      <MockedProvider mocks={[errorMock]} addTypename={false}>
        <StudentsList />
      </MockedProvider>
    );
    
    // Enter university ID and search
    fireEvent.change(screen.getByPlaceholderText(/university id/i), {
      target: { value: 'uni1' }
    });
    fireEvent.click(screen.getByText(/search/i));
    
    // Wait for error message
    expect(await screen.findByText(/error/i)).toBeInTheDocument();
  });

  it('shows loading state while fetching', async () => {
    render(
      <MockedProvider mocks={[getStudentsByUniversityMock]} addTypename={false}>
        <StudentsList />
      </MockedProvider>
    );
    
    // Enter university ID and search
    fireEvent.change(screen.getByPlaceholderText(/university id/i), {
      target: { value: 'uni1' }
    });
    fireEvent.click(screen.getByText(/search/i));
    
    // Check for loading state
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });
});