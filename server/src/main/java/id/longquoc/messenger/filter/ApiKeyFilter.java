package id.longquoc.messenger.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {
    private final String API_KEY_HEADER = "x-api-key";
    @Value("${spring.apiKey}")
    private String validKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(API_KEY_HEADER);
        final Map<String, Object> body = new HashMap<>();
        if (StringUtils.isEmpty(apiKey)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            body.put("status", HttpServletResponse.SC_FORBIDDEN);
            body.put("error", "Unauthorized");
            body.put("message", "API key is missing");
            body.put("path", request.getServletPath());
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
            return;
        }
        if (!isValidApiKey(apiKey)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            body.put("status", HttpServletResponse.SC_FORBIDDEN);
            body.put("error", "Unauthorized");
            body.put("message", "Invalid API key");
            body.put("path", request.getServletPath());
            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
            return;
        }
        // Kiểm tra api key có hợp lệ =? cho phép request tiếp theo đi qua filter
        filterChain.doFilter(request, response);

    }

    private boolean isValidApiKey(String apiKey){
        return validKey.equals(apiKey);
    }

}
