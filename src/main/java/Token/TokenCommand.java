package Token;

import java.util.Arrays;

public class TokenCommand extends Token{
    private final String commandName;
    private final String[] features;
    public TokenCommand(String commandName, String... strings) {
        super(TokenClass.TK_COMMAND);
        this.commandName = commandName;
        this.features = strings;
        this.__tokenType = __TokenType.TK_STRINGS;
    }

    @Override
    public Object getValue() {
        return features;
    }

    public String[] getFeatures(){return features;}
    public String getCommandName(){return commandName;}

    public String toCode(){
        String string = "";
        boolean first = true;
        for(String fea : features){
            if(first){
                string+= fea;
                first = false;
            }else {
                string += ','+fea;
            }
        }
        return commandName+"("+string+")";
    }

    @Override
    public String toString() {
        return "Token(type: " + tokenType + ", commandName: " + commandName + ", feature: " + Arrays.toString(features) +")\n";
    }
}
