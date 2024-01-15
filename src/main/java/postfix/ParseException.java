package postfix;

public class ParseException extends RuntimeException {
    private int lineNumber;
    private int columnNumber; // 新增列号
    private String errorContext; // 可选，错误上下文

    public ParseException(int lineNumber, String message) {
        super("Parse error at line " + lineNumber + ": " + message);
        this.lineNumber = lineNumber;
    }

    public ParseException(int lineNumber, String message, String path) {
        super("Parse error at line " + lineNumber + ": " + message + " at path: " + path);
        this.lineNumber = lineNumber;
    }

    public ParseException(int lineNumber, int columnNumber, String message, String errorContext) {
        super("Parse error at line " + lineNumber + ", column " + columnNumber + ": " + message + ". Context: " + errorContext);
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.errorContext = errorContext;
    }

    public ParseException(int lineNumber, String message, Throwable cause) {
        super("Parse error at line " + lineNumber + ": " + message, cause);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public String getErrorContext() {
        return errorContext;
    }

    @Override
    public String toString() {
        return "ParseException{" +
                "lineNumber=" + lineNumber +
                ", columnNumber=" + columnNumber +
                ", errorContext='" + errorContext + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
