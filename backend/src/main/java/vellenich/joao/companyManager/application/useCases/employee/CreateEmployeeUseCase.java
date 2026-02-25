package vellenich.joao.companyManager.application.useCases.employee;

import vellenich.joao.companyManager.interfaces.rest.employee.CreateEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

public interface CreateEmployeeUseCase {
    public EmployeeResponseDto handle(CreateEmployeeDto requestBody);
}
