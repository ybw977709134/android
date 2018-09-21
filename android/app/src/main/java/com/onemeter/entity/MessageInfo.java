package com.onemeter.entity;

/**
 * 描述：消息列表实体类
 * 项目名称：vishow_project
 * 作者   angelyin
 * 时间：2016/3/24 14:21
 * 备注：
 */
public class MessageInfo {
    /**
     * message对象的id
     **/
    private int id;
    /**
     * 消息的状态
     **/
    private boolean status = false;
    /**
     * 用户uid
     **/
    private int memberId;
    /**
     * 标题
     **/
    private String title;
    /**
     * 时间
     **/
    private Long createDate;
    /**
     * 类型
     **/
    private String source;
    /**内容**/
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
