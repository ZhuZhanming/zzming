package top.zzming.model;

/**
 * 提示用户的信息
 */
public class AlertMessage {

    /**
     * 信息种类
     */
    private MsgKind msgKind;

    /**
     * 主题
     */
    private String strongMsg;

    /**
     * 具体信息
     */
    private String msg;
    
    public AlertMessage(MsgKind msgKind, String strongMsg, String msg) {
        this.msgKind = msgKind;
        this.strongMsg = strongMsg;
        this.msg = msg;
    }

    /**
     * 返回信息在html中对应的class属性
     */
    public String getMsgKind() {
        return msgKind.getKind();
    }

    /**
     * @param msgKind the msgKind to set
     */
    public void setMsgKind(MsgKind msgKind) {
        this.msgKind = msgKind;
    }


    /**
     * @return the strongMsg
     */
    public String getStrongMsg() {
        return strongMsg;
    }

    /**
     * @param strongMsg the strongMsg to set
     */
    public void setStrongMsg(String strongMsg) {
        this.strongMsg = strongMsg;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

}
