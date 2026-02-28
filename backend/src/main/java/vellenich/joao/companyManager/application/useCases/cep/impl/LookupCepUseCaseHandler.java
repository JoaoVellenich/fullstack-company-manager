package vellenich.joao.companyManager.application.useCases.cep.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vellenich.joao.companyManager.application.useCases.cep.LookupCepUseCase;
import vellenich.joao.companyManager.domain.exception.ObjectNotFoundException;
import vellenich.joao.companyManager.infrastructure.externalservices.CepExternalService;
import vellenich.joao.companyManager.interfaces.rest.cep.CepResponseDto;

@Service
public class LookupCepUseCaseHandler implements LookupCepUseCase {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CepExternalService cepExternalService;

    public LookupCepUseCaseHandler(CepExternalService cepExternalService) {
        this.cepExternalService = cepExternalService;
    }

    @Override
    public CepResponseDto handle(String cep) {
        try {
            CepResponseDto result = cepExternalService.lookupCep(cep);
            if (result == null) {
                throw new ObjectNotFoundException("CEP not found: " + cep);
            }
            return result;
        } catch (ObjectNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ObjectNotFoundException("Failed to lookup CEP: " + cep);
        }
    }
}
