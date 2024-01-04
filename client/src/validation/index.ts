import z from "zod";

export const loginSchema = z.object({
    credential: z.string().regex(/^(?=.*[a-zA-Z0-9@._-])[a-zA-Z0-9@._-]+$/, "Invalid username or email"),
    password: z.string().min(8),
});
export const registerSchema = z.object({
    fullName: z.string({
        required_error: "Name is required",
        invalid_type_error: "Name must be a string",
    }),
    username: z.string({
        required_error: "Name is required",
        invalid_type_error: "Name must be a string",
    }).regex(/^\S+$/, 'Username should not contain spaces').trim(),
    email: z.string().email(),
    password: z.string().min(8, 'Password should be at least 8 characters long'),
    confirmPassword: z.string()
}).refine((data) => data.password === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
});;