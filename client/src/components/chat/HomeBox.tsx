import { useDataContext } from "@/context/useDataContext";
import Image from "next/image";

const HomeBox = () => {
    const { currentUser } = useDataContext()
    return (
        <div className="bg-gray-100 rounded-lg p-4 w-full h-full relative flex justify-center items-center ">
            <div className="flex flex-col justify-center items-center">
                <p className="bg-gray-200 px-4 py-2 rounded-full max-w-[60vw] zoom">Ayo, <strong>{currentUser.name}</strong>! Have a great day. Select a chat to have fun.</p>
                <Image src="/stickers/pepe/pepe5.gif" width={800} height={800} alt="pepe frog" className="w-52" />
            </div>
        </div>
    );
}

export default HomeBox;