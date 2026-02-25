package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.employee.GetEmployeeUseCase;
import vellenich.joao.companyManager.application.utils.EmployeeUseCaseUtils;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

@Service
public class GetEmployeeUseCaseHandler extends EmployeeUseCaseUtils implements GetEmployeeUseCase {

    private final EmployeeRepository employeeRepository;

    public GetEmployeeUseCaseHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() ->
            new ObjectNotFoundException("Employee not found for id = " + employeeId)
        );

        return this.buildResponseForEmployee(employee);
    }

    @Override
    public Page<EmployeeResponseDto> listAllEmployees(String name, String document, Pageable pageable) {
        Page<Employee> employees;

        boolean hasName = name != null && !name.isBlank();
        boolean hasDocument = document != null && !document.isBlank();

        if (hasName && hasDocument) {
            employees = employeeRepository.searchByNameAndDocument(name, document, pageable);
        } else if (hasName) {
            employees = employeeRepository.searchByName(name, pageable);
        } else if (hasDocument) {
            employees = employeeRepository.searchByDocument(document, pageable);
        } else {
            employees = employeeRepository.findAll(pageable);
        }

        return employees.map(this::buildResponseForEmployee);
    }
}
