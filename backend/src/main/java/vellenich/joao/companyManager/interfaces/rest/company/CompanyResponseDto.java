package vellenich.joao.companyManager.interfaces.rest.company;

public record CompanyResponseDto(
        String cnpj,
        String companyName,
        String cep,
        String state
) {
}
