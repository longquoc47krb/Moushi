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
import {
    Tabs,
    TabsContent,
    TabsList,
    TabsTrigger,
} from "@/components/ui/tabs"
import { Skeleton } from "@/components/ui/skeleton"

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
            <Tabs defaultValue="my_friends" className="">
                <TabsList className="grid w-full grid-cols-2">
                    <TabsTrigger value="friend_request">Friend request</TabsTrigger>
                    <TabsTrigger value="my_friends">My Friends</TabsTrigger>
                </TabsList>
                <TabsContent value="friend_request">

                    {friendRequestList?.length === 0 && <p className="text-gray-400 px-4 text-sm">No friend requests</p>}
                    {friendRequestList?.map((friend: any, index: number) => <FriendRequestItem friend={friend} key={index} currentUserId={currentUser?.id} />)}
                </TabsContent>
                <TabsContent value="my_friends">
                    {friendList?.map((friend: any, index: number) => <FriendItem friend={friend} key={index} />)}
                </TabsContent>
            </Tabs>
        </div>
    )
}

export default FriendList;