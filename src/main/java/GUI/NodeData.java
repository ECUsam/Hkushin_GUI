package GUI;

public class NodeData {
    private String key;
    private String value;
    private boolean displayKey;

    public NodeData(String key, String value) {
        this.key = key;
        this.value = value;
        this.displayKey = false;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
    public String getCurrentDisplay() {
        return displayKey ? key : value;
    }

    public void toggleDisplay() {
        displayKey = !displayKey;
    }
    @Override
    public String toString() {
        // 在树节点中显示的文本
        return getCurrentDisplay();
    }
}