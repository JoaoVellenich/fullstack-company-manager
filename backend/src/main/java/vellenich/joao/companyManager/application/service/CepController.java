package vellenich.joao.companyManager.application.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vellenich.joao.companyManager.application.useCases.cep.LookupCepUseCase;
import vellenich.joao.companyManager.interfaces.rest.cep.CepResponseDto;

@RestController
@RequestMapping("/cep")
public class CepController {

    private final LookupCepUseCase lookupCepUseCase;

    public CepController(LookupCepUseCase lookupCepUseCase) {
        this.lookupCepUseCase = lookupCepUseCase;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<CepResponseDto> lookupCep(@PathVariable String cep) {
        CepResponseDto response = lookupCepUseCase.handle(cep);
        return ResponseEntity.ok(response);
    }
}
