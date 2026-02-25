package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.companies.AddEmployeeToCompanyUseCase;
import vellenich.joao.companyManager.application.useCases.employee.CreateEmployeeUseCase;
import vellenich.joao.companyManager.application.utils.CompanyUseCaseUtils;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.EmployeeNotFoundException;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeTypeException;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.company.AddEmployeeToCompanyDto;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

import java.util.List;

@Service
public class AddEmployeeToCompanyUseCaseHandler extends CompanyUseCaseUtils implements AddEmployeeToCompanyUseCase {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final CreateEmployeeUseCase createEmployeeUseCase;

    public AddEmployeeToCompanyUseCaseHandler(
        CompanyRepository companyRepository,
        EmployeeRepository employeeRepository,
        CreateEmployeeUseCase createEmployeeUseCase
    ){
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.createEmployeeUseCase = createEmployeeUseCase;
    }

    @Override
    public CompanyResponseDto handle(Long companyId, AddEmployeeToCompanyDto requestBody) {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
            new ObjectNotFoundException("Company not found for id = " + companyId)
        );

        if (requestBody.employeeId() != null && !requestBody.employeeId().isEmpty()){
            List<Employee> employees = employeeRepository.findAllById(requestBody.employeeId());

            if (employees.size() != requestBody.employeeId().size()) {
                throw new EmployeeNotFoundException("One or more employees were not found");
            }

            if ("PR".equals(company.getState())) {
                for (Employee employee : employees) {
                    if (employee.getType() != EmployeeType.LEGAL_ENTITY) {
                        throw new InvalidEmployeeTypeException(
                                "Companies in PR state can only have LEGAL_ENTITY employees"
                        );
                    }
                }
            }

            for (Employee employee : employees) {
                company.addEmployee(employee);
            }
        }

        companyRepository.save(company);

       return this.buildResponseForCompany(company);
    }
}
