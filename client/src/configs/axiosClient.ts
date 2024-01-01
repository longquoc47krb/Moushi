import axios, { AxiosError, AxiosResponse } from "axios";

const axiosClient = axios.create({
    baseURL: "http://localhost:8080/v1/api/",
    timeout: 15000,
    headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
        "x-api-key": process.env.NEXT_PUBLIC_API_KEY
    },
});


// Add a request interceptor
axiosClient.interceptors.request.use(
    function (config) {
        if (!config?.headers) {
            throw new Error(`Expected 'config' and 'config.headers' not to be undefined`);
        }
        // config.headers.Authorization = token ?? "";
        config.headers["Content-Type"] = "application/json";
        config.headers["x-api-key"] = process.env.NEXT_PUBLIC_API_KEY;
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

// Add a response interceptor
axiosClient.interceptors.response.use(
    function (response: AxiosResponse) {
        return response.data;
    },
    function (error: AxiosError) {
        const { data, status } = error.response || {};
        if (status === 401 || status === 403) {

            throw new Error("Unauthorized or Access Token is expired")
        }
        axiosClient.defaults.headers.common[
            "authorization"
        ] = localStorage.getItem("accessToken") || "";

        return Promise.reject(error);
    }
);

export default axiosClient;
