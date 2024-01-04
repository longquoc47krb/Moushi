import { useAuthContext } from "@/context/useAuthContext";
import { useSocketContext } from "@/context/useSocketContext";
import Image from "next/image";
import { useEffect } from "react";

const ContentBox = () => {
    const { currentUser } = useAuthContext()
    const { connect, receivedMessage } = useSocketContext()
    useEffect(() => {
        connect()
    }, []);
    console.log({ receivedMessage })
    return (
        <div className="bg-gray-100 rounded-lg p-4 w-full h-full relative flex justify-center items-center ">
            <div className="flex flex-col justify-center items-center">
                <p className="bg-gray-200 px-4 py-2 rounded-full max-w-[60vw] zoom">
                    Ayo, <span className="font-bold">{currentUser?.fullName}</span>! Have a great day. Select a chat to have fun.</p>
                <Image src="/stickers/pepe/pepe20.gif" width={800} height={800} alt="pepe frog" className="w-52" />
            </div>
        </div>
    );
}

export default ContentBox;