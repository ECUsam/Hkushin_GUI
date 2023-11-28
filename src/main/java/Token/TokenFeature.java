package Token;

import java.util.Arrays;

public class TokenFeature extends Token {

    public String[] strings_feature;
    public String FeatureName;

    public TokenFeature(String word, int num_feature) {
        super(TokenClass.Tk_feature);
        FeatureName = word;
        this.value = num_feature;
        this.__tokenType = __TokenType.TK_INT;
    }

    public TokenFeature(String word, String... strings) {
        super(TokenClass.Tk_feature);
        FeatureName = word;
        strings_feature = strings;
        this.__tokenType = __TokenType.TK_STRINGS;
    }

    public TokenFeature(String word) {
        super(TokenClass.Tk_feature);
        FeatureName = word;
    }

    @Override
    public String toString() {
        return switch (__tokenType) {
            case TK_INT ->  "Token(type: " + tokenType + ", FeaName: " + FeatureName + ", feature: " + value + ")\n";
            case TK_STRINGS ->  "Token(type: " + tokenType + ", FeaName: " + FeatureName + ", feature: " + Arrays.toString(strings_feature) + ")\n";
            default -> "Token(type: " + tokenType + ", FeaName: " + FeatureName +")\n";
        };
    }
}