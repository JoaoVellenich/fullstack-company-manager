package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCompanyUseCaseHandlerTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private DeleteCompanyUseCaseHandler handler;

    @Test
    void shouldDeleteCompanySuccessfully() {
        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        handler.handle(1L);

        verify(companyRepository).delete(company);
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> handler.handle(1L));
        verify(companyRepository, never()).delete(any());
    }
}
