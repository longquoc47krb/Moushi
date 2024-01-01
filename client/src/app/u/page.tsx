"use client"
import { useParams, useRouter } from "next/navigation";
import MessageList from "@/components/chat/MessageList";
import ChatBox from "@/components/chat/ChatBox";
import { user1 } from "@/mocks/messageList";
import DashboardLayout from "./DashboardLayout";
import HomeBox from "@/components/chat/HomeBox";

const Page = () => {
    return (
        <DashboardLayout>
            <MessageList />
        </DashboardLayout>
    );
}

export default Page;