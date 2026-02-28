package vellenich.joao.companyManager.interfaces.rest.cep;

public record CepResponseDto(
        String cep,
        String uf,
        String cidade,
        String bairro,
        String logradouro
) {
}
