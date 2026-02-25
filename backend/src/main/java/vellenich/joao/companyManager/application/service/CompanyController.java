package vellenich.joao.companyManager.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vellenich.joao.companyManager.application.useCases.companies.AddEmployeeToCompanyUseCase;
import vellenich.joao.companyManager.application.useCases.companies.CreateCompanyUseCase;
import vellenich.joao.companyManager.interfaces.rest.company.AddEmployeeToCompanyDto;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.company.CreateCompanyDto;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CreateCompanyUseCase createCompanyUseCase;
    private final AddEmployeeToCompanyUseCase addEmployeeToCompanyUseCase;

    public CompanyController(
            CreateCompanyUseCase createCompanyUseCase,
            AddEmployeeToCompanyUseCase addEmployeeToCompanyUseCase
    ){
        this.createCompanyUseCase = createCompanyUseCase;
        this.addEmployeeToCompanyUseCase = addEmployeeToCompanyUseCase;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
        @RequestBody CreateCompanyDto requestBody
    ){
        CompanyResponseDto responseBody = createCompanyUseCase.handle(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @PutMapping("/{companyId}/employee/add")
    public ResponseEntity<CompanyResponseDto> addEmployeeToCompany(
        @PathVariable Long companyId,
        @RequestBody AddEmployeeToCompanyDto requestBody
    ){
        CompanyResponseDto response = addEmployeeToCompanyUseCase.handle(companyId, requestBody);
        return ResponseEntity.ok(response);
    }
}
