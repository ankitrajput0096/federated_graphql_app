import { ApolloClient, InMemoryCache, ApolloProvider } from '@apollo/client';
import { render } from '@testing-library/react';
import React from 'react';

// Mock Apollo Client for tests
const mockClient = new ApolloClient({
  cache: new InMemoryCache(),
  link: () => {} // Mock link
});

// Custom render function with Apollo Provider
const renderWithApollo = (ui, options = {}) => {
  return render(ui, {
    wrapper: ({ children }) => (
      <ApolloProvider client={options.client || mockClient}>
        {children}
      </ApolloProvider>
    ),
    ...options,
  });
};

// Add this to the bottom of each empty test file
test('placeholder', () => {
  expect(true).toBe(true);
});

// Export everything from RTL
export * from '@testing-library/react';
export { renderWithApollo };