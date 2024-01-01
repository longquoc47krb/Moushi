import { useThemeContext } from "@/context/useThemeContext";
import ThemeItem from "./ThemeItem";
import { themes } from "@/themes";

const ChangeThemeBox = () => {
    const { theme } = useThemeContext()
    const allThemeStyles = Object.values(themes)
    console.log({ allThemeStyles })
    return (
        <div className="bg-gray-100 rounded-lg p-4 w-full h-full relative">
            <p className="font-semibold pb-4 border-b border-b-gray-300 text-2xl">Customize theme</p>
            <div className="my-4 leading-6">
                <p>Current theme:</p>
                <ThemeItem theme={theme} />
                <p>Available themes:</p>
                <div className="flex gap-x-4">
                    {
                        allThemeStyles.map((theme, index) => <ThemeItem theme={theme} key={index} />)
                    }
                </div>
            </div>
        </div>
    );
}

export default ChangeThemeBox;