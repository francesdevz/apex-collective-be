package ApexCollectiveHibernateBE.ApexCollective.Common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Enhanced error response structure
     */
    private Map<String, Object> createErrorResponse(HttpStatus status, String message, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("path", path);
        return errorResponse;
    }

    @ExceptionHandler(ApexCollectiveException.class)
    public ResponseEntity<Object> handleApexCollectiveException(ApexCollectiveException ex, WebRequest request) {
        log.warn("Business exception: {}", ex.getMessage());

        HttpStatus status = determineHttpStatus(ex);
        Map<String, Object> errorResponse = createErrorResponse(status, ex.getMessage(), request.getDescription(false));

        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request.getDescription(false)
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private HttpStatus determineHttpStatus(ApexCollectiveException ex) {
        String message = ex.getMessage().toLowerCase();

        if (message.contains("already exist") || message.contains("duplicate")) {
            return HttpStatus.CONFLICT;
        } else if (message.contains("not found")) {
            return HttpStatus.NOT_FOUND;
        } else if (message.contains("unauthorized") || message.contains("access denied")) {
            return HttpStatus.UNAUTHORIZED;
        } else if (message.contains("validation") || message.contains("invalid")) {
            return HttpStatus.BAD_REQUEST;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
