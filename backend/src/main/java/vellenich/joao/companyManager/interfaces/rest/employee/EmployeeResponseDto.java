package vellenich.joao.companyManager.interfaces.rest.employee;

import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

import java.time.LocalDate;
import java.util.List;

public record EmployeeResponseDto(
        Long id,
        String name,
        String cpf,
        String cnpj,
        String rg,
        LocalDate birthDate,
        EmployeeType type,
        String email,
        String cep,
        String state,
        List<CompanyResponseDto> companies
) {
}
