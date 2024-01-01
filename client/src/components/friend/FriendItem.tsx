import { Avatar, AvatarFallback, AvatarImage } from "../ui/avatar";

const FriendItem = ({ friend }: { friend: any }) => {

    return (
        <div className="flex  py-4 items-start justify-between w-full cursor-pointer hover:bg-gray-200 hover:transition-all hover:duration-100 px-2" >
            <div className='flex items-center gap-x-2 px-4'>
                <Avatar>
                    <AvatarImage src={friend?.profilePicture} />
                    <AvatarFallback>CN</AvatarFallback>
                </Avatar>
                <p className='font-semibold text-base'>{friend?.fullName}</p>
            </div>

        </div>
    );
}

export default FriendItem;