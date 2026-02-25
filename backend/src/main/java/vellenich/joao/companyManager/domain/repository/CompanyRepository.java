package vellenich.joao.companyManager.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vellenich.joao.companyManager.domain.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByCnpj(String cnpj);

    @Query("SELECT c FROM Company c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Company> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE c.cnpj LIKE CONCAT('%', :cnpj, '%')")
    Page<Company> searchByCnpj(@Param("cnpj") String cnpj, Pageable pageable);

    @Query("SELECT c FROM Company c WHERE LOWER(c.companyName) LIKE LOWER(CONCAT('%', :name, '%')) AND c.cnpj LIKE CONCAT('%', :cnpj, '%')")
    Page<Company> searchByNameAndCnpj(@Param("name") String name, @Param("cnpj") String cnpj, Pageable pageable);
}
