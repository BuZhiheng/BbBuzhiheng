package cn.lankao.com.lovelankao.fragment;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.lankao.com.lovelankao.R;
/**
 * Created by BuZhiheng on 2016/4/7.
 */
public class TempFragment extends Fragment {
    private WebView webView;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_temp, container, false);
        initData();
        return view;
    }
    private void initData() {
        webView = (WebView) view.findViewById(R.id.web_webview_content);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });//播放视频
        webView.loadUrl("http://wx18.weixiaoxin.com/Tools/index");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        view.findViewById(R.id.iv_webview_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView != null && webView.canGoBack()){
                    webView.goBack();
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            webView.getClass().getMethod("onPause").invoke(webView, (Object[]) null);
            webView.removeAllViews();
            webView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}