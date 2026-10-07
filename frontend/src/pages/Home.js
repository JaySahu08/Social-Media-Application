import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/api';
import { useAuth } from '../context/AuthContext';

export default function Home() {
  const { username } = useAuth();
  const [profile, setProfile] = useState(null);
  const [friends, setFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    (async () => {
      try {
        const [meRes, friendsRes] = await Promise.all([
          api.get('/api/users/me'),
          api.get('/api/friends'),
        ]);
        setProfile(meRes.data);
        setFriends(friendsRes.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  if (loading) return <div className="page-loading">Loading...</div>;

  return (
    <div className="page-container">
      <div className="welcome-card">
        <h1>Welcome, {profile?.fullName || username}! 👋</h1>
        <p>@{profile?.username}</p>
      </div>

      <h2>Your Friends ({friends.length})</h2>
      {friends.length === 0 ? (
        <div className="empty-state">
          <p>You don't have any friends yet.</p>
          <button className="btn-primary" onClick={() => navigate('/search')}>
            Find People
          </button>
        </div>
      ) : (
        <div className="friends-grid">
          {friends.map((f) => (
            <div
              key={f.id}
              className="friend-card"
              onClick={() => navigate(`/chat/${f.username}`)}
            >
              <div className="avatar">{f.username[0].toUpperCase()}</div>
              <div className="friend-info">
                <strong>{f.fullName}</strong>
                <span>@{f.username}</span>
              </div>
              <button className="btn-chat">💬 Chat</button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}