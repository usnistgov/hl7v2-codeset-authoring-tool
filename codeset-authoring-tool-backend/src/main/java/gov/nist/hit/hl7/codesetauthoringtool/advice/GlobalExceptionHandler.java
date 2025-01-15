package gov.nist.hit.hl7.codesetauthoringtool.advice;

import gov.nist.hit.hl7.codesetauthoringtool.exception.NotFoundException;
import gov.nist.hit.hl7.codesetauthoringtool.model.response.ResponseMessage;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage handleException(Exception e) {
        // You can add more logic here to customize the response based on the exception type
        e.printStackTrace();
//        return  new ErrorResponse("Error");
//        return  new ResponseMessage<>(ResponseMessage.Status.FAILED, ex.getMessage());
        return new ResponseMessage<>(ResponseMessage.Status.FAILED, e.getMessage(), e.getMessage(), new Date());

//        return new ResponseEntity<>("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseMessage handleNoHandlerFoundException(NoHandlerFoundException ex) {
//        return new ResponseEntity<>("API endpoint not found", HttpStatus.NOT_FOUND);
        return  new ResponseMessage<>(ResponseMessage.Status.FAILED, "API endpoint not found");
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseMessage notFoundException(NotFoundException ex) {
//        return  new ErrorResponse(ex.getMessage());
        return  new ResponseMessage<>(ResponseMessage.Status.FAILED, ex.getMessage());

    }

}
