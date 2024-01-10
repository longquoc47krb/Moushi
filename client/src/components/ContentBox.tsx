import { useAuthContext } from "@/context/useAuthContext";
import Image from "next/image";

const ContentBox = () => {
    const { currentUser } = useAuthContext()
    const defaultPage = () => {
        return <div className="flex flex-col justify-center items-center">
        <p className="bg-gray-200 px-4 py-2 rounded-full max-w-[60vw] zoom">
            Ayo, {currentUser?.fullName}! Have a great day. Select a chat to have fun.</p>
        <Image src="/stickers/pepe/pepe20.gif" width={800} height={800} alt="pepe frog" className="w-52" />
    </div>
    }
    return (
        <div className="bg-gray-100 rounded-lg p-4 w-full h-full relative flex justify-center items-center ">
            {defaultPage()}
        </div>
    );
}

export default ContentBox;