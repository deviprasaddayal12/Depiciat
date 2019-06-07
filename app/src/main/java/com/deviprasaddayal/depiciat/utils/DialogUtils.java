package com.deviprasaddayal.depiciat.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.deviprasaddayal.depiciat.R;
import com.deviprasaddayal.depiciat.adapters.FilesAdapter;
import com.deviprasaddayal.depiciat.listeners.OnFileActionListener;
import com.deviprasaddayal.depiciat.listeners.OnFileSourceChoiceListener;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Atul on 6/6/18.
 */

public class DialogUtils {
    public static final String TAG = DialogUtils.class.getSimpleName();

    private static AlertDialog infoDialog;

    public interface Type {
        int OOPS_ERROR = -2;
        int OOPS_NO_NET = -1;
        int OOPS = 0;
        int SORRY = 1;
        int CHEERS = 2;
        int HEY = 3;
        int SURE = 4;
        int LOGOUT = 5;
        int UPDATE = 6;
    }

    /*********************************** App Update Callbacks *************************************/
    public static void showUpdateAppDialog(final Context context, String versionCodeServer) {
        showInfoDialog(context,
                Type.UPDATE, context.getString(R.string.app_update_msg), R.drawable.new_info_app_update_icon,
                new Runnable() {
                    @Override
                    public void run() {
                        final String appPackageName = context.getPackageName();
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
    }
    /**********************************************************************************************/

    /*********************************** App Logout Callbacks *************************************/
    public static void showLogoutDialog(Context context, final Runnable yesRunnable) {
        String dialogMessage = "You're going to logout of this app.";
        String alert = context.getResources().getString(R.string.data_loss_alert);

        dialogMessage = dialogMessage + ("\n" + alert);

        showInfoDialog(context, Type.LOGOUT, dialogMessage, R.drawable.new_info_logout_icon, yesRunnable);
    }
    /**********************************************************************************************/

    /******************************* Delete Overloaded Callbacks **********************************/
    public static void showDeleteDialog(Context context, final Runnable yesRunnable) {
        showYouSureDialog(context, "You're going to delete this.", R.drawable.new_info_delete_icon, yesRunnable);
    }
    /**********************************************************************************************/

    /******************************** Info Overloaded Callbacks ***********************************/
    public static void showHeyDialog(Context context, String message) {
        showInfoDialog(context, Type.HEY, message, R.drawable.new_info_warning_icon, null);
    }

    public static void showHeyDialog(Context context, String message, final Runnable runnable) {
        showInfoDialog(context, Type.HEY, message, R.drawable.new_info_warning_icon, runnable);
    }
    /**********************************************************************************************/

    /****************************** Failure Overloaded Callbacks **********************************/
    public static void showFailureDialog(Context context, String message) {
        showInfoDialog(context, Type.SORRY, message, R.drawable.new_info_sorry_icon, null);
    }

    public static void showFailureDialog(Context context, String message, Runnable runnable) {
        showInfoDialog(context, Type.SORRY, message, R.drawable.new_info_sorry_icon, runnable);
    }

    public static void showFailureDialog(Context context, String message, int failureDrawable, Runnable runnable) {
        showInfoDialog(context, Type.OOPS, message, failureDrawable, runnable);
    }

    public static void showFailureDialog(Context context, boolean exitOnCancel, String message, Runnable runnable) {
        if (exitOnCancel)
            showInfoDialog(context, false, Type.SORRY, message, R.drawable.new_info_sorry_icon,
                    null, runnable);
        else
            showInfoDialog(context, Type.SORRY, message, R.drawable.new_info_sorry_icon, runnable);
    }
    /**********************************************************************************************/

    /****************************** Success Overloaded Callbacks **********************************/
    public static void showSuccessDialog(Context context, String message, Runnable runnable) {
        showInfoDialog(context, Type.CHEERS, message, R.drawable.new_info_cheers_icon, runnable);
    }

    public static void showSuccessNonCancelableDialog(Context context, String message, Runnable runnable) {
        showInfoDialog(context, false, Type.CHEERS, message, R.drawable.new_info_cheers_icon, runnable, null);
    }
    /**********************************************************************************************/


    /****************************** Confirm Overloaded Callbacks **********************************/
    public static void showYouSureDialog(Context context, String infoMessage, int infoDrawable, final Runnable yesRunnable) {
        showInfoDialog(context, Type.SURE, infoMessage, infoDrawable, yesRunnable);
    }
    /**********************************************************************************************/

    /****************************** Base Info Overloaded Callbacks ********************************/
    private static void showInfoDialog(Context context,
                                       int infoType, String infoMessage, int infoDrawable,
                                       final Runnable runnablePositive) {
        showInfoDialog(context, true,
                infoType, infoMessage, infoDrawable,
                runnablePositive, null);
    }

    private static void showInfoDialog(final Context context, boolean cancelable,
                                       int infoType, String infoMessage, int infoDrawable,
                                       final Runnable runnablePositive, final Runnable runnableNegative) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.layout_info_dialog, null);
            builder.setView(view);

            int infoBackground = R.drawable.new_info_layer_yellow;
            int infoColor = R.color.colorDialog_infoYellow;

            ImageView ivInfoIcon = view.findViewById(R.id.iv_info_icon);
            ImageView ivInfoBackground = view.findViewById(R.id.iv_info_background);

            TextView tvTitle = view.findViewById(R.id.tv_info_title);
            TextView tvMessage = view.findViewById(R.id.tv_info_message);

            MaterialButton btnPositive = view.findViewById(R.id.btn_positive);
            MaterialButton btnNegative = view.findViewById(R.id.btn_negative);

            String positiveBtnTxt = null, negativeBtnTxt = null;
            switch (infoType) {
                case Type.OOPS: {
                    tvTitle.setText("Oops!");
                    positiveBtnTxt = "Ok";
                    if (infoDrawable == R.drawable.new_info_wrong_icon) {
                        infoBackground = R.drawable.new_info_layer_wrong;
                        infoColor = R.color.colorDialog_infoWrong;
                    } else if (infoDrawable == R.drawable.new_info_network_icon) {
                        infoBackground = R.drawable.new_info_layer_network;
                        infoColor = R.color.colorDialog_infoNetwork;
                    }
                }
                break;
                case Type.SORRY: {
                    tvTitle.setText("Sorry!");
                    positiveBtnTxt = "Ok";
                }
                break;
                case Type.CHEERS: {
                    tvTitle.setText("Cheers!");
                    positiveBtnTxt = "Ok";
                }
                break;
                case Type.HEY: {
                    tvTitle.setText("Hey!");
                    positiveBtnTxt = "Ok";
                }
                break;
                case Type.SURE: {
                    tvTitle.setText("Hey! You Sure?");
                    positiveBtnTxt = "Yes, I'm";
                    negativeBtnTxt = "No, Cancel";
                    infoBackground = R.drawable.new_info_layer_delete;
                    infoColor = R.color.colorDialog_infoDelete;
                }
                break;
                case Type.LOGOUT: {
                    tvTitle.setText("Hey!");
                    positiveBtnTxt = "Logout";
                    negativeBtnTxt = "Cancel";
                    infoBackground = R.drawable.new_info_layer_logout;
                    infoColor = R.color.colorDialog_infoLogout;
                }
                break;
                case Type.UPDATE: {
                    tvTitle.setText("Hey! Update Available");
                    positiveBtnTxt = "Update Now";
                    negativeBtnTxt = "Continue";
                    infoBackground = R.drawable.new_info_layer_update;
                    infoColor = R.color.colorDialog_infoUpdate;
                }
                break;
            }

            builder.setCancelable(cancelable);
            int color = context.getResources().getColor(infoColor);

            tvTitle.setTextColor(color);
            tvMessage.setText(infoMessage);
            ivInfoIcon.setImageResource(infoDrawable);
            ivInfoBackground.setImageResource(infoBackground);

            btnPositive.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            btnPositive.setText(positiveBtnTxt);
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (runnablePositive != null)
                        runnablePositive.run();

                    if (infoDialog != null) {
                        infoDialog.cancel();
                        infoDialog = null;
                    }
                }
            });
            if (negativeBtnTxt != null) {
                btnNegative.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                btnNegative.setText(negativeBtnTxt);
                btnNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (runnableNegative != null)
                            runnableNegative.run();

                        if (infoDialog != null) {
                            infoDialog.cancel();
                            infoDialog = null;
                        }
                    }
                });
            } else {
                ViewUtils.hideViews(btnNegative);
            }

            infoDialog = builder.create();
            infoDialog.show();

            resizeDialogToMatchWindow(context, infoDialog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**********************************************************************************************/

    /****************************** Failure Overloaded Callbacks **********************************/
    public static void showFileSourceDialog(Context context, final OnFileSourceChoiceListener sourceChoiceListener) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_file_source);

        dialog.findViewById(R.id.view_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sourceChoiceListener != null) {
                    sourceChoiceListener.onCameraChosen();
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.view_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sourceChoiceListener != null) {
                    sourceChoiceListener.onGalleryChosen();
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.view_file_manager).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sourceChoiceListener != null) {
                    sourceChoiceListener.onFileManagerChosen();
                }
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();

        resizeDialogToMatchWindow(context, dialog);
    }

    public static AlertDialog showFilesDialog(Context context, ArrayList<String> filePaths, boolean showDelete,
                                              boolean showAdd, final OnFileActionListener onFileActionListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Files");
        builder.setCancelable(true);

        View builderView = LayoutInflater.from(context).inflate(R.layout.row_file_recycler, null);
        builder.setView(builderView);

        RecyclerView recyclerView = builderView.findViewById(R.id.recycler_files);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        FilesAdapter filesViewAdapter = new FilesAdapter
                (context, recyclerView, filePaths, onFileActionListener, showDelete);
        recyclerView.setAdapter(filesViewAdapter);

        if (showAdd) {
            builder.setPositiveButton("Add File", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onFileActionListener.onFileAddRequest();
                }
            });
        }

        AlertDialog alertDialog = builder.show();
        resizeDialogToMatchWindow(context, alertDialog);
        return alertDialog;
    }

    private static void resizeDialogToMatchWindow(Context context, Dialog dialog) {
        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 1.00);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
    }
}
