import ChatBox from "@/components/chat/ChatBox";
import Profile from "@/components/chat/Profile";
import { useSocketContext } from "@/context/useSocketContext";
import withAuth from "@/hocs/withAuth";
import { useParams, usePathname } from "next/navigation";
import React, { useEffect } from "react";

const ChattingLayout = ({ children }: { children: React.ReactNode }) => {
    const params = useParams()
    const pathname = usePathname()
    const { conversationId } = params;
    return (
        <div className="flex gap-x-2 p-2 h-screen bg-gray-300">
            <Profile pathname={pathname} />
            {children}
            <ChatBox conversationId={conversationId} />
        </div>
    );
}

export default withAuth(ChattingLayout);