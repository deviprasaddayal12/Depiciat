package com.deviprasaddayal.depiciat.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.deviprasaddayal.depiciat.R;
import com.deviprasaddayal.depiciat.listeners.OnDateSelectedListener;
import com.deviprasaddayal.depiciat.listeners.OnSearchVisibilityChangeListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class ViewUtils {

    public static void elevateViewOnClick(final Activity context, final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.animate().translationZ(10f).start();

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                view.animate().translationZ(0f).start();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, 1000);
        }
    }

    public static boolean toggleSearchPanelVisibility(boolean showing, LinearLayout llSearchPanel,
                                                      ImageView ivSearch,
                                                      OnSearchVisibilityChangeListener onSearchVisibilityChangeListener) {
        if (showing) {
            int cx = ivSearch.getWidth() / 2;
            int[] centre = new int[2];
            ivSearch.getLocationOnScreen(centre);
            hideWithCircularContractAnimation(centre[0] + cx, centre[1], llSearchPanel);

            if (onSearchVisibilityChangeListener != null)
                onSearchVisibilityChangeListener.onSearchVisibilityChanged(false);
        } else {
            int cx = ivSearch.getWidth() / 2;
            int[] centre = new int[2];
            ivSearch.getLocationOnScreen(centre);
            showWithCircularExpandAnimation(centre[0] + cx, centre[1], llSearchPanel);

            if (onSearchVisibilityChangeListener != null)
                onSearchVisibilityChangeListener.onSearchVisibilityChanged(true);
        }

        return !showing;
    }

    public static void showDateDialog(Context context, boolean setNowAsMax,
                                      final OnDateSelectedListener onDateSelectedListener) {
        showDateDialog(context, null, null, setNowAsMax, onDateSelectedListener);
    }

    public static void showDateDialog(Context context, String previousDate, boolean setNowAsMax,
                                      final OnDateSelectedListener onDateSelectedListener) {
        showDateDialog(context, null, previousDate, setNowAsMax, onDateSelectedListener);
    }

    public static void showDateDialog(Context context, String title, String previousDate, boolean setNowAsMax,
                                      final OnDateSelectedListener onDateSelectedListener) {
        if (title == null) title = "Please select date";

        Calendar calendar = DateUtils.getCalendarInstance(previousDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.YEAR, i);
                        calendar1.set(Calendar.MONTH, i1);
                        calendar1.set(Calendar.DAY_OF_MONTH, i2);
                        onDateSelectedListener.onDateSelected(DateUtils.getDateInString(calendar1.getTime()), calendar1);
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle(title);
        if (setNowAsMax)
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        else
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public static Snackbar getNetworkSnackbar(Snackbar snackbar, View view, boolean online) {
        String message = online ? "We're back online..." : "Internet unavailable!";
        int duration = online ? Snackbar.LENGTH_SHORT : Snackbar.LENGTH_INDEFINITE;
        if (snackbar != null) {
            snackbar.dismiss();
            snackbar = Snackbar.make(view, message, duration);
            snackbar.show();
        } else {
            snackbar = Snackbar.make(view, message, duration);
            snackbar.show();
        }
        return snackbar;
    }

    // this callback is used only for different departmental fragments used to create new requisition
    public static void toggleButtonWidthWithLayoutVisibility(boolean expandLayout, MaterialButton button, LinearLayout layout) {
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        layoutParams.width = expandLayout ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        button.setLayoutParams(layoutParams);

        toggleViewVisibility(expandLayout, layout);
    }

    public static void showWithCircularExpandAnimation(int centerX, int centerY, final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) Math.hypot(centerX, centerY);
            Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, finalRadius);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    view.setVisibility(View.VISIBLE);
                }
            });
            anim.start();
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideWithCircularContractAnimation(int centerX, int centerY, final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) Math.hypot(centerX, centerY);
            Animator anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, finalRadius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.GONE);
                }
            });
            anim.start();
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void showFromBottomAnimation(Context context, View view) {
        Animation bottomUp = AnimationUtils.loadAnimation(context, R.anim.rise_from_bottom);
        view.startAnimation(bottomUp);
        view.setVisibility(View.VISIBLE);
    }

    public static void hideToBottomAnimation(Context context, View view) {
        Animation bottomDown = AnimationUtils.loadAnimation(context, R.anim.sink_to_bottom);
        view.startAnimation(bottomDown);
        view.setVisibility(View.GONE);
    }

    public static void showByFadingIn(Context context, final View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        view.setAnimation(animation);
//        Animation animation = view.getAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();
    }

    public static void hideByFadingOut(Context context, final View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        view.setAnimation(animation);
//        Animation animation = view.getAnimation();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.start();
    }

    public static void animateLayoutChanges(View view) {
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) view).setLayoutTransition(layoutTransition);
    }

    public static void makeToast(Context context, String message) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
    }

    public static void makeSnackBar(FloatingActionButton fab, String message) {
        makeSnackBar(fab, message, Snackbar.LENGTH_SHORT, null, null);
    }

    public static Snackbar makeSnackBar(FloatingActionButton fab, String message, String actionTitle, final Runnable action) {
        return makeSnackBar(fab, message, Snackbar.LENGTH_LONG, actionTitle, action);
    }

    public static Snackbar makeSnackBar(FloatingActionButton fab, String message, int duration, String actionTitle, final Runnable action) {
        Snackbar snackbar = Snackbar.make(fab, message, duration);
        if (action != null) {
            snackbar.setAction(actionTitle, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    action.run();
                }
            });
        }
        snackbar.show();
        return snackbar;
    }

    public static void toggleViewVisibility(boolean visible, View...views) {
        if (visible)
            showViews(views);
        else
            hideViews(views);
    }

    public static void showViews(View... views) {
        for (View v : views)
            v.setVisibility(View.VISIBLE);
    }

    public static void hideViews(View... views) {
        for (View v : views)
            v.setVisibility(View.GONE);
    }

    public static boolean isVisible(View v) {
        return v.getVisibility() == View.VISIBLE;
    }

    public static int setAnimation(Context context, View viewToAnimate, int position, int lastPosition) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.rise_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }

        return lastPosition;
    }

    public static void disableViews(View...views) {
        for (View v : views)
            v.setEnabled(false);
    }

    public static void enableViews(View...views) {
        for (View v : views)
            v.setEnabled(true);
    }

    public static void makeUneditable(TextInputEditText...textInputEditTexts) {
        for (TextInputEditText textInputEditText : textInputEditTexts)
            textInputEditText.setInputType(InputType.TYPE_NULL);
    }
}
