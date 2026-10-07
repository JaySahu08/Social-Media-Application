import React, { createContext, useContext, useState } from 'react';
import api from '../api/api';

const AuthContext = createContext();

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [username, setUsername] = useState(localStorage.getItem('username'));

  const login = async (username, password) => {
    const res = await api.post('/api/auth/login', { username, password });
    const { token, username: uname } = res.data;
    localStorage.setItem('token', token);
    localStorage.setItem('username', uname);
    setToken(token);
    setUsername(uname);
    return res.data;
  };

  const register = async (username, email, fullName, password) => {
    const res = await api.post('/api/auth/register', {
      username, email, fullName, password,
    });
    const { token, username: uname } = res.data;
    localStorage.setItem('token', token);
    localStorage.setItem('username', uname);
    setToken(token);
    setUsername(uname);
    return res.data;
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setToken(null);
    setUsername(null);
  };

  return (
    <AuthContext.Provider value={{ token, username, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);