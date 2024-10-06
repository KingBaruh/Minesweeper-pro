package baruhrissminesweeper.minesweeperpro;

import java.util.Random;


public class Game
{
    Random rnd=new Random();
    private Cube[][]game_board;
    private int number_of_booms;
    private int number_of_cube_without_boom;
    private int number_of_set_Flags;
    private int rows;
    private int cols;
    private boolean lose;
    private int frozen_count;


    public Game(int row,int col,int num_of_booms){
        game_board=new Cube[row][col];
        number_of_booms=num_of_booms;
        number_of_cube_without_boom=(row*col)-num_of_booms;
        rows=row;
        cols=col;
        lose=false;
        number_of_set_Flags=0;
        frozen_count=0;

        for(int i=0;i<row;i++)
            for(int j=0;j<col;j++)
                game_board[i][j]=new Cube();

        fillBoard(num_of_booms);
        setNumberOfCubeWithoutBooms();
    }
    /////////////////////new///
    public Game(int row,int col,int num_of_booms,String[][] board) {
        game_board=new Cube[row][col];
        number_of_booms=num_of_booms;
        number_of_cube_without_boom=(row*col)-num_of_booms;
        rows=row;
        cols=col;
        lose=false;
        number_of_set_Flags=0;
        frozen_count=0;

        for(int i=0;i<row;i++)
            for(int j=0;j<col;j++)
                game_board[i][j]=new Cube();

        setBoard(board);
        setNumberOfCubeWithoutBooms();
    }////////custom game///
    private void setBoard(String[][] board)
    {
        for(int r=0;r<rows;r++)
            for(int c=0;c<cols;c++)
                switch (board[r][c])
                {
                    case "mine":
                        game_board[r][c].setIsBoom(true);
                        break;
                    case "lock":
                        game_board[r][c].setIs_looked(true);
                        break;
                    case "lock and mine":
                        {
                            game_board[r][c].setIs_looked(true);
                            game_board[r][c].setIsBoom(true);
                        }
                        break;
                    case "key":
                        game_board[r][c].setIs_key(true);
                        break;
                }
    }
    ////////////////end custom game///
    private void fillBoard(int num_of_booms)
    {
        while(num_of_booms>0)
        {
            int r=rnd.nextInt(rows);
            int c=rnd.nextInt(cols);

            if(!game_board[r][c].getIsBoom()) {
                game_board[r][c].setIsBoom(true);
                game_board[r][c].setNumber(-1);
                num_of_booms--;
            }

        }
    }
    private void setNumberOfCubeWithoutBooms()
    {
        for(int i=0;i<getRows();i++)
        {
            for(int j=0;j<getCols();j++)
            {
                if(game_board[i][j].getIsBoom())
                {
                    set_number(i,j);
                }
            }
        }

    }
    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }
    private void set_number(int row,int col)
    {
        for(int i=row-1;i<=row+1;i++)
            for(int j=col-1;j<=col+1;j++)
                if((i>=0&&i<getRows())&&(j>=0&&j<getCols()))
                {
                    if((i!=row||j!=col)&&!game_board[i][j].getIsBoom())
                        game_board[i][j].AddNumber();
                }
    }
    /*
    we use flag to know if he tries to open the boom or flag it. when it is true he trys to open it,when it is false he flags the boom.
    if it return false the player did an error move
     */
    public boolean checkBoom(int row,int col,boolean flag)

    {
        if(game_board[row][col].getIs_open())
            return false;
        else
        {
            if (flag)
            {
                if(game_board[row][col].getIs_flag())
                    return false;
                else
                {
                    if (game_board[row][col].getIsBoom())
                    {
                        if(!game_board[row][col].getIs_frozen())
                            lose = true;
                        else {
                            number_of_set_Flags++;
                            frozen_count--;
                            game_board[row][col].setIs_open(true);
                        }
                    }
                    else
                    {
                        openWhenCubeIsZero(row, col);
                    }
                }
            }
            else
            {
                if (!game_board[row][col].getIs_flag())
                {
                    if(getNumOfBooms()==0)
                        return false;
                    else
                    {
                        game_board[row][col].setIs_flag(true);
                        number_of_set_Flags++;
                    }
                }
                else
                {
                    game_board[row][col].setIs_flag(false);
                    number_of_set_Flags--;
                }

            }
        }
        return true;

    }
    private void openWhenCubeIsZero(int row,int col)
    {
        if(game_board[row][col].getNumber()==0){
            game_board[row][col].setIs_open(true);//without it ,will be an infinite loop
            for(int i=row-1;i<=row+1;i++)
                for(int j=col-1;j<=col+1;j++)
                    if((i>=0&&i<getRows())&&(j>=0&&j<getCols())&&!game_board[i][j].getIs_open())
                    {
                        openWhenCubeIsZero(i, j);
                    }
        }
        if(game_board[row][col].getIs_flag()||game_board[row][col].getIs_looked())
            game_board[row][col].setIs_open(false);
        else {
            number_of_cube_without_boom--;
            game_board[row][col].setIs_open(true);
        }

    }
    public boolean isLose()
    {
        return lose;
    }
    public boolean isGame_over()
    {
        if(isLose()||(number_of_cube_without_boom==0&&number_of_set_Flags==number_of_booms))
            return true;
        return false;
    }
    public int getNumber(int row ,int col){
        return game_board[row][col].getNumber();
    }
    public boolean isOpen(int row,int col){
        return game_board[row][col].getIs_open();
    }
    public boolean isBoom(int row,int col){return game_board[row][col].getIsBoom();}
    public boolean isFlag(int row,int col){return game_board[row][col].getIs_flag();}
    public boolean isFrozen(int row,int col){return game_board[row][col].getIs_frozen();}
    public int getNumOfBooms(){return number_of_booms-number_of_set_Flags;}
    public int getNumber_of_cube_without_boom(){return number_of_cube_without_boom;}
    public boolean isFrozenAvailble(){return number_of_booms!=number_of_set_Flags+frozen_count;}
    public boolean isDestroyed(int row,int col){return game_board[row][col].getIs_destroyed();}
    public boolean isCoin(int row,int col){return game_board[row][col].getCoin();}
    public void changeCoinTofalse(int row,int col){game_board[row][col].setCoin(false);}
    public boolean isCuresd(int row,int col){return game_board[row][col].getCuresd();}
    public boolean isLooked(int row,int col){return game_board[row][col].getIs_looked();}
    public boolean isKey(int row,int col){return game_board[row][col].getIs_key();}
    public void changeKeyToFalse(int row,int col){game_board[row][col].setIs_key(false);}
    public void setIsopen(int row,int col){game_board[row][col].setIs_open(true);}
    public void setCountOffNumberOfCubesWithoutBooms(){number_of_cube_without_boom--;}
    public int getNumberOfLockCubes(){
        int count=0;
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                if(game_board[i][j].getIs_looked())
                    count++;
        return count;
    }
    public int getNumberOfkey(){
        int count=0;
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                if(game_board[i][j].getIs_key())
                    count++;
        return count;
    }
    public boolean if_There_Is_A_Key()
    {
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++){
                if(game_board[i][j].getIs_key())
                    return true;
            }
        return false;
    }
    public void destroyLookedCubes(){
        for(int i=0;i<rows;i++)
            for(int j=0;j<cols;j++)
                game_board[i][j].setIs_looked(false);
    }
    /////////////////special abilitys
    public void setFrozen() {
        boolean temp = false;
        while (!temp) {
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if (!isFrozen(r, c) && isBoom(r, c)&&!isFlag(r,c)) {
                game_board[r][c].setIs_frozen(true);
                game_board[r][c].setNumber(9);
                temp = true;
            }
        }
        frozen_count++;
    }
    public String getMineAndCube()
    {
        String str="";
        boolean flag=false;
        while (!flag) {
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if(game_board[r][c].getIsBoom()&&!game_board[r][c].getIs_flag())
            {
                str+=r+"-"+c+"/";
                flag=true;
            }
        }
        flag=false;
        while (!flag){
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if(!game_board[r][c].getIsBoom()&&!game_board[r][c].getIs_open())
            {
                str+=r+"-"+c;
                flag=true;
            }
        }
        return str;
    }
    //return string because to know the position of the destroyed mine
    public String destroyMine() {
        String str="";
        int row=0;
        int col=0;
        boolean temp = false;
        while (!temp) {
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if (isBoom(r, c) && !isFlag(r, c)&&!isDestroyed(r,c)&&!isOpen(r,c)) {
                game_board[r][c].setIs_destroyed(true);
                game_board[r][c].setNumber(10);
                temp = true;
                row=r;
                col=c;
                if(game_board[r][c].getIs_looked())
                    game_board[r][c].setIs_looked(false);
            }
        }
        game_board[row][col].setIs_open(true);
        number_of_set_Flags++;
        str+=row+"-"+col;
        return str;
    }
    public void coinSpreed(){
        int counter=3;
        while (counter>0){
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if(!isBoom(r,c)&&!isFlag(r,c)&&!isOpen(r,c)&&!isCoin(r,c)&&!isKey(r,c)){
                game_board[r][c].setCoin(true);
                counter--;
            }
        }
    }
    public void curesdSpreed(){
        int counter=20;
        while (counter>0){
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if(!isBoom(r,c)&&!isFlag(r,c)&&!isOpen(r,c)&&!isCuresd(r,c)&&!isKey(r,c)&&game_board[r][c].getNumber()!=0){
                game_board[r][c].setIs_curesd(true);
                counter--;
            }
        }
    }
    public void lookSpreed(int number_of_cubes){
        while (number_of_cubes>0){
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if(!isFlag(r,c)&&!isOpen(r,c)&&!isLooked(r,c)&&!isKey(r,c)){
                game_board[r][c].setIs_looked(true);
                number_of_cubes--;
            }
        }
    }
    public void keySpreed() {
        boolean temp = false;
        while (!temp) {
            int r = rnd.nextInt(rows);
            int c = rnd.nextInt(cols);
            if (!isBoom(r, c) && !isFlag(r, c) && !isLooked(r, c) && !isOpen(r, c)) {
                game_board[r][c].setIs_key(true);
                temp = true;
            }
        }
    }


}
