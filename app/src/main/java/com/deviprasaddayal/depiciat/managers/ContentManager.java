package com.deviprasaddayal.depiciat.managers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.widget.ContentLoadingProgressBar;
import com.deviprasaddayal.depiciat.R;
import com.deviprasaddayal.depiciat.utils.DialogUtils;
import com.deviprasaddayal.depiciat.utils.ViewUtils;

public class ContentManager {
    public static final String TAG = ContentManager.class.getCanonicalName();

    private RelativeLayout layoutNoContent;
    private ImageView ivNoContentIconBg, ivNoContentIcon;
    private TextView tvNoContentInfo;
    private ContentLoadingProgressBar pbContentLoading;

    public ContentManager(View viewRoot) {
        layoutNoContent = viewRoot.findViewById(R.id.rl_layoutContentStatus);
        tvNoContentInfo = layoutNoContent.findViewById(R.id.tv_infoNoContent);
        ivNoContentIcon = layoutNoContent.findViewById(R.id.iv_iconNoContent);
        ivNoContentIconBg = layoutNoContent.findViewById(R.id.iv_iconNoContentBg);
        pbContentLoading = layoutNoContent.findViewById(R.id.pb_loadingContent);

        onContentLoading();
    }

    public void notifyContentChanges(boolean available) {
        if (available)
            onContentAvailable();
        else
            onContentUnavailable();
    }

    public void notifyContentChanges(boolean available, String message, int drawable) {
        if (available)
            onContentAvailable();
        else
            onContentUnavailable(message, drawable);
    }

    public void onLaunched() {
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg);
        ViewUtils.showViews(tvNoContentInfo);

        pbContentLoading.show();
        tvNoContentInfo.setText("Hang on.... while we load your content.");
    }

    public void onContentUnavailable() {
        onContentUnavailable("Oops... it seems we don't have any content to show.", R.drawable.new_info_no_data_as_bg);
    }

    public void onContentUnavailable(String message, int drawable) {
        pbContentLoading.hide();
        ViewUtils.showViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo);

        ivNoContentIcon.setImageResource(drawable);
        tvNoContentInfo.setText(message);
    }

    public void onContentAvailable() {
        pbContentLoading.hide();
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo);
    }

    public void onRefresh() {

    }

    public void onContentLoading(){
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo);
        pbContentLoading.show();
    }

    public void onLoadingFinished(){
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo);
        pbContentLoading.hide();
    }

    public void onLoadingFailed() {
        onLoadingFinished();
    }

    public void onLoadingFailed(Context context, String message) {
        onLoadingFinished();
        DialogUtils.showFailureDialog(context, message);
    }

    public void onRequestStarted() {
        onContentLoading();
    }

    public void onRequestSuccess() {
        onLoadingFinished();
    }

    public void onRequestFailed(Context context, String message) {
        onLoadingFailed(context, message);
    }
}
