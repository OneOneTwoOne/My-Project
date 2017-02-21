package com.oneonetwoone.hoppop;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
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
    public static ArrayList<Gem> mCombo;
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
        mDepth=8;
        mWidth=8;
        mGrid=createGrid(mWidth, mDepth);

    }

    private Gem[][] createGrid(int width, int height){
        Gem[][] grid= new Gem[height][width];

        for(int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                grid[j][i]=new Gem(j, i,height,width);
                dropGem(grid[j][i]);
            }

        }
        set.start();
        /*while(set.isRunning()){

        }*/

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
            Log.i(TAG,"added"+r+" "+c);}
        else{firstPoint=true;
            mCombo.add(mGrid[r][c]);
            Log.i(TAG,"added"+r+" "+c);
            testChain(mCombo);}
    }

    public static void testChain(ArrayList<Gem> combo){
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


