"use client"
import axiosClient from "@/configs/axiosClient";
import { useEffect, useState } from "react";
import FriendItem from "./FriendItem";
import { useAuthContext } from "@/context/useAuthContext";
import { fetchFriendListApi, fetchFriendRequestListApi } from "@/app/services/user";
import { useParams } from "next/navigation";
import { useQuery } from "@tanstack/react-query";
import FriendCollapse from "./FriendCollapse";
import FriendRequestItem from "./FriendRequestItem";

const FriendList = () => {
    const { currentUser } = useAuthContext()
    const { data: friendList } = useQuery({
        queryKey: ['friendList'],
        queryFn: () => fetchFriendListApi(currentUser?.id),

    })
    const { data: friendRequestList } = useQuery({
        queryKey: ['friendRequestList'],
        queryFn: () => fetchFriendRequestListApi(currentUser?.id),

    })
    return (
        <div className='bg-[whitesmoke] rounded-lg w-[40%]'>
            <h1 className='text-2xl font-bold px-4 pt-4'>People</h1>
            <FriendCollapse label="Friend requests" listSize={friendRequestList?.length}>
                {friendRequestList?.map((friend: any, index: number) => <FriendRequestItem friend={friend} key={index} currentUserId={currentUser?.id} />)}
            </FriendCollapse>
            <FriendCollapse label="My friends" listSize={friendList?.length} defaultCollapseStatus={true}>
                {friendList?.map((friend: any, index: number) => <FriendItem friend={friend} key={index} />)}
            </FriendCollapse>

        </div>
    )
}

export default FriendList;