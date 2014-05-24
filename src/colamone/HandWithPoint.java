/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package colamone;

/***
 * 手(Prev,Next)と評価値
 */
public class HandWithPoint {

    Integer[] hand;
    int point;

    public HandWithPoint(Integer[] hand, int point) {
        this.hand = hand;
        this.point = point;
    }
}