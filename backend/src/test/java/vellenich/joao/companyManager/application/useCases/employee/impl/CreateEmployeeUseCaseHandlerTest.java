package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.EmployeeAlreadyExistsException;
import vellenich.joao.companyManager.domain.exception.InvalidEmployeeDataException;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;
import vellenich.joao.companyManager.interfaces.rest.employee.CreateEmployeeDto;
import vellenich.joao.companyManager.interfaces.rest.employee.EmployeeResponseDto;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateEmployeeUseCaseHandlerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private CreateEmployeeUseCaseHandler handler;

    @Test
    void shouldCreateIndividualEmployeeSuccessfully() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "John Doe", "12345678900", null, "123456", LocalDate.of(1990, 1, 1),
                EmployeeType.INDIVIDUAL, "john@email.com", "80000000", "SP"
        );
        when(employeeRepository.existsByCpf("12345678900")).thenReturn(false);
        when(employeeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponseDto response = handler.handle(request);

        assertEquals("John Doe", response.name());
        assertEquals("12345678900", response.cpf());
        assertEquals("123456", response.rg());
        assertEquals(LocalDate.of(1990, 1, 1), response.birthDate());
        assertEquals(EmployeeType.INDIVIDUAL, response.type());
        assertEquals("john@email.com", response.email());
        assertEquals("80000000", response.cep());
        assertEquals("SP", response.state());
        verify(employeeRepository).save(any());
    }

    @Test
    void shouldCreateLegalEntityEmployeeSuccessfully() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "Corp LLC", null, "12345678000100", null, null,
                EmployeeType.LEGAL_ENTITY, "corp@email.com", "80000000", "SP"
        );
        when(employeeRepository.existsByCnpj("12345678000100")).thenReturn(false);
        when(employeeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponseDto response = handler.handle(request);

        assertEquals("Corp LLC", response.name());
        assertEquals("12345678000100", response.cnpj());
        assertEquals(EmployeeType.LEGAL_ENTITY, response.type());
        verify(employeeRepository).save(any());
    }

    @Test
    void shouldThrowWhenCpfAlreadyExists() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "John Doe", "12345678900", null, "123456", LocalDate.of(1990, 1, 1),
                EmployeeType.INDIVIDUAL, null, "80000000", "SP"
        );
        when(employeeRepository.existsByCpf("12345678900")).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class, () -> handler.handle(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCnpjAlreadyExists() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "Corp LLC", null, "12345678000100", null, null,
                EmployeeType.LEGAL_ENTITY, null, "80000000", "SP"
        );
        when(employeeRepository.existsByCnpj("12345678000100")).thenReturn(true);

        assertThrows(EmployeeAlreadyExistsException.class, () -> handler.handle(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenIndividualMissingRg() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "John Doe", "12345678900", null, null, LocalDate.of(1990, 1, 1),
                EmployeeType.INDIVIDUAL, null, "80000000", "SP"
        );

        assertThrows(InvalidEmployeeDataException.class, () -> handler.handle(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenIndividualMissingBirthDate() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "John Doe", "12345678900", null, "123456", null,
                EmployeeType.INDIVIDUAL, null, "80000000", "SP"
        );

        assertThrows(InvalidEmployeeDataException.class, () -> handler.handle(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenIndividualMissingCpf() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "John Doe", null, null, "123456", LocalDate.of(1990, 1, 1),
                EmployeeType.INDIVIDUAL, null, "80000000", "SP"
        );

        assertThrows(InvalidEmployeeDataException.class, () -> handler.handle(request));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenLegalEntityMissingCnpj() {
        CreateEmployeeDto request = new CreateEmployeeDto(
                "Corp LLC", null, null, null, null,
                EmployeeType.LEGAL_ENTITY, null, "80000000", "SP"
        );

        assertThrows(InvalidEmployeeDataException.class, () -> handler.handle(request));
        verify(employeeRepository, never()).save(any());
    }
}
