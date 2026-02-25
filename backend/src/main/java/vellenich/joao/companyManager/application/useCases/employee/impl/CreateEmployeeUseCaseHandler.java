package vellenich.joao.companyManager.application.useCases.employee.impl;

import vellenich.joao.companyManager.application.useCases.employee.CreateEmployeeUseCase;
import vellenich.joao.companyManager.interfaces.rest.employee.CreateEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

public class CreateEmployeeUseCaseHandler implements CreateEmployeeUseCase {
    @Override
    public EmployeeResponseDto handle(CreateEmployeeDto requestBody) {
        return null;
    }
}
