package ApexCollectiveHibernateBE.ApexCollective.Common;

public class ApexCollectiveException extends Exception {
    public ApexCollectiveException(String message) {
        super(message);
    }

    public ApexCollectiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
