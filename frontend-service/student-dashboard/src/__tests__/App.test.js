import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import App from '../App';

test('renders all navigation tabs', () => {
  render(
    <MockedProvider mocks={[]} addTypename={false}>
      <App />
    </MockedProvider>
  );
  
  // Use data-testid to uniquely identify tab buttons
  expect(screen.getByTestId('tab-add')).toBeInTheDocument();
  expect(screen.getByTestId('tab-update')).toBeInTheDocument();
  expect(screen.getByTestId('tab-details')).toBeInTheDocument();
  expect(screen.getByTestId('tab-subscription')).toBeInTheDocument();
});

test('switches between tabs', () => {
  render(
    <MockedProvider mocks={[]} addTypename={false}>
      <App />
    </MockedProvider>
  );
  
  // Click on the Update Student tab using test ID
  fireEvent.click(screen.getByTestId('tab-update'));
  
  // Check that the Update Student form is shown
  expect(screen.getByText(/update student details/i)).toBeInTheDocument();
});