package vellenich.joao.companyManager.application.useCases;

import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.company.CreateCompanyDto;

public interface CreateCompanyUseCase {
    public CompanyResponseDto handle(CreateCompanyDto request);
}
