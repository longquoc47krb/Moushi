import Profile from "@/components/chat/Profile";
import { useSocketContext } from "@/context/useSocketContext";
import withAuth from "@/hocs/withAuth";
import { usePathname } from "next/navigation";
import React, { useEffect } from "react";

const DashboardLayout = ({ children }: { children: React.ReactNode }) => {
    const pathname = usePathname()
    const { connect } = useSocketContext()

    return (
        <div className="flex gap-x-2 p-2 h-screen bg-gray-300">
            <Profile pathname={pathname} />
            {children}
        </div>
    );
}

export default withAuth(DashboardLayout);