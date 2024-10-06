package baruhrissminesweeper.minesweeperpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.lang.reflect.Array;

public class SettingActivity extends AppCompatActivity {

    CheckBox vibarte;
    CheckBox long_click_flag;
    CheckBox lock_game_mode;
    boolean is_viabrte;
    boolean is_long_click_flag;
    boolean is_game_mode_lock;

    Spinner spinner_size;
    ArrayAdapter<CharSequence>adapter_size;

    SharedPreferences sp;

    Spinner spinner_difficulty;
    ArrayAdapter<CharSequence>adapter_difficulty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sp=getSharedPreferences("detalis",MODE_PRIVATE);
        vibarte=(CheckBox)findViewById(R.id.checkBox_vibrate);
        long_click_flag=(CheckBox)findViewById(R.id.checkBox_longclick_flag);
        lock_game_mode=(CheckBox)findViewById(R.id.checkBox_Lock_game_mode_flag);

        spinner_size=(Spinner)findViewById(R.id.spinner_size);
        adapter_size=ArrayAdapter.createFromResource(this,R.array.size_numbers,android.R.layout.simple_spinner_item);
        adapter_size.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_size.setAdapter(adapter_size);
        spinner_size.setSelection(sp.getInt("selected_size_position",0));
        spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor=sp.edit();
                editor.putInt("size",Integer.parseInt((String) adapterView.getItemAtPosition(i)));
                editor.putInt("selected_size_position",i);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spinner_difficulty=(Spinner)findViewById(R.id.spinner_difficulty);
        adapter_difficulty=ArrayAdapter.createFromResource(this,R.array.difficulty,android.R.layout.simple_spinner_item);
        adapter_difficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_difficulty.setAdapter(adapter_difficulty);
        spinner_difficulty.setSelection(sp.getInt("selected_difficulty_position",0));
        spinner_difficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor=sp.edit();
                editor.putString("difficulty",(String) adapterView.getItemAtPosition(i));
                editor.putInt("selected_difficulty_position",i);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        is_viabrte=sp.getBoolean("is_viabrte",true);
        if(is_viabrte)
            vibarte.setChecked(true);
        else
            vibarte.setChecked(false);
        is_long_click_flag=sp.getBoolean("is_long_click_is_active",false);
        if(is_long_click_flag)
            long_click_flag.setChecked(true);
        else
            long_click_flag.setChecked(false);
        is_game_mode_lock=sp.getBoolean("is_game_mode_look_active",false);
        if(is_game_mode_lock)
            lock_game_mode.setChecked(true);
        else
            lock_game_mode.setChecked(false);
    }
    public void change_viabrte(View view){
        if(is_viabrte){
            is_viabrte=false;
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_viabrte",false);
            editor.commit();
        }

        else{
            is_viabrte=true;
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_viabrte",true);
            editor.commit();
        }

    }
    public void change_is_long_click_flag(View view){
        if(is_long_click_flag){
            is_long_click_flag=false;
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_long_click_is_active",false);
            editor.commit();
        }
        else{
            is_long_click_flag=true;
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_long_click_is_active",true);
            editor.commit();
        }
    }
    public void change_game_mode(View view){
        if(is_game_mode_lock){
            is_game_mode_lock=false;
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_game_mode_look_active",false);
            editor.commit();
        }
        else{
            is_game_mode_lock=true;
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_game_mode_look_active",true);
            editor.commit();
        }
    }
    public void back(View view){
        finish();
    }
}
