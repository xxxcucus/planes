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

    fun getGuessColor(): ColorStateList {
        return m_GuessColor
    }

    fun getBoardColor(): ColorStateList {
        return m_BoardColor
    }

    fun getFirstPlaneColor(): ColorStateList {
        return m_FirstPlaneColor
    }

    fun getSecondPlaneColor(): ColorStateList {
        return m_SecondPlaneColor
    }

    fun getThirdPlaneColor(): ColorStateList {
        return m_ThirdPlaneColor
    }

    fun getSelectedPlaneColor(): ColorStateList {
        return m_SelectedPlaneColor
    }

    fun getCockpitColor(): ColorStateList {
        return m_CockpitColor
    }

    fun getPlaneOverlapColor(): ColorStateList {
        return m_PlaneOverlapColor
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
    private lateinit var m_GuessColor: ColorStateList
    private lateinit var m_BoardColor: ColorStateList
    private lateinit var m_FirstPlaneColor: ColorStateList
    private lateinit var m_SecondPlaneColor: ColorStateList
    private lateinit var m_ThirdPlaneColor: ColorStateList
    private lateinit var m_SelectedPlaneColor: ColorStateList
    private lateinit var m_CockpitColor: ColorStateList
    private lateinit var m_PlaneOverlapColor: ColorStateList


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

        val guessColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_guess_color
        if (a.hasValue(guessColorStyle)) {
            m_GuessColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_guess_color)!!
        }
        val boardColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_board_color
        if (a.hasValue(guessColorStyle)) {
            m_BoardColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_board_color)!!
        }
        val firstPlaneColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_first_plane_color
        if (a.hasValue(guessColorStyle)) {
            m_FirstPlaneColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_first_plane_color)!!
        }
        val secondPlaneColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_second_plane_color
        if (a.hasValue(guessColorStyle)) {
            m_SecondPlaneColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_second_plane_color)!!
        }
        val thirdPlaneColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_third_plane_color
        if (a.hasValue(guessColorStyle)) {
            m_ThirdPlaneColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_third_plane_color)!!
        }
        val selectedPlaneColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_selected_plane_color
        if (a.hasValue(guessColorStyle)) {
            m_SelectedPlaneColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_selected_plane_color)!!
        }
        val cockpitColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_cockpit_color
        if (a.hasValue(guessColorStyle)) {
            m_CockpitColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_cockpit_color)!!
        }
        val planeOverlapColorStyle: Int = R.styleable.PlanesVerticalLayout_gc_overlap_planes_color
        if (a.hasValue(guessColorStyle)) {
            m_PlaneOverlapColor = a.getColorStateList(R.styleable.PlanesVerticalLayout_gc_overlap_planes_color)!!
        }
        a.recycle()
    }
}