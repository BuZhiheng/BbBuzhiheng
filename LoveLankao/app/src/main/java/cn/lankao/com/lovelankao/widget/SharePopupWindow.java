package cn.lankao.com.lovelankao.widget;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.lankao.com.lovelankao.R;
/**
 * Created by BuZhiheng on 2016/5/16.
 */
public class SharePopupWindow extends PopupWindow {
    private AppCompatActivity context;
    private View view;
    private LinearLayout llQQ,llQZone,llWx,llWxsquare;
    private TextView tvCancel;
    public SharePopupWindow(AppCompatActivity context, View.OnClickListener listener){
        super(context);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_popwin_share,null);
        tvCancel = (TextView) view.findViewById(R.id.tv_popwinshare_cancel);
        llQQ = (LinearLayout) view.findViewById(R.id.ll_popwinshare_qq);
        llQZone = (LinearLayout) view.findViewById(R.id.ll_popwinshare_qzone);
        llWx = (LinearLayout) view.findViewById(R.id.ll_popwinshare_wx);
        llWxsquare = (LinearLayout) view.findViewById(R.id.ll_popwinshare_wxsquare);
        tvCancel.setOnClickListener(listener);
        llQQ.setOnClickListener(listener);
        llQZone.setOnClickListener(listener);
        llWx.setOnClickListener(listener);
        llWxsquare.setOnClickListener(listener);
        //设置SelectPicPopupWindow的View
        setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        setHeight(500);
        //设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        setBackgroundDrawable(cd);
        setAnimationStyle(R.style.PopwinTheme);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.ll_popwinshare_content).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 0.4f;
        context.getWindow().setAttributes(lp);
    }
    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1f;
        context.getWindow().setAttributes(lp);
    }
}