package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.companies.CreateCompanyUseCase;
import vellenich.joao.companyManager.application.utils.CompanyUseCaseUtils;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.CompanyAlreadyExistsException;
import vellenich.joao.companyManager.domain.exception.EmployeeNotFoundException;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeTypeException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.company.CreateCompanyDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class CreateCompanyUseCaseHandler extends CompanyUseCaseUtils implements CreateCompanyUseCase {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    public CreateCompanyUseCaseHandler(
            CompanyRepository companyRepository,
            EmployeeRepository employeeRepository
    ) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public CompanyResponseDto handle(CreateCompanyDto request) {
        if (companyRepository.existsByCnpj(request.cnpj())) {
            throw new CompanyAlreadyExistsException("Company with this CNPJ already exists");
        }

        Company company = new Company(request.cnpj(), request.companyName(), request.cep(), request.state());

        if (request.employeeId() != null && !request.employeeId().isEmpty()) {
            List<Employee> employees = employeeRepository.findAllById(request.employeeId());

            if (employees.size() != request.employeeId().size()) {
                throw new EmployeeNotFoundException("One or more employees were not found");
            }

            if ("PR".equals(request.state())) {
                for (Employee employee : employees) {
                    if (employee.getType() == EmployeeType.INDIVIDUAL
                            && Period.between(employee.getBirthDate(), LocalDate.now()).getYears() < 18) {
                        throw new InvalidEmployeeTypeException(
                                "Companies in PR state cannot have INDIVIDUAL employees under 18 years old"
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
