package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetEmployeeUseCaseHandlerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private GetEmployeeUseCaseHandler handler;

    @Test
    void shouldGetEmployeeByIdSuccessfully() {
        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John Doe", "80000000", "SP");
        employee.setCpf("12345678901");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponseDto response = handler.getEmployeeById(1L);

        assertEquals("John Doe", response.name());
        assertEquals("12345678901", response.cpf());
        assertEquals(EmployeeType.INDIVIDUAL, response.type());
        assertEquals("80000000", response.cep());
        assertEquals("SP", response.state());
        assertNotNull(response.companies());
        assertTrue(response.companies().isEmpty());
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> handler.getEmployeeById(1L));
    }

    @Test
    void shouldListAllEmployeesWithNoFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John Doe", "80000000", "SP");
        Page<Employee> page = new PageImpl<>(List.of(employee), pageable, 1);
        when(employeeRepository.findAll(pageable)).thenReturn(page);

        Page<EmployeeResponseDto> result = handler.listAllEmployees(null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).name());
        verify(employeeRepository).findAll(pageable);
    }

    @Test
    void shouldListEmployeesWithNameFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John Doe", "80000000", "SP");
        Page<Employee> page = new PageImpl<>(List.of(employee), pageable, 1);
        when(employeeRepository.searchByName("John", pageable)).thenReturn(page);

        Page<EmployeeResponseDto> result = handler.listAllEmployees("John", null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).name());
        verify(employeeRepository).searchByName("John", pageable);
    }

    @Test
    void shouldListEmployeesWithDocumentFilterMatchingCpf() {
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John Doe", "80000000", "SP");
        employee.setCpf("12345678901");
        Page<Employee> page = new PageImpl<>(List.of(employee), pageable, 1);
        when(employeeRepository.searchByDocument("123", pageable)).thenReturn(page);

        Page<EmployeeResponseDto> result = handler.listAllEmployees(null, "123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("12345678901", result.getContent().get(0).cpf());
        verify(employeeRepository).searchByDocument("123", pageable);
    }

    @Test
    void shouldListEmployeesWithDocumentFilterMatchingCnpj() {
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee(EmployeeType.LEGAL_ENTITY, "Corp A", "80000000", "SP");
        employee.setCnpj("12345678000100");
        Page<Employee> page = new PageImpl<>(List.of(employee), pageable, 1);
        when(employeeRepository.searchByDocument("12345678000100", pageable)).thenReturn(page);

        Page<EmployeeResponseDto> result = handler.listAllEmployees(null, "12345678000100", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("12345678000100", result.getContent().get(0).cnpj());
        verify(employeeRepository).searchByDocument("12345678000100", pageable);
    }

    @Test
    void shouldListEmployeesWithBothFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John Doe", "80000000", "SP");
        employee.setCpf("12345678901");
        Page<Employee> page = new PageImpl<>(List.of(employee), pageable, 1);
        when(employeeRepository.searchByNameAndDocument("John", "123", pageable)).thenReturn(page);

        Page<EmployeeResponseDto> result = handler.listAllEmployees("John", "123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("John Doe", result.getContent().get(0).name());
        assertEquals("12345678901", result.getContent().get(0).cpf());
        verify(employeeRepository).searchByNameAndDocument("John", "123", pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoEmployeesMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Employee> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(employeeRepository.searchByName("Nonexistent", pageable)).thenReturn(page);

        Page<EmployeeResponseDto> result = handler.listAllEmployees("Nonexistent", null, pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }
}
