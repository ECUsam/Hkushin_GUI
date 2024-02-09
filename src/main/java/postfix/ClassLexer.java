package postfix;

import Token.TokenFeature;
import Token.TokenCommand;
import Token.CommandType;
import Constants.Constants;
import Token.Token;
import Token.TokenClass;
import Constants.Constants_GUI;
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
    public ClassParse classParse;

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
        classType = null;
        codeLine = 0;
        isSourceDetail = false;
    }

    public void get_ClassParse(ClassParse c){
        classParse = c;
    }
    // 返回Token是初期的错误做法,请前进后直接获取currentToken
    // TODO:错误检测
    public Token Advance(){
        try {
            int current;
            while ((current = bufferedReader.read()) != Constants.EOZ){
                currentChar = (char) current;
//                System.out.print(currentChar);
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
                        if(checkNextChar('/') || checkNextChar('+')){
                            StringBuilder explain = new StringBuilder();
                            do{
                                explain.append(currentChar);
                                currentChar = (char) bufferedReader.read();
                                if (currentChar == '\n') codeLine += 1;
                            }while (currentChar!='\r'&&currentChar!='\n');
                            return NomToken(TokenClass.TK_explain_line, explain.toString());
                        }else if(checkNextChar('*')){
                            StringBuilder explain = new StringBuilder();
                            do{
                                explain.append(currentChar);
                                currentChar = (char) bufferedReader.read();

                                if (currentChar == '\n') codeLine += 1;
                            }while (!(currentChar == '*' && checkNextChar('/')));
                            explain.append("*/");
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_explain_all, explain.toString());
                        }else
                        if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.Tk_divide_assign, "/=");
                        }else if(checkNextChar('+')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.divide_plus, "/+");
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
                    case '!':
                        if(checkNextChar('=')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_NOT_EQ, "!=");
                        }else throw new ParseException(codeLine+classParse.baseCodeLine, "Unexpected character: " + currentChar, classParse.path);
                    case '&':
                        if(checkNextChar('&')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_and, "&&");
                        }else throw new ParseException(codeLine+classParse.baseCodeLine, "Unexpected character: " + currentChar, classParse.path);
                    case '|':
                        if(checkNextChar('|')){
                            currentChar = (char) bufferedReader.read();
                            return NomToken(TokenClass.TK_or, "||");
                        }else throw new ParseException(codeLine+classParse.baseCodeLine, "Unexpected character: " + currentChar, classParse.path);
                    default:
                        if(Character.isDigit(currentChar)){
                            int value = Character.getNumericValue(currentChar);
                            while (checkNextCharisInt()){
                                currentChar = (char) bufferedReader.read();
                                if (currentChar == '\n') codeLine += 1;
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
                                if (currentChar == '\n') codeLine += 1;
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
                                        return StringToken(TokenClass.TK_className, word);
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
                if (currentChar == '\n') codeLine += 1;
            } while (currentChar == ' ' || currentChar == '\r' || currentChar == '\t' || currentChar == '\n');
        }
    }
    private void skipSpaceWithoutEnter() throws IOException {
        if (currentChar == ' ' || currentChar == '\t' || checkNextChar(' ') || checkNextChar('\t')) {

            do {
                currentChar = (char) bufferedReader.read();
                if (currentChar == '\n') codeLine += 1;
            } while (currentChar == ' ' || currentChar == '\t');
        }
    }
    private String getAnyWordFromStream() throws IOException {
        StringBuilder wordBuild = new StringBuilder();
        wordBuild.append(currentChar);
        while (!checkNextCharisSpecial_not_space()){
            currentChar = (char) bufferedReader.read();
            if (currentChar == '\n') codeLine += 1;
            wordBuild.append(currentChar);
        }
        assert wordBuild.length() != 0;
        return wordBuild.toString();
    }

    private String getAnyWordFromStream_feature() throws IOException {
        StringBuilder wordBuild = new StringBuilder();
        wordBuild.append(currentChar);
        while (!checkNextCharisSpecial_not_space_feature()){
            currentChar = (char) bufferedReader.read();
            if (currentChar == '\n') codeLine += 1;
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
        char nextChar = (char) bufferedReader.read();
        boolean isMatch =  nextChar == char2beCheck;
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

    private boolean checkNextCharisSpecial_not_space() throws IOException{
        bufferedReader.mark(1);
        char nextChar = (char) bufferedReader.read();
        boolean isMatch = (Constants.special_char_not_space.contains(nextChar));
        bufferedReader.reset();
        return isMatch;
    }

    private boolean checkNextCharisSpecial_not_space_feature() throws IOException{
        bufferedReader.mark(1);
        char nextChar = (char) bufferedReader.read();
        boolean isMatch = (Constants.special_char_not_space_feature.contains(nextChar));
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

    // 不能两次reset（告诫）

    private CommandType checkWordType() throws IOException {
        bufferedReader.mark(50);
        currentChar = (char) bufferedReader.read();
        if(Constants.space_char.contains(currentChar)){
            do{
                currentChar = (char) bufferedReader.read();
            }while (Constants.space_char.contains(currentChar));
        }
//        if(checkNextChar(' '))skipSpace();
//        else currentChar = (char) bufferedReader.read();

        if (currentChar == '(') return CommandType.command;
        else if (currentChar == '=') {
            currentChar = (char) bufferedReader.read();
            if(currentChar=='='){
                bufferedReader.reset();
                return CommandType.normal;
            }
            bufferedReader.reset();

            if(checkNextChar(' '))skipSpace();
            else currentChar = (char) bufferedReader.read();

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
            if (currentChar == '\n') codeLine += 1;
        }
    }

    private void skip2AnyWordWithoutEnter() throws IOException {
        while (Constants.special_char_without_enter.contains(currentChar)){
            currentChar = (char) bufferedReader.read();
            if (currentChar == '\n') codeLine += 1;
        }
    }

    private TokenCommand parseCommandToken(String word) throws IOException {
        //为什么检测了两遍？
        if(checkNextChar(')')){
            currentChar = (char) bufferedReader.read();
            if (currentChar == '\n') codeLine += 1;
            currentToken = new TokenCommand(word);
            return (TokenCommand) currentToken;
        }
        currentChar = (char) bufferedReader.read();
        if (currentChar == '\n') codeLine += 1;
        skipSpace();
        if(checkNextChar(')')){
            currentChar = (char) bufferedReader.read();
            if (currentChar == '\n') codeLine += 1;
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
        if (currentChar == '\n') codeLine += 1;
        assert currentChar==')': "Unexpected character: " + currentChar;
        currentToken = new TokenCommand(word, strings.toArray(String[]::new));
        return (TokenCommand) currentToken;
    }

    private TokenFeature parseFeatureToken(String word) throws IOException{
        currentChar = (char) bufferedReader.read();
        if (currentChar == '\n') codeLine += 1;
        skipSpaceWithoutEnter();

        if(currentChar == '@')return new TokenFeature(word);
        if(isSourceDetail||(Objects.equals(classType, "scenario") && Objects.equals(word, "text"))){
            StringBuilder stringBuilder = new StringBuilder();
            skipSpace();
            while (currentChar!=';'){
                stringBuilder.append(currentChar);
                int temp =  bufferedReader.read();
                if (currentChar == '\n') codeLine += 1;
                if(temp==-1){
                    LogManager.addError(Constants_GUI.getDescription("detail_parse_error")+codeLine);
                    throw new ParseException(codeLine, "解析错误");
                }
                currentChar = (char) temp;
            }
            currentToken = new TokenFeature(word, stringBuilder.toString());
            return (TokenFeature) currentToken;
        }
        List<String> strings = new ArrayList<>();
        do{
            if (currentChar == '\n') codeLine += 1;// 是这里吗？
            skip2AnyWord();
            strings.add(getAnyWordFromStream_feature());
            skipSpaceWithoutEnter();
        }while (currentChar != '\n' && currentChar != '\r'&& (currentChar = (char) bufferedReader.read())==',');
        currentToken = new TokenFeature(word, strings.toArray(String[]::new));
        return (TokenFeature) currentToken;
    }
}

