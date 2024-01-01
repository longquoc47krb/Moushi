import { IMessage, IUser } from "@/interfaces";

//  Mock data
export const user1: IUser = {
    id: "1",
    avatar: "https://scontent-hkg1-2.xx.fbcdn.net/v/t39.30808-6/330508930_1165912617447802_6872084020065158858_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=9c7eae&_nc_eui2=AeHVmFlkl3Abg0NT08GSmKMtNhfhz1JSg9k2F-HPUlKD2Y5Jw1yXmymvmyRcNeFGH4-tABksmc-1C5wNILcaIJC1&_nc_ohc=8a7YwPkMGdYAX_OuAnI&_nc_ht=scontent-hkg1-2.xx&oh=00_AfAIwyB8pEuGktEihqfb03kEXzSVOWST2wWF3nojlkqzpA&oe=65944C15",
    name: "Truong Anh Ngoc "
};

export const user2: IUser = {
    id: "2",
    name: "Jane Smith",
    avatar: "https://github.com/shadcn.png"
};

export const messages: IMessage[] = [
    {
        sender: user1,
        receiver: user2,
        content: "Muốn trải nghiệm xe khác, nhưng tình hình kinh tế khó khăn, có ai muốn đổi xe với tôi không hở giời...",
        createdAt: new Date("2023-12-29T10:00:00Z"),
        dateSent: new Date("2023-12-29T10:00:00Z"),
        image: "https://vespatopcom.com/wp-content/uploads/2022/11/TGV-17.12-03-1024x1024.jpg"
    },
    {
        sender: user2,
        receiver: user1,
        content: "Em có chiếc xe đạp cần đổi kkk",
        createdAt: new Date("2023-12-29T10:05:00Z"),
        dateSent: new Date("2023-12-29T10:05:00Z"),
    },
    {
        sender: user1,
        receiver: user2,
        content: "Thế thì chỉ đổi được cặp gương chiếu hậu thôi nhé",
        createdAt: new Date("2023-12-29T10:10:00Z"),
        dateSent: new Date("2023-12-29T10:10:00Z"),
        image: "https://vespatopcom.com/wp-content/uploads/2022/11/z4727650160770_170c7e7decdb5052e11ce2663012a258-768x1024.jpg"
    },
    {
        sender: user2,
        receiver: user1,
        content: "Có cặp gương nào 70 triệu ko a, con Trek Procaliber của em cũng tầm đấy",
        createdAt: new Date("2023-12-29T10:15:00Z"),
        dateSent: new Date("2023-12-29T10:15:00Z"),
        image: "https://ep1.pinkbike.org/p6pb19290010/p6pb19290010.jpg"
    },
];