package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.companies.RemoveEmployeeFromCompanyUseCase;
import vellenich.joao.companyManager.application.utils.CompanyUseCaseUtils;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.exception.EmployeeNotFoundException;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.company.AddEmployeeToCompanyDto;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

import java.util.List;

@Service
public class RemoveEmployeeFromCompanyUseCaseHandler extends CompanyUseCaseUtils implements RemoveEmployeeFromCompanyUseCase {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public RemoveEmployeeFromCompanyUseCaseHandler(
        CompanyRepository companyRepository,
        EmployeeRepository employeeRepository
    ) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public CompanyResponseDto handle(Long companyId, AddEmployeeToCompanyDto requestBody) {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
            new ObjectNotFoundException("Company not found for id = " + companyId)
        );

        if (requestBody.employeeId() != null && !requestBody.employeeId().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(requestBody.employeeId());

            if (employees.size() != requestBody.employeeId().size()) {
                throw new EmployeeNotFoundException("One or more employees were not found");
            }

            for (Employee employee : employees) {
                company.removeEmployee(employee);
            }
        }

        companyRepository.save(company);

        return this.buildResponseForCompany(company);
    }
}
