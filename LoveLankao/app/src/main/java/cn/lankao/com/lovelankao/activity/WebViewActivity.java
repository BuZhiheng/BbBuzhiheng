package cn.lankao.com.lovelankao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.utils.CommonCode;

/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        init();
    }
    private void init() {
        title = (TextView) findViewById(R.id.tv_webview_title);
        webView = (WebView) findViewById(R.id.web_webview_content);
        Intent intent = getIntent();
        if (intent != null){
            title.setText(intent.getStringExtra(CommonCode.INTENT_ADVERT_TITLE));
            String webUrl = intent.getStringExtra(CommonCode.INTENT_SETTING_URL);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(webUrl);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
        findViewById(R.id.iv_webview_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
