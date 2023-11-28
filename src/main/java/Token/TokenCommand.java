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
    public String[] getFeatures(){return features;}
    private String getCommandName(){return commandName;}

    @Override
    public String toString() {
        return "Token(type: " + tokenType + ", commandName: " + commandName + ", feature: " + Arrays.toString(features) +")\n";
    }
}
