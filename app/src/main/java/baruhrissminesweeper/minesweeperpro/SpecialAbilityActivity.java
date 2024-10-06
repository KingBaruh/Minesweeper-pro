package baruhrissminesweeper.minesweeperpro;

import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SpecialAbilityActivity extends AppCompatActivity {

    ViewPager mySlideViewSpecialAbility;
    LinearLayout myDotLayout;
    TextView[]myDots;
    SliderAdapter sliderAdapter;
    SharedPreferences sp;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_ability);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sp=getSharedPreferences("detalis", MODE_PRIVATE);
        mySlideViewSpecialAbility=(ViewPager)findViewById(R.id.SlideViewPagerSpecialAbility);
        myDotLayout=(LinearLayout)findViewById(R.id.dotlayout);

        sliderAdapter=new SliderAdapter(this);
        mySlideViewSpecialAbility.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        position=0;
        switch (sp.getInt("speical_ability_key",MainActivity.SPECIAL_ABILITY_FROZEN)){
            case MainActivity.SPECIAL_ABILITY_FROZEN:
                position=0;
                break;
            case MainActivity.SPECIAL_ABILITY_ONE_OR_TWO:
                position=1;
                break;
            case MainActivity.SPECIAL_ABILITY_DESTROYED_MINE:
                position=2;
                break;
            case MainActivity.SPECIAL_ABILITY_POWER_GUN:
                position=3;
                break;
            case MainActivity.SPECIAL_ABILITY_CURESD_BLADE:
                position=4;
                break;
            case MainActivity.SPECIAL_ABILITY_NUMBER_ROULETTE:
                position=5;
                break;
        }
        mySlideViewSpecialAbility.setCurrentItem(position);
        addDotsIndicator(position);
        mySlideViewSpecialAbility.addOnPageChangeListener(viewListener);

    }
    public void addDotsIndicator(int position){
        myDots=new TextView[6];
        myDotLayout.removeAllViews();
        for(int i=0;i<myDots.length;i++){
            myDots[i]=new TextView(this);
            myDots[i].setText(Html.fromHtml("&#8226;"));
            myDots[i].setTextSize(35);
            myDots[i].setTextColor(getResources().getColor(R.color.colorDot));

            myDotLayout.addView(myDots[i]);
        }

        if(myDots.length>0){
            myDots[position].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
        }
    }
    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void back(View view){
        finish();
    }

}
