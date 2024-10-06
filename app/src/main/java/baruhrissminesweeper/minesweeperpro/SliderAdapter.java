package baruhrissminesweeper.minesweeperpro;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Baruh on 17/02/2018.
 */

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    SharedPreferences sp;
    int selected;
    ImageView[] select_images;
    public SliderAdapter(Context context){
        this.context=context;
        selected=-1;
        select_images=new ImageView[6];
    }
    //Arrays
    public Integer[]slide_images={
            R.drawable.frozenmineone,
            R.drawable.halfcircle2,
            R.drawable.destroyedmine,
            R.drawable.abilityfourbig,
            R.drawable.cursedbladebig,
            R.drawable.roulettebig
    };
    public String[]slide_charge={
            "Charge(7)",
            "Charge(10)",
            "Charge(13)",
            "Charge(4)",
            "Charge(3)",
            "Charge(34)"
    };
    public String[]slide_titles={
            "Frozen Mine",
            "50/50",
            "Destroyed Mine",
            "Power Gun",
            "Cursed Blade",
            "Number Roulette"
    };
    public String[]slide_texts={
            "You can freeze an unopened mine that when you open it you wont die,not work on flaged mines",
            "Marks two cube one is a mine and the other is a cube, you can also not tap on both of them,your choice!",
            "You can destroy a random mine in the game board and reveal it,not work on flaged mines",
            "Put 3 coins on the game board on an unopened number cubes.When find all 3 coins change to destroy 2 random mines",
            "Put 20 cursed cubes on the game board,if you open one of them they reset your progrees.change your ability to Charge(20),when charged win the game",
            "Open a roulette that chose between the numbers 1 and 8.Whan it stops on an number,open all the cubes numbers,not work on locked cubes"
    };
    public String[]mission_texts={
            "You need to win 10 games of any kind, mission progress: ",
            "You need to win 20 games of Hard difficulty with any board size, mission progress: ",
            "You need to win 3 games of Extreme difficulty with 14X14 board size, mission progress: ",
    };
    public int[]abilityInt={
            MainActivity.SPECIAL_ABILITY_FROZEN,
            MainActivity.SPECIAL_ABILITY_ONE_OR_TWO,
            MainActivity.SPECIAL_ABILITY_DESTROYED_MINE,
            MainActivity.SPECIAL_ABILITY_POWER_GUN,
            MainActivity.SPECIAL_ABILITY_CURESD_BLADE,
            MainActivity.SPECIAL_ABILITY_NUMBER_ROULETTE
    };
    @Override
    public int getCount() {
        return slide_titles.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);
        sp=layoutInflater.getContext().getSharedPreferences("detalis",Context.MODE_PRIVATE);

        ImageView slideImage=(ImageView)view.findViewById(R.id.slide_image);
        TextView slideTitle=(TextView)view.findViewById(R.id.slide_title);
        TextView slideText=(TextView)view.findViewById(R.id.slide_reading);
        TextView chargeText=(TextView)view.findViewById(R.id.charge_text);
        ImageView select_image=(ImageView)view.findViewById(R.id.error_or_succses);
        select_images[position]=select_image;
        if(!is_unlocked(position)) {
            select_image.setImageResource(R.drawable.lockicon);
        }
        else
            if(sp.getInt("speical_ability_key",MainActivity.SPECIAL_ABILITY_FROZEN)==abilityInt[position] && selected==-1||selected==position)//-----------------
                select_image.setImageResource(R.drawable.success);
            else
                select_image.setImageResource(R.drawable.error);

        slideImage.setImageResource(slide_images[position]);
        slideTitle.setText(""+slide_titles[position]);
        if(is_unlocked(position)) {
            slideText.setText("" + slide_texts[position]);
            chargeText.setText(""+slide_charge[position]);
        }
        else {
            slideText.setText("" + mission_texts[position - 3]+mission_progress(position));
            chargeText.setText("Locked");
        }
        container.addView(view);
        select_image.setTag(position);
        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = Integer.parseInt(view.getTag().toString());
                if (is_unlocked(position)) {
                    setSelected(position);//------------
                    SharedPreferences.Editor editor = sp.edit();
                    switch (position) {
                        case 0:
                            editor.putInt("speical_ability_key", MainActivity.SPECIAL_ABILITY_FROZEN);
                            break;
                        case 1:
                            editor.putInt("speical_ability_key", MainActivity.SPECIAL_ABILITY_ONE_OR_TWO);
                            break;
                        case 2:
                            editor.putInt("speical_ability_key", MainActivity.SPECIAL_ABILITY_DESTROYED_MINE);
                            break;
                        case 3:
                            editor.putInt("speical_ability_key", MainActivity.SPECIAL_ABILITY_POWER_GUN);
                            break;
                        case 4:
                            editor.putInt("speical_ability_key", MainActivity.SPECIAL_ABILITY_CURESD_BLADE);
                            break;
                        case 5:
                            editor.putInt("speical_ability_key", MainActivity.SPECIAL_ABILITY_NUMBER_ROULETTE);
                            break;
                    }
                    editor.commit();
                }
            }
        });
        return view;
    }
    public void setSelected(int selected)
    {
        this.selected=selected;
        for (int i=0;i<select_images.length;i++)
            if(select_images[i]!=null) {
                if (selected == i)
                        select_images[i].setImageResource(R.drawable.success);
                else
                    if(is_unlocked(i))
                        select_images[i].setImageResource(R.drawable.error);
                    else
                        select_images[i].setImageResource(R.drawable.lockicon);
            }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
    private boolean is_unlocked(int postion){
        switch (postion){
            case 3:
                if(!sp.getBoolean("speical_ability_POWER_GUN_is_unlock",false)) {
                    return false;
                }
                else
                    break;
            case 4:
                if(!sp.getBoolean("speical_ability_CURESD_BLADE_is_unlock",false)) {
                    return false;
                }
                else
                    break;
            case 5:
                if(!sp.getBoolean("speical_ability_NUMBER_ROULETTE_is_unlock",false)) {
                    return false;
                }
                else
                    break;
        }
        return true;
    }
    private String mission_progress(int postion){
        switch (postion){
            case 3:
                return ""+sp.getInt("POWER_GUN_MISSION",0)+"/10";
            case 4:
                return ""+sp.getInt("CURESD_BLADE_MISSION",0)+"/20";
            case 5:
                return ""+sp.getInt("NUMBER_ROULETTE_MISSION",0)+"/3";
        }
        return "";
    }
}