import { IMessage, IMessageResponse } from "@/interfaces";
import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";
import { useThemeContext } from "@/context/useThemeContext";
import clsx from "clsx";
import Image from "next/image";
import { useEffect, useState } from "react";
import MessageTooltip from "../MessageTooltip";
import { getTimeAgo } from "@/lib/utils";
type MessageItem = {
  message: IMessageResponse;
  options: {
    currentUserId: string;
    senderExceptCurrentUser: any;
    messageList?: IMessageResponse[];
    messageKey?: number;
  }

}

const Message = (props: MessageItem) => {
  const { message, options } = props;
  const { messageList, messageKey } = options;
  const { theme } = useThemeContext()
  const { backgroundStyle } = theme;
  const shouldHideAvatarForCurrentMessage = () => {
    if (messageList[messageKey]?.sender?.id == messageList[messageKey - 1]?.sender?.id && messageKey > 0) return true;
    return false;
  }
  if (options.currentUserId === message.sender.id) {
    return <><div className="flex items-end self-end">
      <div className={clsx(`chat-bubble`)} style={backgroundStyle}>
        <p>{message.content}</p>

      </div>
      {/* <small>Sent at {message.dateSent.toISOString()}</small> */}
    </div>
      {message.image && <Image src={message.image} width={400} height={400} alt="picture" className="w-[300px] mt-1 rounded-xl self-end" />}
    </>
  }
  return <><div className="flex items-end">
    <Avatar className={clsx("w-8 h-8", { "opacity-0": shouldHideAvatarForCurrentMessage() })}>
      <AvatarImage src={options.senderExceptCurrentUser.profilePicture} />
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
