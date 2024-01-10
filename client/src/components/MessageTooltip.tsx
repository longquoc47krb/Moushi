import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "./ui/tooltip";
type TooltipProps = {
    children: React.ReactNode;
    content: string;
}
const MessageTooltip = (props: TooltipProps) => {
    const { children, content } = props;
    return (
        <TooltipProvider>
            <Tooltip>
                <TooltipTrigger className="w-[200px]">{children}</TooltipTrigger>
                <TooltipContent>
                    <p>{content}</p>
                </TooltipContent>
            </Tooltip>
        </TooltipProvider>

    );
}

export default MessageTooltip;