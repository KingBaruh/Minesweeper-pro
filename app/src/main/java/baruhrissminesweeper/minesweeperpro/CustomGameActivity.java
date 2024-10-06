package baruhrissminesweeper.minesweeperpro;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CustomGameActivity extends AppCompatActivity implements View.OnClickListener {

    public static String[][]board;
    String select;
    int size;
    int mine_count;
    Button size_button;
    LinearLayout custon_game_board;
    ImageButton earse;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_game);
        sp = getSharedPreferences("detalis", MODE_PRIVATE);

        size=9;
        mine_count=0;
        custon_game_board=(LinearLayout)findViewById(R.id.customgameboard);
        size_button=(Button)findViewById(R.id.size);
        earse=(ImageButton)findViewById(R.id.earse);

        createCustomGameBoard();
        board=new String[size][size];
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                board[i][j]="";
        select="";
    }
    public void createCustomGameBoard() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        LinearLayout myLay = (LinearLayout) findViewById(R.id.customgameboard);

        LinearLayout newRow;
        for (int row = 0; row < size; row++) {
            newRow = new LinearLayout(this);
            newRow.setOrientation(LinearLayout.HORIZONTAL);
            newRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int col = 0; col < size; col++) {
                ImageView btn = new ImageView(this);
                btn.setLayoutParams(new LinearLayout.LayoutParams(width / size, width / size));
                btn.setOnClickListener(this);
                btn.setImageResource(R.drawable.opencustom);
                btn.setTag(row + "-" + col);
                newRow.addView(btn);
            }
            myLay.addView(newRow);
        }
        mine_count=0;
    }
    @Override
    public void onClick(View view) {
        ImageView btn=(ImageView)view;
        String string = btn.getTag().toString();
        String[] parts = string.split("-");
        String row = parts[0];
        String col = parts[1];

        update(Integer.parseInt(row),Integer.parseInt(col),btn);
    }
    private void update(int row,int col,ImageView btn){
        switch (select) {
            case "earse":
            {
                if((board[row][col].equals("mine")||board[row][col].equals("lock and mine"))&&mine_count>0)
                    mine_count--;
                btn.setImageResource(R.drawable.opencustom);
                board[row][col]="";
            }
            break;
            case "mine":
            {
                if(!(board[row][col].equals("mine")||board[row][col].equals("lock and mine")))
                    mine_count++;
                btn.setImageResource(R.drawable.bombcustom);
                board[row][col]="mine";
            }
            break;
            case "lock":
            {
                if((board[row][col].equals("mine")||board[row][col].equals("lock and mine"))&&mine_count>0)
                    mine_count--;
                btn.setImageResource(R.drawable.lookcube);
                board[row][col] = "lock";
            }
            break;
            case "lock and mine":
            {
                if(!(board[row][col].equals("mine")||board[row][col].equals("lock and mine")))
                    mine_count++;
                btn.setImageResource(R.drawable.lockandcubecustom);
                board[row][col]="lock and mine";
            }
            break;
            case "key":
            {
                if((board[row][col].equals("mine")||board[row][col].equals("lock and mine"))&&mine_count>0)
                    mine_count--;
                btn.setImageResource(R.drawable.keycustom);
                board[row][col]="key";
            }
            break;
        }
    }
    public void earse(View view){
        //earse.setBackgroundColor(Color.parseColor("#a4c639"));
        if(!select.equals("earse"))
            select="earse";
        else
            select="";
    }
    public void size(View view){
        final int before_size=size;
        final String[]items=new String[]{"9X9","10X10","11X11","12X12","13X13","14X14"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Pick a size");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] parts = items[i].split("X");
                size=Integer.parseInt(parts[0]);
                if(size!=before_size) {
                    size_button.setText(size+"X"+size);
                    custon_game_board.removeAllViews();
                    createCustomGameBoard();

                    board=new String[size][size];
                    for(int r=0;r<size;r++)
                        for(int j=0;j<size;j++)
                            board[r][j]="";
                    select="";
                }
            }
        });
        Dialog dialog=builder.create();
        dialog.show();
    }
    public void mine(View view){
        if(!select.equals("mine"))
            select="mine";
        else
            select="";
    }
    public void lock(View view){
        if(!select.equals("lock"))
            select="lock";
        else
            select="";
    }
    public void lockandkey(View view){
        if(!select.equals("lock and mine"))
            select="lock and mine";
        else
            select="";
    }
    public void key(View view){
        if(!select.equals("key"))
            select="key";
        else
            select="";
    }
    public void play(View view){
        Intent intent = new Intent(this, MainActivity.class);
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean("is_custom",true);
        editor.putInt("custom_size",size);
        editor.putInt("custom_mine_number",mine_count);
        editor.commit();
        startActivity(intent);
    }
    public void home(View view)
    {
        finish();
    }
    public void undo(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset the board?");
        builder.setMessage("Are you sure you want to reset the board?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                custon_game_board.removeAllViews();
                createCustomGameBoard();

                board=new String[size][size];
                for(int r=0;r<size;r++)
                    for(int j=0;j<size;j++)
                        board[r][j]="";
                select="";
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
