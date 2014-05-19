/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colamone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

/**
 *
 * @author gabill
 */
public class Colamone extends Application {

    private final int PANE_SIZE = 100;
    private final int PANE_COUNT = 6;
    private Map<Integer, Integer> map = new HashMap<>();
    final List<Piece> pieces = new ArrayList<>();
    
     /**
      * main
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /***
     * 初期表示
     * @param primaryStage 
     */
    @Override   
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 600);
        final Desk desk = new Desk();

        //コマを生成
        for(int i=1;i<=8;i++){
            pieces.add(new Piece(0,0, i));
            pieces.add(new Piece(0,0, -1*i));
        }
        desk.getChildren().addAll(pieces);
        
        //コマを配置
        this.map=Rule.shuffleMap();
        drawPieaceAll(this.map);
        
        //盤を配置
        root.getChildren().addAll(desk);
        
        primaryStage.setTitle("Colamone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /***
     * コマをすべて描画
     * @param wkMap 
     */
    private void drawPieaceAll(Map<Integer, Integer> wkMap){
        wkMap.entrySet().stream()
                .filter((entry)->entry.getValue()!=0)
                .forEach((entry) -> {
                    int x=((int) Math.ceil(entry.getKey()/10))*PANE_SIZE;
                    int y=((int) Math.ceil(entry.getKey()%10))*PANE_SIZE;
                    pieces.stream()
                            .filter((p)->p.number==entry.getValue())
                            .forEach((p)->{
                                p.setLayoutX(x);
                                p.setLayoutY(y);
                            });
                }); 
    }
    
    /***
     * ボードクラス
     */
    class Desk extends Pane {

        final Color WHITE = Color.rgb(255, 255, 255);
        final Color BLACK = Color.rgb(0, 0, 0);

        Desk() {
            setStyle("-fx-effect: innershadow( two-pass-box , rgba(0,0,0,0.8) , 15, 0.0 , 0 , 4 );"
                    + "-fx-background-color: #cccccc; "
                    + "-fx-border-color: #464646; ");
            double DESK_WIDTH = PANE_SIZE * PANE_COUNT;
            setPrefSize(DESK_WIDTH, DESK_WIDTH);
            setMaxSize(DESK_WIDTH, DESK_WIDTH);
            autosize();
            // 線を定義
            Path grid = new Path();
            grid.setStroke(Color.rgb(42, 42, 42));

            for (int r = 0; r < PANE_COUNT; r++) {
                for (int c = 0; c < PANE_COUNT; c++) {
                    Rectangle rec = new Rectangle();
                    if ((r + c) % 2 == 0) {
                        rec.setFill(WHITE);
                    } else {
                        rec.setFill(BLACK);
                    }

                    rec.setX(c * PANE_SIZE);
                    rec.setY(r * PANE_SIZE);
                    rec.setWidth(PANE_SIZE);
                    rec.setHeight(PANE_SIZE);
                    getChildren().add(rec);
                }
            }
        }
    }

    /***
     * コマクラス
     */
    public static final class Piece extends Parent {
        private final double correctX;
        private final double correctY;
        public final int number;

        private final Shape pieceStroke;
        private final Color piece_color;
        private final int PANE_SIZE = 100;
        
        /***
         * コントラスタ
         * @param correctX
         * @param correctY
         * @param number 
         */
        public Piece(final double correctX, final double correctY,int number) {
            this.correctX = correctX;
            this.correctY = correctY;
            this.number=number;
            
            if(this.number>0){
                piece_color=Color.rgb(0, 0, 255);
            }else{
                piece_color=Color.rgb(255, 0, 0);
            }
            
            
            //文字
            Text text=new Text();
            text.setFont(new Font(30));
            text.setText(Integer.toString(Math.abs(this.number)));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setLayoutX(this.correctX+PANE_SIZE/2-10);
            text.setLayoutY(this.correctY+PANE_SIZE/2+10);
            text.setFill(Color.WHITE);
            
            // 枠線
            pieceStroke = createPiece();
            pieceStroke.setFill(piece_color);
            setFocusTraversable(true);
            
            
            //コマと文字を貼り付け
            getChildren().addAll(pieceStroke,text);
            
            int[] move=Rule.getMoveArray(number);
            for(int i=0;i<move.length;i++){
                if(move[i]==0){
                    continue;
                }
                double x=this.correctX+0.25*PANE_SIZE+( Math.floor (PANE_SIZE-0.25*PANE_SIZE)/3)*Math.floor(i % 3.0);
                double y=this.correctY+0.25*PANE_SIZE+( Math.floor (PANE_SIZE-0.25*PANE_SIZE)/3)*Math.floor (i / 3.0);
                Circle c1 = new Circle(PANE_SIZE*0.04,Color.WHITE);
                c1.setLayoutX(x);
                c1.setLayoutY(y);
                getChildren().addAll(c1);
            }
            
            // キャッシュ有効化
            setCache(true);
            setActive();
        }
        
        /***
         * コマを描画
         * @return 
         */
        private Shape createPiece() {
            Polygon polygon = new Polygon();
            polygon.setLayoutX(correctX);
            polygon.setLayoutY(correctY);
            polygon.getPoints().addAll(new Double[]{
                0.1*PANE_SIZE,  0.1*PANE_SIZE,
                0.9*PANE_SIZE,  0.1*PANE_SIZE,
                0.9*PANE_SIZE,  0.9*PANE_SIZE,
                0.1*PANE_SIZE,  0.9*PANE_SIZE});
            return polygon;
        }
        
        /***
         * 有効化
         */
        public void setActive() {
            setDisable(false);
            setVisible(true);
            setEffect(new DropShadow());
            toFront();
        }
        
        /***
         * 無効化
         */
        public void setNoActive() {
            setDisable(true);
            setVisible(false);
            toBack();
        }
    }

}
