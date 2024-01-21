package Token;

import java.util.Objects;

public class Token {
    public TokenClass tokenType;
    protected int value;
    public String string;
    protected  __TokenType __tokenType;
    public Token(TokenClass tokenType){
        this.tokenType = tokenType;
        this.__tokenType = __TokenType.TK_NUDE;
    }
    public Token(TokenClass tokenType, int num){
        this.tokenType = tokenType;
        this.value = num;
        this.__tokenType = __TokenType.TK_INT;
    }
    public Token(TokenClass tokenType, String s){
        this.tokenType = tokenType;
        this.string = s;
        this.__tokenType = __TokenType.TK_STRING;
    }

    public Object getValue(){
        return string;
    }

    public String toCode(){
        return switch (__tokenType){
            case TK_INT -> ""+value;
            case TK_STRING -> string;
            default -> string;
        };
    }

    @Override
    public String toString() {
        return switch (__tokenType) {
            case TK_INT -> "Token(type: " + tokenType + ", value: " + value + ")\n";
            case TK_STRING -> "Token(type: " + tokenType + ", string: '" + string.replace("\r", "").replace("\n", "") + "')\n";
            default -> "Token(type: " + tokenType + ")\n";
        };
    }

}

enum __TokenType{
    TK_INT,
    TK_STRING,
    TK_STRINGS,
    TK_NUDE
}