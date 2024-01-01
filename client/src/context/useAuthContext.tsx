"use client";
import axiosClient from "@/configs/axiosClient";
import { createContext, useContext, useEffect, useState } from "react";
import { useCookies } from "react-cookie"

export const AuthContext = createContext<any>({
    currentUser: {}
});
AuthContext.displayName = "AuthContext";

export function useAuthContext() {
    return useContext(AuthContext);
}

interface AuthProviderProps {
    children: JSX.Element;
}
export const AuthProvider = ({ children }: AuthProviderProps) => {
    const [cookies] = useCookies(["currentUser"]);
    const { currentUser } = cookies;
    console.log({ currentUser })
    return (
        <AuthContext.Provider value={{ currentUser }}>
            {children}
        </AuthContext.Provider>
    );
};
