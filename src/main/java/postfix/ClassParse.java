package postfix;

import Constants.Constants;
import Token.TokenClass;
import Token.Token;
import Token.ClassType;
import OPcode.*;
import org.json.JSONObject;

import java.nio.file.Path;
import java.util.*;

// TODO:用报错信息重写assert
// 原来生产环境不执行assert
@SuppressWarnings("unused")
public class ClassParse {
    private ClassLexer lexer;
    private Token currentToken;
    private Token pri;
    private String source;
    public Pcode currentOPcode;
    private ClassType classType;
    public TreeNode baseNode;
    private TreeNode currentNode;
    private Stack<Token> tokenStack = new Stack<>();
    private int codeLevel;
    public String path;
    public int baseCodeLine;

    public ClassParse(String source){
        lexer = new ClassLexer(source);
        lexer.get_ClassParse(this);
        this.source = source;
        codeLevel = 0;
        path = "";
    }
    public ClassParse(String source, String path){
        lexer = new ClassLexer(source);
        lexer.get_ClassParse(this);
        this.source = source;
        codeLevel = 0;
        this.path = path;
    }

    public ClassParse(String source, String path, int baseCodeLine){
        lexer = new ClassLexer(source);
        lexer.get_ClassParse(this);
        this.source = source;
        codeLevel = 0;
        this.path = path;
        this.baseCodeLine = baseCodeLine;
    }


    public void updateSource(String source){
        lexer.updateSource(source);
        this.source = source;
        codeLevel = 0;
        tokenStack = new Stack<>();
        baseNode = null;
    }

    public void updateSource(String source, String path){
        lexer.updateSource(source);
        this.source = source;
        codeLevel = 0;
        tokenStack = new Stack<>();
        baseNode = null;
        this.path = path;
    }

    public void updateSource(String source, String path, int baseCodeLine){
        lexer.updateSource(source);
        this.source = source;
        codeLevel = 0;
        tokenStack = new Stack<>();
        baseNode = null;
        this.path = path;
        this.baseCodeLine = baseCodeLine;
    }

    public void run(){
        do{
            advance();
        }while (currentToken.tokenType != TokenClass.TK_EOF);
        DataManger.killOrphans();
    }
    //我不是要写递归下降吗？我在干什么？
    // TODO 写报错
    private void parseCurrentToken(){
        //do{
            switch (currentToken.tokenType){
                // TODO：统一节点的key值
                case TK_classname:
                    if(currentToken.string.equals("class"))classType = ClassType.valueOf("class_unit");
                    else classType = ClassType.valueOf(currentToken.string);
                    var tempClassType = currentToken.string;
                    baseNode = new TreeNode("classType" ,classType.toString());
                    currentNode = baseNode;
                    next();
                    if(currentToken.tokenType == TokenClass.TK_NAME){
                        var classname = new TreeNode("className", currentToken.string);
                        DataManger.dataMap.put(currentToken.string, baseNode);
                        List<String> data = new ArrayList<>();
                        data.add(path);
                        data.add((String) baseNode.value);
                        data.add(""+lexer.codeLine);
                        data.add(null);
                        DataManger.putValue(currentToken.string, data);
                        var tempParentName = currentToken.string;
                        baseNode.addChild(classname);
                        next();
                        if(currentToken.tokenType == TokenClass.TK_colon){
                            next();
                            assert currentToken.tokenType == TokenClass.TK_NAME;
                            data.set(3, currentToken.string);
                            if(DataManger.searchClass(currentToken.string))DataManger.classGetChildren(tempParentName, currentToken.string);
                            else DataManger.ThereIsOrphanFound(tempParentName, currentToken.string);
                            classname.getFather(currentToken.string);
                            next();
                        }
                    }else if(currentToken.tokenType == TokenClass.TK_LEFT_CURLY_BRACE){
                        List<String> data = new ArrayList<>();
                        data.add(path);
                        data.add("all");
                        data.add(""+lexer.codeLine);
                        data.add(null);
                        if(DataManger.checkExist(tempClassType)){
                            Path path1 = Path.of(path);
                            baseNode = DataManger.dataMap.get(tempClassType);
                            tempClassType += ( "_"+path1.getFileName() );
                        }
                        DataManger.putValue(tempClassType, data);
                        DataManger.dataMap.put(tempClassType, baseNode);
                    }
                    assert currentToken.tokenType == TokenClass.TK_LEFT_CURLY_BRACE;
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
                    assert tokenStack.peek().tokenType == TokenClass.TK_LEFT_CURLY_BRACE;
                    codeLevel -= 1;
                    tokenStack.pop();
                    break;
                case TK_close_par:
                    assert tokenStack.peek().tokenType == TokenClass.TK_close_par;
                    codeLevel -= 1;
                    tokenStack.pop();
                    // SUS!!!!
                    // currentNode = currentNode.parent;
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
                    TreeNode if_node = new TreeNode(currentToken.string, currentToken.string);
                    currentNode.addChild(if_node);
                    currentNode = if_node;
                    next();
                    parseLogic();
                    //现在指向‘)’
                    next();
                    //指向‘}’
                    parseBlock();
                    currentNode = currentNode.parent;
                    advance();
                    break;
                case TK_ELSE:
                    //if -> block
//                    assert Objects.equals(currentNode.parent.key, "if")|| Objects.equals(currentNode.parent.key, "rif");
//                    currentNode = currentNode.parent.parent;
                    next();
                    if(currentToken.tokenType == TokenClass.TK_IF || currentToken.tokenType == TokenClass.TK_RIF){
                        TreeNode else_if_node = new TreeNode("elseif", "elseif");
                        currentNode.addChild(else_if_node);
                        currentNode = else_if_node;
                        next();
                        parseLogic();
                        next();
                        parseBlock();
                        currentNode = currentNode.parent;
                        advance();
                    }
                    else if(currentToken.tokenType == TokenClass.TK_LEFT_CURLY_BRACE){
                        TreeNode else_node = new TreeNode("else", "else");
                        currentNode.addChild(else_node);
                        currentNode = else_node;
                        parseBlock();
                        currentNode = currentNode.parent;
                        advance();
                    }else throw new ParseException(lexer.codeLine, currentToken.toString());
                    break;
                case TK_WHILE:
                    TreeNode while_node = new TreeNode(currentToken.string, currentToken.string);
                    currentNode.addChild(while_node);
                    currentNode = while_node;
                    next();
                    parseLogic();
                    next();
                    parseBlock();
                    currentNode = currentNode.parent;
                    advance();
                    break;
                case TK_explain_all:case TK_explain_line:
                    currentNode.addChild(new TreeNode(currentToken.tokenType.toString(), currentToken.string));
                    break;
            }
        //}while (currentToken.tokenType != TokenClass.TK_EOF);
    }
    public static TreeNode craft2Nodes(TreeNode node1, TreeNode node2){
        if(Objects.equals(node1.key, node2.key)&&Objects.equals(node1.value, node2.value)){
            for(TreeNode node:node2.getChildren()){
                node1.addChild(node);
            }
            return node1;
        }else return node2;
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
    // 大量}脱出
    private void parseBlock(){
        int temp_level = tokenStack.toArray().length;
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
//                var expr_list_node = new TreeNode("expr_list");
//                currentNode.addChild(expr_list_node);
//                currentNode = expr_list_node;
//                next();
//                parseLogicExprList();
//                currentNode = currentNode.parent;
                parseCurrentToken();
                next();
                parseExpr();
            }
            if(currentToken.tokenType == TokenClass.TK_at)
                parseStringVar();
            if(currentToken.tokenType != TokenClass.TK_close_par)
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
        next();
        if(currentToken.tokenType==TokenClass.TK_NAME){
            currentToken.tokenType = TokenClass.TK_string_var;
            currentToken.string = "@" + currentToken.string;
        }else{
            pri = currentToken;
            currentToken = new Token(TokenClass.TK_at);
        }
    }
    // 冲刺！冲刺！
    public void advance(){
        next();
        parseCurrentToken();
//        System.out.print(currentToken.toCode()+"    ");
//        currentNode.getAllParents();
//        System.out.print("\n");
    }

    private void next(){
        if(pri != null){
            currentToken = pri;
            pri = null;
            return;
        }
        lexer.Advance();
        currentToken = lexer.currentToken;
        //System.out.print(currentToken);
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
    // 还是递归好写
    // 深度优先
    public String toCode(){
        Tree2CodeTransformer transformer = new Tree2CodeTransformer();
        TreeNode tmp = baseNode;
        return transformer.Node2Code(tmp);
    }


//    public String toCode(){
//        int level = 0;
//        Stack<String> parStack = new Stack<>();
//
//        StringBuilder res = new StringBuilder();
//        TreeNode temp = baseNode;
//        return Node2Code(temp, res);
//    }

    public String Node2Code(TreeNode node, StringBuilder res){
        res.append(node.Tree2Code());
        for(TreeNode child: node.getChildren()){
            Node2Code(child, res);
        }
        return res.toString();
    }
}
// 超级费时 我也不知道为什么
@SuppressWarnings("unused")
class Tree2CodeTransformer{
    private int level;
    private StringBuilder res;
    private Stack<String> parStack;
    private boolean explainNeed = true;
    // 我在干什么？
    private final List<String> spaceNeededKey = Arrays.asList(
            "classType",
            "className",
            "if",
            "rif",
            "while",
            "elseif",
            "else"
    );
    private void setExplainNeed(boolean need){
        explainNeed = need;
    }
    private final List<String> enterNeededKey = Arrays.asList(
            "Command", "Feature", "Logic", "className"
    );
    private final List<String> virtualKey = Arrays.asList(
            "expr", "Logic"
    );
    private final List<String> logicKey = Arrays.asList(
            "if", "rif", "while", "elseif", "else"
    );
    private boolean curlyNeeded;

    public Tree2CodeTransformer() {
        level = 0;
        res = new StringBuilder();
        parStack = new Stack<>();
        curlyNeeded = false;
    }

    public Tree2CodeTransformer(boolean curlyNeeded){
        level = 0;
        res = new StringBuilder();
        parStack = new Stack<>();
        this.curlyNeeded = curlyNeeded;
    }

    public void init(){
        level = 0;
        res = new StringBuilder();
        parStack = new Stack<>();
        curlyNeeded = false;
    }

    public void init(boolean curlyNeeded){
        level = 0;
        res = new StringBuilder();
        parStack = new Stack<>();
        this.curlyNeeded = curlyNeeded;
    }

    public String Node2Code(TreeNode node){
        if(!explainNeed&&(Objects.equals(node.key, "TK_explain_all")||Objects.equals(node.key, "TK_explain_line")))return "";
        if(Objects.equals(node.key, "Block")){
            for (int i = 0; i < level+1; i++) res.append("  ");
            res.append("{\n");
        }
        if(Objects.equals(node.key, "Logic"))res.append("(");
        if(node.Tree2Code()!=null) {
            if (node.parent!=null && !virtualKey.contains(node.parent.key) && !Objects.equals(node.key, "className") && !Objects.equals(node.key, "TK_explain_all"))
                for (int i = 0; i < level; i++) res.append("  ");
            res.append(node.Tree2Code());
            if (spaceNeededKey.contains(node.key)) res.append(" ");
            if(Objects.equals(node.key, "else"))res.append("\n");
        }

        if(Objects.equals(node.key, "className")){
            if(node.hasFather()){
                res.append(": ").append(node.takeFather());
            }
            res.append("\n{");
        }
        if(Objects.equals(node.key, "classType") && !node.checkHasClassName()){
            res.append("\n{\n");
        }
        if(node.hasChild()){
            level +=1;
            for(TreeNode child : node.getChildren()){
                Node2Code(child);
            }
            level -= 1;
        }
        if(Objects.equals(node.key, "TK_explain_all")||Objects.equals(node.key, "TK_explain_line"))res.append("\n");
        if(Objects.equals(node.key, "Logic"))res.append(")");
        if (enterNeededKey.contains(node.key)) {
            res.append("\n");
        }
        if(Objects.equals(node.key, "Block")){
            for (int i = 0; i < level+1; i++) res.append("  ");
            res.append("}\n");
        }
        if(Objects.equals(node.key, "classType"))
            res.append("}");
        return res.toString();
    }
}