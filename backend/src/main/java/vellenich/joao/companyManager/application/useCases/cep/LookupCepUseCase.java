package vellenich.joao.companyManager.application.useCases.cep;

import vellenich.joao.companyManager.interfaces.rest.cep.CepResponseDto;

public interface LookupCepUseCase {
    CepResponseDto handle(String cep);
}
