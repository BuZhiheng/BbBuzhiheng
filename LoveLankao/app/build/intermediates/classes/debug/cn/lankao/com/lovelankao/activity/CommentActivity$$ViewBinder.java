// Generated code from Butter Knife. Do not modify!
package cn.lankao.com.lovelankao.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommentActivity$$ViewBinder<T extends cn.lankao.com.lovelankao.activity.CommentActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427469, "field 'etContent'");
    target.etContent = finder.castView(view, 2131427469, "field 'etContent'");
    view = finder.findRequiredView(source, 2131427470, "field 'tvLast'");
    target.tvLast = finder.castView(view, 2131427470, "field 'tvLast'");
  }

  @Override public void unbind(T target) {
    target.etContent = null;
    target.tvLast = null;
  }
}
