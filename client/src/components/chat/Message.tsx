import { IMessage } from "@/interfaces";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { useThemeContext } from "@/context/useThemeContext";
import clsx from "clsx";
import Image from "next/image";
import { useEffect, useState } from "react";
type MessageItem = {
  message: IMessage;
  currentUserId: string;
  senderExceptCurrentUser: any;
}

const Message = (props: MessageItem) => {
  const { message, currentUserId, senderExceptCurrentUser } = props;
  const { theme } = useThemeContext()
  const { messageStyle } = theme;

  if (currentUserId === message.senderId) {
    return <><div className="flex items-end self-end">
      <div className={clsx(`chat-bubble`)} style={messageStyle}>
        <p>{message.content}</p>

      </div>
      {/* <small>Sent at {message.dateSent.toISOString()}</small> */}
    </div>
      {message.image && <Image src={message.image} width={400} height={400} alt="picture" className="w-[300px] mt-1 rounded-xl self-end" />}
    </>
  }
  return <><div className="flex items-end">
    <Avatar className="w-8 h-8">
      <AvatarImage src={senderExceptCurrentUser.avatar} />
      <AvatarFallback>CN</AvatarFallback>
    </Avatar>
    <div className={`chat-bubble bg-[#e0e0e0]`}>
      {/*  <h2>{message.sender.name}</h2> */}
      <p>{message.content}</p>

    </div>
    {/* <small>Sent at {message.dateSent.toISOString()}</small> */}
  </div>
    {message.image && <Image src={message.image} width={400} height={400} alt="picture" className="w-[300px] mt-1 rounded-xl ml-11" />}
  </>
};

export default Message;
