package vellenich.joao.companyManager.application.useCases.companies;

import org.springframework.data.domain.Pageable;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

import java.util.List;

public interface GetCompanyUseCase {
    public List<CompanyResponseDto> listAllCompanies(String companyName, String companyCnpj);
    public CompanyResponseDto getCompanyById(Long companyId);
}
