package cn.lankao.com.lovelankao.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class Setting extends BmobObject{
    private Integer setType;
    private String setPartnerUrl;
    private String setAboutusUrl;
    private String setJCLKUrl;

    public String getSetJCLKUrl() {
        return setJCLKUrl;
    }

    public void setSetJCLKUrl(String setJCLKUrl) {
        this.setJCLKUrl = setJCLKUrl;
    }

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
