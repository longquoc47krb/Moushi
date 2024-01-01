/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: false,
  images: {
    domains: [
      "vespatopcom.com",
      "ep1.pinkbike.org",
      "avatars.githubusercontent.com",
      "scontent-hkg4-1.xx.fbcdn.net",
      "s3.amazonaws.com"
    ],
  },
};

module.exports = nextConfig;
