import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import StudentSubscription from '../components/StudentSubscription';

// Mock the useSubscription hook
jest.mock('@apollo/client', () => {
  const originalModule = jest.requireActual('@apollo/client');
  return {
    ...originalModule,
    useSubscription: () => ({
      data: null,
      error: null,
    }),
  };
});

describe('StudentSubscription', () => {
  it('renders subscription controls', () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <StudentSubscription studentId="1" />
      </MockedProvider>
    );
    
    expect(screen.getByText(/real-time updates/i)).toBeInTheDocument();
    expect(screen.getByText(/stop subscription/i)).toBeInTheDocument();
  });

  it('toggles subscription status', () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <StudentSubscription studentId="1" />
      </MockedProvider>
    );
    
    // Click the toggle button
    fireEvent.click(screen.getByText(/stop subscription/i));
    
    // Check that the button text changed
    expect(screen.getByText(/start subscription/i)).toBeInTheDocument();
  });

  it('displays message when no student ID is provided', () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <StudentSubscription studentId={null} />
      </MockedProvider>
    );
    
    // Check for the actual message displayed in the component
    expect(screen.getByText(/no updates received yet/i)).toBeInTheDocument();
  });
});