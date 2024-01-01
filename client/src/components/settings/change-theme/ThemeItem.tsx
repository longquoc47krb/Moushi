import { useThemeContext } from "@/context/useThemeContext";
import clsx from "clsx";
import { useState } from "react";

const ThemeItem = ({ theme }: { theme: any }) => {
    const [selected, setSelected] = useState(false);
    const { selectTheme } = useThemeContext()
    return (
        <div className={clsx("border-2  w-fit rounded-full my-4", { "border-sky-500": selected })} onClick={() => { setSelected(!selected); selectTheme(theme) }}>
            <div className="w-12 h-12 rounded-full border-2 border-white shadow-sm shadow-slate-700" style={theme.messageStyle}>
            </div>
        </div>
    );
}

export default ThemeItem;