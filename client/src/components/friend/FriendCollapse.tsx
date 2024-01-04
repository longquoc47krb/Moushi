import {
    Collapsible,
    CollapsibleContent,
    CollapsibleTrigger,
} from "@/components/ui/collapsible"
import React from "react";
interface FriendCollapseProps {
    label: string;
    listSize: number;
    children: any;
    defaultCollapseStatus?: boolean;
}
const FriendCollapse = ({ label, listSize, children, defaultCollapseStatus = false }: FriendCollapseProps) => {
    const [isOpen, setIsOpen] = React.useState(defaultCollapseStatus)
    return (
        <Collapsible open={isOpen}
            onOpenChange={setIsOpen}>
            <CollapsibleTrigger><p className="text-base px-4 pt-4">{label} ({listSize ?? 0})</p></CollapsibleTrigger>
            <CollapsibleContent>
                {children}
            </CollapsibleContent>
        </Collapsible>

    );
}

export default FriendCollapse;