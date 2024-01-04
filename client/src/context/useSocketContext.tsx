"use client";
import axiosClient from "@/configs/axiosClient";
import { useSocket } from "@/hooks/useSocket";
import { createContext, useContext, useEffect, useState } from "react";
import { useCookies } from "react-cookie"

export const SocketContext = createContext<any>({
    connect: null, disconnect: null, receivedMessage: null
});
SocketContext.displayName = "SocketContext";

export function useSocketContext() {
    return useContext(SocketContext);
}

interface SocketProviderProps {
    children: JSX.Element;
}
export const SocketProvider = ({ children }: SocketProviderProps) => {
    const { connect, disconnect, receivedMessage } = useSocket();
    return (
        <SocketContext.Provider value={{ connect, disconnect, receivedMessage }}>
            {children}
        </SocketContext.Provider>
    );
};
