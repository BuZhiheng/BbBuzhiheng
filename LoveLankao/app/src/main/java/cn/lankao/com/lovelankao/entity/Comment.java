package cn.lankao.com.lovelankao.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by BuZhiheng on 2016/4/27.
 */
public class Comment extends BmobObject{
    private String content;
    private String postId;
    private String username;

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
}
