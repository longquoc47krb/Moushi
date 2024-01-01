/* eslint-disable react-hooks/exhaustive-deps */
"use client";

import axios from "axios";
import { useStompContext } from "../../context/useStompContext";
import Message from "./Message";

import { useEffect, useState } from "react";
import Profile from "./Profile";
import MessageList from "./MessageList";
import ChatBox from "./ChatBox";
import { blueTheme, redTheme } from "@/themes";

var stompClient: any = null;
function ChatScreen() {
  const [messages, setMessages] = useState([]);
  const [user, setUser] = useState(null);
  const { isConnected } = useStompContext();


  return (
    <div className="flex gap-x-4 p-2 h-screen bg-gray-300">
      <Profile />
      <MessageList />
      <ChatBox />
    </div>
  );
}

export default ChatScreen;
