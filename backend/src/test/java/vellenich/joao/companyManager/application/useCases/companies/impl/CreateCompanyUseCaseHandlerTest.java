package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.CompanyAlreadyExistsException;
import vellenich.joao.companyManager.domain.exception.EmployeeNotFoundException;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeTypeException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;
import vellenich.joao.companyManager.interfaces.rest.company.CreateCompanyDto;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCompanyUseCaseHandlerTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private vellenich.joao.companyManager.application.useCases.companies.impl.CreateCompanyUseCaseHandler handler;

    @Test
    void shouldCreateCompanySuccessfully() {
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "SP", null);
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(false);

        CompanyResponseDto response = handler.handle(request);

        assertEquals("12345678000100", response.cnpj());
        assertEquals("Test Company", response.companyName());
        assertEquals("80000000", response.cep());
        assertEquals("SP", response.state());
        verify(companyRepository).save(any());
    }

    @Test
    void shouldCreateCompanyWithEmployees() {
        List<Long> employeeIds = List.of(1L, 2L);
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "SP", employeeIds);
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(false);

        Employee emp1 = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        Employee emp2 = new Employee(EmployeeType.LEGAL_ENTITY, "Jane Corp", "80000000", "SP");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1, emp2));

        CompanyResponseDto response = handler.handle(request);

        assertEquals("12345678000100", response.cnpj());
        verify(companyRepository).save(any());
    }

    @Test
    void shouldThrowWhenCnpjAlreadyExists() {
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "SP", null);
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(true);

        assertThrows(CompanyAlreadyExistsException.class, () -> handler.handle(request));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {
        List<Long> employeeIds = List.of(1L, 2L);
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "SP", employeeIds);
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(false);

        Employee emp1 = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1));

        assertThrows(EmployeeNotFoundException.class, () -> handler.handle(request));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenPRStateCompanyHasIndividualEmployee() {
        List<Long> employeeIds = List.of(1L);
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "PR", employeeIds);
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(false);

        Employee emp1 = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "PR");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1));

        assertThrows(InvalidEmployeeTypeException.class, () -> handler.handle(request));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void shouldAllowPRStateCompanyWithLegalEntityEmployees() {
        List<Long> employeeIds = List.of(1L, 2L);
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "PR", employeeIds);
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(false);

        Employee emp1 = new Employee(EmployeeType.LEGAL_ENTITY, "Corp A", "80000000", "PR");
        Employee emp2 = new Employee(EmployeeType.LEGAL_ENTITY, "Corp B", "80000000", "PR");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1, emp2));

        CompanyResponseDto response = handler.handle(request);

        assertEquals("12345678000100", response.cnpj());
        assertEquals("PR", response.state());
        verify(companyRepository).save(any());
    }

    @Test
    void shouldReturnMappedEmployeesInResponse() {
        List<Long> employeeIds = List.of(1L);
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "SP", employeeIds);
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(false);

        Employee emp1 = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        when(employeeRepository.findAllById(employeeIds)).thenReturn(List.of(emp1));

        CompanyResponseDto response = handler.handle(request);

        assertNotNull(response.employees());
        assertEquals(1, response.employees().size());
        var empDto = response.employees().get(0);
        assertEquals("John", empDto.name());
        assertEquals(EmployeeType.INDIVIDUAL, empDto.type());
        assertEquals("80000000", empDto.cep());
        assertEquals("SP", empDto.state());
        assertNull(empDto.cpf());
        assertNull(empDto.cnpj());
        assertNull(empDto.rg());
        assertNull(empDto.birthDate());
        assertNull(empDto.companies());
    }

    @Test
    void shouldCreateCompanyWithEmptyEmployeeList() {
        CreateCompanyDto request = new CreateCompanyDto("12345678000100", "Test Company", "80000000", "SP", Collections.emptyList());
        when(companyRepository.existsByCnpj("12345678000100")).thenReturn(false);

        CompanyResponseDto response = handler.handle(request);

        assertEquals("12345678000100", response.cnpj());
        verify(companyRepository).save(any());
        verify(employeeRepository, never()).findAllById(any());
    }
}
