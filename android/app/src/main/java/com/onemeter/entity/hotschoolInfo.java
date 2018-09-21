package com.onemeter.entity;

/**
 * 订阅学校详情实体类
 */
public class hotschoolInfo {
    /**
     * 学校ID
     **/
    private int schoolId;
    /**
     * 学校的名称
     **/
    private String schoolName;
    /**
     * 学校头像
     **/
    private String headImage;
    /**
     * 订阅数量
     **/
    private int subCount;
    /**
     * 浏览量
     **/
    private int allScanCount;
    /**
     * 所有趣活动名称
     **/
    private String[] hdActivityName;
    /**
     * 所有趣相册名称
     **/
    private String[] photoActivityName;
    /**
     * 所有趣学习名称
     **/
    private String[] studyActivityName;

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

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

    public String[] getHdActivityName() {
        return hdActivityName;
    }

    public void setHdActivityName(String[] hdActivityName) {
        this.hdActivityName = hdActivityName;
    }

    public String[] getPhotoActivityName() {
        return photoActivityName;
    }

    public void setPhotoActivityName(String[] photoActivityName) {
        this.photoActivityName = photoActivityName;
    }

    public String[] getStudyActivityName() {
        return studyActivityName;
    }

    public void setStudyActivityName(String[] studyActivityName) {
        this.studyActivityName = studyActivityName;
    }
}
