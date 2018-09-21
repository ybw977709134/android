package com.onemeter.entity;

/**
 * 描述：图片库实体类
 * 作者：angelyin
 * 时间：2016/4/12 11:58
 * 备注：
 */
public class picturesInfo  {
   private  int  pid;//图片id
    private  String  name;//图片名称
    private  String  url;//图片路径
    private  boolean isPublic;//图片来源

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
