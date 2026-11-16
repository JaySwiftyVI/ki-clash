package com.example.kiclash;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.graphics.*;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.widget.*;
import android.os.Handler;
import java.util.*;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        getWindow().getDecorView().setSystemUiVisibility(5894 | 1024 | 512);
        setContentView(new GameView(this));
    }

    static class Fighter {
        String name; int color; float hp=100, ki=0; boolean powered=false;
        Fighter(String n,int c){name=n;color=c;}
    }

    class GameView extends View {
        Paint p=new Paint(3); Handler handler=new Handler();
        String[] names={"GOKU","VEGETA","BROLY","FRIEZA"};
        int[] colors={Color.rgb(242,126,32),Color.rgb(50,105,220),Color.rgb(65,190,90),Color.rgb(175,90,220)};
        int player=0, enemy=1, round=0, mode=0, screen=0;
        Fighter hero=new Fighter(names[0],colors[0]), foe=new Fighter(names[1],colors[1]);
        float enemyX=.76f, playerX=.25f;
        long last=System.currentTimeMillis();
        boolean attacking=false, blast=false, guarding=false;
        String message="CHOOSE YOUR FIGHTER";
        Random random=new Random();
        float W,H;
        RectF left=new RectF(),right=new RectF(),hit=new RectF(),kiBtn=new RectF(),guard=new RectF(),transform=new RectF();
        GameView(Context c){super(c); setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        void text(Canvas c,String s,float x,float y,float size,int color,Paint.Align a){
            p.setColor(color);p.setTextSize(size);p.setTypeface(Typeface.create("sans-serif-black",Typeface.BOLD));p.setTextAlign(a);p.setShadowLayer(3,2,2,Color.BLACK);c.drawText(s,x,y,p);p.clearShadowLayer();
        }
        void rect(Canvas c,RectF r,int color,float rad){p.setColor(color);c.drawRoundRect(r,rad,rad,p);}
        @Override protected void onDraw(Canvas c){
            super.onDraw(c); W=getWidth();H=getHeight();
            p.setShader(new LinearGradient(0,0,0,H,Color.rgb(12,22,54),Color.rgb(76,36,65),Shader.TileMode.CLAMP));c.drawRect(0,0,W,H,p);p.setShader(null);
            // distant mountains and arena
            p.setColor(Color.rgb(35,48,77)); Path m=new Path();m.moveTo(0,H*.63f);m.lineTo(W*.16f,H*.34f);m.lineTo(W*.31f,H*.63f);m.lineTo(W*.48f,H*.28f);m.lineTo(W*.68f,H*.63f);m.lineTo(W*.83f,H*.38f);m.lineTo(W,H*.62f);m.lineTo(W,H);m.lineTo(0,H);m.close();c.drawPath(m,p);
            p.setColor(Color.rgb(104,75,70));c.drawRect(0,H*.69f,W,H,p);
            p.setColor(Color.rgb(158,112,85)); for(int i=0;i<8;i++) c.drawLine(i*W/7,H*.7f,(i+.5f)*W/7,H,p);
            if(screen==0){drawMenu(c);return;}
            drawHud(c);
            drawFighter(c,playerX*W,H*.68f,hero,true);
            drawFighter(c,enemyX*W,H*.68f,foe,false);
            if(blast){p.setColor(Color.CYAN);p.setShadowLayer(22,0,0,Color.CYAN);c.drawCircle(W*.53f,H*.47f,18,p);p.clearShadowLayer();}
            if(screen==2 || screen==3){
                rect(c,new RectF(W*.2f,H*.24f,W*.8f,H*.76f),0xEE10182D,22);
                text(c,screen==2?"VICTORY!":"DEFEAT",W/2,H*.39f,34,Color.YELLOW,Paint.Align.CENTER);
                text(c,message,W/2,H*.50f,17,Color.WHITE,Paint.Align.CENTER);
                rect(c,new RectF(W*.38f,H*.57f,W*.62f,H*.68f),0xFFEA8B25,15);
                text(c,mode==0?"NEXT BATTLE":"NEXT MATCH",W/2,H*.64f,15,Color.BLACK,Paint.Align.CENTER);
            } else drawControls(c);
        }
        void drawMenu(Canvas c){
            text(c,"KI CLASH",W/2,H*.18f,42,Color.YELLOW,Paint.Align.CENTER);
            text(c,"AN ORIGINAL 2D ANIME-INSPIRED FIGHTING GAME",W/2,H*.26f,13,Color.WHITE,Paint.Align.CENTER);
            text(c,"CHOOSE YOUR FIGHTER",W/2,H*.39f,19,Color.WHITE,Paint.Align.CENTER);
            float cw=W*.19f;
            for(int i=0;i<4;i++){
                float x=W*(.12f+i*.255f);
                rect(c,new RectF(x-cw/2,H*.46f,x+cw/2,H*.78f),i==player?0xFFEA8B25:0xCC182744,14);
                drawFighter(c,x,H*.68f,new Fighter(names[i],colors[i]),true);
                text(c,names[i],x,H*.75f,15,Color.WHITE,Paint.Align.CENTER);
            }
            text(c,"TAP A FIGHTER  •  STORY MODE  •  TOURNAMENT",W/2,H*.9f,16,Color.CYAN,Paint.Align.CENTER);
            text(c,"TAP LOWER LEFT FOR STORY  |  LOWER RIGHT FOR TOURNAMENT",W/2,H*.97f,11,Color.WHITE,Paint.Align.CENTER);
        }
        void drawFighter(Canvas c,float x,float y,Fighter f,boolean facing){
            float s=H/240f;
            // aura
            if(f.powered || f.ki>65){p.setColor(0x5533DDFF);p.setShadowLayer(20,0,0,Color.CYAN);c.drawOval(x-35*s,y-112*s,x+35*s,y+4*s,p);p.clearShadowLayer();}
            // legs
            p.setColor(Color.rgb(35,35,48));c.drawRoundRect(x-18*s,y-37*s,x-5*s,y+1*s,5,5,p);c.drawRoundRect(x+5*s,y-37*s,x+18*s,y+1*s,5,5,p);
            // body
            p.setColor(f.color);c.drawRoundRect(x-25*s,y-91*s,x+25*s,y-34*s,10,10,p);
            // arms
            p.setColor(0xFFFFC69A);c.drawRoundRect(x-38*s,y-83*s,x-23*s,y-42*s,7,7,p);c.drawRoundRect(x+23*s,y-83*s,x+38*s,y-42*s,7,7,p);
            // head
            p.setColor(0xFFFFC69A);c.drawCircle(x,y-108*s,21*s,p);
            p.setColor(Color.rgb(30,25,35));Path hair=new Path();hair.moveTo(x-20*s,y-115*s);hair.lineTo(x-23*s,y-143*s);hair.lineTo(x-7*s,y-128*s);hair.lineTo(x+2*s,y-151*s);hair.lineTo(x+10*s,y-128*s);hair.lineTo(x+24*s,y-139*s);hair.lineTo(x+19*s,y-111*s);hair.close();c.drawPath(hair,p);
            p.setColor(Color.BLACK);c.drawCircle(x-7*s,y-108*s,2*s,p);c.drawCircle(x+7*s,y-108*s,2*s,p);
        }
        void drawHud(Canvas c){
            text(c,hero.name,24,28,16,Color.WHITE,Paint.Align.LEFT);bar(c,24,38,W*.32f,hero.hp,Color.RED);bar(c,24,51,W*.32f,hero.ki,Color.CYAN);
            text(c,foe.name,W-24,28,16,Color.WHITE,Paint.Align.RIGHT);bar(c,W*.68f,38,W*.32f,foe.hp,Color.RED);
            text(c,(mode==0?"STORY":"TOURNAMENT")+"  •  ROUND "+(round+1),W/2,26,14,Color.YELLOW,Paint.Align.CENTER);
        }
        void bar(Canvas c,float x,float y,float width,float value,int color){
            rect(c,new RectF(x,y,x+width,y+8),0xFF252535,4);rect(c,new RectF(x,y,x+width*Math.max(0,value)/100f,y+8),color,4);
        }
        void drawControls(Canvas c){
            left.set(W*.03f,H*.78f,W*.17f,H*.96f);right.set(W*.19f,H*.78f,W*.33f,H*.96f);
            hit.set(W*.67f,H*.78f,W*.79f,H*.96f);kiBtn.set(W*.81f,H*.78f,W*.94f,H*.96f);
            guard.set(W*.48f,H*.84f,W*.59f,H*.96f);transform.set(W*.36f,H*.84f,W*.47f,H*.96f);
            rect(c,left,0xBB263C64,16);rect(c,right,0xBB263C64,16);rect(c,hit,0xCCB83B35,16);rect(c,kiBtn,0xCC247DAA,16);rect(c,guard,0xCC4D6A45,16);rect(c,transform,0xCC8056A8,16);
            text(c,"◀",left.centerX(),left.centerY()+8,25,Color.WHITE,Paint.Align.CENTER);text(c,"▶",right.centerX(),right.centerY()+8,25,Color.WHITE,Paint.Align.CENTER);
            text(c,"PUNCH",hit.centerX(),hit.centerY()+6,12,Color.WHITE,Paint.Align.CENTER);text(c,"KI BLAST",kiBtn.centerX(),kiBtn.centerY()+6,11,Color.WHITE,Paint.Align.CENTER);
            text(c,"GUARD",guard.centerX(),guard.centerY()+5,10,Color.WHITE,Paint.Align.CENTER);text(c,"POWER",transform.centerX(),transform.centerY()+5,10,Color.WHITE,Paint.Align.CENTER);
        }
        void start(int m){
            mode=m;round=0;playerX=.25f;enemyX=.76f;
            hero=new Fighter(names[player],colors[player]); enemy= (player+1)%4; foe=new Fighter(names[enemy],colors[enemy]);
            screen=1;message="FIGHT!";invalidate();
        }
        void next(){
            round++;
            if(round>= (mode==0?3:3)){screen=2;message="YOU WON THE "+(mode==0?"STORY":"TOURNAMENT")+"!";}
            else {enemy=(player+round)%4;if(enemy==player)enemy=(enemy+1)%4;foe=new Fighter(names[enemy],colors[enemy]);hero.hp=Math.min(100,hero.hp+25);hero.ki=0;screen=1;}
            invalidate();
        }
        void attack(boolean special){
            if(screen!=1)return;
            if(special){if(hero.ki<25){message="BUILD KI WITH PUNCHES!";return;}hero.ki-=25;foe.hp-=24;blast=true;}
            else {foe.hp-=10;hero.ki=Math.min(100,hero.ki+13);}
            if(foe.hp<=0){foe.hp=0;screen=2;message="YOU DEFEATED "+foe.name+"!";}
            else {if(random.nextFloat()<.35f){hero.hp-=7;if(hero.hp<=0){hero.hp=0;screen=3;}}}
            invalidate();
        }
        @Override public boolean onTouchEvent(android.view.MotionEvent e){
            if(e.getAction()!=MotionEvent.ACTION_UP)return true;
            float x=e.getX(),y=e.getY();
            if(screen==0){
                if(y>H*.42f&&y<H*.82f){int idx=Math.min(3,Math.max(0,(int)((x/W-.04f)/.255f)));player=idx;}
                else if(y>H*.84f){start(x<W/2?0:1);}
                invalidate();return true;
            }
            if(screen==2 || screen==3){
                if(screen==3){start(mode);}else next();return true;
            }
            if(left.contains(x,y))playerX=Math.max(.12f,playerX-.06f);
            else if(right.contains(x,y))playerX=Math.min(.43f,playerX+.06f);
            else if(hit.contains(x,y))attack(false);
            else if(kiBtn.contains(x,y))attack(true);
            else if(guard.contains(x,y)){hero.hp=Math.min(100,hero.hp+4);message="GUARD!";}
            else if(transform.contains(x,y)){hero.powered=true;hero.ki=Math.min(100,hero.ki+10);message="POWER UP!";}
            invalidate();return true;
        }
    }
}
