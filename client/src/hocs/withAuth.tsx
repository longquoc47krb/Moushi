/* eslint-disable react-hooks/rules-of-hooks */
"use client";
import { useSocketContext } from "@/context/useSocketContext";
import { NextComponentType } from "next";
import { usePathname, useRouter } from "next/navigation";
import { Component, useEffect } from "react";
interface AuthProps {
  isLoggedIn?: boolean;
}
function withAuth(Component: any) {
  const Auth = (props: AuthProps) => {
    const router = useRouter();
    const pathname = usePathname();
    const unauthenticated_path = [
      "/sign-in",
      "/sign-up"
    ]
    let accessToken;
    try {
      accessToken = localStorage.getItem("accessToken");
    } catch (error) { }
    const isLoggedIn = !!accessToken;
    if (!isLoggedIn && !unauthenticated_path.includes(pathname)) return router.push("/sign-in");
    if ((pathname === "/sign-in" || pathname === "/sign-up") && isLoggedIn) {
      return router.back();
    }
    return <Component {...props} />;
  };
  if (Component.getInitialProps) {
    Auth.getInitialProps = Component.getInitialProps;
  }

  return Auth;
}

export default withAuth;
