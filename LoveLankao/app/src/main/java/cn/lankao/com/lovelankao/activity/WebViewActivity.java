package cn.lankao.com.lovelankao.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import cn.lankao.com.lovelankao.R;
import cn.lankao.com.lovelankao.utils.CommonCode;
import cn.lankao.com.lovelankao.widget.ProDialog;
/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class WebViewActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private TextView title;
    private WebView webView;
    private SwipeRefreshLayout refresh;
    private ProgressDialog dialog;
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
            String webUrl = intent.getStringExtra(CommonCode.INTENT_SETTING_URL);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient() {

            });//播放视频
            webView.loadUrl(webUrl);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    refresh.setRefreshing(false);
                    dialog.dismiss();
                }
            });
        }
    }

    private void initView() {
        dialog = ProDialog.getProDialog(this);
        dialog.show();
        title = (TextView) findViewById(R.id.tv_webview_title);
        webView = (WebView) findViewById(R.id.web_webview_content);
        findViewById(R.id.iv_webview_back).setOnClickListener(this);
        refresh = (SwipeRefreshLayout)findViewById(R.id.srl_web_activity);
        refresh.setOnRefreshListener(this);
        refresh.setRefreshing(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
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
    public void onRefresh() {
        initData();
    }
}
