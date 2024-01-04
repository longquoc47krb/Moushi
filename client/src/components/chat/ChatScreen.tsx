/* eslint-disable react-hooks/exhaustive-deps */
"use client";

import { useState } from "react";
import ChatBox from "./ChatBox";
import MessageList from "./MessageList";
import Profile from "./Profile";

var stompClient: any = null;
function ChatScreen() {
  const [messages, setMessages] = useState([]);
  const [user, setUser] = useState(null);


  return (
    <div className="flex gap-x-4 p-2 h-screen bg-gray-300">
      <Profile />
      <MessageList />
      <ChatBox />
    </div>
  );
}

export default ChatScreen;
