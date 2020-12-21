package com.example.new3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
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

    int[][] game = new int[10][11];

    private ViewGroup map;

    boolean isfirst;
    int firstloc;
    int temploc;
    int locc;

    boolean isclear;

    Toast mytoast;

    int tx1, ty1, tx2, ty2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isclear = true;
    }

    public void clear() {
        map.removeAllViews();
        isclear = true;
    }

    private void init() {
        if (!isclear) {
            clear();
        }
        isclear = false;
        isfirst = true;

        map = (ViewGroup) findViewById(R.id.map);
        for (int j = 0; j <= 10; j++) {
            final LinearLayout linearLayout = new LinearLayout(this);
            for (int i = 0; i <= 9; i++) {
                View.inflate(this, R.layout.layout, linearLayout);
                if (j == 0 || j == 10 || i == 0 || i == 9) {
                    continue;
                }
                final View view = linearLayout.getChildAt(i);
                final ImageView img1 = view.findViewById(R.id.img1);
                final int picid = getResources().getIdentifier(String.format("emoji_%d", game[i][j]), "drawable", getApplicationContext().getPackageName());
                img1.setImageResource(picid);
                view.setTag(Integer.toString((j - 1) * 9 + i));
                view.setOnClickListener(this);
            }
            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            map.addView(linearLayout, layoutParams);
        }
    }

    public void saynow(String a) {
        if (mytoast != null) {
            mytoast.cancel();
            mytoast = null;
        }
        mytoast = Toast.makeText(this, a, Toast.LENGTH_SHORT);
        mytoast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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

    public void save(int x1, int y1, int x2, int y2) {
        tx1 = x1;
        tx2 = x2;
        ty1 = y1;
        ty2 = y2;
        System.out.print("save: " + x1 + " " + y1 + " " + x2 + " " + y2);
    }

    public void restart(int t) {
        set0();
        Random r = new Random();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 9; j++) {
                if (game[i][j] != 0) {
                    continue;
                }
                int pic = r.nextInt(t) + 1;
                game[i][j] = pic;

                int pairi;
                int pairj;
                do {
                    pairi = r.nextInt(8) + 1;
                    pairj = r.nextInt(9) + 1;
                } while (game[pairi][pairj] != 0);

                System.out.println("loc1:" + i + "," + j + " pic:" + pic);
                System.out.println("loc2:" + pairi + "," + pairj + " pic:" + pic);
                game[pairi][pairj] = pic;
            }
        }
        printgame();
    }

    public void set0() {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 9; j++) {
                game[i][j] = 0;
            }
        }
    }

    public void printgame() {
        System.out.println();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 9; j++) {
                System.out.print(game[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public int getval(int loc) {
        return game[loc % 9][(loc / 9) + 1];
    }

    public boolean islineok(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            for (int i = Math.min(y1, y2) + 1; i < Math.max(y1, y2); i++) {
                if (game[x1][i] != 0) {
                    //saynow("noway: "+x1+" "+y1+" "+x2+" "+y2);
                    System.out.print("noway: " + x1 + " " + y1 + " " + x2 + " " + y2);
                    return false;
                }
            }
        } else {
            for (int i = Math.min(x1, x2) + 1; i < Math.max(x1, x2); i++) {
                if (game[i][y1] != 0) {
                    //saynow("noway: "+x1+" "+y1+" "+x2+" "+y2);
                    System.out.print("noway: " + x1 + " " + y1 + " " + x2 + " " + y2 + "  ");
                    return false;
                }
            }
        }
        return true;
    }

    public void drawitem(int x1, int y1, boolean t) {
        View view = getviewfromxy(x1, y1);
        final int picid;
        if (t) {
            picid = getResources().getIdentifier("emoji_32", "drawable", getApplicationContext().getPackageName());
        } else {
            picid = getResources().getIdentifier("trans", "drawable", getApplicationContext().getPackageName());
        }
        ImageView img1 = view.findViewById(R.id.img1);
        img1.setImageResource(picid);
        //view.setBackgroundResource(picid);
    }

    public void drawline(int x1, int y1, int x2, int y2, boolean t) {
        Log.d("fuck", "p1:("+x1+","+y1+")  p2:("+x2+","+y2+")");
        if (x1 == x2 && y1 == y2) {
            drawitem(x1, y1, t);
            return;
        }
        if (x1 == x2) {
            for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
                drawitem(x1, i, t);
            }
            return;
        }
        if (y1 == y2) {
            for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
                drawitem(i, y1, t);
            }
            return;
        }
    }

    public void draw(int y1, int x1, int y2, int x2, int ty1, int tx1, int ty2, int tx2, boolean t) {
        //drawitem(x1,y1,t);
        //drawitem(x2,y2,t);
        //drawitem(tx1,ty1,t);
        //drawitem(tx2, ty2, t);

        drawline(x1, y1, tx1, ty1, t);
        drawline(x2, y2, tx2, ty2, t);
        drawline(tx1, ty1, tx2, ty2, t);
    }
    public void timedraw(int loc){
        draw(loc % 9, (loc / 9) + 1, firstloc % 9, (firstloc / 9) + 1, tx2, ty2, tx1, ty1, true);
        locc=loc;

        temploc=firstloc;

        //t();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                draw((locc % 9), (locc / 9) + 1, temploc % 9, (temploc / 9) + 1, tx2, ty2, tx1, ty1, false);
            }
        }, 300);
    }

    public boolean isaway(int x1, int y1, int x2, int y2, int x1t, int y1t, int x2t, int y2t) {
        boolean t1, t2;
        t1 = (game[x1t][y1t] == 0 ? true : false);
        t2 = (game[x2t][y2t] == 0 ? true : false);
        t1 = (((x1 == x1t) && (y1 == y1t)) ? true : t1);
        t2 = (((x2 == x2t) && (y2 == y2t)) ? true : t2);
        return islineok(x1, y1, x1t, y1t) && islineok(x1t, y1t, x2t, y2t) && islineok(x2t, y2t, x2, y2) && t1 && t2;
    }

    public boolean isok(int loc1, int loc2) {

        int x1 = loc1 % 9, y1 = loc1 / 9 + 1;
        int x2 = loc2 % 9, y2 = loc2 / 9 + 1;
        //saynow("loc: "+x1+" "+y1+" "+x2+" "+y2);
        for (int i = Math.min(y1, y2); i <= Math.max(y1, y2); i++) {
            if (isaway(x1, y1, x2, y2, x1, i, x2, i)) {
                save(x1, i, x2, i);
                return true;
            }
        }
        for (int i = Math.min(y1, y2)-1; i >=0 ; i--) {
            if (isaway(x1, y1, x2, y2, x1, i, x2, i)) {
                save(x1, i, x2, i);
                return true;
            }
        }
        for (int i = Math.max(y1, y2) + 1; i <= 10; i++) {
            if (isaway(x1, y1, x2, y2, x1, i, x2, i)) {
                save(x1, i, x2, i);
                return true;
            }
        }

        for (int i = Math.min(x1, x2); i <= Math.max(x1, x2); i++) {
            if (isaway(x1, y1, x2, y2, i, y1, i, y2)) {
                save(i, y1, i, y2);
                return true;
            }
        }
        for (int i = Math.min(x1, x2)-1; i >=0; i--) {
            if (isaway(x1, y1, x2, y2, i, y1, i, y2)) {
                save(i, y1, i, y2);
                return true;
            }
        }
        for (int i = Math.max(x1, x2) + 1; i <= 9; i++) {
            if (isaway(x1, y1, x2, y2, i, y1, i, y2)) {
                save(i, y1, i, y2);
                return true;
            }
        }

        return false;
    }


    public final View getviewfromxy(int x, int y) {
        return ((LinearLayout) (map.getChildAt(x))).getChildAt(y);
    }

    public final View getviewfromloc(int loc) {
        return ((LinearLayout) (map.getChildAt(loc / 9 + 1))).getChildAt(loc % 9);
    }


    @Override
    public void onClick(View v) {
        final Object tag = v.getTag();
        if (tag != null) {
            String tagg = (String) tag;
            Integer temp = new Integer(tagg);
            int loc = temp.intValue();

            //saynow("loc:"+loc);
            //Toast.makeText(this,"点击了 第"+((loc/9)+1)+"行，第"+loc%9+"列,内容为"+getval(loc),Toast.LENGTH_SHORT).show();
            if (isfirst == true) {
                isfirst = false;
                saynow("第一次点击");
                View view = getviewfromloc(loc);
                ImageView img1 = view.findViewById(R.id.img1);
                img1.setAlpha(155);
                firstloc = loc;
            } else {
                isfirst = true;

                if (loc == firstloc) {
                    View view = getviewfromloc(loc);
                    ImageView img1 = view.findViewById(R.id.img1);
                    img1.setAlpha(255);

                    View view2 = getviewfromloc(firstloc);
                    ImageView img2 = view2.findViewById(R.id.img1);
                    img2.setAlpha(255);
                    saynow("不能连续点击同一个方块！");
                    return;
                }

                if (getval(loc) == getval(firstloc)) {
                    saynow("和上一次点的相同！");
                    if (isok(firstloc, loc)) {
                        saynow("可以消除！");

                        //saynow("x1:" + tx1 + " y1:" + ty1 + " x2:" + tx2 + " y2:" + ty2);

                        View view = getviewfromloc(loc);
                        ImageView img1 = view.findViewById(R.id.img1);
                        img1.setImageResource(R.drawable.trans);

                        View view2 = getviewfromloc(firstloc);
                        ImageView img2 = view2.findViewById(R.id.img1);
                        img2.setAlpha(255);
                        img2.setImageResource(R.drawable.trans);

                        view.setTag(null);
                        view2.setTag(null);

                        game[loc % 9][(loc / 9) + 1] = 0;
                        game[firstloc % 9][(firstloc / 9) + 1] = 0;

                        printgame();

                        Vibrator vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
                        vibrator.vibrate(100);

                        //drawitem(ty1,tx1);
                        //drawitem(ty2,tx2);
                        //drawitem((loc/9)+1,loc%9);
                        //drawitem((firstloc/9)+1,firstloc%9);

                        timedraw(loc);

                    } else {
                        View view = getviewfromloc(loc);
                        ImageView img1 = view.findViewById(R.id.img1);
                        img1.setAlpha(255);

                        View view2 = getviewfromloc(firstloc);
                        ImageView img2 = view2.findViewById(R.id.img1);
                        img2.setAlpha(255);

                        saynow("不能消除!");
                    }
                } else {
                    View view = getviewfromloc(loc);
                    ImageView img1 = view.findViewById(R.id.img1);
                    img1.setAlpha(255);

                    View view2 = getviewfromloc(firstloc);
                    ImageView img2 = view2.findViewById(R.id.img1);
                    img2.setAlpha(255);

                    saynow("和上一次点的不同!");
                }
            }
        }
    }
}

