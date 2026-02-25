package vellenich.joao.companyManager.application.useCases.employee;

import vellenich.joao.companyManager.interfaces.rest.employee.AddCompanyToEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

public interface AddCompanyToEmployeeUseCase {
    EmployeeResponseDto handle(Long employeeId, AddCompanyToEmployeeDto requestBody);
}
