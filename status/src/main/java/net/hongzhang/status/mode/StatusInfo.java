package net.hongzhang.status.mode;

/**
 * 作者： Administrator
 * 时间： 2016/10/21
 * 名称：
 * 版本说明：
 * 附加注释：
 * 主要接口：
 */
public class StatusInfo {
/**
 * message_id		信息ID
 ts_id	String	角色名称
 input_name	Integer	评论人或点赞人姓名
 input_url	String	评论人或点赞人头像URL
 input_content	String	评论为评论内容，点赞为“#点赞#”
 dynamic_content	Integer	动态内容，有图片时为URL，没图片时为动态内容
 dynamic_id	List<String>	动态ID
 create_time	String	时间

 */
   /**
    * 信息ID
    */
    private String message_id;
    /**
     * 角色名称
     */
    private String ts_id;
    /**
     * 评论人或点赞人姓名
     */
    private String input_name;
    /**
     * 评论人或点赞人头像URL
     */
    private String input_url;
    /**
     * 	评论为评论内容，点赞为“#点赞#”
     */
    private String input_content;
    /**
     * 动态内容，有图片时为URL，没图片时为动态内容
     */
    private String  dynamic_content;
    /**
     *动态ID
     */
    private String  dynamic_id;
    /**
     * 时间
     */
    private String create_time;
    /**
     * 消息类型
     */
    private String message_type;
    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getDynamic_content() {
        return dynamic_content;
    }

    public void setDynamic_content(String dynamic_content) {
        this.dynamic_content = dynamic_content;
    }

    public String getInput_content() {
        return input_content;
    }

    public void setInput_content(String input_content) {
        this.input_content = input_content;
    }

    public String getInput_url() {
        return input_url;
    }

    public void setInput_url(String input_url) {
        this.input_url = input_url;
    }

    public String getTs_id() {
        return ts_id;
    }

    public void setTs_id(String ts_id) {
        this.ts_id = ts_id;
    }
    public String getInput_name() {
        return input_name;
    }

    public void setInput_name(String input_name) {
        this.input_name = input_name;
    }

    public String getMessage_type() {
        return message_type;
    }
    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }
}
