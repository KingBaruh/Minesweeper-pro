package baruhrissminesweeper.minesweeperpro;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by User on 08/03/2018.
 */

public class ScoresLIst
{
    static final String FILE_NAME="config.txt";
    static ArrayList<Score> scores_list=null;
    static Context context;
    public static void SetList(Context c){
        if(scores_list!=null)
            return;
        scores_list=new ArrayList<>();
        context=c;
        readFromFile();
    }
    public static ArrayList<Score>getRecords(){
        return scores_list;
    }

    public static void SaveData() {
        try
        {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(FILE_NAME, context.MODE_PRIVATE));
            String str = "";
            for (Score score :scores_list) {
                str += score.toString();
                str += "\n";
            }
            outputStreamWriter.write(str);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    public static void readFromFile(){
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = "";
            while ((str=bufferedReader.readLine())!=null){
                String[]arr=str.split(";");
                Score score=new Score(arr[0],arr[1],Integer.parseInt(arr[2]));
                scores_list.add(score);
            }
        }
        catch (FileNotFoundException e) {
            Log.d("login activity", "File not found");
        } catch (IOException e)
        {
            Log.d("login activity", "Can not read file: ");
        }

    }
    public static void addScore(Score score){
        scores_list.add(score);
        SaveData();
    }
    public static void removeScore(int position){
        scores_list.remove(position);
        SaveData();
    }
}
