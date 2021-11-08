package yanson.reflection;

public enum InvokerType {

    FIELD("field"),
    METHOD("method"),
    ALL("all");

    private String value;

    public String getValue() {
        return value;
    }

    InvokerType(String value) {
        this.value = value;
    }

}
