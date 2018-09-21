package com.onemeter.entity;

import java.util.List;

/**
 * 描述：推荐场景详情实体类
 * 时间：2016/3/31 18:48
 * 备注：
 */
public class htjchangjingdetail {
    private  int  id;
    private String schoolName;
    private String headImage;
    private  int  subCount;
    private  int    allScanCount;
    private List<String> hdActivityName;
    private List<String> photoActivityName;
    private List<String> studyActivityName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<String> getHdActivityName() {
        return hdActivityName;
    }

    public void setHdActivityName(List<String> hdActivityName) {
        this.hdActivityName = hdActivityName;
    }

    public List<String> getPhotoActivityName() {
        return photoActivityName;
    }

    public void setPhotoActivityName(List<String> photoActivityName) {
        this.photoActivityName = photoActivityName;
    }

    public List<String> getStudyActivityName() {
        return studyActivityName;
    }

    public void setStudyActivityName(List<String> studyActivityName) {
        this.studyActivityName = studyActivityName;
    }
}
