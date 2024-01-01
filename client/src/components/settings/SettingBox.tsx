import { useEffect } from "react";
import ChangeThemeBox from "./change-theme";

const SettingBox = () => {
    let hash = window.location.hash;
    console.log({ hash })
    const switchSettingMode = (mode: string) => {
        switch (mode) {
            case "#change-theme":
                return <ChangeThemeBox />;
            default:
                return <p>Settings</p>
        }
    }
    useEffect(() => {
        switchSettingMode(hash)
    }, [hash])
    return (
        <div className="bg-gray-100 rounded-lg p-4 w-full h-full relative flex justify-center items-center ">
            {switchSettingMode(hash)}
        </div>
    );
}

export default SettingBox;