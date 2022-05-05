package com.planes.android.game.common

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.planes.android.R

class PlanesVerticalLayoutParams: ViewGroup.LayoutParams {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.PlanesVerticalLayout)
        m_Row = a.getInt(R.styleable.PlanesVerticalLayout_gc_row, 0)
        m_Col = a.getInt(R.styleable.PlanesVerticalLayout_gc_col, 0)
        m_RowSpan = a.getInt(R.styleable.PlanesVerticalLayout_gc_rowspan, 0)
        m_ColSpan = a.getInt(R.styleable.PlanesVerticalLayout_gc_colspan, 0)
        m_GameStage = a.getInt(R.styleable.PlanesVerticalLayout_gc_game_stage, 0)
        m_Text = a.getString(R.styleable.PlanesVerticalLayout_gc_text).toString()
        m_Text1 = a.getString(R.styleable.PlanesVerticalLayout_gc_text1).toString()
        m_Text2 = a.getString(R.styleable.PlanesVerticalLayout_gc_text2).toString()
        m_BackgroundColor = a.getColor(
            R.styleable.PlanesVerticalLayout_gc_background_color, context.resources.getColor(
                R.color.grey
            ))
        a.recycle()
    }

    //TODO: for these 2 constructors there is no initialization of member variables !!!
    constructor(width: Int, height: Int) : super(width, height) {}
    constructor(source: ViewGroup.LayoutParams?) : super(source) {}

    fun getText(): String {
        return m_Text
    }

    fun getText1(): String {
        return m_Text1
    }

    fun getText2(): String {
        return m_Text2
    }

    fun getColor(): Int {
        return m_BackgroundColor
    }

    //TODO: to add text formatting options
    //word wrap, no word wrap
    var m_Row = 0
    var m_Col = 0
    var m_RowSpan = 0
    var m_ColSpan = 0
    var m_GameStage = -1
    private val m_Player = false
    lateinit var m_Text: String
    lateinit var m_Text1: String
    lateinit var m_Text2: String
    var m_BackgroundColor = 0
}