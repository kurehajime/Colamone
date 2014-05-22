/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package colamone;

import java.util.List;
import java.util.Map;

/**
 *
 * @author gabill
 */
public class AI {
    /***
     * 評価パラメータ
     */
    final EvalParam evalParam;
    public AI(EvalParam evalParam) {
        this.evalParam=evalParam;
    }
     /***
     * ZOCを返す
     * @param map
     * @param turnplayer
     * @return 
     */
    private Map<Integer, Integer> getZocMap(Board map,int turnplayer){
        Map<Integer, Integer> zocMap=Board.nullMap();
        for(Map.Entry<Integer, Integer> entry:zocMap.entrySet()){
            if(entry.getValue()!=0){
                List<Integer> canmove=map.getMovalPosition(entry.getKey());
                for(int p:canmove){
                    if(entry.getValue()>0){
                        zocMap.put(p, zocMap.get(p)+1);
                    }else {
                        zocMap.put(p, zocMap.get(p)+1);
                    }
                }
            }
        }
        return zocMap;
    }
    
    
    
    
    
    
}
