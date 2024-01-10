import Axios from "axios";

const api = Axios.create({
    baseURL: '/api/',
});

const chatAPI = {
    getMessages: (groupId: string) => {
        return api.get(`messages/${groupId}`);
    },

    sendMessage: (username: string, text: string) => {
        let msg = {
            sender: username,
            content: text
        }
        return api.post(`send`, msg);
    }
}


export default chatAPI;
