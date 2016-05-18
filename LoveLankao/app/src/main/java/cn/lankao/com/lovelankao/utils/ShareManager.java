package cn.lankao.com.lovelankao.utils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import cn.lankao.com.lovelankao.entity.Shared;
/**
 * Created by BuZhiheng on 2016/5/17.
 */
public class ShareManager implements IUiListener {
    public static int WXTYPE_SQUARE = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
    public static int WXTYPE_CHAT = SendMessageToWX.Req.WXSceneSession;//聊天
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
        tencent = Tencent.createInstance(CommonCode.APP_ID_TENCENT, context);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "掌上兰考APP");//分享标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shared.getDesc());//分享摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shared.getUrl());//点击之后跳转的url
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shared.getImgUrl());//图片
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
        if (shared.getWxType() == WXTYPE_SQUARE){
            msg.title = "(掌上兰考)"+shared.getDesc();
        }else{
            msg.title = "掌上兰考APP";
        }
        webpage.webpageUrl = shared.getUrl();
        msg.description = shared.getDesc();
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
        msg.title = "掌上兰考笑话精选";
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
