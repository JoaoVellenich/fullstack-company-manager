package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.companies.DeleteCompanyUseCase;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;

@Service
public class DeleteCompanyUseCaseHandler implements DeleteCompanyUseCase {
    private final CompanyRepository companyRepository;

    public DeleteCompanyUseCaseHandler(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void handle(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ObjectNotFoundException("Company not found"));
        companyRepository.delete(company);
    }
}
