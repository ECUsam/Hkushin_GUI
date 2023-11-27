package postfix;


import Token.Token;
import Token.ClassType;
import Token.TokenClass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

@SuppressWarnings("unused")
public class ClassParser {
    public String ClassName;
    public ClassType classType;
    public String source;
    private Token token;
    private char currentChar;
    private StringReader stringReader;
    private BufferedReader bufferedReader;
    private int codeLine = 0;

    public ClassParser(String source){
        this.source = source;
        stringReader = new StringReader(source);
        bufferedReader = new BufferedReader(stringReader);
    }
    public void updateSource(String source) {
        this.source = source;
        stringReader = new StringReader(source);
        bufferedReader = new BufferedReader(stringReader);
    }
    public Token Parser(){
        try {
            int current;
            int lookahead;
            while ((current = bufferedReader.read()) != -1){
                currentChar = (char) current;
                switch (currentChar){
                    case ' ':
                        continue;
                    case '\n':
                        codeLine += 1;
                        continue;
                    case 0: case 1: case 2: case 3: case 4:
                    case 5: case 6: case 7: case 8: case 9:
                        return NumToken(TokenClass.TK_NUM, current);
                    case '=':
                        return new Token(TokenClass.TK_EQUAL);


                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return new Token(TokenClass.TK_EOF);
    }

    private Token NumToken(TokenClass tokenClass, int num){
        return new Token(tokenClass, num);
    }

    private Token StringToken(TokenClass tokenClass, String s){
        return new Token(tokenClass, s);
    }

    private boolean checkNextChar(char char2beCheck) throws IOException {
        bufferedReader.mark(1);
        int nextChar = bufferedReader.read();
        boolean isMatch = (char) nextChar == char2beCheck;
        bufferedReader.reset();
        return isMatch;
    }
}

