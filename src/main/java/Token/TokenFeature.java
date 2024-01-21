package Token;

import java.util.Arrays;
import java.util.Objects;

public class TokenFeature extends Token {

    public String[] strings_feature;
    public String FeatureName;

    // 数字？什么数字？
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
        if(strings.length==1){
            this.string = strings[0];
        }
    }

    public Object getValue(){
        return strings_feature;
    }

    public TokenFeature(String word) {
        super(TokenClass.Tk_feature);
        FeatureName = word;
    }

    public String toCode(){
        StringBuilder string = new StringBuilder();
        boolean first = true;
        if(strings_feature.length==0) string = new StringBuilder("@");
        else {
            for (String fea : strings_feature){
                if(first){
                    string.append(fea);
                    first = false;
                }
                else {
                    string.append(",").append(fea);
                }
            }
        }
        return FeatureName + " = " +string;
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