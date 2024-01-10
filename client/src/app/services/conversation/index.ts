import axiosClient from "@/configs/axiosClient";

export const fetchConversationsByUserIdApi = async (userId: string) => {
    try {
        const response = await axiosClient.get("/conversation/by-userId/" + userId);
        return response.data
    } catch (error) {
        return null
    }
}
export const fetchConversationByIdApi = async (conversationId: string) => {
    try {
        const response = await axiosClient.get("/conversation/by-id/" + conversationId);
        return response.data
    } catch (error) {
        return null
    }
}