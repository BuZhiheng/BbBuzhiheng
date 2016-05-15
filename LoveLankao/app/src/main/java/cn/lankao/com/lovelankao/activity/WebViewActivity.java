package cn.lankao.com.lovelankao.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import cn.lankao.com.lovelankao.MainActivity;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.utils.ToastUtil;
import cn.lankao.com.lovelankao.widget.ProDialog;
/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener , IUiListener{
    private LinearLayout layout;
    private TextView title;
    private WebView webView;
    private ProgressDialog dialog;
    private Tencent tencent;
    private String webUrl;
    private String shareDesc;
    private String shareImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null){
            title.setText(intent.getStringExtra(CommonCode.INTENT_ADVERT_TITLE));
            webUrl = intent.getStringExtra(CommonCode.INTENT_SETTING_URL);
            shareDesc = intent.getStringExtra(CommonCode.INTENT_SHARED_DESC);
            shareImg = intent.getStringExtra(CommonCode.INTENT_SHARED_IMG);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient(){});//播放视频
            webView.loadUrl(webUrl);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    dialog.dismiss();
                    super.onPageFinished(view, url);
                }
            });
        }
        qqShare();
    }

    private void initView() {
        dialog = ProDialog.getProDialog(this);
        dialog.show();
        title = (TextView) findViewById(R.id.tv_webview_title);
        webView = (WebView) findViewById(R.id.web_webview_content);
        layout = (LinearLayout) findViewById(R.id.ll_webview_content);
        findViewById(R.id.iv_webview_back).setOnClickListener(this);
    }
    private void qqShare() {
        tencent = Tencent.createInstance("1105245681",this);
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "掌上兰考的分享");//分享标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareDesc);//分享摘要
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, webView.getUrl());//点击之后跳转的url
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareImg);//图片
        tencent.shareToQQ(this, params, this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
            layout.removeAllViews();
            webView.removeAllViews();
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView != null && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        tencent.onActivityResultData(requestCode,resultCode,data,this);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(Object o) {
    }
    @Override
    public void onError(UiError uiError) {
        ToastUtil.show(uiError.errorMessage);
    }
    @Override
    public void onCancel() {
    }
}
