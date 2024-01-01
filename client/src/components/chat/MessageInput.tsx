import Input from "../ui/input";
const MessageInput = () => {
    return (
        <div className="flex items-center border-t py-2 px-4 bottom-0 w-full abs-centerx">

            <div className="flex items-center space-x-4">
                <label htmlFor="file-upload" className="cursor-pointer">
                    <input id="file-upload" type="file" className="hidden" />
                    📎
                </label>
                <span className="cursor-pointer" role="img" aria-label="Emoji">
                    😀
                </span>
                <button className="bg-blue-500 hover:bg-blue-600 text-white py-2 px-4 rounded-lg">
                    <span role="img" aria-label="Send">
                        ➡️
                    </span>
                </button>
            </div>
        </div >
    );
}

export default MessageInput;