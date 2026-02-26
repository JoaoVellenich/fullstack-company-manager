package vellenich.joao.companyManager.application.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vellenich.joao.companyManager.application.useCases.companies.AddEmployeeToCompanyUseCase;
import vellenich.joao.companyManager.application.useCases.companies.CreateCompanyUseCase;
import vellenich.joao.companyManager.application.useCases.companies.DeleteCompanyUseCase;
import vellenich.joao.companyManager.application.useCases.companies.GetCompanyUseCase;
import vellenich.joao.companyManager.application.useCases.companies.RemoveEmployeeFromCompanyUseCase;
import vellenich.joao.companyManager.interfaces.rest.company.AddEmployeeToCompanyDto;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.company.CreateCompanyDto;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CreateCompanyUseCase createCompanyUseCase;
    private final AddEmployeeToCompanyUseCase addEmployeeToCompanyUseCase;
    private final RemoveEmployeeFromCompanyUseCase removeEmployeeFromCompanyUseCase;
    private final GetCompanyUseCase getCompanyUseCase;
    private final DeleteCompanyUseCase deleteCompanyUseCase;

    public CompanyController(
            CreateCompanyUseCase createCompanyUseCase,
            AddEmployeeToCompanyUseCase addEmployeeToCompanyUseCase,
            RemoveEmployeeFromCompanyUseCase removeEmployeeFromCompanyUseCase,
            GetCompanyUseCase getCompanyUseCase,
            DeleteCompanyUseCase deleteCompanyUseCase
    ){
        this.createCompanyUseCase = createCompanyUseCase;
        this.addEmployeeToCompanyUseCase = addEmployeeToCompanyUseCase;
        this.removeEmployeeFromCompanyUseCase = removeEmployeeFromCompanyUseCase;
        this.getCompanyUseCase = getCompanyUseCase;
        this.deleteCompanyUseCase = deleteCompanyUseCase;
    }

    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> listAllCompanies(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String cnpj,
        Pageable pageable
    ){
        Page<CompanyResponseDto> responseBody = getCompanyUseCase.listAllCompanies(name, cnpj, pageable);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(
        @PathVariable Long companyId
    ){
        CompanyResponseDto responseBody = getCompanyUseCase.getCompanyById(companyId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
        @RequestBody CreateCompanyDto requestBody
    ){
        CompanyResponseDto responseBody = createCompanyUseCase.handle(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    @DeleteMapping("/{companyId}")
    public ResponseEntity<Void> deleteCompany(
        @PathVariable Long companyId
    ){
        deleteCompanyUseCase.handle(companyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{companyId}/employee/add")
    public ResponseEntity<CompanyResponseDto> addEmployeeToCompany(
        @PathVariable Long companyId,
        @RequestBody AddEmployeeToCompanyDto requestBody
    ){
        CompanyResponseDto response = addEmployeeToCompanyUseCase.handle(companyId, requestBody);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{companyId}/employee/remove")
    public ResponseEntity<CompanyResponseDto> removeEmployeeFromCompany(
        @PathVariable Long companyId,
        @RequestBody AddEmployeeToCompanyDto requestBody
    ){
        CompanyResponseDto response = removeEmployeeFromCompanyUseCase.handle(companyId, requestBody);
        return ResponseEntity.ok(response);
    }

}
