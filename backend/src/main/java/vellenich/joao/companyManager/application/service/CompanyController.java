package vellenich.joao.companyManager.application.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vellenich.joao.companyManager.application.useCases.CreateCompanyUseCase;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.company.CreateCompanyDto;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private final CreateCompanyUseCase createCompanyUseCase;

    public CompanyController(CreateCompanyUseCase createCompanyUseCase){
        this.createCompanyUseCase = createCompanyUseCase;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
        @RequestBody CreateCompanyDto requestBody
    ){
        CompanyResponseDto responseBody = createCompanyUseCase.handle(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}
