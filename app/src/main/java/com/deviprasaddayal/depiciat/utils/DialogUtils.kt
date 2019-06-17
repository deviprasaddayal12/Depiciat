package com.deviprasaddayal.depiciat.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.adapters.FilesAdapter
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener
import com.deviprasaddayal.depiciat.listeners.OnFileSourceChoiceListener
import com.google.android.material.button.MaterialButton

import java.util.ArrayList
import java.util.Objects

/**
 * Created by Atul on 6/6/18.
 */

object DialogUtils {
    val TAG = DialogUtils::class.java.simpleName

    val OOPS_ERROR = -2
    val OOPS_NO_NET = -1
    val OOPS = 0
    val SORRY = 1
    val CHEERS = 2
    val HEY = 3
    val SURE = 4
    val LOGOUT = 5
    val UPDATE = 6

    private var infoDialog: AlertDialog? = null

    /*********************************** App Update Callbacks  */
    fun showUpdateAppDialog(context: Context, versionCodeServer: String) {
        showInfoDialog(context, UPDATE, context.getString(R.string.app_update_msg),
            R.drawable.new_info_app_update_icon,
            Runnable {
                val appPackageName = context.packageName
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (anfe: android.content.ActivityNotFoundException) {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            })
    }
    /** */

    /*********************************** App Logout Callbacks  */
    fun showLogoutDialog(context: Context, yesRunnable: Runnable) {
        var dialogMessage = "You're going to logout of this app."
        val alert = context.resources.getString(R.string.data_loss_alert)

        dialogMessage = dialogMessage + ("\n" + alert)

        showInfoDialog(context, LOGOUT, dialogMessage, R.drawable.new_info_logout_icon, yesRunnable)
    }
    /** */

    /******************************* Delete Overloaded Callbacks  */
    fun showDeleteDialog(context: Context, yesRunnable: Runnable) {
        showYouSureDialog(context, "You're going to delete this.", R.drawable.new_info_delete_icon, yesRunnable)
    }
    /** */

    /******************************** Info Overloaded Callbacks  */
    fun showHeyDialog(context: Context, message: String) {
        showInfoDialog(context, HEY, message, R.drawable.new_info_warning_icon, null)
    }

    fun showHeyDialog(context: Context, message: String, runnable: Runnable) {
        showInfoDialog(context, HEY, message, R.drawable.new_info_warning_icon, runnable)
    }
    /** */

    /****************************** Failure Overloaded Callbacks  */
    fun showFailureDialog(context: Context, message: String) {
        showInfoDialog(context, SORRY, message, R.drawable.new_info_sorry_icon, null)
    }

    fun showFailureDialog(context: Context, message: String, runnable: Runnable?) {
        showInfoDialog(context, SORRY, message, R.drawable.new_info_sorry_icon, runnable)
    }

    fun showFailureDialog(context: Context, message: String, failureDrawable: Int, runnable: Runnable?) {
        showInfoDialog(context, OOPS, message, failureDrawable, runnable)
    }

    fun showFailureDialog(context: Context, exitOnCancel: Boolean, message: String, runnable: Runnable) {
        if (exitOnCancel)
            showInfoDialog(context, false, SORRY, message, R.drawable.new_info_sorry_icon, null, runnable)
        else
            showInfoDialog(context, SORRY, message, R.drawable.new_info_sorry_icon, runnable)
    }
    /** */

    /****************************** Success Overloaded Callbacks  */
    fun showSuccessDialog(context: Context, message: String, runnable: Runnable) {
        showInfoDialog(context, CHEERS, message, R.drawable.new_info_cheers_icon, runnable)
    }

    fun showSuccessNonCancelableDialog(context: Context, message: String, runnable: Runnable) {
        showInfoDialog(context, false, CHEERS, message, R.drawable.new_info_cheers_icon, runnable, null)
    }
    /** */


    /****************************** Confirm Overloaded Callbacks  */
    fun showYouSureDialog(context: Context, infoMessage: String, infoDrawable: Int, yesRunnable: Runnable) {
        showInfoDialog(context, SURE, infoMessage, infoDrawable, yesRunnable)
    }
    /** */

    /****************************** Base Info Overloaded Callbacks  */
    private fun showInfoDialog(
        context: Context,
        infoType: Int, infoMessage: String, infoDrawable: Int,
        runnablePositive: Runnable?
    ) {
        showInfoDialog(
            context, true,
            infoType, infoMessage, infoDrawable,
            runnablePositive, null
        )
    }

    private fun showInfoDialog(
        context: Context, cancelable: Boolean,
        infoType: Int, infoMessage: String, infoDrawable: Int,
        runnablePositive: Runnable?, runnableNegative: Runnable?
    ) {
        try {
            val builder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.layout_info_dialog, null)
            builder.setView(view)

            var infoBackground = R.drawable.new_info_layer_yellow
            var infoColor = R.color.colorDialog_infoYellow

            val ivInfoIcon = view.findViewById<ImageView>(R.id.iv_info_icon)
            val ivInfoBackground = view.findViewById<ImageView>(R.id.iv_info_background)

            val tvTitle = view.findViewById<TextView>(R.id.tv_info_title)
            val tvMessage = view.findViewById<TextView>(R.id.tv_info_message)

            val btnPositive = view.findViewById<MaterialButton>(R.id.btn_positive)
            val btnNegative = view.findViewById<MaterialButton>(R.id.btn_negative)

            var positiveBtnTxt: String? = null
            var negativeBtnTxt: String? = null
            when (infoType) {
                OOPS -> {
                    tvTitle.text = "Oops!"
                    positiveBtnTxt = "Ok"
                    if (infoDrawable == R.drawable.new_info_wrong_icon) {
                        infoBackground = R.drawable.new_info_layer_wrong
                        infoColor = R.color.colorDialog_infoWrong
                    } else if (infoDrawable == R.drawable.new_info_network_icon) {
                        infoBackground = R.drawable.new_info_layer_network
                        infoColor = R.color.colorDialog_infoNetwork
                    }
                }
                SORRY -> {
                    tvTitle.text = "Sorry!"
                    positiveBtnTxt = "Ok"
                }
                CHEERS -> {
                    tvTitle.text = "Cheers!"
                    positiveBtnTxt = "Ok"
                }
                HEY -> {
                    tvTitle.text = "Hey!"
                    positiveBtnTxt = "Ok"
                }
                SURE -> {
                    tvTitle.text = "Hey! You Sure?"
                    positiveBtnTxt = "Yes, I'm"
                    negativeBtnTxt = "No, Cancel"
                    infoBackground = R.drawable.new_info_layer_delete
                    infoColor = R.color.colorDialog_infoDelete
                }
                LOGOUT -> {
                    tvTitle.text = "Hey!"
                    positiveBtnTxt = "Logout"
                    negativeBtnTxt = "Cancel"
                    infoBackground = R.drawable.new_info_layer_logout
                    infoColor = R.color.colorDialog_infoLogout
                }
                UPDATE -> {
                    tvTitle.text = "Hey! Update Available"
                    positiveBtnTxt = "Update Now"
                    negativeBtnTxt = "Continue"
                    infoBackground = R.drawable.new_info_layer_update
                    infoColor = R.color.colorDialog_infoUpdate
                }
            }

            builder.setCancelable(cancelable)
            val color = context.resources.getColor(infoColor)

            tvTitle.setTextColor(color)
            tvMessage.text = infoMessage
            ivInfoIcon.setImageResource(infoDrawable)
            ivInfoBackground.setImageResource(infoBackground)

            btnPositive.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            btnPositive.text = positiveBtnTxt
            btnPositive.setOnClickListener {
                runnablePositive?.run()

                if (infoDialog != null) {
                    infoDialog!!.cancel()
                    infoDialog = null
                }
            }
            if (negativeBtnTxt != null) {
                btnNegative.background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
                btnNegative.text = negativeBtnTxt
                btnNegative.setOnClickListener {
                    runnableNegative?.run()

                    if (infoDialog != null) {
                        infoDialog!!.cancel()
                        infoDialog = null
                    }
                }
            } else {
                ViewUtils.hideViews(btnNegative)
            }

            infoDialog = builder.create()
            infoDialog!!.show()

            resizeDialogToMatchWindow(context, infoDialog!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    /** */

    /****************************** Failure Overloaded Callbacks  */
    fun showFileSourceDialog(context: Context, sourceChoiceListener: OnFileSourceChoiceListener?) {
        val dialog = Dialog(context)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_file_source)

        dialog.findViewById<View>(R.id.view_camera).setOnClickListener {
            sourceChoiceListener?.onCameraChosen()
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.view_gallery).setOnClickListener {
            sourceChoiceListener?.onGalleryChosen()
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.view_file_manager).setOnClickListener {
            sourceChoiceListener?.onFileManagerChosen()
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.show()

        resizeDialogToMatchWindow(context, dialog)
    }

    fun showFilesDialog(
        context: Context, filePaths: ArrayList<String>, showDelete: Boolean,
        showAdd: Boolean, onFileActionListener: OnFileActionListener
    ): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Files")
        builder.setCancelable(true)

        val builderView = LayoutInflater.from(context).inflate(R.layout.row_file_recycler, null)
        builder.setView(builderView)

        val recyclerView = builderView.findViewById<RecyclerView>(R.id.recycler_files)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val filesViewAdapter = FilesAdapter(context, recyclerView, filePaths, onFileActionListener, showDelete)
        recyclerView.adapter = filesViewAdapter

        if (showAdd) {
            builder.setPositiveButton("Add File") { dialog, which -> onFileActionListener.onFileAddRequest() }
        }

        val alertDialog = builder.show()
        resizeDialogToMatchWindow(context, alertDialog)
        return alertDialog
    }

    private fun resizeDialogToMatchWindow(context: Context, dialog: Dialog) {
        val width = (context.resources.displayMetrics.widthPixels * 1.00).toInt()
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        Objects.requireNonNull(dialog.window).setLayout(width, height)
    }
}
