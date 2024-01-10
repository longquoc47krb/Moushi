"use client";
import { cn } from "@/lib/utils";
import React, { useState } from "react";
import { useFormContext } from "react-hook-form";
import { FaRegEyeSlash, FaRegEye } from "react-icons/fa";
import { ImAttachment } from "react-icons/im";
import { FaPaperPlane } from "react-icons/fa";
import clsx from "clsx";
import { useThemeContext } from "@/context/useThemeContext";

interface InputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  type?: string;
  className?: string;
  label?: string;
  width?: string | number;
  name?: string;
  register?: any;
  onClickSend?: any
}
function Input({
  className,
  label,
  type = "text",
  width,
  name,
  register,
  ...props
}: InputProps) {
  const [togglePassword, setTogglePassword] = useState(false);
  const handleTogglePassword = (type: string) => {
    if (type === "password") return togglePassword ? "text" : "password";
    return "text";
  };
  return (
    <div className={cn("relative", width)}>
      <input
        type={handleTogglePassword(type)}
        className={cn(
          "block px-2.5 pb-2.5 pt-4 w-full text-sm text-gray-900 bg-[#f5f5f5] rounded-lg  dark:text-white dark:border-gray-600 dark:focus:border-sky-500 focus:outline-none focus:ring-0 focus:border-sky-500 focus:border-2 peer",
          className
        )}
        placeholder={label}
        {...register(name)}
      />
      {type === "password" && (
        <p
          className="right-2 text-gray-500 center-y"
          onClick={() => setTogglePassword(!togglePassword)}
        >
          {togglePassword ? <FaRegEye /> : <FaRegEyeSlash />}
        </p>
      )}
    </div>
  );
}
export function MessageInputField({
  className,
  label,
  type = "text",
  width,
  name,
  onClickSend,
  ...props
}: InputProps) {
  const { theme } = useThemeContext()
  const { iconStyle } = theme;
  return (
    <div className={cn("relative flex gap-x-1", "w-full")}>
      <input
        type="text"
        className={cn(
          "block px-2.5 pb-2.5 pt-4 pl-8 w-full text-sm text-gray-900 bg-white rounded-lg  dark:text-white dark:border-gray-60 focus:outline-none focus:ring-0  peer shadow-sm shadow-gray-500",
          className
        )}
        value={props.value}
        onChange={props.onChange}
        placeholder="Type something..."
      />
      <ImAttachment className="absolute left-2 abs-centery" />
      <FaPaperPlane className={clsx(" text-5xl p-2 cursor-pointer")} style={iconStyle} onClick={onClickSend} />
    </div>
  );
}
export default Input;
