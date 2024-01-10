import { IUser } from "@/interfaces"
import { clsx, type ClassValue } from "clsx"
import moment from "moment"
import { twMerge } from "tailwind-merge"
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}
export const getReceiver = (users: IUser[] | undefined, currentUser: IUser) => {
  if (users) {

    return users?.filter((u: IUser) => u.id !== currentUser.id)[0];
  }
  return {}
}
export const getTimeAgo = (timestamp: string) => {
  const timeAgo = moment(timestamp).fromNow(); // Get relative time using fromNow()
  const timeAgoParts = timeAgo.split(" "); // Split the string to analyze parts

  if (timeAgoParts.length > 2) {
    if (timeAgoParts[0].startsWith("a")) {
      if (timeAgoParts[1].startsWith("hour")) return "1h";
      if (timeAgoParts[1].startsWith("minute")) return "1m";
      if (timeAgoParts[1].startsWith("day")) return "1d";
      if (timeAgoParts[1].startsWith("month")) return "1mo";
      if (timeAgoParts[1].startsWith("year")) return null;
    }
    if (timeAgoParts[1].startsWith("days") && Number(timeAgoParts[0]) >= 7) {
      return Math.floor(Number(timeAgoParts[0]) / 4) + "w";
    }
    if (timeAgoParts[1].startsWith("hours")) return timeAgoParts[0] + "h";
    if (timeAgoParts[1].startsWith("minutes")) return timeAgoParts[0] + "m";
    if (timeAgoParts[1].startsWith("days")) return timeAgoParts[0] + "d";
    if (timeAgoParts[1].startsWith("months")) return timeAgoParts[0] + "mo";
    if (timeAgoParts[1].startsWith("years")) return null;
  }
  if (timeAgo === "a few seconds ago") {
    return "1m";
  }

  // Return the original relative time if it's not in minutes
  return timeAgo;
};