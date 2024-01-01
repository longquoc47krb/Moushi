/* eslint-disable react/no-unescaped-entities */
"use client";
import { Button } from "@/components/ui/button";
import Input from "@/components/ui/input";
import Image from "next/image";
import { registerSchema } from "@/validation";
import { zodResolver } from "@hookform/resolvers/zod";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { FcGoogle } from "react-icons/fc";
import { motion } from "framer-motion";
import { useEffect, useState } from "react";
import { FaQuestionCircle } from "react-icons/fa";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import PasswordRules from "@/components/PasswordRules";
import Loading from "@/components/Loading";
import { registerApi } from "@/app/services/auth";
import { Toaster } from "@/components/ui/toaster";
import { useToast } from "@/components/ui/use-toast";
import { FaCheck } from "react-icons/fa";

type RegisterFormInputs = {
  fullName: string;
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
};

function Page() {
  const {
    control,
    handleSubmit,
    register,
    formState: { errors },
    getValues,
    watch,
  } = useForm<RegisterFormInputs>({
    resolver: zodResolver(registerSchema),
  });
  const [passwordState, setPasswordState] = useState({
    validLength: false,
    hasUppercase: false,
    hasLowercase: false,
    hasSpecialSymbol: false,
    hasNumber: false,
  });
  const { toast } = useToast();
  const router = useRouter();
  watch("password");
  const password = getValues("password");
  const onSubmit = async (data: RegisterFormInputs) => {
    toast({
      title: "Processing...",
      description: (
        <div className="flex items-center gap-x-4">
          <Loading />
          <span>Processing...</span>{" "}
        </div>
      ),
    });
    await registerApi({
      email: data.email,
      fullName: data.fullName,
      password: data.password,
      username: data.username,
      roles: ["ROLE_BASIC"],
    }).then((res) => {
      if (res.status === 200) {
        toast({
          title: "Register successfully",
          description: (
            <div className="flex items-center gap-x-4">
              <FaCheck className="text-green-500" />
              <span>
                Registered successfully. Please go to login page to enjoy!
              </span>
            </div>
          ),
        });
      } else {
        toast({
          variant: "destructive",
          title: "Uh oh! Something went wrong.",
          description: "There was a problem with your request.",
        });
      }
    });
  };
  useEffect(() => {
    const validatePassword = () => {
      const passwordValidation = {
        validLength: password?.length >= 8,
        hasUppercase: /[A-Z]/.test(password),
        hasLowercase: /[a-z]/.test(password),
        hasNumber: /\d/.test(password),
        hasSpecialSymbol: /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]+/.test(
          password
        ),
      };

      setPasswordState(passwordValidation);
    };

    validatePassword();
    return () => {
      // Clean-up function (optional)
      setPasswordState({
        validLength: false,
        hasUppercase: false,
        hasLowercase: false,
        hasNumber: false,
        hasSpecialSymbol: false,
      });
    };
  }, [password]);
  return (
    <div className="flex justify-center items-center p-4 h-[100dvh]">
      <Toaster />
      <motion.div
        className="w-2/5 px-4 "
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.5 }}
      >
        <p className="text-[40px] font-bold leading-10 text-gradient">
          Unlock Your Journey
        </p>
        <span className="text-gray-400 text-lg leading-10">
          Register now to begin new adventure!
        </span>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Input
            label="Full name"
            name="fullName"
            className="mt-4"
            register={register}
          />
          {errors.fullName && (
            <span className="text-red-600 text-xs">
              {errors.fullName.message}
            </span>
          )}
          <Input
            label="Username"
            name="username"
            className="mt-4"
            register={register}
          />
          {errors.username && (
            <span className="text-red-600 text-xs">
              {errors.username.message}
            </span>
          )}
          <Input
            label="Email"
            name="email"
            className="mt-4"
            register={register}
          />
          {errors.email && (
            <span className="text-red-600 text-xs">{errors.email.message}</span>
          )}
          <Input
            type="password"
            label="Password"
            name="password"
            className="mt-4"
            register={register}
          />
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger>
                <p className="text-gray-400 text-xs block">
                  <FaQuestionCircle className="inline-block" /> Password rules
                </p>
              </TooltipTrigger>
              <TooltipContent>
                <PasswordRules passwordState={passwordState} />
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>

          {errors.password && (
            <span className="text-red-600 text-xs">
              {errors.password.message}
            </span>
          )}
          <Input
            type="password"
            label="Confirm password"
            name="confirmPassword"
            className="mt-4"
            register={register}
          />
          {errors.confirmPassword && (
            <span className="text-red-600 text-xs">
              {errors.confirmPassword.message}
            </span>
          )}
          <Button
            className="bg-sky-600 rounded-xl hover:bg-sky-700 w-full text-white my-4"
            type="submit"
          >
            Sign up
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
          You had account, didn't you?{" "}
          <a
            className="underline text-sky-600 cursor-pointer"
            onClick={() => router.push("/sign-in")}
          >
            Log in
          </a>
        </p>
      </motion.div>
      <Image
        src="/right-frame.png"
        alt="login"
        width={1027}
        height={800}
        className="w-3/5 rounded-3xl"
      />
    </div>
  );
}

export default Page;
