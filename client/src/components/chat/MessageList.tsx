"use client";
import React, { useEffect, useState } from 'react'
import InboxItem from './InboxItem'
import { useQuery } from '@tanstack/react-query';
import { fetchConversationsByUserIdApi } from '@/app/services/conversation';
import Loading from '../Loading';
import { IConversationResponse, IUser } from '@/interfaces';
import { Skeleton } from '../ui/skeleton';
function MessageList({ currentUser }: { currentUser: IUser }) {
    const { data: conversations, isLoading } = useQuery({
        queryKey: ['messageList'],
        queryFn: () => fetchConversationsByUserIdApi(currentUser.id),

    })
    return (
        <div className='bg-[whitesmoke] rounded-lg w-[40%]'>
            <h1 className='text-2xl font-bold px-2 pt-4'>Chats</h1>
            {isLoading ? [...Array(8)].map((item, index) => <div className="px-4 mb-4" key={index}>
                <Skeleton className="w-full h-12 rounded-lg" />
            </div>)
                : conversations?.map((conversation: IConversationResponse, index: number) => <InboxItem key={index}
                    conversation={conversation} currentUserId={currentUser?.id}
                />)}
            {/* {
               
            } */}
        </div>
    )
}

export default MessageList