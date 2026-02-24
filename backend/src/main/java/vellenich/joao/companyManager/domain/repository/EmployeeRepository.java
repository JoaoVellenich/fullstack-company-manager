package vellenich.joao.companyManager.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vellenich.joao.companyManager.domain.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
