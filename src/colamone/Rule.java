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
final public class Rule {

    /**
     * *
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
     * *
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
     * *
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
     * *
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

}
