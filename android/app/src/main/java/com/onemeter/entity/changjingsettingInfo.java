package com.onemeter.entity;

/**
 * 描述：场景设置获取相关内容
 * 作者：angelyin
 * 时间：2016/3/30 15:29
 * 备注：
 */
public class changjingsettingInfo {
    private int id;//场景ID
    private  int  memberId;//用户ID
    private String name;//场景名称
    private String scanCount;//浏览量
    private Long createDate;//创建时间
    private String type;//类型
    private String subhead;//副标题
    private  int  musicID;//音乐ID
    private String musicName;//音乐名称
    private String musicURL;//音乐路径
    private  int  applyCount;//订阅数
    private String imagpath;//图片地址
    private  String[] hdActivityPages;//场景模板数组

    public String getImagpath() {
        return imagpath;
    }

    public void setImagpath(String imagpath) {
        this.imagpath = imagpath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScanCount() {
        return scanCount;
    }

    public void setScanCount(String scanCount) {
        this.scanCount = scanCount;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public int getMusicID() {
        return musicID;
    }

    public void setMusicID(int musicID) {
        this.musicID = musicID;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

    public String[] getHdActivityPages() {
        return hdActivityPages;
    }

    public void setHdActivityPages(String[] hdActivityPages) {
        this.hdActivityPages = hdActivityPages;
    }
}
