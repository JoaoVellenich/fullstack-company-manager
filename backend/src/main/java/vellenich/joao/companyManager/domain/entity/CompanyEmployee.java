package vellenich.joao.companyManager.domain.entity;

import jakarta.persistence.*;
import vellenich.joao.companyManager.domain.entity.keys.CompanyEmployeeId;

import java.time.LocalDateTime;

@Entity
@Table(name = "company_employees")
public class CompanyEmployee {

    @EmbeddedId
    private CompanyEmployeeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("companyId")
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("employeeId")
    private Employee employee;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected CompanyEmployee() {}

    public CompanyEmployee(Company company, Employee employee) {
        this.company = company;
        this.employee = employee;
        this.id = new CompanyEmployeeId(
                company.getId(),
                employee.getId()
        );
    }

    public Employee getEmployee() {
        return employee;
    }

    public Company getCompany() {
        return company;
    }
}
