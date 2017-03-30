package cn.lankao.com.lovelankao.model;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
/**
 * Created by BuZhiheng on 2016/4/4.
 */
public class Square extends BmobObject {
    private String nickName;
    private String userId;
    private String userPhoto;
    private String squareTitle;
    private String squareContent;
    private BmobFile squarePhoto1;
    private BmobFile squarePhoto2;
    private BmobFile squarePhoto3;
    private BmobFile squarePhoto4;
    private BmobFile squarePhoto5;
    private BmobFile squarePhoto6;
    private String squareUserType;
    private String likeUsers;
    private Integer likeTimes;
    private Integer commentTimes;
    private Integer clickTimes;
    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getLikeUsers() {
        return likeUsers;
    }

    public void setLikeUsers(String likeUsers) {
        this.likeUsers = likeUsers;
    }

    public Integer getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(Integer likeTimes) {
        this.likeTimes = likeTimes;
    }

    public Integer getClickTimes() {
        return clickTimes;
    }

    public void setClickTimes(Integer clickTimes) {
        this.clickTimes = clickTimes;
    }

    public String getSquareTitle() {
        return squareTitle;
    }

    public void setSquareTitle(String squareTitle) {
        this.squareTitle = squareTitle;
    }

    public BmobFile getSquarePhoto1() {
        return squarePhoto1;
    }

    public void setSquarePhoto1(BmobFile squarePhoto1) {
        this.squarePhoto1 = squarePhoto1;
    }

    public BmobFile getSquarePhoto2() {
        return squarePhoto2;
    }

    public void setSquarePhoto2(BmobFile squarePhoto2) {
        this.squarePhoto2 = squarePhoto2;
    }

    public BmobFile getSquarePhoto3() {
        return squarePhoto3;
    }

    public void setSquarePhoto3(BmobFile squarePhoto3) {
        this.squarePhoto3 = squarePhoto3;
    }

    public BmobFile getSquarePhoto4() {
        return squarePhoto4;
    }

    public void setSquarePhoto4(BmobFile squarePhoto4) {
        this.squarePhoto4 = squarePhoto4;
    }

    public BmobFile getSquarePhoto5() {
        return squarePhoto5;
    }

    public void setSquarePhoto5(BmobFile squarePhoto5) {
        this.squarePhoto5 = squarePhoto5;
    }

    public String getSquareUserType() {
        return squareUserType;
    }

    public void setSquareUserType(String squareUserType) {
        this.squareUserType = squareUserType;
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

    public BmobFile getSquarePhoto6() {
        return squarePhoto6;
    }

    public void setSquarePhoto6(BmobFile squarePhoto6) {
        this.squarePhoto6 = squarePhoto6;
    }

    public Integer getCommentTimes() {
        return commentTimes;
    }

    public void setCommentTimes(Integer commentTimes) {
        this.commentTimes = commentTimes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
