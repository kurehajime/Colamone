/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colamone;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * ボードクラス
 * @author gabill
 */
final public  class Board implements Map<Integer, Integer>{

    /***
     * Map
     */
    Map<Integer, Integer> map;
    
    /***
     * ボードクラス
     * @param isRandom 
     */
    public Board(boolean isRandom) {
        if(isRandom){
            this.map = shuffleMap();
        }else{
            this.map = initMap();
        }
    }
    
    /***
     * ボードクラス
     * @param map 
     */
    public Board(Map<Integer, Integer> map){
        this.map= new HashMap<>(map);
    }
    
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
    private static Board initMap() {
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
        return new Board(wkMap);
    }
    
    /**
     * 
     * 空のマップオブジェクトを返す。
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
    private static Map<Integer, Integer> shuffleMap() {
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
     * @param number
     * @return 
     */
    public int getPosiotionByNumber(int number){
        for(int postion:map.keySet()){
            if(map.get(postion)==number){
                return postion;
            }
        }
        return -1;
    }
    
    /***
     * コマを動かしたMAPを返す。
     * @param fromPosition
     * @param toPosition
     * @return 
     */
    public Board putBoard(int fromPosition,int toPosition){
        Board newMap=new Board(this.map);
        newMap.put(fromPosition, 0);
        newMap.put(toPosition, map.get(fromPosition));
        
        return newMap;
    }
    
    /***
     * 動かせるかどうかチェックする。
     * @param fromPosition
     * @param toPosition
     * @return 
     */
    public boolean checkBoard(int fromPosition,int toPosition){
        List<Integer> list=getMovalPosition(fromPosition);
        return list.contains(toPosition);
    }
    
    /***
     * 動かせる場所の配列を返す。
     * @param position
     * @return 
     */
    public List<Integer> getMovalPosition(int position){
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
    
    /***
     * 起こりうる次の一手を返す。
     * @param turnplayer
     * @return (prev,nextのリスト)
     */
    public  List<Integer[]> getNodes(int turnplayer){
        List<Integer[]> list =new ArrayList<>();
        for(Entry<Integer,Integer> entry:map.entrySet()){
            if(entry.getValue()*turnplayer>0){
                int prev=entry.getKey();
                for(int next:getMovalPosition(prev)){
                    list.add(new Integer[]{prev,next});
                }
            }
        }
        return list;
    }
    
    /***
     * 終局判定
     * @param turnplayer
     * @return 
     */
    public boolean isEnd(int turnplayer){
        int sum=0;
        
        //点数勝利
        final int[] goal;
        if(turnplayer>0){
            goal=new int[]{0,10,20,30,40,50};
        }else{
            goal=new int[]{5,15,25,35,45,55};             
        }
        for(int i:goal){
            if(map.get(i)*turnplayer>0){
                sum=map.get(i);
            }
        }
        if(sum*turnplayer>=8){
            return true;
        }
        //全滅判定
        sum=0;
        for(Entry<Integer,Integer> entry:map.entrySet()){
            if(entry.getValue()*turnplayer<0){
                sum+=entry.getValue();
                break;
            }
        }
        return (sum==0);
    }
    
    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Integer get(Object key) {
        return map.get(key);
    }

    @Override
    public Integer put(Integer key, Integer value) {
        return map.put(key,value);
    }

    @Override
    public Integer remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Integer> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<Integer> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Integer> values() {
        return map.values();
    }

    @Override
    public Set<Entry<Integer, Integer>> entrySet() {
        return map.entrySet();
    }
    
}
