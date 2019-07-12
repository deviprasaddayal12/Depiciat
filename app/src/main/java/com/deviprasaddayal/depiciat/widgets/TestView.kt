package com.deviprasaddayal.depiciat.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.deviprasaddayal.depiciat.R
import java.lang.Exception

class TestView : View {
    val TAG = TestView::class.java.canonicalName

    private lateinit var paint: Paint

    val DEFAULT_STROKE_COLOR = 0xAD1457
    val DEFAULT_STROKE_WIDTH = 5
    val DEFAULT_COUNT = 1
    val DEFAULT_SPACING = 5f
    val DEFAULT_RADIUS = 50f

    var strokeWidth: Int = DEFAULT_STROKE_WIDTH
    var strokeColor: Int = DEFAULT_STROKE_COLOR
    var count: Int = DEFAULT_COUNT
    var radius: Float = DEFAULT_RADIUS
    var spacing: Float = DEFAULT_SPACING

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TestView)

        strokeColor = /*typedArray.getColor(typedArray.getIndex(R.styleable.TestView_tv_strokeColor),*/
            context.resources.getColor(R.color.colorAccentLight)/*)*/
        strokeWidth = typedArray.getInt(typedArray.getIndex(R.styleable.TestView_tv_strokeWidth), DEFAULT_STROKE_WIDTH)
        count = typedArray.getInt(typedArray.getIndex(R.styleable.TestView_tv_count), DEFAULT_COUNT)
        radius = typedArray.getFloat(typedArray.getIndex(R.styleable.TestView_tv_radius), DEFAULT_RADIUS)
        spacing = typedArray.getFloat(typedArray.getIndex(R.styleable.TestView_tv_spacing), DEFAULT_SPACING)

        Log.i("$TAG.init", "Radius is: $radius & Spacing is: $spacing")

        typedArray.recycle()

        initPaint()
    }

    private fun initPaint() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.strokeWidth = strokeWidth.toFloat()
        paint.color = strokeColor
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
    }

    override fun onDraw(canvas: Canvas?) {
        if (isInEditMode)
            return

        val centreX = layoutParams.width / 2
        val centreY = layoutParams.height / 2

        Log.i("$TAG.onDraw", "Radius is: $radius & Spacing is: $spacing")

        try {
            for (i in 1..10) {
                val radius1 = radius + i * spacing
                canvas?.drawCircle(centreX.toFloat(), centreY.toFloat(), radius1, paint)
                Log.i("$TAG.oDraw.try", "Updated radius is: $radius1")
            }
        } catch (e: Exception) {
            Log.e("$TAG.onDraw.catch", e.message)
        }
    }
}