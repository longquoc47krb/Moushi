import { useEffect, useState } from "react";
import { over, Client } from "stompjs";
import SockJS from "sockjs-client";
import { NotificationDTO } from "@/interfaces/Notification";

const SOCKET_URL = "http://localhost:8080/ws/";


export const useStomp = (): {
  connect: () => void;
  sendMessage: (notification: NotificationDTO) => void;
  isConnected: boolean;
} => {
  const [stompClient, setStompClient] = useState<Client>();
  // const [receivedMessage, setReceivedMessage] = useState<MessageEntity>();
  // const [statusChangedUser, setStatusChangedUser] =
  //   useState<ChatStatusEntity>();
  // const [receivedNotification, setReceivedNotification] =
  //   useState<NotificationEntity>();
  // const [receivedNewRequest, setReceivedNewRequest] =
  //   useState<FriendRequestEntity>();
  // const [receivedRemovedRequest, setReceivedRemovedRequest] =
  //   useState<FriendRequestEntity>();
  // const [receivedNewFriend, setReceivedNewFriend] =
  //   useState<FriendInformationEntity>();
  // const [receivedRemovedFriend, setReceivedRemovedFriend] =
  //   useState<FriendInformationEntity>();
  const [isConnected, setConnected] = useState(false);

  const connect = () => {
    if (!isConnected) {
      let socket = new SockJS("http://localhost:8080/ws");
      const headers = {
        'x-api-key': '',
      };
      let client = over(socket);
      client.connect(headers, onConnected, onError);
      client.debug = () => { };
      setStompClient(client);
    }
  };

  const onConnected = () => {
    setTimeout(() => {
      setConnected(true);
    }, 1000);
  };


  const onError = (err: any) => {
    setConnected(false);
    setTimeout(connect, 300000);
    console.log("STOMP: Reconecting in 5 minutes");
  };

  const sendMessage = (message: NotificationDTO) => {
    if (stompClient?.connected) {
      stompClient.send("/app/chat", {}, JSON.stringify(message));
    }
  };

  return {
    connect,
    sendMessage,
    isConnected
  };
};