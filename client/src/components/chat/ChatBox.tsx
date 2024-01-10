import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { GoDotFill } from "react-icons/go";
import { BsThreeDotsVertical } from "react-icons/bs";
import Message from "./Message";
import { IConversation, IConversationResponse, IMessage, IMessageReq, IMessageResponse, IUser } from "@/interfaces";
import MessageInput from "./MessageInput";
import { useForm } from "react-hook-form";
import { MessageInputField } from "../ui/input";
import { messages, user2 } from "@/mocks/data";
import { getReceiver } from "@/lib/utils";
import Verified from "../Verified";
import { useAuthContext } from "@/context/useAuthContext";
import { fetchConversationByIdApi } from "@/app/services/conversation";
import { useQuery } from "@tanstack/react-query";
import { UserState } from "@/common/constants";
import moment from "moment"
import { Skeleton } from '@/components/ui/skeleton';
import { useSocketContext } from "@/context/useSocketContext";
import { useEffect, useState } from "react";
import clsx from "clsx";
import { useStompClient, useSubscription } from 'react-stomp-hooks';

const ChatBox = ({ conversationId }: { conversationId: | any }) => {
    const { data: conversation, isLoading } = useQuery({
        queryKey: ["conversation", conversationId],
        queryFn: () => fetchConversationByIdApi(conversationId),

    })
    const [messages, setMessages] = useState(null);
    const [content, setContent] = useState("")
    const { currentUser } = useAuthContext()
    const stompClient = useStompClient();
    const handleStatus = (status: string, lastOnline?: any) => {
        if (status === UserState.ONLINE) {
            return <small className=" text-gray-400 flex items-center"><GoDotFill className="text-lime-500 inline-block" />Active now</small>
        } else {
            return <small className=" text-gray-400 flex items-center"><GoDotFill className="text-gray-500" />Active {moment(lastOnline).fromNow()}</small>
        }
    }
    const myFriend: IUser = conversation?.participants?.filter((user: IUser) => user.id !== currentUser.id)[0];
    useSubscription('/user/' + currentUser.username + '/private-messages', (message) => {
        console.log("Message: ", message.body);
        setMessages(message.body);
    })
    const messageObj: IMessageReq = {
        content,
        conversationId,
        username: currentUser.username
    }
    const onMessageSent = () => {
        if (stompClient && content !== "") {
            stompClient.publish({
                destination: "/app/private.chat",
                body: JSON.stringify(messageObj)
            })
            console.log(JSON.stringify(messageObj))
        } else { }
        setContent("");
    }
    console.log({ messages })
    return (
        <div className="bg-[whitesmoke] rounded-lg p-4 w-full h-full relative">
            <div className="flex justify-between border-b border-b-gray-300 pb-4">
                <div className="flex">
                    {isLoading ? <Skeleton className="w-[40px] h-[40px] rounded-full" /> : <Avatar>
                        <AvatarImage src={myFriend?.profilePicture} />
                        <AvatarFallback>CN</AvatarFallback>
                    </Avatar>}
                    <div className="ml-4">
                        {isLoading ? <Skeleton className="w-[200px] h-4 rounded-lg mb-2" /> : <p className="font-semibold">{myFriend?.fullName}</p>}

                        {isLoading ? <Skeleton className="w-[200px] h-4 rounded-lg mb-2" /> : handleStatus(myFriend?.userState, myFriend?.lastOnline)}
                    </div>
                </div>
                <BsThreeDotsVertical className="text-gray-500" />
            </div>
            <div className="flex flex-col items-start max-h-[75vh] overflow-y-auto pr-2" id="chatbox-content">
                {conversation?.messages?.map((message: IMessageResponse, index: number) => <Message message={message} options={{
                    currentUserId: currentUser.id,
                    senderExceptCurrentUser: getReceiver(conversation.participants, currentUser),
                    messageList: conversation?.messages,
                    messageKey: index,
                }} key={index} />)}
            </div>
            <div className="absolute abs-centerx bottom-2 w-full px-4">
                <MessageInputField value={content} onChange={(event: React.ChangeEvent<HTMLInputElement>) => setContent(event.target.value)} onClickSend={onMessageSent} />
            </div>
        </div>
    );
}

export default ChatBox;