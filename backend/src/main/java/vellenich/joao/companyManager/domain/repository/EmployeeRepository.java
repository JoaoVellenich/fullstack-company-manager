package vellenich.joao.companyManager.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vellenich.joao.companyManager.domain.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByCnpj(String cnpj);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Employee> searchByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE e.cpf LIKE CONCAT('%', :document, '%') OR e.cnpj LIKE CONCAT('%', :document, '%')")
    Page<Employee> searchByDocument(@Param("document") String document, Pageable pageable);

    @Query("SELECT e FROM Employee e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')) AND (e.cpf LIKE CONCAT('%', :document, '%') OR e.cnpj LIKE CONCAT('%', :document, '%'))")
    Page<Employee> searchByNameAndDocument(@Param("name") String name, @Param("document") String document, Pageable pageable);
}
