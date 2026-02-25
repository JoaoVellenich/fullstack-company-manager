package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.companies.GetCompanyUseCase;
import vellenich.joao.companyManager.application.utils.CompanyUseCaseUtils;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

@Service
public class GetCompanyUseCaseHandler extends CompanyUseCaseUtils implements GetCompanyUseCase {

    private CompanyRepository companyRepository;

    public GetCompanyUseCaseHandler(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Page<CompanyResponseDto> listAllCompanies(String companyName, String companyCnpj, Pageable pageable) {
        Page<Company> companies;

        boolean hasName = companyName != null && !companyName.isBlank();
        boolean hasCnpj = companyCnpj != null && !companyCnpj.isBlank();

        if (hasName && hasCnpj) {
            companies = companyRepository.searchByNameAndCnpj(companyName, companyCnpj, pageable);
        } else if (hasName) {
            companies = companyRepository.searchByName(companyName, pageable);
        } else if (hasCnpj) {
            companies = companyRepository.searchByCnpj(companyCnpj, pageable);
        } else {
            companies = companyRepository.findAll(pageable);
        }

        return companies.map(this::buildResponseForCompany);
    }

    @Override
    public CompanyResponseDto getCompanyById(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(() ->
            new ObjectNotFoundException("Company not found for id = " + companyId)
        );

        return this.buildResponseForCompany(company);
    }
}
