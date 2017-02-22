package com.oneonetwoone.hoppop;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import static android.R.attr.width;


public class GridFragment extends Fragment {
    public static final String TAG="GridFragment";
    public static Gem[][] mGrid;
    public AnimatorSet set= new AnimatorSet();
    private ObjectAnimator firstInRow;
    private ObjectAnimator lastRow;
    public int mDepth, mWidth;
    public static ArrayList<Gem> mCombo=new ArrayList<>();
    public static boolean firstPoint=true;
    public static int score=0;

    public static GridFragment newInstance(){
        return new GridFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view=inflater.inflate(R.layout.fragment_grid, container, false);

        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);



        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        //mScoreView.//problem
        mDepth=8;
        mWidth=8;
        mGrid=createGrid(mWidth, mDepth);

    }

    private Gem[][] createGrid( int height, int width){
        Gem[][] grid= new Gem[height][width];

        for(int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                grid[j][i]=new Gem(j, i,height,width);
                dropGem(grid[j][i]);
                Log.i(TAG,"gem");
            }

        }
        set.start();

        setButtons(grid);
        return grid;
    }

    public void dropGem(Gem gem){

        LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gem.v= vi.inflate(gem.type, null);
        ViewGroup insertPoint=(ViewGroup) getActivity().findViewById(gem.point);
        insertPoint.addView(gem.v,0,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        float spawn=insertPoint.getTop();

        int fallDist=dpToPx(500-(40*gem.row));
        int duration=(1000-((-gem.row * 25)/10));

        ObjectAnimator drop= ObjectAnimator.ofFloat(gem.v, "y", spawn,fallDist).setDuration(duration);
        if((gem.column==0)&&(gem.row==0)) {
            firstInRow=drop;
            set.play(firstInRow);
        }else if(gem.column==0) {
            lastRow = firstInRow;
            firstInRow = drop;
            set.play(drop).after(lastRow);
        }else if(gem.row==0){
            set.play(drop).with(firstInRow);
        }else{
            set.play(drop).after(lastRow);
        }
    }
    public int dpToPx(float dpValue){
        Context context=getActivity();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    private void setButtons(Gem[][] grid){
        for(int j = 0; j < mDepth; j++) {
            for (int i = 0; i < mWidth; i++) {
                grid[j][i].setButton();
            }
        }
    }

    public static void buttonMethod(int r, int c){
        if(firstPoint){
            firstPoint=false;
            mCombo.add(mGrid[r][c]);
            Log.i(TAG,"added:"+r+" "+c);}
        else{firstPoint=true;
            mCombo.add(mGrid[r][c]);
            Log.i(TAG,"added:"+r+" "+c);
            runChain();}
    }

    public static void runChain(){
        int typeNum=mCombo.get(0).typeNum;
        //Log.i(TAG, ""+mCombo.get(0).column);
        if(typeNum!=mCombo.get(1).typeNum){
            mCombo.clear();
            return;
        }
        int x1=mCombo.get(0).column;
        int x2=mCombo.get(1).column;
        int y1=mCombo.get(0).row;
        int y2=mCombo.get(1).row;
        int xDiff=(x2-x1);
        int yDiff=(y2-y1);
        int jump=Math.abs(xDiff)+Math.abs(yDiff);
        int nextX=(x2+xDiff);
        int nextY=(y2+yDiff);
        if(nextX>mGrid[0][0].gWidth){nextX=nextX-(mGrid[0][0].gWidth);}
        if(nextX<0){nextX=nextX+(mGrid[0][0].gWidth);}
        if(nextY>mGrid[0][0].gHeight){nextY=nextY-(mGrid[0][0].gHeight);}
        if(nextY<0){nextY=nextY+(mGrid[0][0].gHeight);}
        int n=2;
        while(mGrid[nextY][nextX].typeNum==typeNum) {
            Log.i(TAG, "added:nextX "+nextX+" NextY "+nextY);
            mCombo.add(mGrid[nextY][nextX]);
            x2 = nextX;
            y2 = nextY;
            nextX=(x2+xDiff);
            nextY=(y2+yDiff);
            if(nextX>mGrid[0][0].gWidth){nextX=nextX-(mGrid[0][0].gWidth);}
            if(nextY>mGrid[0][0].gWidth){nextY=nextY-(mGrid[0][0].gHeight);}
            if(nextX<0){nextX=nextX+(mGrid[0][0].gWidth);}
            if(nextY<0){nextY=nextY+(mGrid[0][0].gHeight);}
            n++;
            Log.i(TAG, "added:nextX "+nextX+" NextY "+nextY);
            Log.i(TAG,""+n+"x combo!"+(n*jump)+" points!");
            score+=(n*jump);

            if(x1==nextX&&y1==nextY){
                Log.i(TAG, "LOOPED");
                score=score*score;
                mCombo.clear();
                return;
            }

        }
        mCombo.clear();
    }

    public void setBlank(ArrayList<Gem> combo){
        for (int i=0; i<combo.size(); i++){
            combo.get(i).v.setBackground();
        }
    }
}


