package vellenich.joao.companyManager.application.useCases.cep.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.infrastructure.externalservices.CepExternalService;
import vellenich.joao.companyManager.interfaces.rest.cep.CepResponseDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LookupCepUseCaseHandlerTest {

    @Mock
    private CepExternalService cepExternalService;

    @InjectMocks
    private LookupCepUseCaseHandler handler;

    @Test
    void shouldReturnCepDataWhenCepIsValid() {
        CepResponseDto expected = new CepResponseDto("01001000", "SP", "Sao Paulo", "Se", "Praca da Se");
        when(cepExternalService.lookupCep("01001000")).thenReturn(expected);

        CepResponseDto result = handler.handle("01001000");

        assertEquals("01001000", result.cep());
        assertEquals("SP", result.uf());
        assertEquals("Sao Paulo", result.cidade());
        verify(cepExternalService).lookupCep("01001000");
    }

    @Test
    void shouldThrowWhenCepNotFound() {
        when(cepExternalService.lookupCep("00000000")).thenReturn(null);

        assertThrows(ObjectNotFoundException.class, () -> handler.handle("00000000"));
        verify(cepExternalService).lookupCep("00000000");
    }

    @Test
    void shouldThrowWhenExternalServiceFails() {
        when(cepExternalService.lookupCep("99999999")).thenThrow(new RuntimeException("Connection refused"));

        assertThrows(ObjectNotFoundException.class, () -> handler.handle("99999999"));
        verify(cepExternalService).lookupCep("99999999");
    }
}
