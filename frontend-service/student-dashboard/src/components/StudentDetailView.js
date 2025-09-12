import React from 'react';

const StudentDetailView = ({ student }) => {
  // Safely handle potentially null grade field
  const safeGrade = student.grade || {
    studentId: 'N/A',
    grade: 'N/A',
    gpa: 0.0,
    rating: null
  };

  return (
    <div className="student-detail-view">
      <div className="student-card">
        <h3>{student.text || 'Unnamed Student'}</h3>
        <div className="student-info">
          <div className="info-row">
            <span className="label">ID:</span>
            <span className="value">{student.id}</span>
          </div>
          <div className="info-row">
            <span className="label">Star Rating:</span>
            <span className="value">{'★'.repeat(student.starRating)}</span>
          </div>
          <div className="info-row">
            <span className="label">Status:</span>
            <span className="value status-badge">{student.status}</span>
          </div>
          <div className="info-row">
            <span className="label">Grade:</span>
            <span className="value">{safeGrade.grade} (GPA: {safeGrade.gpa})</span>
          </div>
          {safeGrade.rating && (
            <div className="info-row">
              <span className="label">Rating:</span>
              <span className="value">{safeGrade.rating}</span>
            </div>
          )}
          {!student.grade && (
            <div className="info-row error">
              <span className="label">Data Issue:</span>
              <span className="value">Grade information missing</span>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default StudentDetailView;