package Token;

public class Token {
    public TokenClass tokenType;
    int value;
    String string;
    public Token(TokenClass tokenType){
        this.tokenType = tokenType;
    }
    public Token(TokenClass tokenType, int num){
        this.tokenType = tokenType;
        this.value = num;
    }
    public Token(TokenClass tokenType, String s){
        this.tokenType = tokenType;
        this.string = s;
    }
}

