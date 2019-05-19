package errors;

public class NetworkException extends Exception {

    public String description;

    public NetworkException(String message, String description, Throwable cause) {
        super(message, cause);
        this.description = description;
    }

    public NetworkException(String message, String description) {
        super(message);
        this.description = description;
    }

    public NetworkException(String message) {
        super(message);
    }

    public String getDescription() {
        return (this.description == null ? "" : this.description);
    }

    public boolean equals(TransactionException e) {
        return (e.getMessage().equals(this.getMessage()) && this.getDescription().equals(e.getDescription()));
    }

}
