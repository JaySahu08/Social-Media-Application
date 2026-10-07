import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api/api';

export default function Friends() {
  const [friends, setFriends] = useState([]);
  const [pending, setPending] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const load = async () => {
    try {
      const [fRes, pRes] = await Promise.all([
        api.get('/api/friends'),
        api.get('/api/friends/requests/pending'),
      ]);
      setFriends(fRes.data);
      setPending(pRes.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { load(); }, []);

  const acceptRequest = async (id) => {
    await api.post(`/api/friends/accept/${id}`);
    load();
  };

  const rejectRequest = async (id) => {
    await api.post(`/api/friends/reject/${id}`);
    load();
  };

  if (loading) return <div className="page-loading">Loading...</div>;

  return (
    <div className="page-container">
      <h1>Friends</h1>

      {pending.length > 0 && (
        <>
          <h2>Pending Requests ({pending.length})</h2>
          <div className="requests-list">
            {pending.map((r) => (
              <div key={r.id} className="request-card">
                <div className="avatar">{r.senderUsername[0].toUpperCase()}</div>
                <div className="request-info">
                  <strong>{r.senderFullName}</strong>
                  <span>@{r.senderUsername}</span>
                </div>
                <div className="request-actions">
                  <button
                    className="btn-accept"
                    onClick={() => acceptRequest(r.id)}
                  >
                    ✓ Accept
                  </button>
                  <button
                    className="btn-reject"
                    onClick={() => rejectRequest(r.id)}
                  >
                    ✕ Reject
                  </button>
                </div>
              </div>
            ))}
          </div>
        </>
      )}

      <h2>All Friends ({friends.length})</h2>
      {friends.length === 0 ? (
        <p className="empty-state-text">No friends yet.</p>
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
              <button className="btn-chat">💬</button>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}