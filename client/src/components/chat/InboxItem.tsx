import React from 'react'
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import moment from 'moment';
import { IConversationDto } from '@/interfaces';
import { useRouter } from 'next/navigation';
import Verified from '../Verified';

interface Conversation {
    id: string;
    participants: object[];
    messages: object[];
    dateSent: any;
}
interface InboxProps {
    conversation: Conversation
}
function InboxItem({ conversation }: InboxProps) {
    const router = useRouter()
    const myFriend = conversation?.users?.filter(u => u.id != currentUser.id)[0];
    const lastMessage = conversation?.messages[conversation?.messages?.length - 1]
    return (
        <div className="flex border-b border-b-gray-300 py-4 items-start justify-between w-full cursor-pointer hover:bg-gray-200 hover:transition-all hover:duration-100 px-2" onClick={() => router.push(`/u/${conversation.id}`)}>
            <div className='flex items-start gap-x-4'>
                <Avatar>
                    <AvatarImage src={myFriend?.avatar} />
                    <AvatarFallback>CN</AvatarFallback>
                </Avatar>
                <div>
                    <p className='font-semibold'>{myFriend?.name}{myFriend?.verified && <Verified />}</p>
                    <small className='text-gray-400 ellipsis-one-line'>{lastMessage?.content}</small>
                </div>
            </div>
            <div>
                <small className='text-gray-400 leading-6'>{moment(lastMessage.dateSent).fromNow()}</small>
            </div>
        </div>
    )
}

export default InboxItem