package com.onemeter.entity;

public class Item {

    private int resIdi;
    private int resIdj;
    private String name;
    private String detail;
    private String imagesUrl;

    public int getResIdi() {
        return resIdi;
    }

    public void setResIdi(int resIdi) {
        this.resIdi = resIdi;
    }

    public int getResIdj() {
        return resIdj;
    }

    public void setResIdj(int resIdj) {
        this.resIdj = resIdj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    @Override
    public String toString() {
        return "Item [resIdi=" + resIdi + ", resIdj=" + resIdj + ", name="
                + name + ", detail=" + detail + ", imagesUrl=" + imagesUrl
                + ", getResIdi()=" + getResIdi() + ", getResIdj()="
                + getResIdj() + ", getName()=" + getName() + ", getDetail()="
                + getDetail() + ", getImagesUrl()=" + getImagesUrl() + "]";
    }

    public Item(int resIdi, int resIdj, String name, String detail,
                String imagesUrl) {
        super();
        this.resIdi = resIdi;
        this.resIdj = resIdj;
        this.name = name;
        this.detail = detail;
        this.imagesUrl = imagesUrl;
    }

}
