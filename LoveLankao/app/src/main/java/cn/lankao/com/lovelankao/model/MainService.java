package cn.lankao.com.lovelankao.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by BuZhiheng on 2016/5/11.
 */
public class MainService extends BmobObject {
    private String title;
    private BmobFile titlePhoto;
    private String url;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BmobFile getFile() {
        return titlePhoto;
    }

    public void setFile(BmobFile file) {
        this.titlePhoto = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
