import { settingList } from "@/common/constants";
import SettingItem from "./SettingItem";

const SettingList = () => {
    return (
        <div className='bg-[whitesmoke] rounded-lg w-[40%]'>
            <h1 className='text-2xl font-bold px-2 pt-4'>Settings</h1>
            {settingList.map((s, index) => <SettingItem {...s} key={index} />)}
        </div>
    );
}

export default SettingList;