import { createContext, useContext, useEffect } from "react";
import { useStomp } from "../hooks/useStomp";
import { NotificationDTO } from "@/interfaces/Notification";

interface StompContextInterface {
  sendMessage: ((message: NotificationDTO) => void) | null;
  isConnected: boolean | null;
}
export const StompContext = createContext<StompContextInterface>({
  sendMessage: null,
  isConnected: null,
});
StompContext.displayName = "StompContext";

export function useStompContext() {
  return useContext(StompContext);
}

interface StompProviderProps {
  children: JSX.Element;
}

export const StompProvider = ({ children }: StompProviderProps) => {
  const { connect, sendMessage, isConnected } = useStomp();

  useEffect(() => {
    connect();
  }, []);
  return (
    <StompContext.Provider value={{ sendMessage, isConnected }}>
      {children}
    </StompContext.Provider>
  );
};
