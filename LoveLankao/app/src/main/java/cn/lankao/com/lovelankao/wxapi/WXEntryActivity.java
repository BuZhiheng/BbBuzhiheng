package cn.lankao.com.lovelankao.wxapi;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.lankao.com.lovelankao.utils.CommonCode;
public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, CommonCode.APP_ID_WX,true);
        api.registerApp(CommonCode.APP_ID_WX);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
        case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
            break;
        case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
            break;
        default:
            break;
        }
    }
    @Override
    public void onResp(BaseResp resp) {
    String result;
    switch (resp.errCode) {
    case BaseResp.ErrCode.ERR_OK:
            result = "分享成功";
            break;
        case BaseResp.ErrCode.ERR_USER_CANCEL:
            result = "已取消";
            break;
        default:
            result = "分享失败";
            break;
            }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        this.finish();
    }
}