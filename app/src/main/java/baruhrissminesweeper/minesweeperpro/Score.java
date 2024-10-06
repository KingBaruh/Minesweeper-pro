package baruhrissminesweeper.minesweeperpro;


/**
 * Created by Baruh on 24/02/2018.
 */

public class Score
{
    private String difficulty;
    private String time;
    private int size;


    public Score(String difficulty,String time,int size){
        this.difficulty=difficulty;
        this.time=time;
        this.size=size;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return difficulty+";"+time+";"+size;
    }
}
