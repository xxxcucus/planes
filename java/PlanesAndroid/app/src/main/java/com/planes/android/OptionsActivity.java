package com.planes.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.planes.common.PlanesRoundJava;


public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        m_Spinner_Skill = (Spinner)findViewById(R.id.computer_skill_spinner);
        ArrayAdapter<CharSequence> adapter_skill = ArrayAdapter.createFromResource(this,
                R.array.computer_skills, R.layout.spinner_item);
        adapter_skill.setDropDownViewResource(R.layout.spinner_item);
        m_Spinner_Skill.setAdapter(adapter_skill);
        m_Spinner_Skill.setOnItemSelectedListener(null);

        m_Spinner_Show = (Spinner) findViewById(R.id.show_plane_after_kill);
        ArrayAdapter<CharSequence> adapter_show = ArrayAdapter.createFromResource(this,
                R.array.yesno_options, R.layout.spinner_item);
        adapter_show.setDropDownViewResource(R.layout.spinner_item);
        m_Spinner_Show.setAdapter(adapter_show);
        m_Spinner_Show.setOnItemSelectedListener(null);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            m_ComputerSkill = extras.getInt("gamedifficulty/computerskill");
            m_Spinner_Skill.setSelection(m_ComputerSkill);
            m_ShowPlaneAfterKill = extras.getBoolean("gamedifficulty/showkilledplane");
            m_Spinner_Show.setSelection(m_ShowPlaneAfterKill ? 0 : 1);
        }

        m_Listener_show = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                System.out.println(item.toString());

                int shouldbepos = m_ShowPlaneAfterKill ? 0 : 1;

                if (pos == shouldbepos)
                    return;

                if (!m_PlaneRound.setShowPlaneAfterKill(pos == 0)) {
                    onWarning();
                    parent.setSelection(m_ShowPlaneAfterKill ? 0 : 1);
                } else {
                    m_ShowPlaneAfterKill = pos == 0;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        m_Spinner_Show.setOnItemSelectedListener(m_Listener_show);

        m_Listener_skill = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                System.out.println(item.toString());

                if (pos == m_ComputerSkill)
                    return;

                if (!m_PlaneRound.setComputerSkill(pos)) {
                    onWarning();
                    parent.setSelection(m_ComputerSkill);
                } else {
                    m_ComputerSkill = pos;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        m_Spinner_Skill.setOnItemSelectedListener(m_Listener_skill);
        m_PlaneRound = new PlanesRoundJava();
        m_PlaneRound.createPlanesRound();
    }

    @Override
    public void onBackPressed()
    {
        Intent pushIntent = this.getIntent();
        pushIntent.putExtra("gamedifficulty/computerskill", m_ComputerSkill);
        pushIntent.putExtra("gamedifficulty/showkilledplane", m_ShowPlaneAfterKill);
        setResult(Activity.RESULT_OK, pushIntent);
        finish();
    }

    public void onWarning() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.warning_options, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation((LinearLayout) findViewById(R.id.options_layout), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    private int m_ComputerSkill = 2;
    private boolean m_ShowPlaneAfterKill = false;

    private PlanesRoundInterface m_PlaneRound;

    AdapterView.OnItemSelectedListener m_Listener_skill;
    AdapterView.OnItemSelectedListener m_Listener_show;
    Spinner m_Spinner_Skill;
    Spinner m_Spinner_Show;
}