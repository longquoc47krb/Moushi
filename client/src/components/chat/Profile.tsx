import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { IoSettings, IoPeopleSharp } from "react-icons/io5";
import { IoMdChatbubbles } from "react-icons/io";

import { PiSignOutBold } from "react-icons/pi";
import { ITheme } from "@/interfaces";
import clsx from "clsx";
import { useThemeContext } from "@/context/useThemeContext";
import { useRouter } from "next/navigation";
import { useAuthContext } from "@/context/useAuthContext";
import LogoutDialog from "../LogoutDialog";
const Profile = ({ pathname }: { pathname?: string | undefined }) => {
  const { currentUser } = useAuthContext()
  const router = useRouter()
  const { theme } = useThemeContext()
  return (
    <div className={clsx("h-full py-2 rounded-lg flex flex-col")} style={theme.backgroundStyle}>
      <div className="px-4">
        <Avatar>
          <AvatarImage src={currentUser?.profilePicture} />
          <AvatarFallback>VN</AvatarFallback>
        </Avatar>
      </div>
      <ul className="text-white text-3xl profile-menu mt-8 text-center">

        <div className="flex flex-col profile-menu-sub1">

          <li className={clsx("cursor-pointer", { "active": pathname?.match(/^\/u\/[a-zA-Z0-9]+$/) })} onClick={() => router.push("/u")}>
            <IoMdChatbubbles />
          </li>
          <li className={clsx("cursor-pointer", { "active": pathname?.match(/^\/friends\/[a-zA-Z0-9]+$/) })} onClick={() => router.push(`/friends/${currentUser.id}`)}>
            <IoPeopleSharp />
          </li>
          <li className={clsx("cursor-pointer", { "active": pathname === "/setting" })} onClick={() => router.push("/settings")}>
            <IoSettings />
          </li></div>
        <LogoutDialog>
          <li><PiSignOutBold />
          </li>
        </LogoutDialog>
      </ul>
    </div>
  );
};

export default Profile;
