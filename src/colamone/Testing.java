/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package colamone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 *
 * @author gabill
 */
public class Testing {
    public static int vs(EvalParam evp1 ,EvalParam evp2,int depth,boolean isRandom,Consumer<Board> func ){
        Board b=new Board(isRandom);
        HandWithPoint hwp ;
        int end;
        AI ai1 = new AI(evp1);
        AI ai2 = new AI(evp2);
        try {
            for(int i=0;i<=50;i++){
                //先攻
                hwp = ai1.deepThinkAllAB(b, 1, depth, 0, 0);
                b = b.putBoard(hwp.hand[0], hwp.hand[1]);
                end=b.isEnd();
                func.accept(b);
                if(b.isEnd()!=0){
                    return 1;
                }
                //後攻
                hwp = ai2.deepThinkAllAB(b, -1, depth, 0, 0);
                b = b.putBoard(hwp.hand[0], hwp.hand[1]);
                end=b.isEnd();
                func.accept(b);
                if(b.isEnd()!=0){
                    return -1;
                }
            }
        } catch (Exception e) {
        }
        func.accept(b);
        return 0;
    }
    
    public static EvalParam changeEvalParam(EvalParam ep0,double changerate,long seed){
        EvalParam ep=ep0.copy();
        Random rnd = new Random(seed);
//        if(rnd.nextInt(2)==0){
          if(false){
            int number=rnd.nextInt(8)+1;
            int line=rnd.nextInt(5);
            boolean plusminus=rnd.nextBoolean();
            if(plusminus){
                ep.POSITION_BONUS.get(number)[line]
                        +=(int)(ep.POSITION_BONUS.get(number)[line]*changerate);
            }else{
                ep.POSITION_BONUS.get(number)[line]
                        -=(int)(ep.POSITION_BONUS.get(number)[line]*changerate);
            }
        }else{
            int number=rnd.nextInt(8)+1;
            boolean plusminus=rnd.nextBoolean();
            
            if(plusminus){
                ep.PIECE_POINT.put(number,
                        ep.PIECE_POINT.get(number)
                        +(int)(ep.PIECE_POINT.get(number)*changerate)
                    );
            }else{
                ep.PIECE_POINT.put(number,
                        ep.PIECE_POINT.get(number)
                        -(int)(ep.PIECE_POINT.get(number)*changerate)
                );
            }

        }
        
        return ep;
    }
    
    public static int[] vsLoop(EvalParam evp1 ,EvalParam evp2,int depth,boolean isRandom,int count){
        int[] win=new int[]{0,0};
        
        for(int i=0;i<count;i++){
            if(i % 2==0){
                int result=vs(evp1,evp2,depth,isRandom,(b)->{return;});            
                if(result==1){
                    win[0]+=1;
                }else if(result==-1){
                    win[1]+=1;
                }
            }else{
                int result=vs(evp2,evp1,depth,isRandom,(b)->{return;});            
                if(result==-1){
                    win[0]+=1;
                }else if(result==-1){
                    win[1]+=1;
                }
            }

        }
        return win;
    }
    public static EvalParam vsTournament(EvalParam baseEP,int depth, int team,int count){
        EvalParam top=baseEP.copy();
        List<EvalParam> eps=new ArrayList<>();
        List<Integer> winpoint=new ArrayList<>();
        for(int i=0;i<count;i++){
            eps.add(Testing.multiplyEvalParam(top,0.5+i*0.5));//★テスト用仮コード
            //eps.add(Testing.changeEvalParam(top,0.3,1));//★テスト用仮コード
            winpoint.add(0);
        }
        for(int x=0;x<count;x++){
            for(int y=0;y<count;y++){
                if(x==y||x>y){continue;}
                int[] wins= vsLoop(eps.get(x),eps.get(y),depth,true,count);
                winpoint.set(x, winpoint.get(x)+(wins[0]-wins[1]));
            }
        }
        top= eps.get(winpoint.indexOf(Collections.max(winpoint)));
        
        return top;
    }
   public static EvalParam multiplyEvalParam(EvalParam ep0,double rate){
        EvalParam ep=ep0.copy();
        for(int i=1;i<=8;i++){
            ep.PIECE_POINT.put(i, (int)(ep.PIECE_POINT.get(i)*rate));
        }
        
        return ep;
    }    
}
