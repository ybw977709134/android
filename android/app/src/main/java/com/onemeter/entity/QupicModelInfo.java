package com.onemeter.entity;

/**
 * 描述：趣相册分类模板实体类
 * 作者：angelyin
 * 时间：2016/4/27 15:58
 * 备注：
 */
public class QupicModelInfo {
    private  int  id;//模板id
    private String  name;//模板名称
    private String  cover;//图片地址
    private int   usecount;//使用次数

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getUsecount() {
        return usecount;
    }

    public void setUsecount(int usecount) {
        this.usecount = usecount;
    }
}
