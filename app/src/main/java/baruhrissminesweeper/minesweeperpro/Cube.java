package baruhrissminesweeper.minesweeperpro;

public class Cube {
    private boolean is_boom;
    private int number;
    private boolean is_flag;
    private boolean is_open;
    private boolean is_frozen;
    private boolean is_destroyed;
    private boolean is_have_coin;
    private boolean is_curesd;
    private boolean is_looked;
    private boolean is_key;


    public Cube() {
        is_flag = false;
        number = 0;
        is_boom = false;
        is_open = false;
        is_frozen = false;
        is_destroyed = false;
        is_have_coin = false;
        is_curesd = false;
        is_looked = false;
        is_key = false;

    }

    public void AddNumber() {
        number++;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int num) {
        number = num;
    }

    public boolean getIsBoom() {
        return is_boom;
    }

    public void setIsBoom(boolean flag) {
        is_boom = flag;
    }

    public void setIs_flag(boolean flag) {
        is_flag = flag;
    }

    public boolean getIs_flag() {
        return is_flag;
    }

    public void setIs_open(boolean flag) {
        is_open = flag;
    }

    public boolean getIs_open() {
        return is_open;
    }

    public boolean getIs_frozen() {
        return is_frozen;
    }

    public void setIs_frozen(boolean flag) {
        is_frozen = flag;
    }

    public boolean getIs_destroyed() {
        return is_destroyed;
    }

    public void setIs_destroyed(boolean flag) {
        is_destroyed = flag;
    }

    public boolean getCoin() {
        return is_have_coin;
    }

    public void setCoin(boolean flag) {
        is_have_coin = flag;
    }

    public boolean getCuresd() {
        return is_curesd;
    }

    public void setIs_curesd(boolean flag) {
        is_curesd = flag;
    }

    public boolean getIs_looked() {
        return is_looked;
    }

    public void setIs_looked(boolean is_looked) {
        this.is_looked = is_looked;
    }

    public boolean getIs_key() {
        return is_key;
    }

    public void setIs_key(boolean is_key) {
        this.is_key = is_key;
    }
}
