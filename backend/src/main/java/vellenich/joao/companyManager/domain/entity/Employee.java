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
}
