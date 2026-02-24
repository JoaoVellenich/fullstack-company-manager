package vellenich.joao.companyManager.domain.exception;

public class InvalidEmployeeTypeException extends RuntimeException {
    public InvalidEmployeeTypeException(String message) {
        super(message);
    }
}
