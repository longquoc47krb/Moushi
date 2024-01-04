import type { Metadata } from "next";
import { Inter } from "next/font/google";
import { Providers } from "@/providers";
import withAuth from "@/hocs/withAuth";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Login | Connect, Communicate, Celebrate",
  description: "Take it easy to hang out with friends",
};

function LoginLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <Providers>
        <body className={inter.className}>{children}</body>
      </Providers>
    </html>
  );
}
export default (LoginLayout)