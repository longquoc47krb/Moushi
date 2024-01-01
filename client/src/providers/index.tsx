"use client";
"use client";

import { SessionProvider } from "next-auth/react";
import { StompProvider } from "../context/useStompContext";
import { ThemeProvider } from "@/context/useThemeContext";
import { DataProvider } from "@/context/useDataContext";

export function Providers({ children }: any) {
  return (
    <ThemeProvider>
      <DataProvider>
        <SessionProvider>
          <StompProvider>{children}</StompProvider>
        </SessionProvider></DataProvider>
    </ThemeProvider>
  );
}
