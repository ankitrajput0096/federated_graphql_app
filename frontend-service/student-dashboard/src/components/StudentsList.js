import React, { useState } from 'react';
import { useLazyQuery } from '@apollo/client';
import { GET_STUDENTS_BY_UNIVERSITY } from '../graphql/queries';
import StudentDetailView from './StudentDetailView';

const StudentsList = () => {
  const [universityId, setUniversityId] = useState('');
  const [getStudents, { loading, error, data }] = useLazyQuery(
    GET_STUDENTS_BY_UNIVERSITY,
    {
      variables: { universityId },
      errorPolicy: 'all'
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
    <div className="students-list-container">
      <h2>Student Details</h2>
      
      <div className="university-input-section">
        <div className="form-group">
          <label htmlFor="universityId">Enter University ID:</label>
          <div className="search-controls">
            <input
              type="text"
              id="universityId"
              value={universityId}
              onChange={(e) => setUniversityId(e.target.value)}
              onKeyPress={handleKeyPress}
              placeholder="University ID"
            />
            <button onClick={handleSearch} disabled={loading || !universityId.trim()}>
              {loading ? 'Searching...' : 'Search'}
            </button>
          </div>
        </div>
      </div>

      {loading && <div className="loading">Loading students...</div>}
      
      {error && (
        <div className="error warning">
          Error: {error.message}
        </div>
      )}
      
      {data && data.student && (
        <>
          <h3>Students for University {universityId}</h3>
          <div className="students-list">
            {data.student.length === 0 ? (
              <p>No students found for this university.</p>
            ) : (
              data.student.map((student) => (
                <StudentDetailView key={student.id} student={student} />
              ))
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default StudentsList;