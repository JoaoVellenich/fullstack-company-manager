package vellenich.joao.companyManager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vellenich.joao.companyManager.domain.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCnpj(String cnpj);
}
