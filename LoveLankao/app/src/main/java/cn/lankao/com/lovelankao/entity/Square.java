package cn.lankao.com.lovelankao.entity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class Square extends BmobObject{
    private String nickName;
    private String squareContent;
    private BmobFile squarePhoto;

    public BmobFile getSquarePhoto() {
        return squarePhoto;
    }

    public void setSquarePhoto(BmobFile squarePhoto) {
        this.squarePhoto = squarePhoto;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSquareContent() {
        return squareContent;
    }

    public void setSquareContent(String squareContent) {
        this.squareContent = squareContent;
    }
}
