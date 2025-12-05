package com.smartspend.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
@ControllerAdvice
public class GlobalExceptionHandler {
@ExceptionHandler(IllegalStateException.class)
public ResponseEntity<ApiError> handleIllegalState(IllegalStateException ex,WebRequest request){
ApiError error=new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST.getReasonPhrase(),ex.getMessage(),request.getDescription(false));
return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
}
@ExceptionHandler(RuntimeException.class)
public ResponseEntity<ApiError> handleRuntime(RuntimeException ex,WebRequest request){
ApiError error=new ApiError(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),ex.getMessage(),request.getDescription(false));
return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
}
@ExceptionHandler(Exception.class)
public ResponseEntity<ApiError> handleGeneric(Exception ex,WebRequest request){
ApiError error=new ApiError(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),ex.getMessage(),request.getDescription(false));
return new ResponseEntity<>(error,HttpStatus.INTERNAL_SERVER_ERROR);
}
}
