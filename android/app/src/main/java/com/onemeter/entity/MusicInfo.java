package com.onemeter.entity;

/**
 * 描述：本地音乐文件的实体类
 * 作者：angelyin
 * 时间：2016/3/28 11:06
 * 备注：
 */
public class MusicInfo {
    private int  mid;//音乐的id
    private String title;//音频名称
    private String path;//路径
    private  long  size;//文件大小
    private  long  duration;//时长
    private String type;//类型
   public MusicInfo(){
       super();
   }

    public MusicInfo(String title,String path,long size,int duration, String type){
        super();
        this.title=title;
        this.path=path;
        this.size=size;
        this.duration=duration;
        this.type=type;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
