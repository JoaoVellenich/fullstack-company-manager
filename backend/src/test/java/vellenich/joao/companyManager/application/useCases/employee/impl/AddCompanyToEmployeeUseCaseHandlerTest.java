package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.entity.CompanyEmployee;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeTypeException;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.employee.AddCompanyToEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddCompanyToEmployeeUseCaseHandlerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private AddCompanyToEmployeeUseCaseHandler handler;

    @Test
    void shouldAddCompanyToEmployeeSuccessfully() throws Exception {
        List<Long> companyIds = List.of(1L, 2L);
        AddCompanyToEmployeeDto request = new AddCompanyToEmployeeDto(companyIds);

        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        setId(employee, 1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Company company1 = new Company("12345678000100", "Company A", "80000000", "SP");
        setId(company1, 1L);
        Company company2 = new Company("98765432000100", "Company B", "80000000", "RJ");
        setId(company2, 2L);
        when(companyRepository.findAllById(companyIds)).thenReturn(List.of(company1, company2));

        EmployeeResponseDto response = handler.handle(1L, request);

        assertEquals("John", response.name());
        assertEquals(EmployeeType.INDIVIDUAL, response.type());
        assertNotNull(response.companies());
        assertEquals(2, response.companies().size());
        verify(companyRepository).saveAll(List.of(company1, company2));
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {
        AddCompanyToEmployeeDto request = new AddCompanyToEmployeeDto(List.of(1L));
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> handler.handle(99L, request));
        verify(companyRepository, never()).saveAll(any());
    }

    @Test
    void shouldThrowWhenCompanyNotFound() throws Exception {
        List<Long> companyIds = List.of(1L, 2L);
        AddCompanyToEmployeeDto request = new AddCompanyToEmployeeDto(companyIds);

        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        setId(employee, 1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Company company1 = new Company("12345678000100", "Company A", "80000000", "SP");
        setId(company1, 1L);
        when(companyRepository.findAllById(companyIds)).thenReturn(List.of(company1));

        assertThrows(ObjectNotFoundException.class, () -> handler.handle(1L, request));
        verify(companyRepository, never()).saveAll(any());
    }

    @Test
    void shouldThrowWhenPRCompanyAndEmployeeIsNotLegalEntity() throws Exception {
        List<Long> companyIds = List.of(1L);
        AddCompanyToEmployeeDto request = new AddCompanyToEmployeeDto(companyIds);

        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        setId(employee, 1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Company company = new Company("12345678000100", "Company PR", "80000000", "PR");
        setId(company, 1L);
        when(companyRepository.findAllById(companyIds)).thenReturn(List.of(company));

        assertThrows(InvalidEmployeeTypeException.class, () -> handler.handle(1L, request));
        verify(companyRepository, never()).saveAll(any());
    }

    @Test
    void shouldAllowPRCompanyWithLegalEntityEmployee() throws Exception {
        List<Long> companyIds = List.of(1L);
        AddCompanyToEmployeeDto request = new AddCompanyToEmployeeDto(companyIds);

        Employee employee = new Employee(EmployeeType.LEGAL_ENTITY, "Corp A", "80000000", "PR");
        setId(employee, 1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Company company = new Company("12345678000100", "Company PR", "80000000", "PR");
        setId(company, 1L);
        when(companyRepository.findAllById(companyIds)).thenReturn(List.of(company));

        EmployeeResponseDto response = handler.handle(1L, request);

        assertEquals("Corp A", response.name());
        assertEquals(EmployeeType.LEGAL_ENTITY, response.type());
        assertNotNull(response.companies());
        assertEquals(1, response.companies().size());
        assertEquals("PR", response.companies().get(0).state());
        verify(companyRepository).saveAll(List.of(company));
    }

    @Test
    void shouldIgnoreAlreadyAssociatedCompany() throws Exception {
        List<Long> companyIds = List.of(1L, 2L);
        AddCompanyToEmployeeDto request = new AddCompanyToEmployeeDto(companyIds);

        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        setId(employee, 1L);

        Company existingCompany = new Company("12345678000100", "Company A", "80000000", "SP");
        setId(existingCompany, 1L);

        // Simulate existing association
        existingCompany.addEmployee(employee);
        employee.getCompanies().add(new CompanyEmployee(existingCompany, employee));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Company newCompany = new Company("98765432000100", "Company B", "80000000", "RJ");
        setId(newCompany, 2L);
        when(companyRepository.findAllById(companyIds)).thenReturn(List.of(existingCompany, newCompany));

        EmployeeResponseDto response = handler.handle(1L, request);

        assertEquals("John", response.name());
        assertNotNull(response.companies());
        assertEquals(2, response.companies().size());
        // Only the new company should be saved
        verify(companyRepository).saveAll(List.of(newCompany));
    }

    @Test
    void shouldReturnAllAssociatedCompanies() throws Exception {
        List<Long> companyIds = List.of(2L);
        AddCompanyToEmployeeDto request = new AddCompanyToEmployeeDto(companyIds);

        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        setId(employee, 1L);

        Company existingCompany = new Company("12345678000100", "Company A", "80000000", "SP");
        setId(existingCompany, 1L);

        // Simulate existing association
        employee.getCompanies().add(new CompanyEmployee(existingCompany, employee));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        Company newCompany = new Company("98765432000100", "Company B", "80000000", "RJ");
        setId(newCompany, 2L);
        when(companyRepository.findAllById(companyIds)).thenReturn(List.of(newCompany));

        EmployeeResponseDto response = handler.handle(1L, request);

        assertEquals(2, response.companies().size());
        verify(companyRepository).saveAll(List.of(newCompany));
    }

    private void setId(Object entity, Long id) throws Exception {
        Field idField = entity.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, id);
    }
}
