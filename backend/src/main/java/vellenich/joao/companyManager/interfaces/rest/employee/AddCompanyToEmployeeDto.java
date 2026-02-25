package vellenich.joao.companyManager.interfaces.rest.employee;

import java.util.List;

public record AddCompanyToEmployeeDto(
        List<Long> companyId
) {
}
