package baruhrissminesweeper.minesweeperpro;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baruh on 24/02/2018.
 */

public class ScoreAdapter extends ArrayAdapter<Score>
{
    private Context context;
    private ArrayList<Score>list_of_Scores;
    private TextView time;
    private TextView difficulty;
    private TextView size;

    public ScoreAdapter(Context context,ArrayList<Score>list_of_Scores){
        super(context,R.layout.score_layout,list_of_Scores);
        this.context=context;
        this.list_of_Scores=list_of_Scores;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=inflater.inflate(R.layout.score_layout,parent,false);
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ScoresLIst.removeScore(position);
                notifyDataSetChanged();
                return true;
            }
        });
        time=(TextView)rowView.findViewById(R.id.time__id);
        difficulty=(TextView)rowView.findViewById(R.id.difficulty__id);
        size=(TextView)rowView.findViewById(R.id.size__id);

        showItem(position);
        return rowView;
    }
    private void showItem(int position){
        time.setText(""+list_of_Scores.get(position).getTime());
        difficulty.setText(""+list_of_Scores.get(position).getDifficulty());
        size.setText(""+list_of_Scores.get(position).getSize());

    }
}
