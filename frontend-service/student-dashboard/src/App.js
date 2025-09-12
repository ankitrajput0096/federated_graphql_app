import React, { useState } from 'react';
import AddStudentForm from './components/AddStudentForm';
import UpdateStudentForm from './components/UpdateStudentForm';
import StudentsList from './components/StudentsList';
import StudentSubscription from './components/StudentSubscription';
import './App.css';

function App() {
  const [activeTab, setActiveTab] = useState('add');
  const [addedStudent, setAddedStudent] = useState(null);
  const [selectedStudentId, setSelectedStudentId] = useState(null);

  const handleStudentAdded = (student) => {
    setAddedStudent(student);
    setActiveTab('details');
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Student Dashboard</h1>
        <p>Monitor and manage student data in real-time</p>
      </header>
      
      <nav className="tabs">
        <button 
          data-testid="tab-add"
          className={activeTab === 'add' ? 'active' : ''} 
          onClick={() => setActiveTab('add')}
        >
          Add Student
        </button>
        <button 
          data-testid="tab-update"
          className={activeTab === 'update' ? 'active' : ''} 
          onClick={() => setActiveTab('update')}
        >
          Update Student
        </button>
        <button 
          data-testid="tab-details"
          className={activeTab === 'details' ? 'active' : ''} 
          onClick={() => setActiveTab('details')}
        >
          Student Details
        </button>
        <button 
          data-testid="tab-subscription"
          className={activeTab === 'subscription' ? 'active' : ''} 
          onClick={() => setActiveTab('subscription')}
        >
          Subscription
        </button>
      </nav>
      
      <main className="App-main">
        {activeTab === 'add' && (
          <section className="tab-content">
            <AddStudentForm onStudentAdded={handleStudentAdded} />
            {addedStudent && (
              <div className="added-student-confirmation">
                <h3>Student Added Successfully!</h3>
                <div className="student-card">
                  <h4>{addedStudent.text}</h4>
                  <p>ID: {addedStudent.id}</p>
                  <p>Star Rating: {addedStudent.starRating}</p>
                  <p>Status: {addedStudent.status}</p>
                </div>
              </div>
            )}
          </section>
        )}
        
        {activeTab === 'update' && (
          <section className="tab-content">
            <UpdateStudentForm />
          </section>
        )}
        
        {activeTab === 'details' && (
          <section className="tab-content">
            <StudentsList />
          </section>
        )}
        
        {activeTab === 'subscription' && (
          <section className="tab-content">
            <div className="subscription-controls">
              <h2>Student Subscription</h2>
              <div className="student-selection">
                <label htmlFor="studentId">Enter Student ID to subscribe:</label>
                <input
                  type="text"
                  id="studentId"
                  value={selectedStudentId || ''}
                  onChange={(e) => setSelectedStudentId(e.target.value)}
                  placeholder="Student ID"
                />
              </div>
            </div>
            {selectedStudentId && (
              <StudentSubscription studentId={selectedStudentId} />
            )}
          </section>
        )}
      </main>
    </div>
  );
}

export default App;