"use client";
import Image from "next/image";

import withAuth from "@/hocs/withAuth";
import ChatRoom from "@/components/chat/ChatScreen";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

function Home() {
  const router = useRouter()
  const accessToken = localStorage.getItem("accessToken") ?? null;
  useEffect(() => {
    if (accessToken) {
      router.push("/u");
    } else {
      router.push("/sign-in");
    }
  }, [accessToken])
  return <ChatRoom />;
}
export default withAuth(Home);
