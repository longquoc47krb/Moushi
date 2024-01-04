import React from "react";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { Button } from "../ui/button";
import { TiTick, TiTimes } from "react-icons/ti";
import { changeFriendRequestStatus } from "@/app/services/user";
import { FriendRequestStatus } from "@/common/constants";
import { IFriendInvitationResponse } from "@/interfaces";

const FriendRequestItem = ({ currentUserId, friend }: { currentUserId: string, friend: IFriendInvitationResponse }) => {
    const handleAccept = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.stopPropagation();
        const AcceptResponse = await changeFriendRequestStatus(friend.requestId, {
            senderId: currentUserId,
            friendRequestStatus: FriendRequestStatus.ACCEPTED
        })
        console.log({ AcceptResponse })
    }
    const handleReject = async (event: React.MouseEvent<HTMLButtonElement>) => {
        event.stopPropagation();
        const AcceptResponse = await changeFriendRequestStatus(friend.requestId, {
            senderId: currentUserId,
            friendRequestStatus: FriendRequestStatus.REJECTED
        })
        console.log({ AcceptResponse })
    }
    return (
        <div className="flex  py-4 items-start justify-between w-full cursor-pointer hover:bg-gray-200 hover:transition-all hover:duration-100 px-2 relative" >
            <div className='flex items-center gap-x-2 px-4'>
                <div className="relative">
                    <Avatar>
                        <AvatarImage src={friend.sender.profilePicture} />
                        <AvatarFallback>AVT</AvatarFallback>
                    </Avatar>
                </div>
                <p className='font-semibold text-base'>{friend.sender.fullName}</p>
            </div>
            <div className="flex gap-1">
                <Button className="bg-sky-600 text-white" onClick={handleAccept}><TiTick /></Button>
                <Button className="bg-gray-300 text-gray-800" onClick={handleReject}><TiTimes /></Button>
            </div>

        </div>
    );
}

export default FriendRequestItem;