"use client"
import MessageList from "@/components/chat/MessageList";
import { useAuthContext } from "@/context/useAuthContext";
import DashboardLayout from "./DashboardLayout";
import { useSocketContext } from "@/context/useSocketContext";
import { useEffect } from "react";

const Page = () => {
    const { currentUser } = useAuthContext()
    // const { connect } = useSocketContext()
    // useEffect(() => { connect() }, [])
    return (
        <DashboardLayout>
            <MessageList currentUser={currentUser} />
        </DashboardLayout>
    );
}

export default Page;