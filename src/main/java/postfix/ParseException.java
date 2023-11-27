package postfix;

public class ParseException extends RuntimeException {
    private int lineNumber;
    private String message;

    public ParseException(int lineNumber, String message) {
        this.lineNumber = lineNumber;
        this.message = message;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String getMessage() {
        return "Parse error at line " + lineNumber + ": " + message;
    }
}
