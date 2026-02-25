package vellenich.joao.companyManager.domain.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String state;

    @OneToMany(mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<CompanyEmployee> employees = new HashSet<>();

    protected Company() {}

    public Company(String cnpj, String companyName, String cep, String state) {
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.cep = cep;
        this.state = state;
    }

    public void addEmployee(Employee employee) {
        CompanyEmployee association = new CompanyEmployee(this, employee);
        employees.add(association);
    }

    public void removeEmployee(Employee employee) {
        employees.removeIf(e -> e.getEmployee().equals(employee));
    }

    public Long getId() {
        return id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCep() {
        return cep;
    }

    public String getState() {
        return state;
    }

    public Set<CompanyEmployee> getEmployees() {
        return employees;
    }
}
