"use client"
import axiosClient from "@/configs/axiosClient";
import { useEffect, useState } from "react";
import FriendItem from "./FriendItem";
import { useAuthContext } from "@/context/useAuthContext";

const FriendList = () => {
    const { currentUser } = useAuthContext()
    const [friendList, setFriendList] = useState([])
    useEffect(() => {
        const fetchFriends = async () => {
            const response = await axiosClient.get(`/user/friend-list/${currentUser.id}`)
            if (response.status === 200) {
                setFriendList(response.data)
            }
        }

        fetchFriends()
    }, [])
    return (
        <div className='bg-[whitesmoke] rounded-lg w-[40%]'>
            <h1 className='text-2xl font-bold px-2 pt-4'>People</h1>
            {friendList.map((friend, index) => <FriendItem friend={friend} />)}
        </div>
    )
}

export default FriendList;