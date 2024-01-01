"use client";
import Image from "next/image";

import withAuth from "@/hocs/withAuth";
import ChatRoom from "@/components/chat/ChatScreen";

function Home() {
  return <ChatRoom />;
}
export default withAuth(Home);
