package vellenich.joao.companyManager.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vellenich.joao.companyManager.application.useCases.employee.CreateEmployeeUseCase;
import vellenich.joao.companyManager.interfaces.rest.employee.CreateEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final CreateEmployeeUseCase createEmployeeUseCase;

    public EmployeeController(CreateEmployeeUseCase createEmployeeUseCase) {
        this.createEmployeeUseCase = createEmployeeUseCase;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
        @RequestBody CreateEmployeeDto requestBody
    ) {
        EmployeeResponseDto responseBody = createEmployeeUseCase.handle(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
