package com.onemeter.entity;

/**
 * 登录后用户的信息类
 */
public class userInfo {
    /**
     * 用户ID
     **/
    private int uid;
    /**
     * 用户头像
     **/
    private String headImage;
    /**
     * 用户性别
     **/
    private boolean sex;//(i为女，0为男)
    /**
     * 手机号码
     **/
    private String phone;
    /**
     * 邮箱
     **/
    private String email;
    /**
     * 用户昵称
     **/
    private String username;
    /**
     * 用户类型
     **/
    private String VIPName;
    /**
     * 个性描述
     **/
    private String describe;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVIPName() {
        return VIPName;
    }

    public void setVIPName(String VIPName) {
        this.VIPName = VIPName;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
