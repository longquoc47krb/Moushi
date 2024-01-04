"use client";

import { AuthProvider } from "@/context/useAuthContext";
import { SocketProvider } from "@/context/useSocketContext";
import { ThemeProvider } from "@/context/useThemeContext";
import {
  QueryClient,
  QueryClientProvider
} from '@tanstack/react-query';
import React from "react";
import { CookiesProvider } from "react-cookie";

export function Providers({ children }: any) {
  const [queryClient] = React.useState(() => new QueryClient())
  return (
    <SocketProvider>
      <QueryClientProvider client={queryClient}>
        <ThemeProvider>
          <AuthProvider>
            <CookiesProvider>
              {children}
            </CookiesProvider>
          </AuthProvider>
        </ThemeProvider>
      </QueryClientProvider>
    </SocketProvider>
  );
}
