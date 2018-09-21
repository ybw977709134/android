package com.onemeter.entity;

/**
 * 推荐订阅学校实体类
 */
public class hotdyInfo {
    /**学校名称**/
    public String school;
    /**学校头像**/
    public String headImage;
    /**学校id**/
    public int  schooId;
    /**学校宣传图**/
    public String banner;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public hotdyInfo( ) {
        super();
    }


    public hotdyInfo(int  schooId,String school, String headImage ) {
        super();
        this.schooId=schooId;
        this.school = school;
        this.headImage = headImage ;
    }

    public int getSchooId() {
        return schooId;
    }

    public void setSchooId(int schooId) {
        this.schooId = schooId;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String content) {
        this.school = content;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }
}
