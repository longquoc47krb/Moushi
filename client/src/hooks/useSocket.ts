import { useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client, over } from "stompjs";
const SOCKET_URL = "http://localhost:8080/ws";

var stompClient;

export const useSocket = () => {
  const [isConnected, setConnected] = useState(false);
  const [stompClient, setStompClient] = useState<Client>();
  const [receivedMessage, setReceivedMessage] = useState();
  const connect = () => {
    if (!isConnected) {
      let accessToken;
      try {
        accessToken = localStorage.getItem("accessToken") ?? "";
      } catch (error) { }
      let socket = new SockJS(SOCKET_URL);
      let client = over(socket);
      client.connect({
        "Authorization": `Bearer ${accessToken}`,
        "x-api-key": process.env.NEXT_PUBLIC_API_KEY
      }, onConnected, onError);
      setStompClient(client);
    }
  };
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
    setTimeout(connect, 300000);
    console.log("STOMP: Reconecting in 5 minutes");
  };
  useEffect(() => {
    if (isConnected && stompClient?.connected) {
      stompClient.subscribe(`/topic/online`, onMessageReceived);
    }
  }, [isConnected, stompClient]);
  const onMessageReceived = (payload: any) => {
    const payloadData = JSON.parse(payload.body);
    setReceivedMessage(payloadData);
  };
  console.log({ isConnected })
  return {
    connect,
    disconnect,
    receivedMessage
  }
}