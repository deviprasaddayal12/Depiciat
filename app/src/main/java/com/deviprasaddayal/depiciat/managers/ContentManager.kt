package com.deviprasaddayal.depiciat.managers

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.utils.DialogUtils
import com.deviprasaddayal.depiciat.utils.ViewUtils

class ContentManager(viewRoot: View) {

    private val layoutNoContent: RelativeLayout
    private val ivNoContentIconBg: ImageView
    private val ivNoContentIcon: ImageView
    private val tvNoContentInfo: TextView
    private val pbContentLoading: ContentLoadingProgressBar

    init {
        layoutNoContent = viewRoot.findViewById(R.id.rl_layoutContentStatus)
        tvNoContentInfo = layoutNoContent.findViewById(R.id.tv_infoNoContent)
        ivNoContentIcon = layoutNoContent.findViewById(R.id.iv_iconNoContent)
        ivNoContentIconBg = layoutNoContent.findViewById(R.id.iv_iconNoContentBg)
        pbContentLoading = layoutNoContent.findViewById(R.id.pb_loadingContent)

        onContentLoading()
    }

    fun notifyContentChanges(available: Boolean) {
        if (available)
            onContentAvailable()
        else
            onContentUnavailable()
    }

    fun notifyContentChanges(available: Boolean, message: String, drawable: Int) {
        if (available)
            onContentAvailable()
        else
            onContentUnavailable(message, drawable)
    }

    fun onLaunched() {
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg)
        ViewUtils.showViews(tvNoContentInfo)

        pbContentLoading.show()
        tvNoContentInfo.text = "Hang on.... while we load your content."
    }

    @JvmOverloads
    fun onContentUnavailable(
        message: String = "Oops... it seems we don't have any content to show.",
        drawable: Int = R.drawable.new_info_no_data_as_bg
    ) {
        pbContentLoading.hide()
        ViewUtils.showViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo)

        ivNoContentIcon.setImageResource(drawable)
        tvNoContentInfo.text = message
    }

    fun onContentAvailable() {
        pbContentLoading.hide()
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo)
    }

    fun onRefresh() {

    }

    fun onContentLoading() {
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo)
        pbContentLoading.show()
    }

    fun onLoadingFinished() {
        ViewUtils.hideViews(ivNoContentIcon, ivNoContentIconBg, tvNoContentInfo)
        pbContentLoading.hide()
    }

    fun onLoadingFailed() {
        onLoadingFinished()
    }

    fun onLoadingFailed(context: Context, message: String) {
        onLoadingFinished()
        DialogUtils.showFailureDialog(context, message)
    }

    fun onRequestStarted() {
        onContentLoading()
    }

    fun onRequestSuccess() {
        onLoadingFinished()
    }

    fun onRequestFailed(context: Context, message: String) {
        onLoadingFailed(context, message)
    }

    companion object {
        val TAG = ContentManager::class.java.canonicalName
    }
}
