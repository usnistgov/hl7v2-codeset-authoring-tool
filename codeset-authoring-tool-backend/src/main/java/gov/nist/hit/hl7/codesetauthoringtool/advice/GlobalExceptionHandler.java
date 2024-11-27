package gov.nist.hit.hl7.codesetauthoringtool.advice;

import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseMessage<Object> handleException(Exception e) {
        // You can add more logic here to customize the response based on the exception type
        e.printStackTrace();
//        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseMessage<>(ResponseMessage.Status.FAILED, e.getMessage(), e.getMessage(), new Date());

    }
}
