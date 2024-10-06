package baruhrissminesweeper.minesweeperpro;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity implements OnClickListener, View.OnLongClickListener {

    ImageButton change;
    ImageButton speical_ability_button;

    int size;
    int num_of_booms;
    int speical_counter;
    int speical_key;
    int timer_counter;
    int charge_ability;
    static final float FACTOR=45;
    static final int SPECIAL_ABILITY_ONE_OR_TWO = 241532523;
    static final int SPECIAL_ABILITY_FROZEN = 475435547;
    static final int SPECIAL_ABILITY_DESTROYED_MINE = 548375803;
    static final int SPECIAL_ABILITY_POWER_GUN = 1577743734;
    static final int SPECIAL_ABILITY_CURESD_BLADE = 984775842;
    static final int SPECIAL_ABILITY_NUMBER_ROULETTE=396940495;

    String difficulty;
    int ability_power_gun_stage;
    int ability_curesd_stage;
    int number_of_coins;

    SharedPreferences sp;

    Game game;

    boolean flag;
    boolean one_or_two_flag;
    boolean temp_one_or_two_flag;
    boolean is_vibrate;
    boolean isStart;
    boolean flag_thread_timer;
    boolean sound;
    boolean flag_foces;
    String oppendMine;

    View root;
    Vibrator vibrator;
    Button number_of_mines_box;
    ProgressBar speical_bar;
    GifImageView fireworks;
    TextView timer_tv;
    SoundPool soundPool;
    float volume;
    int coin_sound;
    int mine_sound;
    int frozen_sound;
    int key_sound;
    int destroy_sound;
    int click_sound;
    int activate_sound;
    int two_cubes_sound;
    int degree=0;
    int degree_old=0;
    MediaPlayer fireworks_sound;
    boolean isLoaded = false;
    ImageView restart_button;
    ImageView number_roulette;
    ImageView number_pointer;
    ImageView sound_icon;
    ImageView home_button;

    Random rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("detalis", MODE_PRIVATE);
        root = findViewById(R.id.rl);
        change = (ImageButton) findViewById(R.id.change_button);
        number_of_mines_box = (Button) findViewById(R.id.button);
        home_button=(ImageView)findViewById(R.id.home_button);
        speical_bar = (ProgressBar) findViewById(R.id.special_ability_bar);
        speical_ability_button = (ImageButton) findViewById(R.id.speical_ability_button);
        fireworks = (GifImageView) findViewById(R.id.fire_works);
        timer_tv = (TextView) findViewById(R.id.timer_text);
        restart_button = (ImageView) findViewById(R.id.imageView3);
        number_roulette=(ImageView)findViewById(R.id.imageView21);
        number_pointer=(ImageView)findViewById(R.id.imageView22);
        sound_icon=(ImageView)findViewById(R.id.imageView5);
        rnd=new Random();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            soundPool = new SoundPool.Builder().setMaxStreams(20).build();
        else
            soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 1);
        volume = 1;
        loadSound(this);

        if(sp.getBoolean("is_custom",false)){//new
            size=sp.getInt("custom_size", 9);//new
            num_of_booms=sp.getInt("custom_mine_number",0);//new
            home_button.setImageResource(R.drawable.grid);
        }
        else {//new
            size = sp.getInt("size", 9);
            difficulty = sp.getString("difficulty", "Easy");
            switch (difficulty) {
                case "Easy":
                    num_of_booms = size * size / 10 + 2;
                    break;
                case "Medium":
                    num_of_booms = (size * size / 10) + 9;
                    break;
                case "Hard":
                    num_of_booms = (size * size / 10) + 18;
                    break;
                case "Extreme":
                    num_of_booms = (size * size / 10) + 25;
                    break;
            }
        }
        flag = true;
        one_or_two_flag = false;
        temp_one_or_two_flag = false;
        isStart = false;
        timer_counter = 0;
        flag_thread_timer = false;
        flag_foces=true;
        ability_power_gun_stage = 1;//when ability4 is active
        ability_curesd_stage = 1;//when ability5 is active
        number_of_coins = 0;//when ability4 is active
        oppendMine = "";
        sound=sp.getBoolean("volume_in_game_board",true);
        if(sound)
            sound_icon.setImageResource(R.drawable.volumeonicon);
        else
            sound_icon.setImageResource(R.drawable.volumeofficon);

        speical_counter = 0;
        speical_key = sp.getInt("speical_ability_key", SPECIAL_ABILITY_FROZEN);
        if (speical_key == SPECIAL_ABILITY_FROZEN) {
            speical_ability_button.setImageResource(R.drawable.frozenmineicon);
            charge_ability = 15;
        } else if (speical_key == SPECIAL_ABILITY_ONE_OR_TWO) {
            speical_ability_button.setImageResource(R.drawable.halfcircle);
            charge_ability = 10;
        } else if (speical_key == SPECIAL_ABILITY_DESTROYED_MINE) {
            speical_ability_button.setImageResource(R.drawable.destroyedminetwo);
            charge_ability = 8;
        } else if (speical_key == SPECIAL_ABILITY_POWER_GUN) {
            speical_ability_button.setImageResource(R.drawable.abilityfour);
            charge_ability = 25;
        } else if(speical_key==SPECIAL_ABILITY_CURESD_BLADE){
            speical_ability_button.setImageResource(R.drawable.cursedbladeicon);
            charge_ability = 34;
        }
        else{
            speical_ability_button.setImageResource(R.drawable.roulette);
            charge_ability=3;
        }

        if(sp.getBoolean("is_custom",false))//new
        {
            game = new Game(sp.getInt("custom_size", 9), sp.getInt("custom_size", 9), sp.getInt("custom_mine_number", 0), CustomGameActivity.board);//new
        }
        else
             game = new Game(size, size, num_of_booms);
        number_of_mines_box.setText("" + game.getNumOfBooms());
        number_of_mines_box.setEnabled(false);
        number_of_mines_box.setTextColor(Color.parseColor("#3c3c3c"));

        fireworks.setVisibility(View.INVISIBLE);
        fireworks_sound = MediaPlayer.create(this, R.raw.fireworks);
        fireworks_sound.setLooping(true);
        fireworks_sound.start();
        fireworks_sound.pause();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        is_vibrate = sp.getBoolean("is_viabrte", true);

        if (sp.getBoolean("is_game_mode_look_active", false)&&!sp.getBoolean("is_custom",false)) {
            game.lookSpreed(num_of_booms / 2);
            game.keySpreed();
        }
        createGameBoard();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createGameBoard() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        LinearLayout myLay = (LinearLayout) findViewById(R.id.gameBoard);

        LinearLayout newRow;
        for (int row = 0; row < size; row++) {
            newRow = new LinearLayout(this);
            newRow.setOrientation(LinearLayout.HORIZONTAL);
            newRow.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            for (int col = 0; col < size; col++) {
                ImageView btn = new ImageView(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(width / size, width / size));
                btn.setOnClickListener(this);
                if (sp.getBoolean("is_long_click_is_active", false))
                    btn.setOnLongClickListener(this);
                if (game.isLooked(row, col)) {
                    btn.setImageResource(R.drawable.lookcube);
                    btn.setEnabled(false);
                } else
                    btn.setImageResource(R.drawable.buttom);
                btn.setTag(row + "-" + col);
                newRow.addView(btn);
            }
            myLay.addView(newRow);
        }
    }

    @Override
    public void onClick(View view) {
        ImageView btn = (ImageView) view;
        String string = btn.getTag().toString();
        one_or_two_flag = false;//when one or two ability is active

        if (!isStart) {
            isStart = true;
            flag_thread_timer = true;
            StartTimer();
        }

        String[] parts = string.split("-");
        String row = parts[0];
        String col = parts[1];

        if (game.checkBoom(Integer.parseInt(row), Integer.parseInt(col), flag)) {
            if (!flag) {
                if (game.isFlag(Integer.parseInt(row), Integer.parseInt(col))) {
                    btn.setImageResource(R.drawable.flag);
                    if (game.isGame_over()) {
                        flag_thread_timer = false;
                        speical_ability_button.setEnabled(false);
                        Toast.makeText(MainActivity.this, "You won!!!", Toast.LENGTH_SHORT).show();
                        fireworks.setVisibility(View.VISIBLE);
                        if(sound)
                            fireworks_sound.start();
                        if(!sp.getBoolean("is_custom",false)) {//new
                            saveScore();
                            is_unlock();
                        }
                    }
                } else
                    btn.setImageResource(R.drawable.buttom);
                number_of_mines_box.setText("" + game.getNumOfBooms());
            } else if (game.isGame_over()) {
                if (game.isLose()) {
                    if (is_vibrate)
                        vibrator.vibrate(500);
                    setEnabledAll();
                    btn.setImageResource(R.drawable.openboom);
                    oppendMine = string;
                    playSound(mine_sound);
                } else {
                    if (game.getNumber(Integer.parseInt(row), Integer.parseInt(col)) == 0)
                        openBoomsWithZero();
                    else
                        openBoom(Integer.parseInt(row), Integer.parseInt(col), btn);
                    if (game.isFrozen(Integer.parseInt(row), Integer.parseInt(col)))
                        number_of_mines_box.setText("" + game.getNumOfBooms());
                    Toast.makeText(MainActivity.this, "You won!!!", Toast.LENGTH_SHORT).show();
                    fireworks.setVisibility(View.VISIBLE);
                    if(sound)
                        fireworks_sound.start();
                    if(!sp.getBoolean("is_custom",false)) {//new
                        saveScore();
                        is_unlock();
                    }
                }
                speical_ability_button.setEnabled(false);
                flag_thread_timer = false;
            } else {
                speical_counter += charge_ability;
                if (speical_counter >= 100) {/////////////////////////////////////set speical ablility countur
                    speical_counter = 100;
                    speical_ability_button.setBackgroundColor(Color.parseColor("#a4c639"));
                }
                speical_bar.setProgress(speical_counter);
                if (game.isCoin(Integer.parseInt(row), Integer.parseInt(col))) {
                    if (game.getNumber(Integer.parseInt(row), Integer.parseInt(col)) == 0) {
                        openBoomsWithZero();
                    } else {
                        final int row_coin = Integer.parseInt(row);
                        final int col_coin = Integer.parseInt(col);
                        btn.setImageResource(R.drawable.coincube);
                        playSound(coin_sound);
                        game.changeCoinTofalse(Integer.parseInt(row), Integer.parseInt(col));
                        restart_button.setEnabled(false);//avoid bug
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(600);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message myMsg = coinHandler.obtainMessage();
                                myMsg.getData().putInt("row", row_coin);
                                myMsg.getData().putInt("col", col_coin);
                                coinHandler.sendMessage(myMsg);
                            }
                        };
                        Thread myThread = new Thread(runnable);
                        myThread.start();
                    }
                } else if (game.isCuresd(Integer.parseInt(row), Integer.parseInt(col))) {
                    openCuresd(Integer.parseInt(row), Integer.parseInt(col), btn);
                    speical_counter = 0;
                    speical_bar.setProgress(0);
                } else if (game.isKey(Integer.parseInt(row), Integer.parseInt(col))) {
                    if (game.getNumber(Integer.parseInt(row), Integer.parseInt(col)) == 0) {
                        openBoomsWithZero();
                    }
                    btn.setImageResource(R.drawable.keyopen);
                    game.changeKeyToFalse(Integer.parseInt(row), Integer.parseInt(col));
                    playSound(key_sound);
                    restart_button.setEnabled(false);//להימנע מבגים
                    final int row_coin = Integer.parseInt(row);
                    final int col_coin = Integer.parseInt(col);
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message myMsg = keyHandler.obtainMessage();
                            myMsg.getData().putInt("row", row_coin);
                            myMsg.getData().putInt("col", col_coin);
                            keyHandler.sendMessage(myMsg);
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                } else {
                    playSound(click_sound);
                    switch (game.getNumber(Integer.parseInt(row), Integer.parseInt(col))) {
                        case 0:
                            openBoomsWithZero();
                            break;
                        case 1:
                            btn.setImageResource(R.drawable.one);
                            break;
                        case 2:
                            btn.setImageResource(R.drawable.two);
                            break;
                        case 3:
                            btn.setImageResource(R.drawable.three);
                            break;
                        case 4:
                            btn.setImageResource(R.drawable.four);
                            break;
                        case 5:
                            btn.setImageResource(R.drawable.five);
                            break;
                        case 6:
                            btn.setImageResource(R.drawable.six);
                            break;
                        case 7:
                            btn.setImageResource(R.drawable.seven);
                            break;
                        case 8:
                            btn.setImageResource(R.drawable.eight);
                            break;
                        case 9: {
                            btn.setImageResource(R.drawable.frozenopen2);
                            playSound(frozen_sound);
                            number_of_mines_box.setText("" + game.getNumOfBooms());
                        }
                        break;
                    }
                }
            }
        }

    }

    @Override
    public boolean onLongClick(View view) {
        ImageView btn = (ImageView) view;
        String string = btn.getTag().toString();
        one_or_two_flag = false;//when one or two ability is active

        if (!isStart) {
            isStart = true;
            flag_thread_timer = true;
            StartTimer();
        }

        String[] parts = string.split("-");
        String row = parts[0];
        String col = parts[1];

        if (game.checkBoom(Integer.parseInt(row), Integer.parseInt(col), false)) {
            if (game.isFlag(Integer.parseInt(row), Integer.parseInt(col))) {
                btn.setImageResource(R.drawable.flag);
                if (game.isGame_over()) {
                    flag_thread_timer = false;
                    speical_ability_button.setEnabled(false);
                    Toast.makeText(MainActivity.this, "You won!!!", Toast.LENGTH_SHORT).show();
                    fireworks.setVisibility(View.VISIBLE);
                    if(sound)
                        fireworks_sound.start();
                    if(!sp.getBoolean("is_custom",false)) {//new
                        saveScore();
                        is_unlock();
                    }
                }
            } else
                btn.setImageResource(R.drawable.buttom);
            number_of_mines_box.setText("" + game.getNumOfBooms());
        }
        return true;
    }

    public void createNewGame(View view) {
        if(sp.getBoolean("is_custom",false))//new
            game=new Game(sp.getInt("custom_size",9),sp.getInt("custom_size",9),sp.getInt("custom_mine_number",0),CustomGameActivity.board);//new
        else
            game = new Game(size, size, num_of_booms);
        ImageView btn;
        number_of_mines_box.setText("" + game.getNumOfBooms());
        speical_counter = 0;
        one_or_two_flag = false;
        isStart = false;
        timer_counter = 0;
        flag_thread_timer = false;
        if (ability_power_gun_stage != 1 && sp.getInt("speical_ability_key", SPECIAL_ABILITY_FROZEN) == SPECIAL_ABILITY_POWER_GUN) {
            speical_ability_button.setImageResource(R.drawable.abilityfour);
            charge_ability = 25;
            ability_power_gun_stage = 1;
        }
        if (ability_curesd_stage != 1 && sp.getInt("speical_ability_key", SPECIAL_ABILITY_FROZEN) == SPECIAL_ABILITY_CURESD_BLADE) {
            charge_ability = 34;
            ability_curesd_stage = 1;
        }

        timer_tv.setText("00:00");
        speical_bar.setProgress(0);
        speical_ability_button.setEnabled(true);
        speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
        fireworks.setVisibility(View.INVISIBLE);
        fireworks_sound.pause();

        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++) {
                btn = (ImageView) root.findViewWithTag(row + "-" + col);
                btn.setImageResource(R.drawable.buttom);
                btn.setEnabled(true);
            }

        if (sp.getBoolean("is_game_mode_look_active", false)&&!sp.getBoolean("is_custom",false)) {
            game.lookSpreed(num_of_booms / 2);
            game.keySpreed();
            for (int r = 0; r < size; r++)
                for (int c = 0; c < size; c++)
                    if (game.isLooked(r, c)) {
                        btn = (ImageView) root.findViewWithTag(r + "-" + c);
                        btn.setImageResource(R.drawable.lookcube);
                        btn.setEnabled(false);
                    }
        }/////new
        if(sp.getBoolean("is_custom",false)){
            for (int r = 0; r < size; r++)
                for (int c = 0; c < size; c++)
                    if (game.isLooked(r, c)) {
                        btn = (ImageView) root.findViewWithTag(r + "-" + c);
                        btn.setImageResource(R.drawable.lookcube);
                        btn.setEnabled(false);
                    }
        }
        //////
    }

    public void setEnabledAll() {
        ImageView btn;
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++) {
                btn = (ImageView) root.findViewWithTag(row + "-" + col);
                if (!game.isOpen(row, col))
                    if (!game.isBoom(row, col) && game.isFlag(row, col))
                        //btn.setImageResource(R.drawable.flag_false);
                        btn.setImageResource(R.drawable.flagfalse);
                    else if (game.isBoom(row, col) && game.isFlag(row, col))
                        btn.setImageResource(R.drawable.flag);
                    else if (game.isLooked(row, col))
                        btn.setImageResource(R.drawable.lookcube);
                    else
                        openBoom(row, col, btn);
                btn.setEnabled(false);
            }
    }

    public void openBoomsWithZero() {
        ImageView btn;
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++) {
                if (game.isOpen(row, col)) {
                    btn = (ImageView) root.findViewWithTag(row + "-" + col);
                    openBoom(row, col, btn);
                }

            }
    }

    private void openBoom(int row, int col, ImageView btn) {
        if (!game.isLooked(row, col)) {
            if (game.isBoom(row, col) && !game.isFrozen(row, col) && !game.isDestroyed(row, col))
                btn.setImageResource(R.drawable.bomb);
            else {
                if (game.isCoin(row, col)) {
                    final int row_coin = row;
                    final int col_coin = col;
                    btn.setImageResource(R.drawable.coincube);
                    if (!game.isGame_over())
                        playSound(coin_sound);
                    game.changeCoinTofalse(row, col);
                    restart_button.setEnabled(false);//avoid bug
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message myMsg = coinHandler.obtainMessage();
                            myMsg.getData().putInt("row", row_coin);
                            myMsg.getData().putInt("col", col_coin);
                            coinHandler.sendMessage(myMsg);
                        }
                    };
                    Thread myThread = new Thread(runnable);
                    myThread.start();

                } else if (game.isKey(row, col)) {
                    btn.setImageResource(R.drawable.keyopen);
                    game.changeKeyToFalse(row, col);
                    if (!game.isGame_over())
                        playSound(key_sound);
                    restart_button.setEnabled(false);//להימנע מבגים
                    final int row_coin = row;
                    final int col_coin = col;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message myMsg = keyHandler.obtainMessage();
                            myMsg.getData().putInt("row", row_coin);
                            myMsg.getData().putInt("col", col_coin);
                            keyHandler.sendMessage(myMsg);
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                } else
                    switch (game.getNumber(row, col)) {
                        case 0:
                            btn.setImageResource(R.drawable.open);
                            break;
                        case 1:
                            btn.setImageResource(R.drawable.one);
                            break;
                        case 2:
                            btn.setImageResource(R.drawable.two);
                            break;
                        case 3:
                            btn.setImageResource(R.drawable.three);
                            break;
                        case 4:
                            btn.setImageResource(R.drawable.four);
                            break;
                        case 5:
                            btn.setImageResource(R.drawable.five);
                            break;
                        case 6:
                            btn.setImageResource(R.drawable.six);
                            break;
                        case 7:
                            btn.setImageResource(R.drawable.seven);
                            break;
                        case 8:
                            btn.setImageResource(R.drawable.eight);
                            break;
                        case 9:
                            btn.setImageResource(R.drawable.frozenopen2);
                            break;
                        case 10: {
                            btn.setImageResource(R.drawable.destroyedmineopen2);
                            number_of_mines_box.setText("" + game.getNumOfBooms());
                            if (game.isGame_over()) {
                                speical_ability_button.setEnabled(false);
                                flag_thread_timer = false;
                                Toast.makeText(MainActivity.this, "You won!!!", Toast.LENGTH_SHORT).show();
                                fireworks.setVisibility(View.VISIBLE);
                                if(sound)
                                    fireworks_sound.start();
                                if(!sp.getBoolean("is_custom",false)) {//new
                                    saveScore();
                                    is_unlock();
                                }
                            }
                        }
                        break;
                    }
            }
        }
    }

    private void openCuresd(final int row, final int col, ImageView btn) {
        switch (game.getNumber(row, col)) {
            case 1:
                btn.setImageResource(R.drawable.onecuresd);
                break;
            case 2:
                btn.setImageResource(R.drawable.twocuresd);
                break;
            case 3:
                btn.setImageResource(R.drawable.threecuresd);
                break;
            case 4:
                btn.setImageResource(R.drawable.fourcuresd);
                break;
            case 5:
                btn.setImageResource(R.drawable.fivecuresd);
                break;
            case 6:
                btn.setImageResource(R.drawable.sixcuresd);
                break;
            case 7:
                btn.setImageResource(R.drawable.sevencuresd);
                break;
            case 8:
                btn.setImageResource(R.drawable.eightcuresd);
                break;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message myMsg = coinHandler.obtainMessage();
                myMsg.getData().putInt("row", row);
                myMsg.getData().putInt("col", col);
                curesdHandler.sendMessage(myMsg);
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();
        if (game.getNumber(row, col) == 0)
            openBoomsWithZero();

    }

    public void flag(View view) {
        ImageButton btn = (ImageButton) view;
        if (flag) {
            flag = false;
            btn.setImageResource(R.drawable.flagtwo);

        } else {
            flag = true;
            btn.setImageResource(R.drawable.bombtwo);
        }
    }

    public void StartTimer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (flag_thread_timer) {
                    synchronized (this) {
                        try {
                            Thread.sleep(1000);
                            if(flag_foces)
                                timer_counter++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Message myMsg = TimerHandler.obtainMessage();
                    myMsg.getData().putInt("count", timer_counter);
                    TimerHandler.sendMessage(myMsg);
                }
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    Handler TimerHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (flag_thread_timer&&flag_foces) {
                if (!isStart)
                    timer_tv.setText("00:00");
                else {
                    int count = message.getData().getInt("count");
                    int mins = 0;
                    String plus_c = "";
                    String plus_m = "";
                    while (count >= 60) {
                        if (count >= 60) {
                            mins++;
                            count -= 60;
                        }
                    }
                    if (count < 10)
                        plus_c = "0";
                    else
                        plus_c = "";
                    if (mins < 10)
                        plus_m = "0";
                    else
                        plus_m = "";
                    timer_tv.setText(plus_m + mins + ":" + plus_c + count);
                }
            }
        }
    };

    private void loadSound(Context context) {
        if (!isLoaded) {
            coin_sound = soundPool.load(context, R.raw.coin, 1);
            mine_sound = soundPool.load(context, R.raw.mine, 1);
            frozen_sound = soundPool.load(context, R.raw.freeze, 1);
            key_sound = soundPool.load(context, R.raw.key, 1);
            destroy_sound = soundPool.load(context, R.raw.destroyrobotsound, 1);
            click_sound = soundPool.load(context, R.raw.clickacube, 1);
            activate_sound = soundPool.load(context, R.raw.activateability, 1);
            two_cubes_sound=soundPool.load(context,R.raw.twoenemies,1);
        }
        isLoaded = true;
    }

    private void playSound(int id) {
        if(sound)
            soundPool.play(id, volume, volume, 1, 0, 1);
    }

    public void special_ability(final View view) {
        if (speical_key == SPECIAL_ABILITY_FROZEN)
        {
            if (speical_counter == 100 && game.getNumOfBooms() != 0 && game.isFrozenAvailble()) {
                game.setFrozen();
                speical_counter = 0;
                speical_bar.setProgress(0);
                speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
            }
            if (!game.isFrozenAvailble()) {
                speical_counter = 0;
                speical_bar.setProgress(0);
                speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
            }
        } else if (speical_key == SPECIAL_ABILITY_ONE_OR_TWO) {
            if (speical_counter == 100 && game.getNumOfBooms() != 0 && game.getNumber_of_cube_without_boom() != 0) {
                String str = game.getMineAndCube();

                String[] parts = str.split("/");
                String mine_str = parts[0];
                String cube_str = parts[1];

                String[] parts2 = mine_str.split("-");
                String mine_one = parts2[0];
                String mine_two = parts2[1];
                int mine_r = Integer.parseInt(mine_one);
                int mine_c = Integer.parseInt(mine_two);

                String[] parts3 = cube_str.split("-");
                String cube_one = parts3[0];
                String cube_two = parts3[1];
                int cube_r = Integer.parseInt(cube_one);
                int cube_c = Integer.parseInt(cube_two);

                one_or_two_flag = true;
                Message myMsg = handler.obtainMessage();
                myMsg.getData().putBoolean("key", true);
                myMsg.getData().putInt("mine_r", mine_r);
                myMsg.getData().putInt("mine_c", mine_c);
                myMsg.getData().putInt("cube_r", cube_r);
                myMsg.getData().putInt("cube_c", cube_c);
                one_or_two_change(mine_r, mine_c, cube_r, cube_c);
                handler.sendMessage(myMsg);
            }
            if (speical_counter == 100) {
                playSound(two_cubes_sound);
                speical_counter = 0;
                speical_bar.setProgress(0);
                speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
            }
        } else if (speical_key == SPECIAL_ABILITY_DESTROYED_MINE) {
            if (speical_counter == 100 && game.getNumOfBooms() != 0) {
                String str = game.destroyMine();

                ImageView btn = (ImageView) root.findViewWithTag(str);
                String[] parts = str.split("-");
                String destroyed_mine_r = parts[0];
                String desyroyed_cube_c = parts[1];
                if (!game.isGame_over())
                    playSound(destroy_sound);
                openBoom(Integer.parseInt(destroyed_mine_r), Integer.parseInt(desyroyed_cube_c), btn);
            }
            if (speical_counter == 100) {
                speical_counter = 0;
                speical_bar.setProgress(0);
                speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
            }

        } else if (speical_key == SPECIAL_ABILITY_POWER_GUN) {
            if (speical_counter == 100 && game.getNumOfBooms() != 0) {
                if (ability_power_gun_stage == 1 && game.getNumber_of_cube_without_boom() >= 3 && game.getNumber_of_cube_without_boom() - game.getNumberOfkey() >= 3) {
                    game.coinSpreed();
                    playSound(activate_sound);
                    speical_ability_button.setImageResource(R.drawable.abilityfournumberzero);
                    speical_ability_button.setEnabled(false);
                    charge_ability = 0;
                    speical_counter = 0;
                    speical_bar.setProgress(0);
                    speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
                    number_of_coins = 3;
                    ability_power_gun_stage = 2;
                } else if (ability_power_gun_stage == 3) {
                    int counter = 2;
                    while (counter > 0) {
                        if (game.getNumOfBooms() != 0) {
                            String str = game.destroyMine();
                            ImageView btn = (ImageView) root.findViewWithTag(str);
                            String[] parts = str.split("-");
                            String destroyed_mine_r = parts[0];
                            String desyroyed_cube_c = parts[1];
                            openBoom(Integer.parseInt(destroyed_mine_r), Integer.parseInt(desyroyed_cube_c), btn);
                            counter--;
                        } else
                            counter = 0;
                    }
                    if (!game.isGame_over())
                        playSound(destroy_sound);
                    speical_counter = 0;
                    speical_bar.setProgress(0);
                    speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
                }
                if (speical_counter == 100) {
                    speical_counter = 0;
                    speical_bar.setProgress(0);
                    speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
                }

            }

        } else if(speical_key==SPECIAL_ABILITY_CURESD_BLADE){
            if (speical_counter == 100 && game.getNumOfBooms() != 0) {
                if (ability_curesd_stage == 1 && game.getNumber_of_cube_without_boom() >= 20 && game.getNumber_of_cube_without_boom() - game.getNumberOfkey() >= 20) {
                    game.curesdSpreed();
                    charge_ability = 5;
                    speical_counter = 0;
                    ability_curesd_stage = 2;
                    speical_bar.setProgress(0);
                    speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
                } else if (ability_curesd_stage == 2) {
                    setEnabledAll();
                    flag_thread_timer = false;
                    speical_ability_button.setEnabled(false);
                    speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
                    speical_counter = 0;
                    speical_bar.setProgress(0);
                    Toast.makeText(MainActivity.this, "You won!!!", Toast.LENGTH_SHORT).show();
                    fireworks.setVisibility(View.VISIBLE);
                    fireworks_sound.start();
                    number_of_mines_box.setText("0");
                }
            }
        }
        else{
            if(speical_counter == 100){
                number_roulette.setImageResource(R.drawable.numberspiner);
                number_pointer.setImageResource(R.drawable.arrow);
                number_pointer.setVisibility(View.VISIBLE);
                speical_bar.setProgress(0);
                speical_ability_button.setBackgroundColor(Color.parseColor("#dadadc"));
                speical_counter=0;
                degree_old=degree%360;
                degree= rnd.nextInt(3600)+720;
                final RotateAnimation rotate=new RotateAnimation(degree_old,degree,
                        RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
                rotate.setDuration(3600);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new DecelerateInterpolator());
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        final int number=curruntNumberFromRoulette(360-(degree %360));
                        Runnable runnable=new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(600);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Message myMsg = stopAnim.obtainMessage();
                                myMsg.getData().putInt("number",number);
                                stopAnim.sendMessage(myMsg);
                            }
                        };
                        Thread thread=new Thread(runnable);
                        thread.start();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                number_roulette.startAnimation(rotate);
            }
        }
    }
    private int curruntNumberFromRoulette(int degrees){
        int num=-1;
        if(degrees>=0&&degrees<FACTOR)
            num=4;
        else if(degrees>=FACTOR&&degrees<FACTOR*2)
            num= 6;
        else if(degrees>=FACTOR*2&&degrees<FACTOR*3)
            num=2;
        else if(degrees>=FACTOR*3&&degrees<FACTOR*4)
            num=5;
        else if(degrees>=FACTOR*4&&degrees<FACTOR*5)
            num=3;
        else if(degrees>=FACTOR*5&&degrees<FACTOR*6)
            num=8;
        else if(degrees>=FACTOR*6&&degrees<FACTOR*7)
            num=1;
        else
            num=7;
        return num;
    }

    public void sound(View view) {
        SharedPreferences.Editor editor=sp.edit();
        if (sound) {
            sound_icon.setImageResource(R.drawable.volumeofficon);
            editor.putBoolean("volume_in_game_board",false);
            sound=false;
            if(!game.isLose()&&game.isGame_over())
                fireworks_sound.pause();

        } else
        {
            sound_icon.setImageResource(R.drawable.volumeonicon);
            editor.putBoolean("volume_in_game_board",true);
            sound=true;
            if(!game.isLose()&&game.isGame_over())
                fireworks_sound.start();
        }
        editor.commit();
    }

    public void back(View view) {
        ////new
        if(sp.getBoolean("is_custom",false))
        {
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_custom",false);
            editor.commit();
        }
        /////
        finish();
        fireworks_sound.stop();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Game?");
        builder.setMessage("Are you sure you want to exit the game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /////new
                if(sp.getBoolean("is_custom",false))
                {
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean("is_custom",false);
                    editor.commit();
                }
                /////
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

    private void one_or_two_change(final int mine_r, final int mine_c, final int cube_r, final int cube_c) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (one_or_two_flag) {
                    {
                        synchronized (this) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Message myMsg = handler.obtainMessage();
                    if (temp_one_or_two_flag) {
                        myMsg.getData().putBoolean("key", true);
                        temp_one_or_two_flag = false;
                    } else {
                        myMsg.getData().putBoolean("key", false);
                        temp_one_or_two_flag = true;
                    }
                    myMsg.getData().putInt("mine_r", mine_r);
                    myMsg.getData().putInt("mine_c", mine_c);
                    myMsg.getData().putInt("cube_r", cube_r);
                    myMsg.getData().putInt("cube_c", cube_c);
                    handler.sendMessage(myMsg);
                }
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();
    }
    public void saveScore(){
        String time=timer_tv.getText().toString();
        Score score=new Score(difficulty,time,size);
        ScoresLIst.addScore(score);
    }
    ///for missoins!//////////////////
    private void is_unlock(){
        SharedPreferences.Editor editor = sp.edit();
        int power_gun_mission=sp.getInt("POWER_GUN_MISSION",0);
        if(power_gun_mission+1==10) {
            editor.putInt("POWER_GUN_MISSION", power_gun_mission + 1);
            editor.putBoolean("speical_ability_POWER_GUN_is_unlock", true);
            Toast.makeText(MainActivity.this, "You unlocked the Power Gun!!!", Toast.LENGTH_SHORT).show();
        }
        if(power_gun_mission<9) {
            Toast.makeText(MainActivity.this, " Power Gun Mission progress: "+(power_gun_mission+1)+"/10", Toast.LENGTH_SHORT).show();
            editor.putInt("POWER_GUN_MISSION", power_gun_mission + 1);
        }

        if(difficulty.equals("Hard")){
            int cursed_blade_mission=sp.getInt("CURESD_BLADE_MISSION",0);
            if(cursed_blade_mission+1==20) {
                editor.putInt("CURESD_BLADE_MISSION", cursed_blade_mission + 1);
                editor.putBoolean("speical_ability_CURESD_BLADE_is_unlock", true);
                Toast.makeText(MainActivity.this, "You unlocked the Cursed Blade!!!", Toast.LENGTH_SHORT).show();
            }
            if(cursed_blade_mission<19) {
                Toast.makeText(MainActivity.this, " Curesd Blade Mission progress: "+(cursed_blade_mission+1)+"/20", Toast.LENGTH_SHORT).show();
                editor.putInt("CURESD_BLADE_MISSION", cursed_blade_mission + 1);
            }
        }

        if(difficulty.equals("Extreme")&&size==14){
            int number_roulette_mission=sp.getInt("NUMBER_ROULETTE_MISSION",0);
            if(number_roulette_mission+1==3) {
                editor.putInt("NUMBER_ROULETTE_MISSION", number_roulette_mission + 1);
                editor.putBoolean("speical_ability_NUMBER_ROULETTE_is_unlock", true);
                Toast.makeText(MainActivity.this, "You unlocked the Number Roulette!!!", Toast.LENGTH_SHORT).show();
            }
            if(number_roulette_mission<2) {
                Toast.makeText(MainActivity.this, " Number Roulette Mission progress: "+(number_roulette_mission+1)+"/3", Toast.LENGTH_SHORT).show();
                editor.putInt("NUMBER_ROULETTE_MISSION", number_roulette_mission + 1);
            }
        }
        editor.commit();
    }
    //////////////
    @Override
    public void onStop() {
        fireworks_sound.pause();
        super.onStop();
    }

    @Override
    public void onRestart() {
        if (game.isGame_over() && !game.isLose()&&sound)
            fireworks_sound.start();
        super.onRestart();
    }

    @Override
    public void onDestroy() {
        /////new
        if(sp.getBoolean("is_custom",false))
        {
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean("is_custom",false);
            editor.commit();
        }
        /////
        fireworks_sound.stop();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
            flag_foces=true;
        else
            flag_foces=false;

    }

    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            boolean temp = message.getData().getBoolean("key");
            ImageView btn_mine = (ImageView) root.findViewWithTag(message.getData().getInt("mine_r") + "-" + message.getData().getInt("mine_c"));
            ImageView btn_cube = (ImageView) root.findViewWithTag(message.getData().getInt("cube_r") + "-" + message.getData().getInt("cube_c"));
            if (!one_or_two_flag)
                if (game.isGame_over() && oppendMine.equals(message.getData().getInt("mine_r") + "-" + message.getData().getInt("mine_c")))
                    btn_mine.setImageResource(R.drawable.openboom);
                else {
                    if (!game.isOpen(message.getData().getInt("cube_r"), message.getData().getInt("cube_c")) && !game.isFlag(message.getData().getInt("cube_r"), message.getData().getInt("cube_c")) && !game.isGame_over())
                        if (!game.isLooked(message.getData().getInt("cube_r"), message.getData().getInt("cube_c")))
                            btn_cube.setImageResource(R.drawable.buttom);
                        else
                            btn_cube.setImageResource(R.drawable.lookcube);
                    if (!game.isFlag(message.getData().getInt("mine_r"), message.getData().getInt("mine_c")) && !game.isGame_over())
                        if (!game.isLooked(message.getData().getInt("mine_r"), message.getData().getInt("mine_c")))
                            btn_mine.setImageResource(R.drawable.buttom);
                        else
                            btn_mine.setImageResource(R.drawable.lookcube);
                }
            else if (temp) {
                if (game.isLooked(message.getData().getInt("mine_r"), message.getData().getInt("mine_c")))
                    btn_mine.setImageResource(R.drawable.lockonortwo);
                else
                    btn_mine.setImageResource(R.drawable.abilitiyoneoftwo);
                if (game.isLooked(message.getData().getInt("cube_r"), message.getData().getInt("cube_c")))
                    btn_cube.setImageResource(R.drawable.lockonortwo);
                else
                    btn_cube.setImageResource(R.drawable.abilitiyoneoftwo);
            } else {
                if (game.isLooked(message.getData().getInt("mine_r"), message.getData().getInt("mine_c")))
                    btn_mine.setImageResource(R.drawable.lookcube);
                else
                    btn_mine.setImageResource(R.drawable.buttom);
                if (game.isLooked(message.getData().getInt("cube_r"), message.getData().getInt("cube_c")))
                    btn_cube.setImageResource(R.drawable.lookcube);
                else
                    btn_cube.setImageResource(R.drawable.buttom);
            }
        }
    };

    Handler coinHandler = new Handler() {
        public void handleMessage(Message message) {
            int row = message.getData().getInt("row");
            int col = message.getData().getInt("col");
            ImageView btn = (ImageView) root.findViewWithTag(message.getData().getInt("row") + "-" + message.getData().getInt("col"));
            if (!game.isGame_over()) {
                switch (game.getNumber(row, col)) {
                    case 0:
                        btn.setImageResource(R.drawable.open);
                        break;
                    case 1:
                        btn.setImageResource(R.drawable.one);
                        break;
                    case 2:
                        btn.setImageResource(R.drawable.two);
                        break;
                    case 3:
                        btn.setImageResource(R.drawable.three);
                        break;
                    case 4:
                        btn.setImageResource(R.drawable.four);
                        break;
                    case 5:
                        btn.setImageResource(R.drawable.five);
                        break;
                    case 6:
                        btn.setImageResource(R.drawable.six);
                        break;
                    case 7:
                        btn.setImageResource(R.drawable.seven);
                        break;
                    case 8:
                        btn.setImageResource(R.drawable.eight);
                        break;
                }
                number_of_coins--;
                switch (3 - number_of_coins) {
                    case 1:
                        speical_ability_button.setImageResource(R.drawable.abilityfournumberone);
                        speical_bar.setProgress(33);
                        speical_counter = 33;
                        break;
                    case 2:
                        speical_ability_button.setImageResource(R.drawable.abilityfournumberthree);
                        speical_bar.setProgress(66);
                        speical_counter = 66;
                        break;
                    case 3:
                        speical_ability_button.setImageResource(R.drawable.abilityfour);
                        speical_bar.setProgress(0);
                        ability_power_gun_stage = 3;
                        speical_ability_button.setEnabled(true);
                        charge_ability = 25;
                        speical_counter = 0;
                        break;
                }
            } else if (!game.isLose()) {
                switch (game.getNumber(row, col)) {
                    case 0:
                        btn.setImageResource(R.drawable.open);
                        break;
                    case 1:
                        btn.setImageResource(R.drawable.one);
                        break;
                    case 2:
                        btn.setImageResource(R.drawable.two);
                        break;
                    case 3:
                        btn.setImageResource(R.drawable.three);
                        break;
                    case 4:
                        btn.setImageResource(R.drawable.four);
                        break;
                    case 5:
                        btn.setImageResource(R.drawable.five);
                        break;
                    case 6:
                        btn.setImageResource(R.drawable.six);
                        break;
                    case 7:
                        btn.setImageResource(R.drawable.seven);
                        break;
                    case 8:
                        btn.setImageResource(R.drawable.eight);
                        break;
                }
            }
            restart_button.setEnabled(true);
        }

    };
    Handler curesdHandler = new Handler() {
        public void handleMessage(Message message) {
            int row = message.getData().getInt("row");
            int col = message.getData().getInt("col");
            ImageView btn = (ImageView) root.findViewWithTag(message.getData().getInt("row") + "-" + message.getData().getInt("col"));
            switch (game.getNumber(row, col)) {
                case 1:
                    btn.setImageResource(R.drawable.one);
                    break;
                case 2:
                    btn.setImageResource(R.drawable.two);
                    break;
                case 3:
                    btn.setImageResource(R.drawable.three);
                    break;
                case 4:
                    btn.setImageResource(R.drawable.four);
                    break;
                case 5:
                    btn.setImageResource(R.drawable.five);
                    break;
                case 6:
                    btn.setImageResource(R.drawable.six);
                    break;
                case 7:
                    btn.setImageResource(R.drawable.seven);
                    break;
                case 8:
                    btn.setImageResource(R.drawable.eight);
                    break;
            }
        }
    };
    Handler keyHandler = new Handler() {
        public void handleMessage(Message message) {
            int row = message.getData().getInt("row");
            int col = message.getData().getInt("col");
            ImageView btn = (ImageView) root.findViewWithTag(message.getData().getInt("row") + "-" + message.getData().getInt("col"));
            if (!game.isGame_over()) {
                switch (game.getNumber(row, col)) {
                    case 0:
                        btn.setImageResource(R.drawable.open);
                        break;
                    case 1:
                        btn.setImageResource(R.drawable.one);
                        break;
                    case 2:
                        btn.setImageResource(R.drawable.two);
                        break;
                    case 3:
                        btn.setImageResource(R.drawable.three);
                        break;
                    case 4:
                        btn.setImageResource(R.drawable.four);
                        break;
                    case 5:
                        btn.setImageResource(R.drawable.five);
                        break;
                    case 6:
                        btn.setImageResource(R.drawable.six);
                        break;
                    case 7:
                        btn.setImageResource(R.drawable.seven);
                        break;
                    case 8:
                        btn.setImageResource(R.drawable.eight);
                        break;
                }

                for (int r = 0; r < size; r++)
                    for (int c = 0; c < size; c++) {
                        if (game.isLooked(r, c)) {
                            ImageView img_btn = (ImageView) root.findViewWithTag(r + "-" + c);
                            img_btn.setImageResource(R.drawable.buttom);
                            img_btn.setEnabled(true);
                            game.changeKeyToFalse(r, c);
                        }

                    }
            }
            game.destroyLookedCubes();
            restart_button.setEnabled(true);//להימנע מבגים
        }
    };
    Handler stopAnim=new Handler(){
        public void handleMessage(Message message){
            int number=message.getData().getInt("number");
            for(int i=0;i<size;i++)
                for (int j=0;j<size;j++){
                    if(game.getNumber(i,j)==number&&!game.isOpen(i,j)&&!game.isLooked(i,j)&&!game.isFlag(i,j)) {
                        game.setCountOffNumberOfCubesWithoutBooms();
                        game.setIsopen(i, j);
                        ImageView img_btn = (ImageView) root.findViewWithTag(i + "-" + j);
                        openBoom(i,j,img_btn);
                    }
                }
            if(game.isGame_over()&&!game.isLose()) {
                Toast.makeText(MainActivity.this, "You won!!!", Toast.LENGTH_SHORT).show();
                fireworks.setVisibility(View.VISIBLE);
                if(sound)
                    fireworks_sound.start();
                if(!sp.getBoolean("is_custom",false)) {//new
                    saveScore();
                    is_unlock();
                }
            }
            number_roulette.setImageResource(R.drawable.transparnt);
            number_pointer.setImageResource(R.drawable.transparnt);
        }
    };

}
