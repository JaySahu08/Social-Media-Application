import React, { useEffect, useRef, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../api/api';
import { useAuth } from '../context/AuthContext';
import {
  connectWebSocket,
  sendMessage,
  disconnectWebSocket,
} from '../utils/websocket';

export default function Chat() {
  const { username: otherUsername } = useParams();
  const { username: me, token } = useAuth();
  const navigate = useNavigate();

  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [connected, setConnected] = useState(false);
  const bottomRef = useRef(null);

  // Load history + connect WS
  useEffect(() => {
    let mounted = true;

    const init = async () => {
      try {
        const res = await api.get(`/api/chat/history/${otherUsername}`);
        if (mounted) setMessages(res.data);
      } catch (err) {
        console.error('Failed to load history', err);
      }

      try {
        await connectWebSocket(me, token, (msg) => {
          // Only add if it belongs to this conversation
          if (
            (msg.senderUsername === me && msg.receiverUsername === otherUsername) ||
            (msg.senderUsername === otherUsername && msg.receiverUsername === me)
          ) {
            setMessages((prev) => {
              // Avoid duplicate if we already added optimistically
              if (prev.some((m) => m.id === msg.id)) return prev;
              return [...prev, msg];
            });
          }
        });
        if (mounted) setConnected(true);
      } catch (err) {
        console.error('WS connect failed', err);
      }
    };

    init();

    return () => {
      mounted = false;
      disconnectWebSocket();
    };
  }, [otherUsername, me, token]);

  // Auto-scroll on new messages
  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const handleSend = (e) => {
    e.preventDefault();
    if (!input.trim()) return;
    sendMessage(otherUsername, input.trim());
    setInput('');
  };

  return (
    <div className="chat-page">
      <div className="chat-header">
        <button className="btn-back" onClick={() => navigate('/')}>←</button>
        <div className="avatar">{otherUsername[0].toUpperCase()}</div>
        <div className="chat-user-info">
          <strong>{otherUsername}</strong>
          <span className={connected ? 'status online' : 'status offline'}>
            {connected ? '● Online' : '● Connecting...'}
          </span>
        </div>
      </div>

      <div className="chat-messages">
        {messages.length === 0 && (
          <div className="chat-empty">No messages yet. Say hi! 👋</div>
        )}
        {messages.map((m, i) => {
          const mine = m.senderUsername === me;
          return (
            <div key={m.id || i} className={`message-row ${mine ? 'mine' : 'theirs'}`}>
              <div className={`message-bubble ${mine ? 'bubble-mine' : 'bubble-theirs'}`}>
                <div className="message-content">{m.content}</div>
                <div className="message-time">
                  {m.sentAt ? new Date(m.sentAt).toLocaleTimeString() : ''}
                </div>
              </div>
            </div>
          );
        })}
        <div ref={bottomRef} />
      </div>

      <form className="chat-input-form" onSubmit={handleSend}>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder="Type a message..."
          autoFocus
        />
        <button type="submit" className="btn-send" disabled={!connected}>
          ➤
        </button>
      </form>
    </div>
  );
}