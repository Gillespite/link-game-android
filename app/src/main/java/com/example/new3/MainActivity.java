package com.example.new3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int[][]game=new int[10][11];
    private ViewGroup map;

    boolean isfirst;
    int firstloc;

    boolean isclear;

    Toast mytoast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isclear=true;
    }

    public void clear(){
        map.removeAllViews();
        isclear=true;
    }

    private void init(){
        if(!isclear){
            clear();
        }
        isclear=false;

        isfirst=true;
        map=(ViewGroup)findViewById(R.id.map);
        for(int j=1;j<=9;j++){
            final LinearLayout linearLayout=new LinearLayout(this);
            for(int i=1;i<=8;i++){
                View.inflate(this,R.layout.layout,linearLayout);
                final View view=linearLayout.getChildAt(i-1);
                final ImageView img1=view.findViewById(R.id.img1);
                final int picid=getResources().getIdentifier(String.format("emoji_%d",game[i][j]),"drawable",getApplicationContext().getPackageName());
                img1.setImageResource(picid);

                view.setTag(Integer.toString((j-1)*9+i));
                view.setOnClickListener(this);
            }
            ViewGroup.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            map.addView(linearLayout,layoutParams);
        }
    }

    public void saynow(String a){
        if(mytoast!=null){
            mytoast.cancel();
            mytoast=null;
        }
        mytoast=Toast.makeText(this,a,Toast.LENGTH_SHORT);
        mytoast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lv1:
                saynow("选择了简单模式");
                restart(10);
                init();
                break;
            case R.id.lv2:
                saynow("选择了一般模式");
                restart(15);
                init();
                break;
            case R.id.lv3:
                saynow("选择了困难模式");
                restart(25);
                init();
                break;
            case R.id.cl:
                clear();
                break;
            default:
        }
        return true;
    }
    public void restart(int t){
        set0();
        Random r =new Random();
        for(int i=1;i<=8;i++){
            for(int j=1;j<=9;j++){
                if(game[i][j]!=0){
                    continue;
                }
                int pic=r.nextInt(t)+1;
                int pairi=r.nextInt(8)+1;
                int pairj=r.nextInt(9)+1;
                while (game[pairi][pairj]!=0){
                    pairi=r.nextInt(8)+1;
                    pairj=r.nextInt(9)+1;
                }

                game[i][j]=pic;
                game[pairi][pairj]=pic;
            }
        }
        printgame();
    }
    public void set0(){
        for(int i=1;i<=8;i++){
            for(int j=1;j<=9;j++){
                game[i][j]=0;
            }
        }
    }
    public void printgame()
    {
        System.out.println();
        for(int i = 1; i <=8; i++)
        {
            for(int j = 1; j <=9; j++)
            {
                System.out.print(game[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    public int getval(int loc){
        return game[loc%9][(loc/9)+1];
    }

    public boolean isok(int loc1,int loc2){
        return true;
    }

    public final View getviewfromloc(int loc){
        return ((LinearLayout)(map.getChildAt(loc/9+1-1))).getChildAt(loc%9-1);
    }

    @Override
    public void onClick(View v) {
        final Object tag =v.getTag();
        if(tag!=null){
            String tagg=(String)tag;
            Integer temp=new Integer(tagg);
            int loc=temp.intValue();


            //Toast.makeText(this,"点击了 第"+((loc/9)+1)+"行，第"+loc%9+"列,内容为"+getval(loc),Toast.LENGTH_SHORT).show();
            if(isfirst==true){
                isfirst=false;
                saynow("第一次点击");
                firstloc=loc;
            }else{
                isfirst=true;

                if(loc==firstloc){
                    saynow("不能连续点击同一个方块！");
                    return;
                }

                if(getval(loc)==getval(firstloc)){
                    saynow("和上一次点的相同！");
                    if(isok(loc,firstloc)){
                        saynow("可以消除！");

                        View view=getviewfromloc(loc);
                        ImageView img1=view.findViewById(R.id.img1);
                        img1.setImageResource(R.drawable.white);

                        View view2=getviewfromloc(firstloc);
                        ImageView img2=view2.findViewById(R.id.img1);
                        img2.setImageResource(R.drawable.white);

                        view.setTag(null);
                        view2.setTag(null);
                    }else{
                        saynow("不能消除！");
                    }
                }else{
                    saynow("和上一次点的不同！");
                }
            }
        }
    }
}