"use client";
"use client";

import { StompProvider } from "../context/useStompContext";
import { ThemeProvider } from "@/context/useThemeContext";
import { AuthProvider } from "@/context/useAuthContext";
import { CookiesProvider } from "react-cookie"

export function Providers({ children }: any) {
  return (
    <ThemeProvider>
      <AuthProvider>
        <CookiesProvider>
          <StompProvider>{children}</StompProvider>
        </CookiesProvider>
      </AuthProvider>
    </ThemeProvider>
  );
}
