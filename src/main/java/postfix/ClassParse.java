package postfix;

import Token.Token;

@SuppressWarnings("unused")
public class ClassParse {
    private ClassLexer lexer;
    private Token currentToken;
    private String source;

    public void ClassLexer(String source){
        lexer = new ClassLexer(source);
        this.source = source;
    }


}
