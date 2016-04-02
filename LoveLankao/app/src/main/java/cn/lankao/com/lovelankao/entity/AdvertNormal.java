package cn.lankao.com.lovelankao.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by BuZhiheng on 2016/3/30.
 */
public class AdvertNormal extends BmobObject implements Serializable{
    private String title;
    private String titleContent;
    private String advPhoneNumber;
    private String advContent;
    private String advPrice;
    private String advAddress;
    private String advActivity;
    private String advRemark;
    private String advNewPinglun;
    private Float advLat;
    private Float advLng;
    private Integer advClicked;
    private Integer advType;
    private Boolean avdCanTakeOut;
    private Boolean avdCanShow;

    private BmobFile advPhoto;
    private BmobFile advPhoto1;
    private BmobFile advPhoto2;
    private BmobFile advPhoto3;
    private BmobFile advPhoto4;
    private BmobFile advPhoto5;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleContent() {
        return titleContent;
    }

    public void setTitleContent(String titleContent) {
        this.titleContent = titleContent;
    }

    public String getAdvPhoneNumber() {
        return advPhoneNumber;
    }

    public void setAdvPhoneNumber(String advPhoneNumber) {
        this.advPhoneNumber = advPhoneNumber;
    }

    public String getAdvContent() {
        return advContent;
    }

    public void setAdvContent(String advContent) {
        this.advContent = advContent;
    }

    public String getAdvPrice() {
        return advPrice;
    }

    public void setAdvPrice(String advPrice) {
        this.advPrice = advPrice;
    }

    public String getAdvAddress() {
        return advAddress;
    }

    public void setAdvAddress(String advAddress) {
        this.advAddress = advAddress;
    }

    public String getAdvActivity() {
        return advActivity;
    }

    public void setAdvActivity(String advActivity) {
        this.advActivity = advActivity;
    }

    public String getAdvRemark() {
        return advRemark;
    }

    public void setAdvRemark(String advRemark) {
        this.advRemark = advRemark;
    }

    public Float getAdvLat() {
        return advLat;
    }

    public void setAdvLat(Float advLat) {
        this.advLat = advLat;
    }

    public Float getAdvLng() {
        return advLng;
    }

    public void setAdvLng(Float advLng) {
        this.advLng = advLng;
    }

    public Integer getAdvClicked() {
        return advClicked;
    }

    public void setAdvClicked(Integer advClicked) {
        this.advClicked = advClicked;
    }

    public Integer getAdvType() {
        return advType;
    }

    public void setAdvType(Integer advType) {
        this.advType = advType;
    }

    public Boolean getAvdCanTakeOut() {
        return avdCanTakeOut;
    }

    public void setAvdCanTakeOut(Boolean avdCanTakeOut) {
        this.avdCanTakeOut = avdCanTakeOut;
    }

    public Boolean getAvdCanShow() {
        return avdCanShow;
    }

    public void setAvdCanShow(Boolean avdCanShow) {
        this.avdCanShow = avdCanShow;
    }

    public BmobFile getAdvPhoto() {
        return advPhoto;
    }

    public void setAdvPhoto(BmobFile advPhoto) {
        this.advPhoto = advPhoto;
    }

    public BmobFile getAdvPhoto1() {
        return advPhoto1;
    }

    public void setAdvPhoto1(BmobFile advPhoto1) {
        this.advPhoto1 = advPhoto1;
    }

    public BmobFile getAdvPhoto2() {
        return advPhoto2;
    }

    public void setAdvPhoto2(BmobFile advPhoto2) {
        this.advPhoto2 = advPhoto2;
    }

    public BmobFile getAdvPhoto3() {
        return advPhoto3;
    }

    public void setAdvPhoto3(BmobFile advPhoto3) {
        this.advPhoto3 = advPhoto3;
    }

    public BmobFile getAdvPhoto4() {
        return advPhoto4;
    }

    public void setAdvPhoto4(BmobFile advPhoto4) {
        this.advPhoto4 = advPhoto4;
    }

    public BmobFile getAdvPhoto5() {
        return advPhoto5;
    }

    public void setAdvPhoto5(BmobFile advPhoto5) {
        this.advPhoto5 = advPhoto5;
    }

    public String getAdvNewPinglun() {
        return advNewPinglun;
    }

    public void setAdvNewPinglun(String advNewPinglun) {
        this.advNewPinglun = advNewPinglun;
    }
}
