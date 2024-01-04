import axiosClient from "@/configs/axiosClient";

type LoginType = {
    credential: string;
    password: string;
}
type RegisterType = {
    fullName: string;
    username: string;
    password: string;
    email: string;
    roles: string[]
}
export const loginApi = async (data: LoginType) => {
    try {
        const response = await axiosClient.post("/auth/login", data);
        return response
    } catch (error) {
        return {
            status: 400,
            message: error
        }
    }
}
export const registerApi = async (data: RegisterType) => {
    try {
        const response = await axiosClient.post("/auth/register", data);
        return response;
    } catch (error) {
        return {
            status: 400,
            message: error
        }
    }
}
export const logoutApi = async () => {
    try {
        const response = await axiosClient.post("/auth/logout");
        return response;
    } catch (error) {
        return {
            status: 400,
            message: error
        }
    }
}