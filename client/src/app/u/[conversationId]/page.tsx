"use client"
import { useParams, useRouter } from "next/navigation";
import DashboardLayout from "../DashboardLayout";
import MessageList from "@/components/chat/MessageList";
import ChatBox from "@/components/chat/ChatBox";
import { user1 } from "@/mocks/messageList";
import { useEffect, useState } from "react";
import { messages } from '../../../mocks/messageList';

const Page = () => {
    const params = useParams()
    const { conversationId } = params;
    const [conversation, setConversation] = useState({});
    useEffect(() => {
        const fetchConversation = async () => {
            try {
                const conversationResponse = await fetch(`http://localhost:8000/conversations/${conversationId}`);
                const usersResponse = await fetch('http://localhost:8000/users');
                if (!conversationResponse.ok) {
                    throw new Error('Failed to fetch conversations');
                }
                const conversationData = await conversationResponse.json();
                setConversation(conversationData)
                let messages = [];
                // Fetch users for conversation
                const usersPromises = conversationData?.participants.map(async userId => {
                    const userResponse = await fetch(`http://localhost:8000/users/${userId}`)
                    if (!userResponse.ok) {
                        throw new Error(`Failed to fetch users for conversation ${conversationId}`);
                    }
                    const userData = await userResponse.json();
                    return userData;
                })
                // Fetch messages for conversation
                const messagesPromises = conversationData?.messages.map(async messageId => {
                    const messagesResponse = await fetch(`http://localhost:8000/messages/${messageId}`);
                    if (!messagesResponse.ok) {
                        throw new Error(`Failed to fetch messages for conversation ${conversationId}`);
                    }
                    const messagesData = await messagesResponse.json();
                    // const usersInConversation = usersData.filter(user => messagesData.find(msg => msg.senderId === user.id || msg.receiverId === user.id));
                    return messagesData;


                });
                const messageList = await Promise.all(messagesPromises);
                const userList = await Promise.all(usersPromises);
                setConversation({
                    ...conversationData,
                    messages: messageList,
                    participants: userList
                });

            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchConversation();
    }, []);
    return (
        <DashboardLayout>
            <MessageList />
            <ChatBox conversation={conversation} />
        </DashboardLayout>
    );
}

export default Page;