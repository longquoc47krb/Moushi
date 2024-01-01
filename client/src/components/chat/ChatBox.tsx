import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { GoDotFill } from "react-icons/go";
import { BsThreeDotsVertical } from "react-icons/bs";
import Message from "./Message";
import { IMessage, IUser } from "@/interfaces";
import MessageInput from "./MessageInput";
import { useForm } from "react-hook-form";
import { MessageInputField } from "../ui/input";
import { messages, user2 } from "@/mocks/messageList";
import { getReceiver } from "@/lib/utils";
import Verified from "../Verified";

interface ChatBoxProps {
    conversation?: any;

}
interface Content {
    content: string
}
const ChatBox = ({ conversation }: ChatBoxProps) => {
    const handleStatus = (status: string) => {
        if (status === "online") {
            return <small className=" text-gray-400"><GoDotFill className="text-lime-500 inline-block" />Online</small>
        } else {
            return <small className=" text-gray-400"><GoDotFill className="text-gray-500" />Offline</small>
        }
    }


    const {
        control,
        handleSubmit,
        register,
        formState: { errors },
    } = useForm<Content>();
    const myFriend = getReceiver(conversation.participants, currentUser);
    return (
        <div className="bg-gray-100 rounded-lg p-4 w-full h-full relative">
            <div className="flex justify-between border-b border-b-gray-300 pb-4">
                <div className="flex">
                    <Avatar>
                        <AvatarImage src={myFriend.avatar} />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>
                    <div className="ml-4">
                        <p className="font-semibold leading-4">{myFriend.name}{myFriend.verified && <Verified />}</p>
                        {handleStatus("online")}
                    </div>
                </div>
                <BsThreeDotsVertical className="text-gray-500" />
            </div>
            <div className="flex flex-col items-start max-h-[75vh] overflow-y-auto pr-4" id="chatbox-content">
                {conversation.messages?.map((message: any, index) => <Message message={message} currentUserId={currentUser.id} key={index} senderExceptCurrentUser={getReceiver(conversation.participants, currentUser)} />)}
            </div>
            <div className="absolute abs-centerx bottom-2 w-full px-4">
                <MessageInputField />
            </div>
        </div>
    );
}

export default ChatBox;