"use client";
import React, { useEffect, useState } from 'react'
import InboxItem from './InboxItem'
import { user1 } from '@/mocks/messageList';
import { useDataContext } from '@/context/useDataContext';

function MessageList() {
    const { conversations } = useDataContext()
    return (
        <div className='bg-[whitesmoke] rounded-lg w-[40%]'>
            <h1 className='text-2xl font-bold px-2 pt-4'>Chats</h1>
            {
                conversations.map((item, index) => <InboxItem key={index}
                    conversation={item}
                />)
            }
        </div>
    )
}

export default MessageList