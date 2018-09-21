package com.onemeter.entity;

/**
 * 微秀热榜信息实体类
 */
public class VishowInfo {

    private int  id;//活动ID
    private String name;//活动名称
    private String scanCount;//浏览量
    private Long createDate;//创建时间

    private int memberId;//
    private String type;//活动类型
    private String hdTemplate;//图片地址
    private int applyCount;//订阅数
    private String subhead;//副标题
    private String[] hdActivityPages;//活动的页数

    private int musicId;//音乐的ID
    private String musicName;//音乐名称
    private String musicURL;//音乐的地址
    private String musicCreateDate;//音乐创建时间
    private String musicIsPublic;//音乐来源
    private String musicType;//音乐类型
    private String musicMember;//
    private int  scanCountId;//比较大小时使用

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHdTemplate() {
        return hdTemplate;
    }

    public void setHdTemplate(String hdTemplate) {
        this.hdTemplate = hdTemplate;
    }

    public int getApplyCount() {
        return applyCount;
    }

    public void setApplyCount(int applyCount) {
        this.applyCount = applyCount;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String[] getHdActivityPages() {
        return hdActivityPages;
    }

    public void setHdActivityPages(String[] hdActivityPages) {
        this.hdActivityPages = hdActivityPages;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
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

    public String getMusicCreateDate() {
        return musicCreateDate;
    }

    public void setMusicCreateDate(String musicCreateDate) {
        this.musicCreateDate = musicCreateDate;
    }

    public String getMusicIsPublic() {
        return musicIsPublic;
    }

    public void setMusicIsPublic(String musicIsPublic) {
        this.musicIsPublic = musicIsPublic;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public String getMusicMember() {
        return musicMember;
    }

    public void setMusicMember(String musicMember) {
        this.musicMember = musicMember;
    }

    public int getScanCountId() {
        return scanCountId;
    }

    public void setScanCountId(int scanCountId) {
        this.scanCountId = scanCountId;
    }
}
