package cn.lankao.com.lovelankao.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class Setting extends BmobObject{
    private Integer setType;
    private String setPartnerUrl;
    private String setAboutusUrl;

    public Integer getSetType() {
        return setType;
    }

    public void setSetType(Integer setType) {
        this.setType = setType;
    }

    public String getSetPartnerUrl() {
        return setPartnerUrl;
    }

    public void setSetPartnerUrl(String setPartnerUrl) {
        this.setPartnerUrl = setPartnerUrl;
    }

    public String getSetAboutusUrl() {
        return setAboutusUrl;
    }

    public void setSetAboutusUrl(String setAboutusUrl) {
        this.setAboutusUrl = setAboutusUrl;
    }
}
