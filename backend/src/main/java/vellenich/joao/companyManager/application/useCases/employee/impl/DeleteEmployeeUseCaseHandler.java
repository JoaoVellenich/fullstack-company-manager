package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.employee.DeleteEmployeeUseCase;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;

@Service
public class DeleteEmployeeUseCaseHandler implements DeleteEmployeeUseCase {
    private final EmployeeRepository employeeRepository;

    public DeleteEmployeeUseCaseHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void handle(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ObjectNotFoundException("Employee not found"));
        employeeRepository.delete(employee);
    }
}
