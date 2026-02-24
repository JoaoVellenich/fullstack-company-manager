package vellenich.joao.companyManager.domain.entity.keys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CompanyEmployeeId implements Serializable {

    private Long companyId;
    private Long employeeId;

    protected CompanyEmployeeId() {}

    public CompanyEmployeeId(Long companyId, Long employeeId) {
        this.companyId = companyId;
        this.employeeId = employeeId;
    }

    // equals and hashCode REQUIRED
}
