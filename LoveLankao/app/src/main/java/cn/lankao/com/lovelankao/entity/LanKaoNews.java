package cn.lankao.com.lovelankao.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by BuZhiheng on 2016/4/19.
 */
public class LanKaoNews extends BmobObject{
    private String newsImg;
    private String newsTitle;
    private String newsFrom;
    private String newsFromUrl;
    private String newsTime;

    public String getNewsImg() {
        return newsImg;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsFrom() {
        return newsFrom;
    }

    public void setNewsFrom(String newsFrom) {
        this.newsFrom = newsFrom;
    }

    public String getNewsFromUrl() {
        return newsFromUrl;
    }

    public void setNewsFromUrl(String newsFromUrl) {
        this.newsFromUrl = newsFromUrl;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }
}
