import React from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { disconnectWebSocket } from '../utils/websocket';

export default function Navbar() {
  const { username, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    disconnectWebSocket();
    logout();
    navigate('/login');
  };

  const isActive = (path) => location.pathname === path ? 'nav-link active' : 'nav-link';

  return (
    <nav className="navbar">
      <div className="nav-brand">
        <Link to="/">💬 SocialChat</Link>
      </div>
      <div className="nav-links">
        <Link to="/" className={isActive('/')}>Home</Link>
        <Link to="/friends" className={isActive('/friends')}>Friends</Link>
        <Link to="/search" className={isActive('/search')}>Find People</Link>
      </div>
      <div className="nav-user">
        <span className="username">👤 {username}</span>
        <button onClick={handleLogout} className="btn-logout">Logout</button>
      </div>
    </nav>
  );
}