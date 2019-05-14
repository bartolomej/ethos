package errors;

public class BlockException extends Exception{

    public String description;

    public BlockException(String message, String description, Throwable cause) {
        super(message, cause);
        this.description = description;
    }

    public BlockException(String message, String description) {
        super(message);
        this.description = description;
    }

    public BlockException(String message) {
        super(message);
    }

    public String getDescription() {
        return (this.description == null ? "" : this.description);
    }

    public boolean equals(BlockException e) {
        return (e.getMessage().equals(this.getMessage()) && this.getDescription().equals(e.getDescription()));
    }

}
