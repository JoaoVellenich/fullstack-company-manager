package vellenich.joao.companyManager.application.useCases.companies.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vellenich.joao.companyManager.domain.entity.Company;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.domain.repository.CompanyRepository;
import vellenich.joao.companyManager.interfaces.rest.company.CompanyResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCompanyUseCaseHandlerTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private GetCompanyUseCaseHandler handler;

    @Test
    void shouldGetCompanyByIdSuccessfully() {
        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        CompanyResponseDto response = handler.getCompanyById(1L);

        assertEquals("12345678000100", response.cnpj());
        assertEquals("Test Company", response.companyName());
        assertEquals("80000000", response.cep());
        assertEquals("SP", response.state());
        assertNotNull(response.employees());
        assertTrue(response.employees().isEmpty());
    }

    @Test
    void shouldThrowWhenCompanyNotFound() {
        when(companyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class, () -> handler.getCompanyById(1L));
    }

    @Test
    void shouldListAllCompaniesWithNoFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        Page<Company> page = new PageImpl<>(List.of(company), pageable, 1);
        when(companyRepository.findAll(pageable)).thenReturn(page);

        Page<CompanyResponseDto> result = handler.listAllCompanies(null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Company", result.getContent().get(0).companyName());
        verify(companyRepository).findAll(pageable);
    }

    @Test
    void shouldListCompaniesWithNameFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        Page<Company> page = new PageImpl<>(List.of(company), pageable, 1);
        when(companyRepository.searchByName("Test", pageable)).thenReturn(page);

        Page<CompanyResponseDto> result = handler.listAllCompanies("Test", null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Company", result.getContent().get(0).companyName());
        verify(companyRepository).searchByName("Test", pageable);
    }

    @Test
    void shouldListCompaniesWithCnpjFilter() {
        Pageable pageable = PageRequest.of(0, 10);
        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        Page<Company> page = new PageImpl<>(List.of(company), pageable, 1);
        when(companyRepository.searchByCnpj("123", pageable)).thenReturn(page);

        Page<CompanyResponseDto> result = handler.listAllCompanies(null, "123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("12345678000100", result.getContent().get(0).cnpj());
        verify(companyRepository).searchByCnpj("123", pageable);
    }

    @Test
    void shouldListCompaniesWithBothFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        Company company = new Company("12345678000100", "Test Company", "80000000", "SP");
        Page<Company> page = new PageImpl<>(List.of(company), pageable, 1);
        when(companyRepository.searchByNameAndCnpj("Test", "123", pageable)).thenReturn(page);

        Page<CompanyResponseDto> result = handler.listAllCompanies("Test", "123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Company", result.getContent().get(0).companyName());
        assertEquals("12345678000100", result.getContent().get(0).cnpj());
        verify(companyRepository).searchByNameAndCnpj("Test", "123", pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoCompaniesMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Company> page = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(companyRepository.searchByName("Nonexistent", pageable)).thenReturn(page);

        Page<CompanyResponseDto> result = handler.listAllCompanies("Nonexistent", null, pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }
}
