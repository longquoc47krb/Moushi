"use client"

import DashboardLayout from "@/app/u/DashboardLayout";
import SettingBox from "@/components/settings/SettingBox";
import SettingList from "@/components/settings/SettingList";
import { useParams } from "next/navigation";

const Page = () => {
    return (
        <DashboardLayout>
            <SettingList />
            {/* <SettingBox /> */}
        </DashboardLayout>
    );
}

export default Page;