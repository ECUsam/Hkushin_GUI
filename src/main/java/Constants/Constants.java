package Constants;

import Token.TokenClass;
import Token.TokenClass.*;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Constants {
    public static final int EOZ = -1;   //文件末尾
    public static final int POOL_SIZE = 10000;  //对象池大小 没用
    public static final int GUI_height = 800;
    public static final int GUI_width = 800;
    public static final int TabWidth = 80;
    public static final int TabHeight = 40;
    public static final String FontName = "Microsoft YaHei";
    public static final List<String> NodeKeyList = Arrays.asList(
            "Logic",
            "Block",
            "if",
            "rif",
            "elseif",
            "expr",
            "expr_list",
            "LogicSymbol",
            "Feature",
            "Command",
            "classType"
    );
    public static final List<String> ClassNoName = Arrays.asList(
            "context",
            "attribute",
            "sound",
            "detail"
    );
    public static final List<TokenClass> LogicSymbol = Arrays.asList(
            TokenClass.TK_and,
            TokenClass.TK_or
    );
    public static final List<TokenClass> ComparisonOperators = Arrays.asList(
            TokenClass.TK_EQUAL,
            TokenClass.TK_NOT_EQ,
            TokenClass.TK_IF_GT,
            TokenClass.TK_IF_LT,
            TokenClass.TK_IF_GT_OR_EQ,
            TokenClass.TK_IF_LT_OR_EQ
    );
    //*:已删，勿Q
    public static final List<Character> special_char = Arrays.asList(
            '{',
            '}',
            '(',
            ')',
            '=',
            ']',
            '[',
            ':',
            ' ',
            '\t',
            '\r',
            '>',
            '<',
            '&',
            '|',
            '-',
            '+',
            '\\',
            '~',
            ',',
            '\n'
    );

    public static final List<Character> special_char_without_enter = Arrays.asList(
            '{',
            '}',
            '(',
            ')',
            '=',
            ']',
            '[',
            ':',
            ' ',
            '\t',
            '>',
            '<',
            '&',
            '|',
            '-',
            '+',
            '\\',
            '~',
            ','
    );
    //类名
    public static final List<String> class_type = Arrays.asList(
            "story",
            "skill",
            "class",
            "event",
            "field",
            "object",
            "movetype",
            "power",
            "race",
            "scenario",
            "voice",
            "spot",
            "unit",
            "detail",
            "context",
            "sound",
            "attribute"
    );
    public static final List<String> complexClass = Arrays.asList(
      "story",
      "event"
    );

    //函数列表
    public static final List<String> functionTypes = Arrays.asList(
            "playBGM",
            "volume",
            "wait",
            "spotmark",
            "erase",
            "hideLink",
            "scroll",
            "select",
            "setTruce",
            "setLeague",
            "changePowerFix",
            "eraseUnit",
            "eraseUnit2",
            "playSE",
            "dialog",
            "terminate",
            "set",
            "addMoney",
            "yet"
            // 添加其他函数类型
    );
}
