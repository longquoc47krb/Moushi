export interface IUser {
    id: string; // Assuming UUID is represented as a string in TypeScript
    fullName: string;
    username: string;
    email: string;
    phone: string;
    address: string;
    roles: Role[]; // Assuming Role is another interface or type
    userState: UserState; // Assuming UserState is another interface or type
    conversationsByUserId: IConversation[]; // Assuming Conversation is another interface or type
    lastOnline: Date | null; // Using Date or null to represent Instant
    profilePicture: string;
    sessions: number;
}
export interface IFriendInvitationResponse {
    requestId: string;
    sender: IUser;
}
export interface IConversation {
    id: string;
    participants: IUser[];
    messages: IMessage[];
    groupConversation: boolean;
    dateStarted: Date; // Or number if representing milliseconds since epoch
    status: ConversationStatus;
}
enum ConversationStatus {
    ACCEPTED = 'ACCEPTED',
    PENDING = 'PENDING',
    REJECTED = 'REJECTED',
    // Other possible conversation statuses
}
enum UserState {
    ONLINE = 'ONLINE',
    OFFLINE = 'OFFLINE',
    BUSY = 'BUSY',
    AWAY = 'AWAY'
}
enum Role {
    ROLE_BASIC = 'ROLE_BASIC',
    ROLE_PREMIUM = 'ROLE_PREMIUM',
    ROLE_ADMIN = 'ROLE_ADMIN',
}
enum MessageState {
    RECEIVED = 'RECEIVED',
    DELIVERED = 'DELIVERED',
    READ = 'READ',
    DELETED = 'DELETED',
    EDITED = 'EDITED',
}

export interface IMessage {
    id: string;
    sender: IUser;
    conversation: IConversation;
    content: string;
    image: ArrayBuffer | null; // Representing binary data as ArrayBuffer or null
    createdAt: Date; // Or number if representing milliseconds since epoch

    dateSent: Date | null;
    dateDelivered: Date | null;
    dateRead: Date | null;
    states: MessageState[];
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

export interface ISenderReq {
    senderId: string;
    friendRequestStatus: string;
    friendRole?: string;
}
type FriendRequestStatus =
    | "PENDING"
    | "ACCEPTED"
    | "REJECTED"