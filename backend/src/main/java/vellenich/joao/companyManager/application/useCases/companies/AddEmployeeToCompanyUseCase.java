package vellenich.joao.companyManager.application.useCases.companies;

import vellenich.joao.companyManager.interfaces.rest.company.AddEmployeeToCompanyDto;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

public interface AddEmployeeToCompanyUseCase {
    public CompanyResponseDto handle(Long companyId, AddEmployeeToCompanyDto requestBody);
}
