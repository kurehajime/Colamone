/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colamone;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author gabill
 */
public class AI {

    /**
     * *
     * 評価パラメータ
     */
    final EvalParam evalParam;

    public AI(EvalParam evalParam) {
        this.evalParam = evalParam;
    }

    /**
     * *
     * ZOCを返す
     *
     * @param board
     * @param turnplayer
     * @return
     */
    private Map<Integer, Integer> getZocMap(Board board) {
        Map<Integer, Integer> zocMap = Board.nullMap();
        for (Map.Entry<Integer, Integer> entry : zocMap.entrySet()) {
            if (entry.getValue() != 0) {
                List<Integer> canmove = board.getMovalPosition(entry.getKey());
                for (int p : canmove) {
                    if (entry.getValue() > 0) {
                        zocMap.put(p, zocMap.get(p) + 1);
                    } else {
                        zocMap.put(p, zocMap.get(p) + 1);
                    }
                }
            }
        }
        return zocMap;
    }

    /**
     * *
     * 局面を評価する
     *
     * @param board
     * @param turnplayer
     * @return
     */
    private int evalBoard(Board board, int turnplayer) {
        if (board.isEnd(1) == true) {
            return +999999 * turnplayer;
        } else if (board.isEnd(-1) == true) {
            return -999999;
        }

        int ev = 0;
        Board evBoard = new Board(board);
        //ZOCを取得
        Map<Integer, Integer> zocMap = getZocMap(evBoard);
        //次にすぐ取られるコマは死んだ扱い。
        boolean death_flag = false;
        for (Entry<Integer, Integer> zentry : zocMap.entrySet()) {
            if (zentry.getValue() * turnplayer < 0 && evBoard.get(zentry.getKey()) * turnplayer > 0) {
                evBoard.put(zentry.getKey(), 0);
                death_flag = true;
            }
        }
        if (death_flag) {
            zocMap = getZocMap(evBoard);
        }

        //評価
        for (Entry<Integer, Integer> entry : evBoard.entrySet()) {
            final int number = entry.getValue();//コマ
            final int position = entry.getKey();
            final int z = zocMap.get(position);//ZOC
            final int player = (int) Math.signum(number);
            int line = 0;
            if (number > 0) {
                line = 5 - position % 10;
            } else if (number < 0) {
                line = position % 10;
            }
            if(number!=0){
                //コマのポイント
                ev += evalParam.PIECE_POINT.get(Math.abs(number)) * player;
                //ポジションボーナス
                ev += evalParam.POSITION_BONUS.get(Math.abs(number))[line] * player;
            }

            //ZOC連携ボーナス
            if (number * z > 0) {
                ev += evalParam.EFF_POINT * z;
            }
            //空き地ZOCボーナス
            ev += z * evalParam.ZOC_POINT;
        }
        return ev;
    }

    //よく考える。 node=[q,map0]
    public HandWithPoint deepThinkAllAB(Board board, int turn_player, int depth, int a, int b) {
        int best_score = turn_player * 9999999 * -1;
        Integer[] besthand = null;
        if (depth == 0) {
            best_score = evalBoard(board, turn_player);
            return new HandWithPoint(new Integer[]{0, 0}, best_score);
        }
        if (a == 0 && b == 0) {
            a = 9999999 * turn_player * -1;
            b = 9999999 * turn_player;
        }

        List<Integer[]> nodeList = board.getNodes(turn_player);
        for (Integer[] NodePair : nodeList) {
            Integer[] hand = NodePair;
            Board board2 = board.putBoard(hand[0], hand[1]);
            //必勝
            if (board2.isEnd(turn_player)) {
                return new HandWithPoint(hand, 999999 * turn_player);
            }
            //必敗
            if (board2.isEnd(turn_player * -1)) {
                if (besthand == null) {
                    best_score = 999999 * turn_player * -1;
                    besthand = hand;
                }
                continue;
            }
            //再帰的に評価する。
            int sc = deepThinkAllAB(board2, turn_player * -1, depth - 1, b, a).point;
            //最善手がまだないなら初期化
            if (besthand == null) {
                best_score = sc;
                besthand = hand;
            }
            //最善手を更新
            if (turn_player == 1 && sc > best_score) {
                best_score = sc;
                besthand = hand;
            } else if (turn_player == -1 && sc < best_score) {
                best_score = sc;
                besthand = hand;
            }
            //最適手をAに
            if (turn_player == 1 && a < best_score || turn_player == -1 && a > best_score) {
                a = best_score;
            }
            //B枝刈り
            if (turn_player == 1 && b <= best_score || turn_player == -1 && b >= best_score) {
                break;
            }
        }
        return new HandWithPoint(besthand, best_score);
    }
}
