package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.employee.CreateEmployeeUseCase;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.EmployeeAlreadyExistsException;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeDataException;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.employee.CreateEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.util.List;

@Service
public class CreateEmployeeUseCaseHandler implements CreateEmployeeUseCase {

    private final EmployeeRepository employeeRepository;

    public CreateEmployeeUseCaseHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public EmployeeResponseDto handle(CreateEmployeeDto requestBody) {
        validateEmployeeData(requestBody);
        checkForDuplicates(requestBody);

        Employee employee = new Employee(requestBody.type(), requestBody.name(), requestBody.cep(), requestBody.state());
        employee.setCpf(requestBody.cpf());
        employee.setCnpj(requestBody.cnpj());
        employee.setRg(requestBody.rg());
        employee.setBirthDate(requestBody.birthDate());
        employee.setEmail(requestBody.email());

        Employee saved = employeeRepository.save(employee);

        return new EmployeeResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getCpf(),
                saved.getCnpj(),
                saved.getRg(),
                saved.getBirthDate(),
                saved.getType(),
                saved.getEmail(),
                saved.getCep(),
                saved.getState(),
                List.of()
        );
    }

    private void validateEmployeeData(CreateEmployeeDto dto) {
        if (dto.type() == EmployeeType.INDIVIDUAL) {
            if (dto.cpf() == null || dto.cpf().isBlank()) {
                throw new InvalidEmployeeDataException("CPF is required for INDIVIDUAL employees");
            }
            if (dto.rg() == null || dto.rg().isBlank()) {
                throw new InvalidEmployeeDataException("RG is required for INDIVIDUAL employees");
            }
            if (dto.birthDate() == null) {
                throw new InvalidEmployeeDataException("Birth date is required for INDIVIDUAL employees");
            }
        } else if (dto.type() == EmployeeType.LEGAL_ENTITY) {
            if (dto.cnpj() == null || dto.cnpj().isBlank()) {
                throw new InvalidEmployeeDataException("CNPJ is required for LEGAL_ENTITY employees");
            }
        }
    }

    private void checkForDuplicates(CreateEmployeeDto dto) {
        if (dto.cpf() != null && employeeRepository.existsByCpf(dto.cpf())) {
            throw new EmployeeAlreadyExistsException("Employee with CPF " + dto.cpf() + " already exists");
        }
        if (dto.cnpj() != null && employeeRepository.existsByCnpj(dto.cnpj())) {
            throw new EmployeeAlreadyExistsException("Employee with CNPJ " + dto.cnpj() + " already exists");
        }
    }
}
