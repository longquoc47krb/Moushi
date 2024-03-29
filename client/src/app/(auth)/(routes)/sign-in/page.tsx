/* eslint-disable react/no-unescaped-entities */
"use client";

import { loginApi } from "@/app/services/auth";
import Loading from "@/components/Loading";
import { Button } from "@/components/ui/button";
import Input from "@/components/ui/input";
import { Toaster } from "@/components/ui/toaster";
import { useToast } from "@/components/ui/use-toast";
import { loginSchema } from "@/validation";
import { zodResolver } from "@hookform/resolvers/zod";
import { motion } from "framer-motion";
import { useCookies } from "react-cookie";

import { useThemeContext } from "@/context/useThemeContext";
import withAuth from '@/hocs/withAuth';
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { FaCheck } from "react-icons/fa";
import { FcGoogle } from "react-icons/fc";

type LoginFormInputs = {
  credential: string;
  password: string;
};

function Page() {
  const {
    control,
    handleSubmit,
    register,
    formState: { errors },
  } = useForm<LoginFormInputs>({
    resolver: zodResolver(loginSchema),
  });
  const { push } = useRouter()
  const [cookie, setCookie] = useCookies(["currentUser"])
  const { toast } = useToast();
  const { theme } = useThemeContext()
  const onSubmit = async (data: LoginFormInputs) => {
    // Handle form submission logic here (e.g., login request)
    toast({
      title: "Processing...",
      description: (
        <div className="flex items-center gap-x-4">
          <Loading />
          <span>Processing...</span>{" "}
        </div>
      ),
    });
    await loginApi({
      credential: data.credential,
      password: data.password
    }).then((res: any) => {
      if (res.status === 201) {
        toast({
          title: "Login successfully",
          description: (
            <div className="flex items-center gap-x-4">
              <FaCheck className="text-green-500" />
              <span>Login successfully. Welcome to Moushi!</span>
            </div>
          ),
        });
        localStorage.setItem("accessToken", res?.data?.accessToken);
        setCookie("currentUser", JSON.stringify(res.data.user), {
          path: "/",
          maxAge: 3600 * 24, // Expires after 24hr
          sameSite: true,
        })
        push("/u")
      } else {
        toast({
          variant: "destructive",
          title: "Uh oh! Something went wrong.",
          description: "There was a problem with your request.",
        });
      }
    });
  };
  return (
    <div className="flex justify-center items-center p-4 h-[100dvh]">
      <Toaster />
      <motion.div
        className="md:w-2/5 md:px-4 w-full px-2"
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <p className="md:text-[40px] text-[32px] font-bold leading-10 text-gradient">
          Connect, Communicate, Celebrate
        </p>
        <span className="text-gray-400 text-lg leading-10">
          Take it easy to hang out with friends
        </span>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Input
            label="Email/Username"
            name="credential"
            className="mt-4"
            register={register}
          />
          {errors.credential && (
            <span className="text-red-600 text-xs">{errors.credential.message}</span>
          )}
          <Input
            type="password"
            label="Password"
            name="password"
            className="mt-4"
            register={register}
          />
          {errors.password && (
            <span className="text-red-600 text-xs">
              {errors.password.message}
            </span>
          )}
          <Button
            className="rounded-xl w-full my-4"
            type="submit"
            style={theme.backgroundStyle}
          >
            Log in
          </Button>
        </form>
        <div className="relative">
          <div className="absolute inset-0 flex items-center">
            <div className="w-full border-t border-gray-300" />
          </div>
          <div className="relative flex justify-center text-sm">
            <span className="bg-white px-2 text-gray-500">
              or continue with
            </span>
          </div>
        </div>
        <Button
          className="bg-gray-200 rounded-xl hover:bg-gray-300 w-full text-gray-500 my-4"
          type="submit"
        >
          <FcGoogle className="text-xl mr-2" />
          Sign in with Google
        </Button>
        <p className="text-gray-500 text-center">
          Please sign up if you don't have an account yet!{" "}
          <a
            className="underline text-sky-600 cursor-pointer"
            onClick={() => push("/sign-up")}
          >
            Register
          </a>
        </p>
      </motion.div>
      <Image
        src="/right-frame.png"
        alt="login"
        width={1027}
        height={800}
        className="w-3/5 rounded-3xl hidden md:block"
      />
    </div>
  );
}

export default withAuth(Page);
