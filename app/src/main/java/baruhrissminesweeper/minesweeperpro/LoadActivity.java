package baruhrissminesweeper.minesweeperpro;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadActivity extends AppCompatActivity {
    TextView tv;
    boolean flag;
    boolean key;
    boolean canOpen;
    ProgressBar bar;
    int loadCount;
    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load);
        tv=(TextView)findViewById(R.id.popText);
        bar=(ProgressBar)findViewById(R.id.loadBar);
        flag=false;
        key=false;
        canOpen=false;
        myHandler=new Handler();
        loadCount=0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(loadCount<100){
                    loadCount++;
                    android.os.SystemClock.sleep(7);
                    myHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            bar.setProgress(loadCount);
                        }
                    });
                }
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("");
                    }
                });
            }

        }).start();

        Runnable r=new Runnable() {
            @Override
            public void run()
            {
                synchronized (this) {
                    try {
                        Thread.sleep(1000);
                        //bar.setVisibility(View.INVISIBLE);//need to check / work in Emulator not in phone
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                canOpen=true;
                while (!key)
                {
                    {
                        synchronized (this)
                        {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    Message myMsg=handler.obtainMessage();
                    if(flag) {
                        myMsg.getData().putBoolean("key", true);
                        flag=false;
                    }
                    else {
                        myMsg.getData().putBoolean("key", false);
                        flag=true;
                    }
                    handler.sendMessage(myMsg);

                }
            }
        };
        Thread myThread=new Thread(r);
        myThread.start();
    }
    Handler handler=new Handler(){
        public void handleMessage(Message message){
            boolean temp=message.getData().getBoolean("key");
            if(!temp)
                tv.setText("Tap anywhere to countinue");
            else
                tv.setText("");
        }
    };
    public void onClick(View view){
        if(canOpen) {
            key = true;
            Intent homeIntent = new Intent(LoadActivity.this, StartActivity.class);
            startActivity(homeIntent);
            finish();
        }

    }
}