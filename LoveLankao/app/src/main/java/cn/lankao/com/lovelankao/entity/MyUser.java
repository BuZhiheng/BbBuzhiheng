package cn.lankao.com.lovelankao.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by BuZhiheng on 2016/4/2.
 */
public class MyUser extends BmobUser{
    private Integer point;//
    private String nickName;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
