package cn.lankao.com.lovelankao.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by BuZhiheng on 2016/4/3.
 */
public class ChatRoom extends BmobObject{
    private String nickName;
    private String chatContent;
    private BmobFile chatUserPhoto;
    private String chatUserType;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getChatContent() {
        return chatContent;
    }

    public void setChatContent(String chatContent) {
        this.chatContent = chatContent;
    }

    public BmobFile getChatUserPhoto() {
        return chatUserPhoto;
    }

    public void setChatUserPhoto(BmobFile chatUserPhoto) {
        this.chatUserPhoto = chatUserPhoto;
    }

    public String getChatUserType() {
        return chatUserType;
    }

    public void setChatUserType(String chatUserType) {
        this.chatUserType = chatUserType;
    }
}
