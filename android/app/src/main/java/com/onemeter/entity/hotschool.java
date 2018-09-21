package com.onemeter.entity;

/**
 * 热门学校机构类
 */
public class hotschool{
    /**学校(机构)id**/
    private  int  schoolId;
    /**学校名称**/
    private String school;
    /**学校头像**/
    private String headImage;
    /**浏览量**/
    private int  subCount;
    /**订阅数**/
    private int  allScanCount;

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public int getAllScanCount() {
        return allScanCount;
    }

    public void setAllScanCount(int allScanCount) {
        this.allScanCount = allScanCount;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }
}
