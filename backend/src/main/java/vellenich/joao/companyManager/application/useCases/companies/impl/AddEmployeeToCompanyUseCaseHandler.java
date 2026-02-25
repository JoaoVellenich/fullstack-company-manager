package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.companies.AddEmployeeToCompanyUseCase;
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
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.util.List;

@Service
public class AddEmployeeToCompanyUseCaseHandler implements AddEmployeeToCompanyUseCase {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public AddEmployeeToCompanyUseCaseHandler(
        CompanyRepository companyRepository,
        EmployeeRepository employeeRepository
    ){
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
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

        // TODO Get the list of employee Create DTO and pass it to the create use case. And then add it to company

        companyRepository.save(company);

        List<EmployeeResponseDto> employeeResponseDtos = company.getEmployees().stream()
                .map(ce -> {
                    Employee e = ce.getEmployee();
                    return new EmployeeResponseDto(
                            e.getId(), e.getName(), e.getCpf(), e.getCnpj(),
                            e.getRg(), e.getBirthDate(), e.getType(),
                            e.getEmail(), e.getCep(), e.getState(),
                            null
                    );
                })
                .toList();

        return new CompanyResponseDto(
                company.getCnpj(),
                company.getCompanyName(),
                company.getCep(),
                company.getState(),
                employeeResponseDtos
        );
    }
}
