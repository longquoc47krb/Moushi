import { useRouter } from "next/navigation";

type SettingItemProps = {
    icon: any;
    title: string;
    slug: string
}

const SettingItem = (props: SettingItemProps) => {
    const router = useRouter()
    return (
        <div className="flex gap-x-2 items-center p-4 hover:bg-gray-200 text-gray-900 cursor-pointer" onClick={() => router.push(`/settings#${props.slug}`)}>
            {props.icon} <span>{props.title}</span>
        </div>
    );
}

export default SettingItem;