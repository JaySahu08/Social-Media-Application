import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export function connectWebSocket(username, token, onMessage) {
  return new Promise((resolve, reject) => {
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      },
      debug: (str) => console.log('[STOMP]', str),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log('✅ WebSocket connected');
        client.subscribe('/user/queue/messages', (msg) => {
          const body = JSON.parse(msg.body);
          onMessage(body);
        });
        stompClient = client;
        resolve(client);
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame);
        reject(frame);
      },
    });

    client.activate();
  });
}

export function sendMessage(receiverUsername, content) {
  if (!stompClient || !stompClient.connected) {
    console.error('WebSocket not connected');
    return;
  }
  stompClient.publish({
    destination: '/app/chat',
    body: JSON.stringify({ receiverUsername, content }),
  });
}

export function disconnectWebSocket() {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
  }
}