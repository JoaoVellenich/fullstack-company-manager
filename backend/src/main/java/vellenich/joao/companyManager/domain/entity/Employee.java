package vellenich.joao.companyManager.domain.entity;

import jakarta.persistence.*;
import vellenich.joao.companyManager.domain.enums.EmployeeType;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeType type;

    private String cpf;
    private String cnpj;
    private String rg;

    private LocalDate birthDate;

    @Column(nullable = false)
    private String name;

    private String email;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String state;

    protected Employee() {}

    public Employee(EmployeeType type, String name, String cep, String state) {
        this.type = type;
        this.name = name;
        this.cep = cep;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public EmployeeType getType() {
        return type;
    }

    public String getName() { return name; }
    public String getCpf() { return cpf; }
    public String getCnpj() { return cnpj; }
    public String getRg() { return rg; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getEmail() { return email; }
    public String getCep() { return cep; }
    public String getState() { return state; }

    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public void setRg(String rg) { this.rg = rg; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    public void setEmail(String email) { this.email = email; }
}
