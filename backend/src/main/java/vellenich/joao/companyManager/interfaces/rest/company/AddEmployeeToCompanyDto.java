package vellenich.joao.companyManager.interfaces.rest.company;

import vellenich.joao.companyManager.interfaces.rest.employee.CreateEmployeeDto;

import java.util.List;

public record AddEmployeeToCompanyDto(
        List<Long> employeeId,
        List<CreateEmployeeDto> createEmployeeDtos
) {
}
