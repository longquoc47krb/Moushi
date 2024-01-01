import Profile from "@/components/chat/Profile";
import { usePathname } from "next/navigation";
import React from "react";

const DashboardLayout = ({ children }: { children: React.ReactNode }) => {
    const pathname = usePathname()
    return (
        <div className="flex gap-x-2 p-2 h-screen bg-gray-300">
            <Profile pathname={pathname} />
            {children}
        </div>
    );
}

export default DashboardLayout;