package net.hongzhang.message.bean;

import io.rong.imlib.model.Conversation;

/**
 * 作者： Administrator
 * 时间： 2016/10/25
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class IMessage {

    /**
     * UId : 5BVJ-KVQ0-GERV-2FR7
     * content : {"extra":"解散","message":"该群已被群主解散"}
     * conversationType : GROUP
     * messageDirection : RECEIVE
     * targetId : 810a148ee09943788c7084ca91dbdaf7
     * objectName : RC:InfoNtf
     * receivedStatus : {"flag":0,"isDownload":false,"isListened":false,"isMultipleReceive":false,"isRead":false,"isRetrieved":false}
     * sentStatus : SENT
     * senderUserId : 7595deacadf74b728c85d9830f2f9de3
     * receivedTime : 1477365213362
     * sentTime : 1477365201412
     * messageId : 63
     */

    private String UId;
    /**
     * extra : 解散
     * message : 该群已被群主解散
     */

    private ContentBean content;
    private  Conversation.ConversationType conversationType;
    private String messageDirection;
    private String targetId;
    private String objectName;
    /**
     * flag : 0
     * isDownload : false
     * isListened : false
     * isMultipleReceive : false
     * isRead : false
     * isRetrieved : false
     */

    private ReceivedStatusBean receivedStatus;
    private String sentStatus;
    private String senderUserId;
    private long receivedTime;
    private long sentTime;
    private int messageId;

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public Conversation.ConversationType getConversationType() {
        return conversationType;
    }

    public void setConversationType( Conversation.ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(String messageDirection) {
        this.messageDirection = messageDirection;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public ReceivedStatusBean getReceivedStatus() {
        return receivedStatus;
    }

    public void setReceivedStatus(ReceivedStatusBean receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public String getSentStatus() {
        return sentStatus;
    }

    public void setSentStatus(String sentStatus) {
        this.sentStatus = sentStatus;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public static class ContentBean {
        private String message;
        private String extra;
        public  String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ReceivedStatusBean {
        private int flag;
        private boolean isDownload;
        private boolean isListened;
        private boolean isMultipleReceive;
        private boolean isRead;
        private boolean isRetrieved;

        public int getFlag() {
            return flag;
        }

        public void setFlag(int flag) {
            this.flag = flag;
        }

        public boolean isIsDownload() {
            return isDownload;
        }

        public void setIsDownload(boolean isDownload) {
            this.isDownload = isDownload;
        }

        public boolean isIsListened() {
            return isListened;
        }

        public void setIsListened(boolean isListened) {
            this.isListened = isListened;
        }

        public boolean isIsMultipleReceive() {
            return isMultipleReceive;
        }

        public void setIsMultipleReceive(boolean isMultipleReceive) {
            this.isMultipleReceive = isMultipleReceive;
        }

        public boolean isIsRead() {
            return isRead;
        }

        public void setIsRead(boolean isRead) {
            this.isRead = isRead;
        }

        public boolean isIsRetrieved() {
            return isRetrieved;
        }

        public void setIsRetrieved(boolean isRetrieved) {
            this.isRetrieved = isRetrieved;
        }
    }
}
