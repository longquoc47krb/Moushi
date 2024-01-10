package id.longquoc.messenger.config.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DestinationStore {
    private final Map<String, String> activeDestinations = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(DestinationStore.class);

    public void connectWebsocket(String username){
        logger.info("User «" + username + "» connected" );
    }
    public void disconnectWebsocket(String username){
        logger.info("User «" + username + "» disconnected" );
    }
    public void registerDestination(String sessionId, String destination, String username) {
        logger.info("«" + username + "» subscribed " + destination);
        activeDestinations.put(sessionId, destination);
    }
    public void unregisterDestination(String sessionId, String destination, String username) {
        logger.info("«" +username + "» unsubscribed " + destination);
        activeDestinations.remove(sessionId);
    }
    public boolean destinationIsActive(String destination) {
        logger.info(destination + " is active");
        return activeDestinations.containsValue(destination);
    }

    public Map<String, String> getActiveDestinations() {
        return this.activeDestinations;
    }
}
