"use client"
import { ITheme } from "@/interfaces";
import { themes } from "@/themes";
import { createContext, useContext, useState } from "react";

export const ThemeContext = createContext<any>({
    theme: null,
    selectTheme: null
});
ThemeContext.displayName = "ThemeContext";

export function useThemeContext() {
    return useContext(ThemeContext);
}

interface ThemeProviderProps {
    children: JSX.Element;
}
export const ThemeProvider = ({ children }: ThemeProviderProps) => {
    const { redGradient, witchingHour, flare, byDesign, viciousStance } = themes;
    const [currentTheme, setCurrentTheme] = useState(viciousStance)
    const [tempTheme, setTempTheme] = useState(null)
    const { iconStyle, backgroundStyle } = currentTheme;
    const selectTheme = (theme: any) => {
        setTempTheme(theme)
    }
    const theme = tempTheme ?? currentTheme;
    return (
        <ThemeContext.Provider value={{ theme, selectTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};
