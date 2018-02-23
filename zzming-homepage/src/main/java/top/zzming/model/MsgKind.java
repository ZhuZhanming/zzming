package top.zzming.model;

public enum MsgKind {

    SUCCESS("alert alert-success"),INFO("alert alert-info"),WARNING("alert alert-warning"),DANGER("alert alert-danger");
    
    private String kind;
    
    private MsgKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    
}
