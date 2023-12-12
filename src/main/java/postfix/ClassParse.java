package postfix;

import Constants.Constants;
import Token.TokenClass;
import Token.Token;
import Token.ClassType;
import OPcode.*;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;
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

    public void run(){
        do{
            advance();
        }while (currentToken.tokenType != TokenClass.TK_EOF);
    }
    //我不是要写递归下降吗？我在干什么？
    private void parseCurrentToken(){
        //do{
        System.out.print(currentToken);
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
                    break;
                //估计用不到
                case TK_open_par:
                    codeLevel += 1;
                    tokenStack.add(currentToken);
                    break;
                case TK_open_bra: case TK_LEFT_CURLY_BRACE:
                    codeLevel += 1;
                    tokenStack.add(currentToken);
                    break;
                case TK_RIGHT_CURLY_BRACE:

                    System.out.print(codeLevel);
                    System.out.print("\n");
                    assert tokenStack.peek().tokenType == TokenClass.TK_LEFT_CURLY_BRACE;
                    codeLevel -= 1;
                    tokenStack.pop();
                    break;
                case TK_close_par:
                    assert tokenStack.peek().tokenType == TokenClass.TK_close_par;
                    codeLevel -= 1;
                    tokenStack.pop();
                    currentNode = currentNode.parent;
                    break;
                case TK_close_bra:
                    assert tokenStack.peek().tokenType == TokenClass.TK_close_bra;
                    codeLevel -= 1;
                    tokenStack.pop();
                    break;
                case TK_COMMAND:
                    TreeNode C_node = new TreeNode("Command", currentToken);
                    currentNode.addChild(C_node);
                    break;
                case Tk_feature:
                    TreeNode F_node = new TreeNode("Feature", currentToken);
                    currentNode.addChild(F_node);
                    break;
                case TK_IF: case TK_RIF:
                    TreeNode if_node = new TreeNode(currentToken.string);
                    currentNode.addChild(if_node);
                    currentNode = if_node;
                    next();
                    parseLogic();
                    //现在指向‘)’
                    next();
                    //指向‘}’
                    parseBlock();
                    currentNode = currentNode.parent;
                    break;
                case TK_ELSE:
                    //if -> block
//                    assert Objects.equals(currentNode.parent.key, "if")|| Objects.equals(currentNode.parent.key, "rif");
//                    currentNode = currentNode.parent.parent;
                    next();
                    if(currentToken.tokenType == TokenClass.TK_IF || currentToken.tokenType == TokenClass.TK_RIF){
                        TreeNode else_if_node = new TreeNode("elseif");
                        currentNode.addChild(else_if_node);
                        currentNode = else_if_node;
                        next();
                        parseLogic();
                        next();
                        parseBlock();
                        currentNode = currentNode.parent;
                    }
                    else if(currentToken.tokenType == TokenClass.TK_LEFT_CURLY_BRACE){
                        TreeNode else_node = new TreeNode(currentToken.string);
                        currentNode.addChild(else_node);
                        currentNode = else_node;
                        parseBlock();
                        currentNode = currentNode.parent;
                    }else throw new ParseException(lexer.codeLine, currentToken.toString());
                    break;
                case TK_WHILE:
                    TreeNode while_node = new TreeNode(currentToken.string);
                    currentNode.addChild(while_node);
                    currentNode = while_node;
                    next();
                    parseLogic();
                    next();
                    parseBlock();
                    currentNode = currentNode.parent;
                    break;
            }
        //}while (currentToken.tokenType != TokenClass.TK_EOF);
    }

    //写的什么破玩意
//    private void parseLogic() {
//        assert currentToken.tokenType == TokenClass.TK_open_par;
//        parseCurrentToken();
//        do {
//            next();
//            switch (currentToken.tokenType) {
//                case TK_open_bra -> parseLogic();
//                case TK_close_bra -> {
//                    LogManager.addLog("WARNING:逻辑体为空");
//                    currentNode.addChild(new TreeNode("Logic", null));
//                    return;
//                }
//                default -> {
//                    parseExpr();
//                }
//            }
//        }while (true);
//    }
    // 现在Token为逻辑字符，接下来要解析括号内内容
    //结束之时指向‘)’
    //现在指向‘(‘
    private void parseLogic(){
        consume(TokenClass.TK_open_par);
        if(currentToken.tokenType == TokenClass.TK_close_par){
            LogManager.addLog("WARNING:逻辑体为空");
            return;
        }
        var temp = currentNode.key;

        var LogicNode = new TreeNode("Logic");
        currentNode.addChild(LogicNode);
        currentNode = LogicNode;

        parseLogicExprList();

        currentNode = currentNode.parent;
        assert currentNode.key.equals(temp);
        assert currentToken.tokenType == TokenClass.TK_close_par;
    }

    private void parseLogicExprList(){
        do{
            if(checkCurrentTokenType(Constants.LogicSymbol)){
                currentNode.addChild(new TreeNode("LogicSymbol", currentToken));
                next();
            }
            parseExpr();
        }while (checkCurrentTokenType(Constants.LogicSymbol));
    }
    private void parseBlock(){
        var temp = currentNode.key;
        var blockNode = new TreeNode("Block");
        currentNode.addChild(blockNode);
        currentNode = blockNode;
        // 现在指向‘{’
        parseCurrentToken();
        do{
            advance();
        }while(currentToken.tokenType != TokenClass.TK_RIGHT_CURLY_BRACE);

        currentNode = currentNode.parent;
        assert currentNode.key.equals(temp);
    }
    // 现在的Token：expr
    // 解析的内容 a>b 之类的,现在指向a
    // 发电机死了，沉痛怀念
    private void parseExpr() {
        TreeNode exprNode = new TreeNode("expr");
        currentNode.addChild(exprNode);
        currentNode = exprNode;
        while (currentToken.tokenType != TokenClass.TK_close_par && !Constants.LogicSymbol.contains(currentToken.tokenType)) {
            if (currentToken.tokenType == TokenClass.TK_open_par) {
                var expr_list_node = new TreeNode("expr_list");
                currentNode.addChild(expr_list_node);
                currentNode = expr_list_node;
                next();
                parseLogicExprList();
                currentNode = currentNode.parent;
            }
            currentNode.addChild(new TreeNode(currentToken.tokenType.toString(), currentToken));
            next();
        }
        currentNode = currentNode.parent;
    }

    // TODO:递归的括号处理

//    private void meetOpenPar(){
//        var parNode = new TreeNode("open_par", "(");
//        currentNode.addChild(parNode);
//        currentNode = parNode;
//        tokenStack.add(currentToken);
//        codeLevel += 1;
//    }


    private boolean checkCurrentTokenType(List<TokenClass> tokenClassList){
        return tokenClassList.contains(currentToken.tokenType);
    }

    private void parseStringVar(){
        assert currentToken.tokenType == TokenClass.TK_at;
        tokenStack.add(currentToken);
        next();
        if(currentToken.tokenType==TokenClass.TK_NAME){
            currentToken.tokenType = TokenClass.TK_string_var;
        }
    }
    // 冲刺！冲刺！
    public void advance(){
        next();
        parseCurrentToken();
    }

    private void next(){
        lexer.Advance();
        currentToken = lexer.currentToken;
    }
    //有必要吗？
    private void consume(TokenClass tokenClass){
        if(tokenClass == currentToken.tokenType){
            parseCurrentToken();
            next();
        }
        else throw new ParseException(codeLevel, currentToken.toString());
    }

    public JSONObject getJson(){
        return baseNode.toJSON();
    }
}

