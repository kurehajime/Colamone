/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colamone;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gabill
 */
final public  class Rule {

    /**
     *
     * 動ける方向の配列を返す
     *
     * @param num
     * @return
     */
    public static int[] getMoveArray(int num) {
        switch (num) {
            case 1:
                return new int[]{1, 1, 1,
                    1, 0, 1,
                    1, 1, 1};
            case 2:
                return new int[]{1, 1, 1,
                    1, 0, 1,
                    1, 0, 1};
            case 3:
                return new int[]{1, 1, 1,
                    0, 0, 0,
                    1, 1, 1};
            case 4:
                return new int[]{1, 1, 1,
                    0, 0, 0,
                    1, 0, 1};
            case 5:
                return new int[]{1, 0, 1,
                    0, 0, 0,
                    1, 0, 1};
            case 6:
                return new int[]{1, 0, 1,
                    0, 0, 0,
                    0, 1, 0};
            case 7:
                return new int[]{0, 1, 0,
                    0, 0, 0,
                    0, 1, 0};
            case 8:
                return new int[]{0, 1, 0,
                    0, 0, 0,
                    0, 0, 0};
            case -1:
                return new int[]{1, 1, 1,
                    1, 0, 1,
                    1, 1, 1};
            case -2:
                return new int[]{1, 0, 1,
                    1, 0, 1,
                    1, 1, 1};
            case -3:
                return new int[]{1, 1, 1,
                    0, 0, 0,
                    1, 1, 1};
            case -4:
                return new int[]{1, 0, 1,
                    0, 0, 0,
                    1, 1, 1};
            case -5:
                return new int[]{1, 0, 1,
                    0, 0, 0,
                    1, 0, 1};
            case -6:
                return new int[]{0, 1, 0,
                    0, 0, 0,
                    1, 0, 1};
            case -7:
                return new int[]{0, 1, 0,
                    0, 0, 0,
                    0, 1, 0};
            case -8:
                return new int[]{0, 0, 0,
                    0, 0, 0,
                    0, 1, 0};
            default:
                return new int[]{0, 0, 0,
                    0, 0, 0,
                    0, 0, 0};
        }
    }

    /**
     * 
     * 標準配置のマップを返す(テスト用)。
     *
     * @return
     */
    public static Map<Integer, Integer> initMap() {
        Map<Integer, Integer> wkMap = new HashMap<>();
        wkMap.put(0, -1);
        wkMap.put(10, -2);
        wkMap.put(20, -3);
        wkMap.put(30, -4);
        wkMap.put(40, -5);
        wkMap.put(50, -6);
        wkMap.put(1, 0);
        wkMap.put(11, -8);
        wkMap.put(21, 0);
        wkMap.put(31, 0);
        wkMap.put(41, -7);
        wkMap.put(51, 0);
        wkMap.put(2, 0);
        wkMap.put(12, 0);
        wkMap.put(22, 0);
        wkMap.put(32, 0);
        wkMap.put(42, 0);
        wkMap.put(52, 0);
        wkMap.put(3, 0);
        wkMap.put(13, 0);
        wkMap.put(23, 0);
        wkMap.put(33, 0);
        wkMap.put(43, 0);
        wkMap.put(53, 0);
        wkMap.put(4, 0);
        wkMap.put(14, 7);
        wkMap.put(24, 0);
        wkMap.put(34, 0);
        wkMap.put(44, 8);
        wkMap.put(54, 0);
        wkMap.put(5, 6);
        wkMap.put(15, 5);
        wkMap.put(25, 4);
        wkMap.put(35, 3);
        wkMap.put(45, 2);
        wkMap.put(55, 1);
        return wkMap;
    }
    
    /**
     * 
     * 空のマップを返す。
     *
     * @return
     */
    public static Map<Integer, Integer> nullMap() {
        Map<Integer, Integer> wkMap = new HashMap<>();
        int[] posi_all={0,10,20,30,40,50,
                      1,11,21,31,41,51,
                      2,12,22,32,42,52,
                      3,13,23,33,43,53,
                      4,14,24,34,44,54,
                      5,15,25,35,45,55};
        for(int posi:posi_all){
            wkMap.put(posi, 0);
        }
        return wkMap;
    }
    
    /**
     * 
     * ランダムマップを返す。
     *
     * @return
     */
    public static Map<Integer, Integer> shuffleMap() {
        Map<Integer, Integer> wkMap = nullMap();
        for(int posi:wkMap.keySet()){
            wkMap.put(posi, 0);
        }
        int[] posi_m={0,10,20,30,40,50,11,41};
        int[] posi_p={55,45,35,25,15,5,44,14};
        int[] nums={1,2,3,4,5,6,7,8};
        List<Integer> numlist=new ArrayList<>();
        for(int i:nums){
            numlist.add(i);
        }
        Collections.shuffle(numlist);
        for(int i=0;i<numlist.size();i++){
            wkMap.put(posi_p[i], numlist.get(i));
            wkMap.put(posi_m[i], -1*numlist.get(i));
        }
        
        return wkMap;
    }
    
    /***
     * コマから座標を取得
     * @param map
     * @param number
     * @return 
     */
    public static int getPosiotionByNumber(Map<Integer, Integer> map,int number){
        for(int postion:map.keySet()){
            if(map.get(postion)==number){
                return postion;
            }
        }
        return -1;
    }
    
    /***
     * コマを動かしたMAPを返す。
     * @param map
     * @param fromPosition
     * @param toPosition
     * @return 
     */
    public static Map<Integer, Integer> putMap(Map<Integer, Integer> map,int fromPosition,int toPosition){
        Map<Integer, Integer> newMap=new HashMap<>(map);
        newMap.put(fromPosition, 0);
        newMap.put(toPosition, map.get(fromPosition));
        
        return newMap;
    }
    
    /***
     * 動かせるかどうかチェックする。
     * @param map
     * @param fromPosition
     * @param toPosition
     * @return 
     */
    public static boolean checkMap(Map<Integer, Integer> map,int fromPosition,int toPosition){
        List<Integer> list=getMovalPosition(map,fromPosition);
        return list.contains(toPosition);
    }
    
    /***
     * 動かせる場所の配列を返す。
     * @param map
     * @param position
     * @return 
     */
    public static List<Integer> getMovalPosition(Map<Integer, Integer> map,int position){
        List<Integer> list=new ArrayList<>();
        int number=map.get(position);
        if(number==0){
            return list;
        }
        int x = (int)Math.floor(position / 10);
        int y = (int)Math.floor(position % 10);
        //アガリのコマは動かしたらダメ。
        if(number>0 & y==0){
            return list;   
        }else if(number<0 &y==5){
            return list;   
        }
        int[] PIECES=getMoveArray(number);
        
        for(int i=0;i<=PIECES.length-1;i++){
            int target_x= (int)(x + Math.floor(i%3)-1);
            int target_y= (int)(y + Math.floor(i/3)-1);
            if(PIECES[i]==0){
                continue;
            }
            if(target_x<0 || target_y<0|target_x>5|target_y>5 ){
                continue;
            }
            int target_number=map.get(target_x*10+target_y);
            if(target_number*number>0){
                continue;   
            }
            //アガリのコマはとったらダメ。
            if(target_number>0 & target_y==0){
                continue;   
            }else if(target_number<0 &target_y==5){
                continue;
            }
            list.add(target_x*10+target_y);
        }
        return list;
    }
     
}
