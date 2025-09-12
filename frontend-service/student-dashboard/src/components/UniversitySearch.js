import React, { useState } from 'react';
import { useLazyQuery } from '@apollo/client';
import { GET_STUDENTS_BY_UNIVERSITY } from '../graphql/queries';

const UniversitySearch = ({ onSelectStudent }) => {
  const [universityId, setUniversityId] = useState('');
  const [getStudents, { called, loading, error, data }] = useLazyQuery(
    GET_STUDENTS_BY_UNIVERSITY,
    {
      variables: { universityId },
      fetchPolicy: 'network-only' // Always fetch from network
    }
  );

  const handleSearch = () => {
    if (universityId.trim()) {
      getStudents();
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSearch();
    }
  };

  return (
    <div className="university-search">
      <h2>Search Students by University</h2>
      <div className="search-controls">
        <input
          type="text"
          value={universityId}
          onChange={(e) => setUniversityId(e.target.value)}
          onKeyPress={handleKeyPress}
          placeholder="Enter University ID"
        />
        <button onClick={handleSearch} disabled={loading || !universityId.trim()}>
          {loading ? 'Searching...' : 'Search'}
        </button>
      </div>
      
      {error && <div className="error">Error: {error.message}</div>}
      
      {called && !loading && data && (
        <div className="search-results">
          <h3>Search Results:</h3>
          {data.student.length === 0 ? (
            <p>No students found for this university.</p>
          ) : (
            data.student.map((student) => (
              <div 
                key={student.id} 
                className="student-result"
                onClick={() => onSelectStudent(student.id)}
              >
                <p><strong>{student.text}</strong> (Rating: {student.starRating})</p>
                <p className="click-hint">Click to monitor real-time updates</p>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default UniversitySearch;