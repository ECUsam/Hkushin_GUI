package postfix;

import Token.TokenClass;
import Token.Token;
import Token.ClassType;
import OPcode.*;

import java.util.Stack;


@SuppressWarnings("unused")
public class ClassParse {
    private ClassLexer lexer;
    private Token currentToken;
    private String source;
    public Pcode currentOPcode;
    private ClassType classType;
    public TreeNode baseNode;
    private TreeNode currentNode;
    private Stack<Token> tokenStack = new Stack<>();
    private int codeLevel;

    public ClassParse(String source){
        lexer = new ClassLexer(source);
        this.source = source;
        codeLevel = 0;
    }

    public void updateSource(String source){
        lexer.updateSource(source);
        this.source = source;
        codeLevel = 0;
        tokenStack = new Stack<>();
        baseNode = null;
    }
    //我不是要写递归下降吗？我在干什么？
    private void parseCurrentToken(){
        //do{
            switch (currentToken.tokenType){
                case TK_classname:
                    classType = ClassType.valueOf(currentToken.string);
                    next();
                    if(currentToken.tokenType == TokenClass.TK_LEFT_CURLY_BRACE){
                        baseNode = new TreeNode(classType.toString());
                        currentNode = baseNode;
                    }else if(currentToken.tokenType == TokenClass.TK_NAME){
                        baseNode = new TreeNode(classType.toString(), currentToken.string);
                        currentNode = baseNode;
                        next();
                        assert currentToken.tokenType == TokenClass.TK_LEFT_CURLY_BRACE;
                    }else throw new ParseException(codeLevel, currentToken.toString());
                    parseCurrentToken();
                case TK_open_par: case TK_open_bra: case TK_LEFT_CURLY_BRACE:
                    codeLevel += 1;
                    tokenStack.add(currentToken);
                case TK_RIGHT_CURLY_BRACE:
                    assert tokenStack.peek().tokenType == TokenClass.TK_LEFT_CURLY_BRACE;
                    codeLevel -= 1;
                    tokenStack.pop();
                case TK_close_par:
                    assert tokenStack.peek().tokenType == TokenClass.TK_close_par;
                    codeLevel -= 1;
                    tokenStack.pop();
                case TK_close_bra:
                    assert tokenStack.peek().tokenType == TokenClass.TK_close_bra;
                    codeLevel -= 1;
                    tokenStack.pop();
                case TK_COMMAND: case Tk_feature:
                    TreeNode CF_node = new TreeNode(currentToken.tokenType.toString(), currentToken);
                    currentNode.addChild(CF_node);
                case TK_IF: case TK_RIF:
                    TreeNode if_node = new TreeNode("if");
                    currentNode.addChild(if_node);
                    currentNode = if_node;

            }
        //}while (currentToken.tokenType != TokenClass.TK_EOF);
    }
    // 现在Token为逻辑字符，接下来要解析括号内内容
    private void parseLogic(){

    }

    public void advance(){
        next();
        parseCurrentToken();
    }



    private void next(){
        lexer.Advance();
        currentToken = lexer.currentToken;
    }

}