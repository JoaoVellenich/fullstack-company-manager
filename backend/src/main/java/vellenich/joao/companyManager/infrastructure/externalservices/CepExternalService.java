package vellenich.joao.companyManager.infrastructure.externalservices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vellenich.joao.companyManager.interfaces.rest.cep.CepResponseDto;

@Service
public class CepExternalService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;

    public CepExternalService() {
        this.restTemplate = new RestTemplate();
    }

    public CepResponseDto lookupCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";

        ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);

        if (response == null || response.erro()) {
            return null;
        }

        return new CepResponseDto(
                response.cep(),
                response.uf(),
                response.localidade(),
                response.bairro(),
                response.logradouro()
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ViaCepResponse(
            String cep,
            String logradouro,
            String bairro,
            String localidade,
            String uf,
            boolean erro
    ) {}
}
