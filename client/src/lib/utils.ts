import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"
import cookie from "cookie"
export function parseCookies(req) {
  return cookie.parse(req ? req.headers.cookie || "" : document.cookie)
}
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}
export const getReceiver = (users: any[], currentUser: object) => {
  if (users) {

    return users?.filter(u => u.id !== currentUser.id)[0];
  }
  return {}
}