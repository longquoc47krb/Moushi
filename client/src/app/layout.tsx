import type { Metadata } from "next";
import { Inter } from "next/font/google";
import { Providers } from "../providers";
import "./globals.scss";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Moushi | Hang out with everyone",
  description: "Welcome to Moushi!",
};

export default function RootLayout({
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
