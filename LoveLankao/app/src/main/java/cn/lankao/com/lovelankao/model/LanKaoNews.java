package cn.lankao.com.lovelankao.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by BuZhiheng on 2016/4/19.
 */
public class LanKaoNews extends BmobObject{

    private String newsImg;

    private BmobFile newsPhoto;

    private String newsTitle;
    private String newsContent;
    private String newsFrom;
    private String newsFromUrl;
    private String newsTime;
    private String newsType;
    public String getNewsImg() {
        return newsImg;
    }

    public void setNewsImg(String newsImg) {
        this.newsImg = newsImg;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public BmobFile getNewsPhoto() {
        return newsPhoto;
    }

    public void setNewsPhoto(BmobFile newsPhoto) {
        this.newsPhoto = newsPhoto;
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

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }
}
