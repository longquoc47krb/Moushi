/* eslint-disable react-hooks/exhaustive-deps */
import { useAuthContext } from '@/context/useAuthContext';
import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client, Message, over } from "stompjs";
const SOCKET_URL = "http://localhost:8080/ws";

var accessToken = null;
try {
  accessToken = localStorage.getItem("accessToken") ?? null;
} catch (error) { };
export const useSocket = () => {
  const [isConnected, setConnected] = useState(false);
  const [stompClient, setStompClient] = useState<Client>();
  const { currentUser } = useAuthContext()
  const connect = () => {
    if (!isConnected && accessToken) {

      let socket = new SockJS(SOCKET_URL);
      let client = over(socket);
      client.connect({
        "Authorization": `Bearer ${accessToken}`,
        "x-api-key": process.env.NEXT_PUBLIC_API_KEY
      }, onConnected, onError);
      setStompClient(client);
    }
  };
  console.log({ connectWebsocket: isConnected })
  const disconnect = () => {
    if (isConnected && stompClient) {
      stompClient.disconnect(() => {
        setConnected(false);
        setStompClient(undefined); // Clear the stompClient reference
      });
    }
  };
  const onConnected = () => {
    setTimeout(() => {
      setConnected(true);
    }, 1000);

  }
  const onError = (err: any) => {
    setConnected(false);
    setTimeout(connect, 1000 * 60);
    console.log("STOMP: Reconecting in 1 minute");
  };
  const onSubscribePrivateChat = (username: string, callback: (message: Message) => any) => {
    if (isConnected && stompClient?.connected) {
      stompClient?.subscribe('/user/' + username + '/private-messages', callback);
    }
  }
  const onSendPrivateChat = (payload: any) => {
    if (isConnected && stompClient?.connected) {
      console.log(">> Send: ", payload);
      stompClient?.send(`/app/private-chat`, {}, JSON.stringify(payload));
    }
  }
  return {
    stompClient,
    isConnected,
    connect,
    disconnect,
    onSubscribePrivateChat,
    onSendPrivateChat
  }
}