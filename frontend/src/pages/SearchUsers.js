import React, { useState } from 'react';
import api from '../api/api';

export default function SearchUsers() {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [searched, setSearched] = useState(false);
  const [message, setMessage] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;
    try {
      const res = await api.get(`/api/users/search?query=${encodeURIComponent(query)}`);
      setResults(res.data);
      setSearched(true);
    } catch (err) {
      console.error(err);
    }
  };

  const sendRequest = async (id) => {
    try {
      await api.post(`/api/friends/request/${id}`);
      setMessage('Friend request sent! ✅');
      setTimeout(() => setMessage(''), 3000);
    } catch (err) {
      setMessage(err.response?.data?.message || 'Failed to send request');
      setTimeout(() => setMessage(''), 3000);
    }
  };

  return (
    <div className="page-container">
      <h1>Find People</h1>

      <form onSubmit={handleSearch} className="search-form">
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search by username or name..."
        />
        <button type="submit" className="btn-primary">🔍 Search</button>
      </form>

      {message && <div className="toast">{message}</div>}

      {searched && results.length === 0 && (
        <p className="empty-state-text">No users found for "{query}"</p>
      )}

      <div className="results-list">
        {results.map((u) => (
          <div key={u.id} className="user-result-card">
            <div className="avatar">{u.username[0].toUpperCase()}</div>
            <div className="user-info">
              <strong>{u.fullName}</strong>
              <span>@{u.username}</span>
            </div>
            <button
              className="btn-primary"
              onClick={() => sendRequest(u.id)}
            >
              ➕ Add Friend
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}