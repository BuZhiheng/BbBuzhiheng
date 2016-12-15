package cn.lankao.com.lovelankao.utils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.model.CommonCode;
import cn.lankao.com.lovelankao.model.Shared;
/**
 * Created by BuZhiheng on 2016/5/17.
 */
public class ShareManager implements IUiListener {
    public static int SHARE_TYPE_CHAT = SendMessageToWX.Req.WXSceneSession;//聊天
    public static int SHARE_TYPE_SQUARE = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
    private static ShareManager manager;
    private AppCompatActivity context;
    private Tencent tencent;
    private IWXAPI api;
    private ShareManager(AppCompatActivity context){
        this.context = context;
    }
    public static ShareManager getInstance(AppCompatActivity context){
        if (manager == null){
            manager = new ShareManager(context);
        }
        return manager;
    }
    public void shareQQ(Shared shared){
        tencent = Tencent.createInstance(CommonCode.APP_ID_QQ, context);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shared.getTitle());//分享标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shared.getDesc());//分享摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shared.getUrl());//点击之后跳转的url
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shared.getImgUrl());//图片
        if (shared.getWxType() == SHARE_TYPE_SQUARE){
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        tencent.shareToQQ(context, params, this);
    }
    public void shareWx(Shared shared){
        api = WXAPIFactory.createWXAPI(context, CommonCode.APP_ID_WX, true);
        api.registerApp(CommonCode.APP_ID_WX);
        if (!api.isWXAppInstalled()) {
            ToastUtil.show("您还未安装微信客户端");
            return;
        }
        //点击跳转的网页
        WXWebpageObject webpage = new WXWebpageObject();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        if (shared.getWxType() == SHARE_TYPE_SQUARE){
            msg.title = shared.getTitle()+shared.getDesc();
        } else {
            msg.title = shared.getTitle();
        }
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share_bmp);
        webpage.webpageUrl = shared.getUrl();
        msg.description = shared.getDesc();
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = shared.getWxType();
        api.sendReq(req);
    }
    public void shareWxText(Shared shared){
        api = WXAPIFactory.createWXAPI(context, CommonCode.APP_ID_WX, true);
        api.registerApp(CommonCode.APP_ID_WX);
        if (!api.isWXAppInstalled()) {
            ToastUtil.show("您还未安装微信客户端");
            return;
        }
        // 初始化一个WXTextObject对象
        WXTextObject textObj = new WXTextObject();
        textObj.text = shared.getDesc();
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        // 发送文本类型的消息时，title字段不起作用
        msg.title = "title";
        msg.description = shared.getDesc();
        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
        req.message = msg;
        req.scene = shared.getWxType();
        // 调用api接口发送数据到微信
        api.sendReq(req);
    }
    private String buildTransaction(final String type) {
        return (type == null)
                ? String.valueOf(System.currentTimeMillis())
                :type + System.currentTimeMillis();
    }
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//        tencent.onActivityResultData(requestCode, resultCode, data, this);
//    }
    @Override
    public void onComplete(Object o) {
    }
    @Override
    public void onError(UiError uiError) {
    }
    @Override
    public void onCancel() {
    }
}