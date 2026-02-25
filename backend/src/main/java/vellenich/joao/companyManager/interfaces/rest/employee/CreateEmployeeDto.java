package vellenich.joao.companyManager.interfaces.rest.employee;

import vellenich.joao.companyManager.domain.enums.EmployeeType;

import java.time.LocalDate;

public record CreateEmployeeDto(
        String name,
        String cpf,
        String cnpj,
        String rg,
        LocalDate birthDate,
        EmployeeType type,
        String email,
        String cep,
        String state
) {
}
