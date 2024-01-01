"use client"

import DashboardLayout from "@/app/u/DashboardLayout";
import FriendList from "@/components/chat/FriendList";
import { useParams } from "next/navigation";

const Page = () => {
    return (
        <DashboardLayout>
            <FriendList />
        </DashboardLayout>
    );
}

export default Page;