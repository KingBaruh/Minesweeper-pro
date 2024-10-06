package baruhrissminesweeper.minesweeperpro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListOfScoresActivity extends AppCompatActivity {

    ListView list_of_scores;
    ArrayList<Score>scores_list;
    ScoreAdapter score_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_of_scores);
        list_of_scores=(ListView)findViewById(R.id.list_of_scores);
        scores_list=ScoresLIst.getRecords();
        score_Adapter=new ScoreAdapter(this,scores_list);
        list_of_scores.setAdapter(score_Adapter);
        score_Adapter.notifyDataSetChanged();

    }
    public void back(View view){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
