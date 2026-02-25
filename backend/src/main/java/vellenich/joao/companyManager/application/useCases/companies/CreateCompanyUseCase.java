package vellenich.joao.companyManager.application.useCases.companies;

import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.company.CreateCompanyDto;

public interface CreateCompanyUseCase {
    public CompanyResponseDto handle(CreateCompanyDto request);
}
