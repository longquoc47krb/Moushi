import { UserState } from "@/common/constants";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { GoDotFill } from "react-icons/go";
import clsx from "clsx";
import moment from "moment";
import { getTimeAgo } from "@/lib/utils";
const FriendItem = ({ friend }: { friend: any }) => {

    return (
        <div className="flex  py-4 items-start justify-between w-full cursor-pointer hover:bg-gray-200 hover:transition-all hover:duration-100 px-2" >
            <div className='flex items-center gap-x-2 px-4'>
                <div className="relative">
                    <Avatar>
                        <AvatarImage src={friend?.profilePicture} />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    {friend?.userState && <GoDotFill className={clsx("absolute right-0 bottom-[-5px] drop-shadow-md", { "stroke-text": friend?.userState === UserState.ONLINE }, { "hidden": friend?.userState !== UserState.ONLINE })} />}
                    {friend?.userState === UserState.OFFLINE && <p className="absolute right-0 bottom-[-5px] bg-gray-100 text-gray-950 px-[4px] rounded-md text-xs">{getTimeAgo(friend?.lastOnline
                    )}</p>
                    }
                </div>
                <p className='font-semibold text-base'>{friend?.fullName}</p>
            </div>

        </div>
    );
}

export default FriendItem;