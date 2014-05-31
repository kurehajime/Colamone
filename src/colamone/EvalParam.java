/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package colamone;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gabill
 */
public final class EvalParam  implements Cloneable{
    //<Number,前に進んだ数>=ボーナスポイント
    public  Map<Integer,Integer[]> POSITION_BONUS=new HashMap<>();
    //コマの価値
    public  Map<Integer,Integer> PIECE_POINT=new HashMap<>();

    public EvalParam() {
        
        POSITION_BONUS.put(1, new Integer[]{0,50,100,150,300,1000});
        POSITION_BONUS.put(2, new Integer[]{0,60,120,300,600,2000});
        POSITION_BONUS.put(3, new Integer[]{0,70,140,450,900,3000});
        POSITION_BONUS.put(4, new Integer[]{0,80,160,600,1200,4000});
        POSITION_BONUS.put(5, new Integer[]{0,90,180,750,1500,5000});
        POSITION_BONUS.put(6, new Integer[]{0,100,200,900,1800,6000});
        POSITION_BONUS.put(7, new Integer[]{0,110,220,1050,2100,7000});
        POSITION_BONUS.put(8, new Integer[]{0,120,240,1200,3100,10000});
       
        PIECE_POINT.put(1, 3600);
        PIECE_POINT.put(2, 3600);
        PIECE_POINT.put(3, 2900);
        PIECE_POINT.put(4, 2900);
        PIECE_POINT.put(5, 2700);
        PIECE_POINT.put(6, 2700);
        PIECE_POINT.put(7, 2500);
        PIECE_POINT.put(8, 2500);
               
        
        PIECE_POINT.put(1, 1800);
        PIECE_POINT.put(2, 1800);
        PIECE_POINT.put(3, 1450);
        PIECE_POINT.put(4, 1450);
        PIECE_POINT.put(5, 1350);
        PIECE_POINT.put(6, 1350);
        PIECE_POINT.put(7, 1250);
        PIECE_POINT.put(8, 1250);
    }
    public EvalParam(int mode) {
        
        POSITION_BONUS.put(1, new Integer[]{0,50,100,150,300,1000});
        POSITION_BONUS.put(2, new Integer[]{0,60,120,300,600,2000});
        POSITION_BONUS.put(3, new Integer[]{0,70,140,450,900,3000});
        POSITION_BONUS.put(4, new Integer[]{0,80,160,600,1200,4000});
        POSITION_BONUS.put(5, new Integer[]{0,90,180,750,1500,5000});
        POSITION_BONUS.put(6, new Integer[]{0,100,200,900,1800,6000});
        POSITION_BONUS.put(7, new Integer[]{0,110,220,1050,2100,7000});
        POSITION_BONUS.put(8, new Integer[]{0,120,240,1200,3100,10000});
        switch(mode){
            case 0:
                PIECE_POINT.put(1, 3600);
                PIECE_POINT.put(2, 3600);
                PIECE_POINT.put(3, 2900);
                PIECE_POINT.put(4, 2900);
                PIECE_POINT.put(5, 2700);
                PIECE_POINT.put(6, 2700);
                PIECE_POINT.put(7, 2500);
                PIECE_POINT.put(8, 2500);
                break;
            case 1:
                PIECE_POINT.put(1, 1800);
                 PIECE_POINT.put(2, 1800);
                 PIECE_POINT.put(3, 1450);
                 PIECE_POINT.put(4, 1450);
                 PIECE_POINT.put(5, 1350);
                 PIECE_POINT.put(6, 1350);
                 PIECE_POINT.put(7, 1250);
                 PIECE_POINT.put(8, 1250);
        }

               
        
 
    }
    
    @Override
    public String toString() {
        String s="";
        for(int i=1;i<=8;i++){
            s+="POSITION_BONUS:";
            for(int line=0;line<=5;line++){
                s+=Integer.toString(POSITION_BONUS.get(i)[line])+",";
            }
            s+="\n";
        }
        for(int i=1;i<=8;i++){
            s+="PIECE_POINT:"+Integer.toString(PIECE_POINT.get(i))+"\n";
        }
        return s;
    }
    public EvalParam copy(){
        EvalParam ep =new EvalParam();
        ep.PIECE_POINT= new HashMap<Integer,Integer>(this.PIECE_POINT);
        ep.POSITION_BONUS= new HashMap<Integer,Integer[]>(this.POSITION_BONUS);
        return ep;
    }
}
