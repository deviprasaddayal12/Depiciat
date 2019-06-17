package com.deviprasaddayal.depiciat.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.LayoutTransition
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.deviprasaddayal.depiciat.R
import com.deviprasaddayal.depiciat.listeners.OnDateSelectedListener
import com.deviprasaddayal.depiciat.listeners.OnSearchVisibilityChangeListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

object ViewUtils {

    fun elevateViewOnClick(context: Activity, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.animate().translationZ(10f).start()

            Timer().schedule(object : TimerTask() {
                override fun run() {
                    context.runOnUiThread {
                        try {
                            view.animate().translationZ(0f).start()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }, 1000)
        }
    }

    fun toggleSearchPanelVisibility(
        showing: Boolean, llSearchPanel: LinearLayout,
        ivSearch: ImageView,
        onSearchVisibilityChangeListener: OnSearchVisibilityChangeListener?
    ): Boolean {
        if (showing) {
            val cx = ivSearch.width / 2
            val centre = IntArray(2)
            ivSearch.getLocationOnScreen(centre)
            hideWithCircularContractAnimation(centre[0] + cx, centre[1], llSearchPanel)

            onSearchVisibilityChangeListener?.onSearchVisibilityChanged(false)
        } else {
            val cx = ivSearch.width / 2
            val centre = IntArray(2)
            ivSearch.getLocationOnScreen(centre)
            showWithCircularExpandAnimation(centre[0] + cx, centre[1], llSearchPanel)

            onSearchVisibilityChangeListener?.onSearchVisibilityChanged(true)
        }

        return !showing
    }

    fun showDateDialog(
        context: Context, setNowAsMax: Boolean,
        onDateSelectedListener: OnDateSelectedListener
    ) {
        showDateDialog(context, null, null, setNowAsMax, onDateSelectedListener)
    }

    fun showDateDialog(
        context: Context, previousDate: String, setNowAsMax: Boolean,
        onDateSelectedListener: OnDateSelectedListener
    ) {
        showDateDialog(context, null, previousDate, setNowAsMax, onDateSelectedListener)
    }

    fun showDateDialog(
        context: Context, title: String?, previousDate: String?, setNowAsMax: Boolean,
        onDateSelectedListener: OnDateSelectedListener
    ) {
        var title = title
        if (title == null) title = "Please select date"

        val calendar = DateUtils.getCalendarInstance(previousDate)
        val datePickerDialog = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->
                val calendar1 = Calendar.getInstance()
                calendar1.set(Calendar.YEAR, i)
                calendar1.set(Calendar.MONTH, i1)
                calendar1.set(Calendar.DAY_OF_MONTH, i2)
                onDateSelectedListener.onDateSelected(DateUtils.getDateInString(calendar1.time), calendar1)
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setTitle(title)
        if (setNowAsMax)
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        else
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun getNetworkSnackbar(snackbar: Snackbar?, view: View, online: Boolean): Snackbar {
        var snackbar = snackbar
        val message = if (online) "We're back online..." else "Internet unavailable!"
        val duration = if (online) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_INDEFINITE
        if (snackbar != null) {
            snackbar.dismiss()
            snackbar = Snackbar.make(view, message, duration)
            snackbar.show()
        } else {
            snackbar = Snackbar.make(view, message, duration)
            snackbar.show()
        }
        return snackbar
    }

    // this callback is used only for different departmental fragments used to create new requisition
    fun toggleButtonWidthWithLayoutVisibility(expandLayout: Boolean, button: MaterialButton, layout: LinearLayout) {
        val layoutParams = button.layoutParams
        layoutParams.width =
            if (expandLayout) ViewGroup.LayoutParams.WRAP_CONTENT else ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        button.layoutParams = layoutParams

        toggleViewVisibility(expandLayout, layout)
    }

    fun showWithCircularExpandAnimation(centerX: Int, centerY: Int, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = Math.hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0f, finalRadius)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    view.visibility = View.VISIBLE
                }
            })
            anim.start()
        } else {
            view.visibility = View.VISIBLE
        }
    }

    fun hideWithCircularContractAnimation(centerX: Int, centerY: Int, view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val finalRadius = Math.hypot(centerX.toDouble(), centerY.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, finalRadius, 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.visibility = View.GONE
                }
            })
            anim.start()
        } else {
            view.visibility = View.GONE
        }
    }

    fun showFromBottomAnimation(context: Context, view: View) {
        val bottomUp = AnimationUtils.loadAnimation(context, R.anim.rise_from_bottom)
        view.startAnimation(bottomUp)
        view.visibility = View.VISIBLE
    }

    fun hideToBottomAnimation(context: Context, view: View) {
        val bottomDown = AnimationUtils.loadAnimation(context, R.anim.sink_to_bottom)
        view.startAnimation(bottomDown)
        view.visibility = View.GONE
    }

    fun showByFadingIn(context: Context, view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        view.animation = animation
        //        Animation animation = view.getAnimation();
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        animation.start()
    }

    fun hideByFadingOut(context: Context, view: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)
        view.animation = animation
        //        Animation animation = view.getAnimation();
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                view.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        animation.start()
    }

    fun animateLayoutChanges(view: View) {
        val layoutTransition = LayoutTransition()
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        (view as ViewGroup).layoutTransition = layoutTransition
    }

    fun makeToast(context: Context, message: String) {
        Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show()
    }

    fun makeSnackBar(fab: FloatingActionButton, message: String) {
        makeSnackBar(fab, message, Snackbar.LENGTH_SHORT, null, null)
    }

    fun makeSnackBar(fab: FloatingActionButton, message: String, actionTitle: String, action: Runnable): Snackbar {
        return makeSnackBar(fab, message, Snackbar.LENGTH_LONG, actionTitle, action)
    }

    fun makeSnackBar(
        fab: FloatingActionButton,
        message: String,
        duration: Int,
        actionTitle: String?,
        action: Runnable?
    ): Snackbar {
        val snackbar = Snackbar.make(fab, message, duration)
        if (action != null) {
            snackbar.setAction(actionTitle) { action.run() }
        }
        snackbar.show()
        return snackbar
    }

    fun toggleViewVisibility(visible: Boolean, vararg views: View) {
        if (visible)
            showViews(*views)
        else
            hideViews(*views)
    }

    fun showViews(vararg views: View) {
        for (v in views)
            v.visibility = View.VISIBLE
    }

    fun hideViews(vararg views: View) {
        for (v in views)
            v.visibility = View.GONE
    }

    fun isVisible(v: View): Boolean {
        return v.visibility == View.VISIBLE
    }

    fun setAnimation(context: Context, viewToAnimate: View, position: Int, lastPosition: Int): Int {
        var lastPosition = lastPosition
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.rise_up)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }

        return lastPosition
    }

    fun disableViews(vararg views: View) {
        for (v in views)
            v.isEnabled = false
    }

    fun enableViews(vararg views: View) {
        for (v in views)
            v.isEnabled = true
    }

    fun makeUneditable(vararg textInputEditTexts: TextInputEditText) {
        for (textInputEditText in textInputEditTexts)
            textInputEditText.inputType = InputType.TYPE_NULL
    }
}
