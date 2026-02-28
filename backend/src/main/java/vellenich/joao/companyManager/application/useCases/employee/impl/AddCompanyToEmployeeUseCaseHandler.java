package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.employee.AddCompanyToEmployeeUseCase;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeTypeException;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.employee.AddCompanyToEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AddCompanyToEmployeeUseCaseHandler implements AddCompanyToEmployeeUseCase {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    public AddCompanyToEmployeeUseCaseHandler(
            EmployeeRepository employeeRepository,
            CompanyRepository companyRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public EmployeeResponseDto handle(Long employeeId, AddCompanyToEmployeeDto requestBody) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
                new ObjectNotFoundException("Employee not found for id = " + employeeId)
        );

        List<Company> companies = companyRepository.findAllById(requestBody.companyId());

        if (companies.size() != requestBody.companyId().size()) {
            throw new ObjectNotFoundException("One or more companies were not found");
        }

        for (Company company : companies) {
            if ("PR".equals(company.getState())
                    && employee.getType() == EmployeeType.INDIVIDUAL
                    && Period.between(employee.getBirthDate(), LocalDate.now()).getYears() < 18) {
                throw new InvalidEmployeeTypeException(
                        "Companies in PR state cannot have INDIVIDUAL employees under 18 years old"
                );
            }
        }

        Set<Long> existingCompanyIds = employee.getCompanies().stream()
                .map(ce -> ce.getCompany().getId())
                .collect(Collectors.toSet());

        List<Company> newCompanies = companies.stream()
                .filter(c -> !existingCompanyIds.contains(c.getId()))
                .toList();

        for (Company company : newCompanies) {
            company.addEmployee(employee);
        }

        if (!newCompanies.isEmpty()) {
            companyRepository.saveAll(newCompanies);
        }

        Set<Company> allCompanies = new HashSet<>();
        employee.getCompanies().stream()
                .map(ce -> ce.getCompany())
                .forEach(allCompanies::add);
        allCompanies.addAll(newCompanies);

        List<CompanyResponseDto> companyResponseDtos = allCompanies.stream()
                .map(c -> new CompanyResponseDto(
                        c.getId(), c.getCnpj(), c.getCompanyName(),
                        c.getCep(), c.getState(), null
                ))
                .toList();

        return new EmployeeResponseDto(
                employee.getId(), employee.getName(), employee.getCpf(), employee.getCnpj(),
                employee.getRg(), employee.getBirthDate(), employee.getType(),
                employee.getEmail(), employee.getCep(), employee.getState(),
                companyResponseDtos
        );
    }
}
