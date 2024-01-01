import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}
export const getReceiver = (users: any[], currentUser: object) => {
  if (users) {

    return users?.filter(u => u.id !== currentUser.id)[0];
  }
  return {}
}