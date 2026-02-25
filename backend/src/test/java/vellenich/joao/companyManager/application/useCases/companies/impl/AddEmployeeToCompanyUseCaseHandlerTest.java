package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.EmployeeNotFoundException;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeTypeException;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.company.AddEmployeeToCompanyDto;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddEmployeeToCompanyUseCaseHandlerTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private AddEmployeeToCompanyUseCaseHandler handler;

    @Test
    void shouldAddEmployeesToCompanySuccessfully() {
        List<Long> employeeIds = List.of(1L, 2L);
        AddEmployeeToCompanyDto request = new AddEmployeeToCompanyDto(employeeIds, null);

        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Employee emp1 = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        Employee emp2 = new Employee(EmployeeType.LEGAL_ENTITY, "Jane Corp", "80000000", "SP");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1, emp2));

        CompanyResponseDto response = handler.handle(1L, request);

        assertEquals("12345678000100", response.cnpj());
        assertEquals("Test Company", response.companyName());
        assertEquals("SP", response.state());
        assertNotNull(response.employees());
        assertEquals(2, response.employees().size());
        verify(companyRepository).save(company);
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        AddEmployeeToCompanyDto request = new AddEmployeeToCompanyDto(List.of(1L), null);
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> handler.handle(99L, request));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {
        List<Long> employeeIds = List.of(1L, 2L);
        AddEmployeeToCompanyDto request = new AddEmployeeToCompanyDto(employeeIds, null);

        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Employee emp1 = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1));

        assertThrows(EmployeeNotFoundException.class, () -> handler.handle(1L, request));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenPRStateCompanyHasNonLegalEntityEmployee() {
        List<Long> employeeIds = List.of(1L);
        AddEmployeeToCompanyDto request = new AddEmployeeToCompanyDto(employeeIds, null);

        Company company = new Company("12345678000100", "Test Company", "80000000", "PR");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Employee emp1 = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "PR");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1));

        assertThrows(InvalidEmployeeTypeException.class, () -> handler.handle(1L, request));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void shouldAllowLegalEntityEmployeesInPRStateCompany() {
        List<Long> employeeIds = List.of(1L);
        AddEmployeeToCompanyDto request = new AddEmployeeToCompanyDto(employeeIds, null);

        Company company = new Company("12345678000100", "Test Company", "80000000", "PR");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        Employee emp1 = new Employee(EmployeeType.LEGAL_ENTITY, "Corp A", "80000000", "PR");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1));

        CompanyResponseDto response = handler.handle(1L, request);

        assertEquals("PR", response.state());
        assertNotNull(response.employees());
        assertEquals(1, response.employees().size());
        verify(companyRepository).save(company);
    }
}
