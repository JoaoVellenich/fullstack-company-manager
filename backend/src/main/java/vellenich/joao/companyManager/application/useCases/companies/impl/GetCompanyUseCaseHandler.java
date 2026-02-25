package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.companies.GetCompanyUseCase;
import vellenich.joao.companyManager.application.utils.CompanyUseCaseUtils;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

import java.util.List;

@Service
public class GetCompanyUseCaseHandler extends CompanyUseCaseUtils implements GetCompanyUseCase {

    private CompanyRepository companyRepository;

    public GetCompanyUseCaseHandler(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyResponseDto> listAllCompanies(String companyName, String companyCnpj) {
        return null;
    }

    @Override
    public CompanyResponseDto getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
            new ObjectNotFoundException("Company not found for id = " + companyId)
        );

        return this.buildResponseForCompany(company);
    }
}
