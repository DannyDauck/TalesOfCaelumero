package com.example.talesofcaelumora.data.utils

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.talesofcaelumora.R

class GradientText : AppCompatTextView {

    private val defaultStartColor = 0xFFFF0000.toInt() // Default-Startfarbe (Rot)
    private val defaultEndColor = 0xFF00FF00.toInt()   // Default-Endfarbe (Grün)

    private var startColor: Int = defaultStartColor
    private var middleColor: Int = defaultEndColor
    private var endColor: Int = defaultEndColor

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        parseAttributes(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        parseAttributes(attrs)
        init()
    }

    private fun parseAttributes(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView)

        startColor = typedArray.getColor(R.styleable.GradientTextView_startColor, defaultStartColor)
        middleColor = typedArray.getColor(R.styleable.GradientTextView_middleColor, defaultEndColor)
        endColor = typedArray.getColor(R.styleable.GradientTextView_endColor, defaultEndColor)

        typedArray.recycle()
    }

    private fun init() {
        val textShader: Shader = LinearGradient(
            0f, 0f, paint.measureText(text.toString()), textSize, intArrayOf(
                startColor,
                middleColor,
                endColor
            ), null, Shader.TileMode.CLAMP
        )
        paint.shader = textShader
    }
}
