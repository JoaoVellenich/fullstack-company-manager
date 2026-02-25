package vellenich.joao.companyManager.application.utils;

import org.springframework.stereotype.Component;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.util.List;

@Component
public class CompanyUseCaseUtils {
    public CompanyResponseDto buildResponseForCompany(Company company){
        List<EmployeeResponseDto> employeeResponseDtos = company.getEmployees().stream()
                .map(ce -> {
                    Employee e = ce.getEmployee();
                    return new EmployeeResponseDto(
                            e.getId(), e.getName(), e.getCpf(), e.getCnpj(),
                            e.getRg(), e.getBirthDate(), e.getType(),
                            e.getEmail(), e.getCep(), e.getState(),
                            null
                    );
                })
                .toList();

        return new CompanyResponseDto(
                company.getId(),
                company.getCnpj(),
                company.getCompanyName(),
                company.getCep(),
                company.getState(),
                employeeResponseDtos
        );
    }
}
