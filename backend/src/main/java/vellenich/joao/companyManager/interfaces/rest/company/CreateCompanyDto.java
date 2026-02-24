package vellenich.joao.companyManager.interfaces.rest.company;

import java.util.List;

public record CreateCompanyDto(
        String cnpj,
        String companyName,
        String cep,
        String state,
        List<Long> employeeId
) {
}
