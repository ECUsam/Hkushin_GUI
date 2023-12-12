package postfix;

import Token.TokenFeature;
import Token.TokenCommand;
import Token.CommandType;
import Constants.Constants;
import Token.Token;
import Token.TokenClass;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ClassLexer {
    public String classType;
    public String source;
    private char currentChar;
    private StringReader stringReader;
    private BufferedReader bufferedReader;
    public int codeLine = 0;
    private List<Token> tokenList;
    private boolean isSourceDetail = false;
    public Token currentToken;

    public ClassLexer(String source){
        this.source = source;
        stringReader = new StringReader(source);
        bufferedReader = new BufferedReader(stringReader);
        tokenList = new ArrayList<>();
    }

    public void updateSource(String source) {
        this.source = source;
        stringReader = new StringReader(source);
        bufferedReader = new BufferedReader(stringReader);
        tokenList = new ArrayList<>();
        currentToken = null;
        currentChar = '\u0000';
    }
    // 返回Token是初期的错误做法,请前进后直接获取currentToken
    // TODO:错误检测
    public Token Advance(){
        try {
            int current;
            while ((current = bufferedReader.read()) != Constants.EOZ){
                currentChar = (char) current;
                //System.out.print(currentChar);
                switch (currentChar){
                    //可以换成skipSpace();，但是也可能再改
                    case ' ': case '\t': case '\r':
                        continue;
                    case '\n':
                        codeLine += 1;
                        continue;
                    case '=':
                        if( checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_IF_EQUAL, "==");
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
                            return NomToken(TokenClass.TK_IF_LT_OR_EQ, "<=");
                        }else return NomToken(TokenClass.TK_IF_LT);
                    case '>':
                        if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_IF_GT_OR_EQ, ">=");
                        }else return NomToken(TokenClass.TK_IF_GT);
                    case '*':
                        if( checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_multi_assign, "*=");
                        }else return NomToken(TokenClass.TK_asterisk);
                    case '/':
                        if( checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.Tk_divide_assign, "/=");
                        }else return NomToken(TokenClass.Tk_divide);
                    case '+':
                        if( checkNextChar('+')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_inc, "++");
                        }else if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_add_assign, "+=");
                        }else return NomToken(TokenClass.TK_plus);
                    case '-':
                        if(checkNextChar('-')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_dec, "--");
                        }else if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_multi_assign, "-=");
                        }else return NomToken(TokenClass.TK_minus);
                        //TODO:codeLine肯定要改
                    case '!':
                        if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_NOT_EQ, "!=");
                        }else throw new ParseException(codeLine, "Unexpected character: " + currentChar);
                    case '&':
                        if(checkNextChar('&')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_and, "&&");
                        }else throw new ParseException(codeLine, "Unexpected character: " + currentChar);
                    case '|':
                        if(checkNextChar('|')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_or, "||");
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
                        // Character.isLetter(currentChar)||currentChar=='_'
                        if(!Constants.special_char.contains(currentChar)){
                            StringBuilder word_builder = new StringBuilder("" + currentChar);
                            // checkNextCharisLetterOr_OrDigit()
                            while (!checkNextCharisSpecial()){
                                currentChar = (char) bufferedReader.read();
                                word_builder.append(currentChar);
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
                                case "rif":
                                    return StringToken(TokenClass.TK_RIF, word);
                                default:
                                    if(Constants.class_type.contains(word) && classType == null){
                                        classType = word;
                                        if(word.equals("detail"))isSourceDetail=true;
                                        return StringToken(TokenClass.TK_classname, word);
                                    }
                                    CommandType commandType = checkWordType();
                                    if(commandType == CommandType.command){
                                        return parseCommandToken(word);
                                    }
                                    if(commandType == CommandType.feature){
                                        return parseFeatureToken(word);
                                    }

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

    public List<Token> getTokenList() {
        return tokenList;
    }
    public void printTokenList(){
        for (Token token : tokenList){
            System.out.print(token);
        }
    }

    public void ParseSource(){
        Token token;
        do {
            Advance();
            tokenList.add(currentToken);
            //System.out.print(currentToken);
        } while (currentToken.tokenType != TokenClass.TK_EOF);
    }

    private void skipSpace() throws IOException {
        if (currentChar == ' ' || currentChar == '\r' || currentChar == '\t' || currentChar == '\n'
                || checkNextChar(' ')|| checkNextChar('\r')
                || checkNextChar('\t') || checkNextChar('\n')) {
            do {
                currentChar = (char) bufferedReader.read();
                if (currentChar == '\r' || currentChar == '\n') codeLine += 1;
            } while (currentChar == ' ' || currentChar == '\r' || currentChar == '\t' || currentChar == '\n');
        }
    }
    private void skipSpaceWithoutEnter() throws IOException {
        if (currentChar == ' ' || currentChar == '\t' || checkNextChar(' ') || checkNextChar('\t')) {

            do {
                currentChar = (char) bufferedReader.read();
            } while (currentChar == ' ' || currentChar == '\t');
        }
    }
    private String getAnyWordFromStream() throws IOException {
        StringBuilder wordBuild = new StringBuilder();
        wordBuild.append(currentChar);
        while (!checkNextCharisSpecial()){
            currentChar = (char) bufferedReader.read();
            wordBuild.append(currentChar);
        }
        assert wordBuild.length() != 0;
        return wordBuild.toString();
    }

    private Token NomToken(TokenClass tokenClass){
        currentToken = new Token(tokenClass, ""+currentChar);
        return currentToken;
    }
    private Token NomToken(TokenClass tokenClass, String string){
        currentToken = new Token(tokenClass, string);
        return currentToken;
    }

    private Token NumToken(TokenClass tokenClass, int num){
        currentToken = new Token(tokenClass, num);
        return currentToken;
    }

    private Token StringToken(TokenClass tokenClass, String s){
        currentToken = new Token(tokenClass, s);
        return currentToken;
    }

    public Token getLastToken(){
        int lastIndex = tokenList.size() - 1;
        if (lastIndex<0)return null;
        return tokenList.get(lastIndex);
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

    private boolean checkNextCharisLetterOr_OrDigit() throws IOException{
        bufferedReader.mark(1);
        char nextChar = (char) bufferedReader.read();
        boolean isMatch = (Character.isLetter(nextChar) || nextChar == '_' || Character.isDigit(nextChar));
        bufferedReader.reset();
        return isMatch;
    }

    private boolean checkNextCharisSpecial() throws IOException{
        bufferedReader.mark(1);
        char nextChar = (char) bufferedReader.read();
        boolean isMatch = (Constants.special_char.contains(nextChar));
        bufferedReader.reset();
        return isMatch;
    }

    private boolean checkNextCharisSpecialWithoutEnter() throws IOException{
        bufferedReader.mark(1);
        char nextChar = (char) bufferedReader.read();
        boolean isMatch = (Constants.special_char_without_enter.contains(nextChar));
        bufferedReader.reset();
        return isMatch;
    }

    private CommandType checkWordType() throws IOException {
        bufferedReader.mark(10);
        if(checkNextChar(' '))skipSpace();
        else currentChar = (char) bufferedReader.read();

        if (currentChar == '(') return CommandType.command;
        else if (currentChar == '=') {
            if(checkNextChar('=')){
                bufferedReader.reset();
                return CommandType.normal;
            }
            return CommandType.feature;
        }
        else {
            bufferedReader.reset();
            return CommandType.normal;
        }
    }
    private void skip2AnyWord() throws IOException {
        while (Constants.special_char.contains(currentChar)){
            currentChar = (char) bufferedReader.read();
        }
    }

    private void skip2AnyWordWithoutEnter() throws IOException {
        while (Constants.special_char_without_enter.contains(currentChar)){
            currentChar = (char) bufferedReader.read();
        }
    }

    private TokenCommand parseCommandToken(String word) throws IOException {
        //为什么检测了两遍？
        if(checkNextChar(')')){
            currentChar = (char) bufferedReader.read();
            currentToken = new TokenCommand(word);
            return (TokenCommand) currentToken;
        }
        currentChar = (char) bufferedReader.read();
        skipSpace();
        if(checkNextChar(')')){
            currentChar = (char) bufferedReader.read();
            currentToken = new TokenCommand(word);
            return (TokenCommand) currentToken;
        }
        List<String> strings = new ArrayList<>();
        do {
            skip2AnyWord();
            strings.add(getAnyWordFromStream());
            skipSpace();
        }while (checkNextChar(',') && (currentChar = (char) bufferedReader.read())==',');
        currentChar = (char) bufferedReader.read();
        assert currentChar==')': "Unexpected character: " + currentChar;
        currentToken = new TokenCommand(word, strings.toArray(String[]::new));
        return (TokenCommand) currentToken;
    }

    private TokenFeature parseFeatureToken(String word) throws IOException{
        currentChar = (char) bufferedReader.read();
        skipSpaceWithoutEnter();

        if(currentChar == '@')return new TokenFeature(word);
        if(isSourceDetail||(Objects.equals(classType, "scenario") && Objects.equals(word, "text"))){
            StringBuilder stringBuilder = new StringBuilder();
            skipSpace();
            while (currentChar!=';'){
                stringBuilder.append(currentChar);
                currentChar = (char) bufferedReader.read();
            }
            currentToken = new TokenFeature(word, stringBuilder.toString());
            return (TokenFeature) currentToken;
        }
        List<String> strings = new ArrayList<>();
        do{
            skip2AnyWord();
            strings.add(getAnyWordFromStream());
            skipSpaceWithoutEnter();
        }while (currentChar != '\n' && currentChar != '\r'&& (currentChar = (char) bufferedReader.read())==',');
        currentToken = new TokenFeature(word, strings.toArray(String[]::new));
        return (TokenFeature) currentToken;
    }
}

