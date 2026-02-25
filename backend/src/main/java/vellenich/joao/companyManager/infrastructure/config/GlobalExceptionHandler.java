package vellenich.joao.companyManager.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vellenich.joao.companyManager.domain.exception.CompanyAlreadyExistsException;
import vellenich.joao.companyManager.domain.exception.EmployeeNotFoundException;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeTypeException;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CompanyAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleCompanyAlreadyExists(CompanyAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidEmployeeTypeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEmployeeType(InvalidEmployeeTypeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleObjectNotFoundException(ObjectNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}
