/* eslint-disable react-hooks/rules-of-hooks */
"use client";
import { NextComponentType } from "next";
import { useRouter } from "next/navigation";
import { Component } from "react";
interface AuthProps {
  isLoggedIn: boolean;
}
function withAuth(Component: any) {
  const Auth = (props: AuthProps) => {
    const router = useRouter();
    let accessToken;
    try {
      accessToken = localStorage.getItem("accessToken");
    } catch (error) {}
    const isLoggedIn = !!accessToken;
    if (!isLoggedIn) return router.push("/sign-in");
    return <Component {...props} />;
  };
  if (Component.getInitialProps) {
    Auth.getInitialProps = Component.getInitialProps;
  }

  return Auth;
}

export default withAuth;
