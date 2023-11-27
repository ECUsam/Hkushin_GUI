package postfix;

import Constants.Constants;
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
            while ((current = bufferedReader.read()) != Constants.EOZ){
                currentChar = (char) current;
                switch (currentChar){
                    case ' ': case '\t':
                        continue;
                    case '\n': case '\r':
                        codeLine += 1;
                        continue;
                    case '=':
                        if( checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_IF_EQUAL);
                        }else return NomToken(TokenClass.TK_EQUAL);
                    case '[':
                        return NomToken(TokenClass.TK_open_bra);
                    case ']':
                        return NomToken(TokenClass.TK_close_bra);
                    case '(':
                        return NomToken(TokenClass.TK_open_par);
                    case ')':
                        return NomToken(TokenClass.TK_close_par);
                    case '{':
                        return NomToken(TokenClass.TK_LEFT_CURLY_BRACE);
                    case '}':
                        return NomToken(TokenClass.TK_RIGHT_CURLY_BRACE);
                    case '@':
                        return NomToken(TokenClass.TK_at);
                    case ',':
                        return NomToken(TokenClass.TK_comma);
                    case ';':
                        return NomToken(TokenClass.TK_semicolon);
                    case ':':
                        return NomToken(TokenClass.TK_colon);
                    case '<':
                        if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_IF_LT_OR_EQ);
                        }else return NomToken(TokenClass.TK_IF_LT);
                    case '>':
                        if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_IF_GT_OR_EQ);
                        }else return NomToken(TokenClass.TK_IF_GT);
                    case '*':
                        if( checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_multi_assign);
                        }else return NomToken(TokenClass.TK_asterisk);
                    case '/':
                        if( checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.Tk_divide_assign);
                        }else return NomToken(TokenClass.Tk_divide);
                    case '+':
                        if( checkNextChar('+')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_inc);
                        }else if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_add_assign);
                        }else return NomToken(TokenClass.TK_plus);
                    case '-':
                        if(checkNextChar('-')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_dec);
                        }else if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_multi_assign);
                        }else return NomToken(TokenClass.TK_minus);
                    case '!':
                        if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_NOT_EQ);
                        }else throw new ParseException(codeLine, "Unexpected character: " + currentChar);
                    case '&':
                        if(checkNextChar('&')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_and);
                        }else throw new ParseException(codeLine, "Unexpected character: " + currentChar);
                    case '|':
                        if(checkNextChar('|')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_or);
                        }else throw new ParseException(codeLine, "Unexpected character: " + currentChar);
                    default:
                        if(Character.isDigit(currentChar)){
                            int value = Character.getNumericValue(currentChar);
                            while (checkNextCharisInt()){
                                currentChar = (char) bufferedReader.read();
                                value = value * 10 + Character.getNumericValue(currentChar);
                            }
                            return NumToken(TokenClass.TK_NUM, value);
                        }
                        if(Character.isLetter(currentChar)||currentChar=='_'){
                            StringBuilder word_builder = new StringBuilder("" + currentChar);
                            while (checkNextCharisLetterOr_()){
                                currentChar = (char) bufferedReader.read();
                                word_builder.append(current);
                            }
                            String word = word_builder.toString();
                            switch (word) {
                                case "break":
                                    return StringToken(TokenClass.TK_BREAK, word);
                                case "while":
                                    return StringToken(TokenClass.TK_WHILE, word);
                                case "if":
                                    return StringToken(TokenClass.TK_IF, word);
                                case "else":
                                    return StringToken(TokenClass.TK_ELSE, word);
                                default:
                                    if(Constants.class_type.contains(word))
                                        return  StringToken(TokenClass.TK_classname, word);
                                    return StringToken(TokenClass.TK_NAME, word);
                            }

                        }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return NomToken(TokenClass.TK_EOF);
    }
    private Token NomToken(TokenClass tokenClass){
        return new Token(tokenClass);
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
    private boolean checkNextCharisInt() throws IOException{
        bufferedReader.mark(1);
        int nextChar = bufferedReader.read();
        boolean isMatch = Character.isDigit ((char) nextChar);
        bufferedReader.reset();
        return isMatch;
    }

    private boolean checkNextCharisLetterOr_() throws IOException{
        bufferedReader.mark(1);
        char nextChar = (char) bufferedReader.read();
        boolean isMatch = (Character.isLetter(nextChar) || nextChar == '_');
        bufferedReader.reset();
        return isMatch;
    }

}

