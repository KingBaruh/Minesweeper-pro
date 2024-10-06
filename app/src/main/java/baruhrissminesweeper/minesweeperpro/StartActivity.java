package baruhrissminesweeper.minesweeperpro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;

public class StartActivity extends AppCompatActivity {

    static MediaPlayer mp;
    boolean flag_mp;
    int ability_key;
    ImageView music_icon;
    SharedPreferences sp;
    static boolean anim_flag=false;
    ImageView grid;
    ImageView scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScoresLIst.SetList(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        music_icon = (ImageView) findViewById(R.id.music_icon_start);
        sp=getSharedPreferences("detiles",MODE_PRIVATE);
        ability_key=sp.getInt("speical_ability_key",MainActivity.SPECIAL_ABILITY_FROZEN);
        flag_mp = sp.getBoolean("home_music",true);
        mp = MediaPlayer.create(this, R.raw.music);
        mp.setLooping(true);
        mp.start();
        if(!flag_mp)
        {
            mp.pause();
            music_icon.setImageResource(R.drawable.soundoff);
            flag_mp = false;
        }
    }

    public void toMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void toability(View view) {
        Intent intent=new Intent(this,SpecialAbilityActivity.class);
        startActivity(intent);
    }

    public void toInfo(View view) {
        Intent intent = new Intent(this, InformationActivity.class);
        startActivity(intent);
    }

    public void toScores(View view){
        ////new
        final Intent intent_grid=new Intent(this,CustomGameActivity.class);
        final Intent intent_scores=new Intent(this,ListOfScoresActivity.class);
        final Dialog d;
        d=new Dialog(this);
        d.setTitle("");
        d.setContentView(R.layout.choose_mode_or_score);
        d.setCancelable(true);
        grid=(ImageView)d.findViewById(R.id.to_grid);
        scores=(ImageView)d.findViewById(R.id.to_scores);

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_grid);
                d.dismiss();
            }
        });
        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_scores);
                d.dismiss();
            }
        });
        d.show();
        //////
        /*
        Intent intent=new Intent(this,ListOfScoresActivity.class);
        startActivity(intent);
        */
    }

    public void toSetting(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    public void toshare(View view) {
        Intent emailIntent=new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: baruhriss@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_STREAM,"");
        emailIntent.putExtra(Intent.EXTRA_TEXT,"");
        try {
            startActivity(emailIntent);
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void music(View view) {
        if (flag_mp) {
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("home_music",false);
            editor.commit();
            mp.pause();
            music_icon.setImageResource(R.drawable.soundoff);
            flag_mp = false;
        } else {
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("home_music",true);
            editor.commit();
            mp.start();
            music_icon.setImageResource(R.drawable.volume);
            flag_mp = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_FIRST_USER) {
            ability_key = data.getIntExtra("speical_ability_key", 0);
        }
    }
    @Override
    public void onRestart() {
        if(flag_mp)
            mp.start();
        super.onRestart();
    }
    @Override
    public void onStop() {
        mp.pause();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mp.stop();
        super.onDestroy();
    }
    @Override
    public void finish(){
        mp.stop();
        super.finish();
    }
    @Override
    public void onStart(){
        super.onStart();
        if(!anim_flag) {
            ImageView play_bottom = (ImageView) findViewById(R.id.imageView4);
            play_bottom.setAlpha(0f);
            play_bottom.setTranslationY(200f);
            play_bottom.animate().alpha(1f).translationY(0f).setStartDelay(500).setDuration(1500)
                    .setInterpolator(new EasingInterpolator(Ease.ELASTIC_OUT)).start();
            ImageView info_bottom = (ImageView) findViewById(R.id.imageView25);
            info_bottom.setAlpha(0f);
            info_bottom.setTranslationY(-50f);
            info_bottom.animate().alpha(1f).translationY(0f).setStartDelay(1000).setDuration(500)
                    .setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).start();
            ImageView sound_bottom = (ImageView) findViewById(R.id.music_icon_start);
            sound_bottom.setAlpha(0f);
            sound_bottom.setTranslationY(50f);
            sound_bottom.animate().alpha(1f).translationY(0f).setStartDelay(1000).setDuration(500)
                    .setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).start();
            ImageView ability_bottom = (ImageView) findViewById(R.id.imageView19);
            ability_bottom.setAlpha(0f);
            ability_bottom.setTranslationY(50f);
            ability_bottom.animate().alpha(1f).translationY(0f).setStartDelay(1000).setDuration(500)
                    .setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).start();
            ImageView share_bottom = (ImageView) findViewById(R.id.share_icon);
            share_bottom.setAlpha(0f);
            share_bottom.setTranslationY(50f);
            share_bottom.animate().alpha(1f).translationY(0f).setStartDelay(1000).setDuration(500)
                    .setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).start();
            ImageView scores_bottom = (ImageView) findViewById(R.id.imageView17);
            scores_bottom.setAlpha(0f);
            scores_bottom.setTranslationY(-50f);
            scores_bottom.animate().alpha(1f).translationY(0f).setStartDelay(1000).setDuration(500)
                    .setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).start();
            ImageView setting_bottom = (ImageView) findViewById(R.id.imageView20);
            setting_bottom.setAlpha(0f);
            setting_bottom.setTranslationY(-50f);
            setting_bottom.animate().alpha(1f).translationY(0f).setStartDelay(1000).setDuration(500)
                    .setInterpolator(new EasingInterpolator(Ease.CUBIC_IN)).start();
            anim_flag=true;
        }

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Minesweeper?");
        builder.setMessage("Are you sure you want to exit Minesweeper?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                anim_flag=false;
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
