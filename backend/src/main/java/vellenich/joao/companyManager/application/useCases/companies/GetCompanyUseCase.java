package vellenich.joao.companyManager.application.useCases.companies;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

public interface GetCompanyUseCase {
    Page<CompanyResponseDto> listAllCompanies(String companyName, String companyCnpj, Pageable pageable);
    CompanyResponseDto getCompanyById(Long companyId);
}
