"use client";
import { useSocket } from "@/hooks/useSocket";
import { createContext, useContext, useEffect, useState } from "react";

export const SocketContext = createContext<any>({
    connect: null, disconnect: null
});
SocketContext.displayName = "SocketContext";

export function useSocketContext() {
    return useContext(SocketContext);
}

interface SocketProviderProps {
    children: JSX.Element;
}
export const SocketProvider = ({ children }: SocketProviderProps) => {
    const { connect, disconnect, isConnected, stompClient, onSubscribePrivateChat, onSendPrivateChat } = useSocket();

    return (
        <SocketContext.Provider value={{ connect, disconnect, isConnected, stompClient, onSubscribePrivateChat, onSendPrivateChat }}>
            {children}
        </SocketContext.Provider>
    );
};
