package vellenich.joao.companyManager.application.utils;

import org.springframework.stereotype.Component;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.util.List;

@Component
public class EmployeeUseCaseUtils {
    public EmployeeResponseDto buildResponseForEmployee(Employee employee) {
        List<CompanyResponseDto> companyResponseDtos = employee.getCompanies().stream()
                .map(ce -> {
                    Company c = ce.getCompany();
                    return new CompanyResponseDto(
                            c.getId(), c.getCnpj(), c.getCompanyName(),
                            c.getCep(), c.getState(),
                            null
                    );
                })
                .toList();

        return new EmployeeResponseDto(
                employee.getId(),
                employee.getName(),
                employee.getCpf(),
                employee.getCnpj(),
                employee.getRg(),
                employee.getBirthDate(),
                employee.getType(),
                employee.getEmail(),
                employee.getCep(),
                employee.getState(),
                companyResponseDtos
        );
    }
}
