package com.planes.android

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat


class Popups {
    companion object Popups {

        fun onWarning(context: Context, mainLayout: ViewGroup, errorString: String) {
            val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.warning_options, null)

            val textView = popupView.findViewById<TextView>(R.id.warning_options_text)
            textView.text = errorString

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(
                mainLayout,
                Gravity.CENTER,
                0,
                0
            )

            // dismiss the popup window when touched
            popupView.setOnTouchListener { _, _ ->
                popupWindow.dismiss()
                true
            }
        }

        fun showSaveCredentialsPopup(context: Context, mainLayout: LinearLayoutCompat, username: String, password: String,
                yeslambda: (username: String, password: String) -> Unit) {

            // inflate the layout of the popup window
            val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView = inflater.inflate(R.layout.savecredentials_popup, null)

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(mainLayout, Gravity.CENTER, 0, 0)
            popupView.findViewById(R.id.savecredentials_text) as TextView
            popupView.findViewById(R.id.savecredentials_title) as TextView

            val yesButton = popupView.findViewById(R.id.savecredentials_yes_button) as Button
            val noButton = popupView.findViewById(R.id.savecredentials_no_button) as Button

            // dismiss the popup window when touched
            popupView.setOnTouchListener { _, _ ->
                popupWindow.dismiss()
                true
            }

            noButton.setOnClickListener {
                popupWindow.dismiss()
            }

            yesButton.setOnClickListener {
                yeslambda(username, password)
                popupWindow.dismiss()
            }
        }

        fun showCreateNewGamePopup(context: Context, mainLayout: RelativeLayout, createGameLambda: () -> Unit) {
            val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.creategame_popup, null)

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(
                mainLayout,
                Gravity.CENTER,
                0,
                0
            )

            // dismiss the popup window when touched
            popupView.setOnTouchListener { _, _ ->
                popupWindow.dismiss()
                true
            }

            val createGameButton = popupView.findViewById(R.id.createnewgame_button) as Button
            val cancelButton  = popupView.findViewById(R.id.creategame_cancel_button) as Button

            cancelButton.setOnClickListener {
                popupWindow.dismiss()
            }

            createGameButton.setOnClickListener {
                createGameLambda()
                popupWindow.dismiss()
            }
        }

        fun showConnectToGamePopup(context: Context, mainLayout: RelativeLayout, connectToGameLambda: () -> Unit, opponentName: String) {
            val inflater = context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popupView: View = inflater.inflate(R.layout.connecttogame_popup, null)

            // create the popup window
            val width = LinearLayout.LayoutParams.WRAP_CONTENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val focusable = true // lets taps outside the popup also dismiss it
            val popupWindow = PopupWindow(popupView, width, height, focusable)

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window token
            popupWindow.showAtLocation(
                mainLayout,
                Gravity.CENTER,
                0,
                0
            )

            // dismiss the popup window when touched
            popupView.setOnTouchListener { _, _ ->
                popupWindow.dismiss()
                true
            }

            val connectToGameButton = popupView.findViewById(R.id.connecttogame_button) as Button
            val cancelButton  = popupView.findViewById(R.id.connecttogame_cancel_button) as Button

            val connectToGameText = popupView.findViewById(R.id.connecttogame_text) as TextView
            connectToGameText.text = context.getString(R.string.connecttogame_possible, opponentName)

            cancelButton.setOnClickListener {
                popupWindow.dismiss()
            }

            connectToGameButton.setOnClickListener {
                connectToGameLambda()
                popupWindow.dismiss()
            }
        }
    }
}