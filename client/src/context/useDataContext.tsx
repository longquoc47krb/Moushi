"use client";
import { createContext, useContext, useEffect, useState } from "react";

export const DataContext = createContext<any>({
    conversations: [],
    messages: [],
    users: [],
    currentUser: {}
});
DataContext.displayName = "DataContext";

export function useDataContext() {
    return useContext(DataContext);
}

interface DataProviderProps {
    children: JSX.Element;
}
export const DataProvider = ({ children }: DataProviderProps) => {
    const [conversations, setConversations] = useState([]);
    const [messages, setMessages] = useState([]);
    const [users, setUsers] = useState([]);
    const currentUser = {
        "id": "2",
        "name": "Nguyen Quoc Long",
        "avatar": "https://avatars.githubusercontent.com/u/54442229?v=4"
    }
    useEffect(() => {
        const fetchConversations = async () => {
            try {
                const conversationsResponse = await fetch('http://localhost:8000/conversations');
                const usersResponse = await fetch('http://localhost:8000/users');
                if (!conversationsResponse.ok) {
                    throw new Error('Failed to fetch conversations');
                }
                if (!usersResponse.ok) {
                    throw new Error('Failed to fetch users');
                }
                const conversationsData = await conversationsResponse.json();
                const usersData = await usersResponse.json();
                setConversations(conversationsData);

                // Fetch messages for each conversation
                const messagesPromises = conversationsData.map(async conversation => {
                    const messagesResponse = await fetch(`http://localhost:8000/messages?conversationId=${conversation.id}`);
                    if (!messagesResponse.ok) {
                        throw new Error(`Failed to fetch messages for conversation ${conversation.id}`);
                    }
                    const messagesData = await messagesResponse.json();
                    const usersInConversation = usersData.filter(user => messagesData.find(msg => msg.senderId === user.id || msg.receiverId === user.id));
                    return {
                        ...conversation,
                        messages: messagesData,
                        users: usersInConversation
                    };

                });
                const conversationsWithMessages = await Promise.all(messagesPromises);
                setConversations(conversationsWithMessages);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchConversations();
    }, []);
    return (
        <DataContext.Provider value={{ conversations, currentUser }}>
            {children}
        </DataContext.Provider>
    );
};
