package com.planes.android.game.common

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import com.planes.android.R


class PlanesVerticalLayoutParams(context: Context, attrs: AttributeSet) :
    ViewGroup.LayoutParams(context, attrs) {

    fun getText(): String {
        return m_Text
    }

    fun getText1(): String {
        return m_Text1
    }

    fun getText2(): String {
        return m_Text2
    }

    fun getBackgroundColor(): ColorStateList {
        return m_BackgroundColor
    }

    fun getForegroundColor(): ColorStateList {
        return m_ForegroundColor
    }

    //TODO: to add text formatting options
    //word wrap, no word wrap
    var m_Row = 0
    var m_Col = 0
    var m_RowSpan = 0
    var m_ColSpan = 0
    var m_GameStage = -1
    private var m_Text: String
    private var m_Text1: String
    private var m_Text2: String
    private lateinit var m_BackgroundColor : ColorStateList
    private lateinit var m_ForegroundColor : ColorStateList

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PlanesVerticalLayout)
        m_Row = a.getInt(R.styleable.PlanesVerticalLayout_gc_row, 0)
        m_Col = a.getInt(R.styleable.PlanesVerticalLayout_gc_col, 0)
        m_RowSpan = a.getInt(R.styleable.PlanesVerticalLayout_gc_rowspan, 0)
        m_ColSpan = a.getInt(R.styleable.PlanesVerticalLayout_gc_colspan, 0)
        m_GameStage = a.getInt(R.styleable.PlanesVerticalLayout_gc_game_stage, 0)
        m_Text = a.getString(R.styleable.PlanesVerticalLayout_gc_text).toString()
        m_Text1 = a.getString(R.styleable.PlanesVerticalLayout_gc_text1).toString()
        m_Text2 = a.getString(R.styleable.PlanesVerticalLayout_gc_text2).toString()
        val colorBackStyle: Int = R.styleable.PlanesVerticalLayout_gc_background_color
        if (a.hasValue(colorBackStyle)) {
            m_BackgroundColor =
                a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_background_color)!!
        }
        val colorForeStyle: Int = R.styleable.PlanesVerticalLayout_gc_foreground_color
        if (a.hasValue(colorForeStyle)) {
            m_ForegroundColor =
                a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_foreground_color)!!
        }
        a.recycle()
    }
}