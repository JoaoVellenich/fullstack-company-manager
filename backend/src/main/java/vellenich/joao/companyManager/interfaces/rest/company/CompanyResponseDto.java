package vellenich.joao.companyManager.interfaces.rest.company;

import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.util.List;

public record CompanyResponseDto(
        String cnpj,
        String companyName,
        String cep,
        String state,
        List<EmployeeResponseDto> employees
) {
}
