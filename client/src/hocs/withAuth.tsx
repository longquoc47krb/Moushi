/* eslint-disable react-hooks/rules-of-hooks */
"use client";
import { NextComponentType } from "next";
import { usePathname, useRouter } from "next/navigation";
import { Component } from "react";
interface AuthProps {
  isLoggedIn?: boolean;
}
function withAuth(Component: any) {
  const Auth = (props: AuthProps) => {
    const router = useRouter();
    const pathname = usePathname();
    let accessToken;
    try {
      accessToken = localStorage.getItem("accessToken");
    } catch (error) { }
    // const isLoggedIn = !!accessToken;
    const isLoggedIn = true;
    if (!isLoggedIn) return router.push("/sign-in");
    console.log({ pathname })
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
