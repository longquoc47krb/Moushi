"use client";

import { AuthProvider } from "@/context/useAuthContext";
import { ThemeProvider } from "@/context/useThemeContext";
import {
  QueryClient,
  QueryClientProvider
} from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import React, { Fragment } from "react";
import { CookiesProvider } from "react-cookie";
import {
  StompSessionProvider
} from "react-stomp-hooks";
import { StompSessionProviderProps } from "react-stomp-hooks/dist/interfaces/StompSessionProviderProps";

const SOCKET_URL = "http://localhost:8080/ws";

export function Providers({ children }: any) {
  const [queryClient] = React.useState(() => new QueryClient())
  const token = localStorage.getItem("accessToken");
  const isAuthenticated = Boolean(token);

  const Wrapper = isAuthenticated ? StompSessionProvider : Fragment;

  const options = {
    ...(isAuthenticated
      ? {
        connectHeaders: {
          Authorization: "Bearer " + token,
          "x-api-key": process.env.NEXT_PUBLIC_API_KEY
        },
        url: SOCKET_URL,
        debug: (str) => {
          console.log(str);
        }
      }
      : {}),
  } as StompSessionProviderProps;
  return (
    <Wrapper {...options}>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider>
          <AuthProvider>
            <CookiesProvider>
              {children}
            </CookiesProvider>
          </AuthProvider>
        </ThemeProvider>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </Wrapper >
  );
}
