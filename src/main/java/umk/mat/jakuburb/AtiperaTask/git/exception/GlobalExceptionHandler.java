package umk.mat.jakuburb.AtiperaTask.git.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umk.mat.jakuburb.AtiperaTask.git.model.dto.NotFoundResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<NotFoundResponse> handle404(FeignException.NotFound ex) throws JsonProcessingException {
        NotFoundResponse notFind = new NotFoundResponse(ex.status(), "Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFind);
    }
}
