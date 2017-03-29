package cn.lankao.com.lovelankao.model;
import cn.bmob.v3.BmobObject;
/**
 * Created by BuZhiheng on 2016/4/27.
 */
public class Comment extends BmobObject{
    private String content;
    private String postId;
    private String username;
    private String userPhotoUrl;
    private String likeUsers;
    private String lastUserName;
    private String lastUserContent;
    private Integer likeTimes;
    private String userType;
    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getLikeUsers() {
        return likeUsers;
    }

    public void setLikeUsers(String likeUsers) {
        this.likeUsers = likeUsers;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public String getLastUserContent() {
        return lastUserContent;
    }

    public void setLastUserContent(String lastUserContent) {
        this.lastUserContent = lastUserContent;
    }

    public Integer getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(Integer likeTimes) {
        this.likeTimes = likeTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
