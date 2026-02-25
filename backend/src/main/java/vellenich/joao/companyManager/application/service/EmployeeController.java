package vellenich.joao.companyManager.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vellenich.joao.companyManager.application.useCases.employee.AddCompanyToEmployeeUseCase;
import vellenich.joao.companyManager.application.useCases.employee.CreateEmployeeUseCase;
import vellenich.joao.companyManager.application.useCases.employee.GetEmployeeUseCase;
import vellenich.joao.companyManager.interfaces.rest.employee.AddCompanyToEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.CreateEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final CreateEmployeeUseCase createEmployeeUseCase;
    private final AddCompanyToEmployeeUseCase addCompanyToEmployeeUseCase;
    private final GetEmployeeUseCase getEmployeeUseCase;

    public EmployeeController(
            CreateEmployeeUseCase createEmployeeUseCase,
            AddCompanyToEmployeeUseCase addCompanyToEmployeeUseCase,
            GetEmployeeUseCase getEmployeeUseCase
    ) {
        this.createEmployeeUseCase = createEmployeeUseCase;
        this.addCompanyToEmployeeUseCase = addCompanyToEmployeeUseCase;
        this.getEmployeeUseCase = getEmployeeUseCase;
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDto>> listAllEmployees(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String document,
        Pageable pageable
    ) {
        Page<EmployeeResponseDto> responseBody = getEmployeeUseCase.listAllEmployees(name, document, pageable);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(
        @PathVariable Long employeeId
    ) {
        EmployeeResponseDto responseBody = getEmployeeUseCase.getEmployeeById(employeeId);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping
    public ResponseEntity<EmployeeResponseDto> createEmployee(
        @RequestBody CreateEmployeeDto requestBody
    ) {
        EmployeeResponseDto responseBody = createEmployeeUseCase.handle(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping("/{employeeId}/company/add")
    public ResponseEntity<EmployeeResponseDto> addCompanyToEmployee(
        @PathVariable Long employeeId,
        @RequestBody AddCompanyToEmployeeDto requestBody
    ) {
        EmployeeResponseDto response = addCompanyToEmployeeUseCase.handle(employeeId, requestBody);
        return ResponseEntity.ok(response);
    }
}
