import React from 'react'
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import moment from 'moment';
import { useRouter } from 'next/navigation';
import Verified from '../Verified';
import { IConversationResponse, IUser } from '@/interfaces';
import { UserState } from '@/common/constants';
import clsx from 'clsx';
import { getTimeAgo } from '@/lib/utils';


interface InboxProps {
    conversation: IConversationResponse | undefined;
    currentUserId: string
}
function InboxItem({ conversation, currentUserId }: InboxProps) {
    const router = useRouter()
    const myFriend: IUser = conversation?.participants?.filter((user: IUser) => user.id !== currentUserId)[0];
    const lastMessage = conversation?.messages[conversation?.messages.length - 1] ?? null;
    return (
        <div className="flex py-4 items-start justify-between w-full cursor-pointer hover:bg-gray-200 hover:transition-all hover:duration-100 px-2" onClick={() => router.push(`/u/${conversation.id}`)}>
            <div className='flex items-start gap-x-4'>
                <div className="relative">
                    <Avatar>
                        <AvatarImage src={myFriend?.profilePicture} />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    {myFriend?.userState && <div className={clsx("absolute right-0 bottom-[-5px] drop-shadow-md w-3 h-3", { "stroke-text": myFriend?.userState === UserState.ONLINE }, { "hidden": myFriend?.userState !== UserState.ONLINE })}></div>}
                    {myFriend?.userState === UserState.OFFLINE && <p className="absolute right-0 bottom-[-5px] bg-gray-100 text-gray-950 px-[4px] rounded-md text-xs">{getTimeAgo(myFriend?.lastOnline
                    )}</p>
                    }
                </div>
                <div>
                    <p className='font-semibold'>{myFriend?.fullName}
                        {/* {my && <Verified />}*/}
                    </p>
                    <small className='text-gray-400 ellipsis-one-line'>{lastMessage ? lastMessage.content : "Let's share our stories!"}</small>
                </div>
            </div>
            <div>
                <small className='text-gray-400 leading-6'>{getTimeAgo(lastMessage?.dateSent)} ago</small>
            </div>
        </div>
    )
}

export default InboxItem