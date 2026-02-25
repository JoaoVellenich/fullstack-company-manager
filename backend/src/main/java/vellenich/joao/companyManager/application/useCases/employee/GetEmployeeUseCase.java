package vellenich.joao.companyManager.application.useCases.employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

public interface GetEmployeeUseCase {
    EmployeeResponseDto getEmployeeById(Long employeeId);
    Page<EmployeeResponseDto> listAllEmployees(String name, String document, Pageable pageable);
}
