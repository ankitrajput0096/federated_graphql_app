import React, { useState } from 'react';
import { useMutation } from '@apollo/client';
import { ADD_STUDENT } from '../graphql/mutations';
import { GET_ALL_STUDENTS } from '../graphql/queries';

const AddStudentForm = ({ onStudentAdded }) => {
  const [formData, setFormData] = useState({
    id: '',
    text: '',
    starRating: 0,
    universityId: '',
    status: 'A',
    grade: {
      studentId: '',
      grade: 'A',
      gpa: 0.0
    }
  });

  const [addStudent, { loading, error }] = useMutation(ADD_STUDENT, {
    update(cache, { data: { addStudent } }) {
      // Read the existing data from cache
      try {
        const existingStudents = cache.readQuery({ 
          query: GET_ALL_STUDENTS 
        });
        
        if (existingStudents && existingStudents.allStudents) {
          // Write back to the cache with the new student
          cache.writeQuery({
            query: GET_ALL_STUDENTS,
            data: {
              allStudents: [...existingStudents.allStudents, addStudent]
            }
          });
        }
      } catch (e) {
        console.log("Cache might be empty, skipping cache update", e);
      }
      
      // Notify parent component about the added student
      if (onStudentAdded) {
        onStudentAdded(addStudent);
      }
    }
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    
    if (name.startsWith('grade.')) {
      const gradeField = name.split('.')[1];
      setFormData(prev => ({
        ...prev,
        grade: {
          ...prev.grade,
          [gradeField]: gradeField === 'gpa' ? parseFloat(value) || 0 : value
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: name === 'starRating' ? parseInt(value) : value
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      await addStudent({ 
        variables: { 
          input: {
            ...formData,
            starRating: parseInt(formData.starRating),
            grade: {
              ...formData.grade,
              gpa: parseFloat(formData.grade.gpa)
            }
          } 
        } 
      });
      // Reset form
      setFormData({
        id: '',
        text: '',
        starRating: 0,
        universityId: '',
        status: 'A',
        grade: {
          studentId: '',
          grade: 'A',
          gpa: 0.0
        }
      });
    } catch (err) {
      console.error('Error adding student:', err);
    }
  };

  return (
    <div className="add-student-form">
      <h2>Add New Student</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="studentId">ID:</label>
            <input
              id="studentId"
              type="text"
              name="id"
              value={formData.id}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="studentName">Name:</label>
            <input
              id="studentName"
              type="text"
              name="text"
              value={formData.text}
              onChange={handleChange}
              required
            />
          </div>
        </div>
        
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="starRating">Star Rating:</label>
            <input
              id="starRating"
              type="number"
              name="starRating"
              value={formData.starRating}
              onChange={handleChange}
              min="0"
              max="5"
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="universityId">University ID:</label>
            <input
              id="universityId"
              type="text"
              name="universityId"
              value={formData.universityId}
              onChange={handleChange}
              required
            />
          </div>
        </div>
        
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="status">Status:</label>
            <select id="status" name="status" value={formData.status} onChange={handleChange}>
              <option value="A">A</option>
              <option value="B">B</option>
              <option value="C">C</option>
            </select>
          </div>
        </div>
        
        <div className="form-section">
          <h3>Grade Information</h3>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="gradeStudentId">Student ID (for grade):</label>
              <input
                id="gradeStudentId"
                type="text"
                name="grade.studentId"
                value={formData.grade.studentId}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="letterGrade">Letter Grade:</label>
              <select id="letterGrade" name="grade.grade" value={formData.grade.grade} onChange={handleChange}>
                <option value="A">A</option>
                <option value="B">B</option>
                <option value="C">C</option>
                <option value="D">D</option>
                <option value="F">F</option>
              </select>
            </div>
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="gpa">GPA:</label>
              <input
                id="gpa"
                type="number"
                name="grade.gpa"
                value={formData.grade.gpa}
                onChange={handleChange}
                step="0.1"
                min="0"
                max="4.0"
                required
              />
            </div>
          </div>
        </div>
        
        <button type="submit" disabled={loading} className="submit-btn">
          {loading ? 'Adding...' : 'Add Student'}
        </button>
        
        {error && <div className="error">Error: {error.message}</div>}
      </form>
    </div>
  );
};

export default AddStudentForm;