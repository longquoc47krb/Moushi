export interface IUser {
    id: string;
    name: string;
    avatar: string;
}

export interface IMessage {
    id: string;
    senderId: string;
    receiverId: string;
    content: string;
    createAt: Date;
    dateSent: Date;
    image?: string;
    conversationId?: string;
}
export interface IConversationDto {
    id: string; // UUID as string
    fullName: string;
    username: string;
    image: string; // Base64-encoded string or URL
    lastOnline: Date;
    typingState: TypingState;
}
type TypingState = | "IDLE" | "TYPING";

export interface INotificationDTO {
    content: string;
    type: NotificationType;
    metadata: string;
}
export interface ITheme {
    profileStyle: object;
    messageStyle: object;
    iconStyle: object
}
type NotificationType =
    | "USER_STATE"
    | "ONLINE_USERS"
    | "INCOMING_MESSAGE"
    | "INCOMING_CALL"
    | "REJECTED_CALL"
    | "CANCELLED_CALL"
    | "ACCEPTED_CALL";

