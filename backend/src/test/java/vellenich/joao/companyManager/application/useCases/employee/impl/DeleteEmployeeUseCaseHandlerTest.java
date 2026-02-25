package vellenich.joao.companyManager.application.useCases.employee.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vellenich.joao.companyManager.domain.entity.Employee;
import vellenich.joao.companyManager.domain.enums.EmployeeType;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.EmployeeRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteEmployeeUseCaseHandlerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DeleteEmployeeUseCaseHandler handler;

    @Test
    void shouldDeleteEmployeeSuccessfully() {
        Employee employee = new Employee(EmployeeType.INDIVIDUAL, "John", "80000000", "SP");
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        handler.handle(1L);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void shouldThrowWhenEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> handler.handle(1L));
        verify(employeeRepository, never()).delete(any());
    }
}
