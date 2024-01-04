import { logoutApi } from "@/app/services/auth";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog"
import { useSocketContext } from "@/context/useSocketContext";
import { useRouter } from "next/navigation";

const LogoutDialog = ({ children }: { children: any }) => {
    const router = useRouter()
    const { disconnect } = useSocketContext()
    return (
        <AlertDialog>
            <AlertDialogTrigger>{children}</AlertDialogTrigger>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                    <AlertDialogDescription>
                        Are you sure you want to sign out? All unsaved changes will be lost.
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                    <AlertDialogAction onClick={async () => {
                        localStorage.removeItem("accessToken");
                        router.push("/sign-in")
                        await logoutApi()
                        disconnect()
                    }}>Continue</AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>

    );
}

export default LogoutDialog;