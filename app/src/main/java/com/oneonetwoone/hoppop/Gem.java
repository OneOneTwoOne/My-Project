package com.oneonetwoone.hoppop;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;
import static com.oneonetwoone.hoppop.GridFragment.mCombo;
import static com.oneonetwoone.hoppop.GridFragment.firstPoint;
import static com.oneonetwoone.hoppop.GridFragment.mGrid;
import static com.oneonetwoone.hoppop.GridFragment.score;

public class Gem {
    private static final String TAG="Gem";

    public int column;
    public int row;
    public int type;
    public int typeNum;
    public static Random mRand;
    public int point;
    public int gWidth, gHeight;
    private Button mButton;
    int typesOfGem=5;
    public View v;
    public boolean animTrue=false;


    public Gem (int yPos,int xPos, int gridHeight, int gridWidth){
        column=xPos;
        row= yPos;
        gHeight=gridHeight;
        gWidth=gridWidth;
        typeNum=0;
        applyType();
        findPoint();

    }

    public Gem (int xPos,int yPos,int gridHeight, int gridWidth, int colour){
        column=xPos;
        row= yPos;
        gHeight=gridHeight;
        gWidth=gridWidth;
        typeNum=colour;
        applyType();
        findPoint();
    }

    private void applyType(){
        switch(typeNum){
            case 0:
                mRand=new Random();
                typeNum=mRand.nextInt(typesOfGem)+1;
                applyType();
                break;
            case 1:
                type=R.layout.gem1;
                break;
            case 2:
                type=R.layout.gem2;
                break;
            case 3:
                type=R.layout.gem3;
                break;
            case 4:
                type=R.layout.gem4;
                break;
            case 5:
                type=R.layout.gem5;
                break;
            default:
                break;

        }
    }

    private void findPoint(){
        switch(column) {
            case 0:
                point = R.id.Point1;
                break;
            case 1:
                point = R.id.Point2;
                break;
            case 2:
                point = R.id.Point3;
                break;
            case 3:
                point = R.id.Point4;
                break;
            case 4:
                point = R.id.Point5;
                break;
            case 5:
                point = R.id.Point6;
                break;
            case 6:
                point = R.id.Point7;
                break;
            case 7:
                point = R.id.Point8;

                break;
            default:
                break;
        }
    }

    public void setButton(){
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int r=column;
                int g=row;
                v.setBackgroundColor(0xFF00FF00);
                Log.i(TAG, "this point:"+r+" "+g);
                //reached
                if(firstPoint){
                    firstPoint=false;
                    mCombo.add(Gem.this);}
                else{firstPoint=true;
                    mCombo.add(Gem.this);
                    testChain(mCombo);}
            }});
    }

    public void testChain(ArrayList<Gem> combo){
        int typeNum=combo.get(0).typeNum;
        if(typeNum!=combo.get(1).typeNum){
            combo.clear();
            return;
        }
        int x1=combo.get(0).gWidth;
        int x2=combo.get(1).gWidth;
        int y1=combo.get(0).gHeight;
        int y2=combo.get(1).gHeight;

        int xDiff=(x1-x2);
        int yDiff=(y1-y2);
        int jump=Math.abs(xDiff)+Math.abs(yDiff);

        int nextX=(x2+xDiff)-1;
        int nextY=(y2+yDiff)-1;

        if (mGrid[nextY][nextX].typeNum!=typeNum){
            combo.clear();
            return;
        }
        int n=2;
        while(mGrid[nextY][nextX].typeNum!=typeNum) {
            combo.add(mGrid[nextY][nextX]);

            x2 = nextX;
            y2 = nextY;
            nextX=(x2+xDiff);
            nextY=(y2+yDiff);
            n++;
            Log.i(TAG,""+n+"x combo!");
            score+=(n*jump);

        }

    }
}
