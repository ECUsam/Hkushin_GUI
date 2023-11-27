package Constants;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Constants {
    public static final int EOZ = -1;   //文件末尾
    public static final int POOL_SIZE = 10000;  //对象池大小

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
            "unit"
    );

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
