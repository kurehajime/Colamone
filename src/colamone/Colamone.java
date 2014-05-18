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
    

    @Override   
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root, 600, 600);
        final Desk desk = new Desk();

        //コマを配置
        initMap();
        final List<Piece> pieces = new ArrayList<>();
        map.entrySet().stream()
                .filter((entry)->entry.getValue()!=0)
                .map((entry) -> {
                    int x=((int) Math.ceil(entry.getKey()/10))*PANE_SIZE;
                    int y=((int) Math.ceil(entry.getKey()%10))*PANE_SIZE;
                    final Piece piece = new Piece(x, y,entry.getValue());
                    return piece;
                }).forEach((piece) -> {
                    pieces.add(piece);
                });
        desk.getChildren().addAll(pieces);
        
        //盤を配置
        root.getChildren().addAll(desk);

        primaryStage.setTitle("Colamone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    private void initMap(){
        map= new HashMap<>();
        map.put(0,-1);map.put(10,-2);map.put(20,-3);map.put(30,-4);map.put(40,-5);map.put(50,-6);
        map.put(1, 0); map.put(11,-8);map.put(21, 0);map.put(31, 0);map.put(41,-7);map.put(51, 0);
        map.put(2, 0); map.put(12, 0);map.put(22, 0);map.put(32, 0);map.put(42, 0);map.put(52, 0);
        map.put(3, 0); map.put(13, 0);map.put(23, 0);map.put(33, 0);map.put(43, 0);map.put(53, 0);
        map.put(4, 0); map.put(14, 7);map.put(24, 0);map.put(34, 0);map.put(44, 8);map.put(54, 0);
        map.put(5, 6); map.put(15, 5);map.put(25, 4);map.put(35, 3);map.put(45, 2);map.put(55, 1);
    }
    private void shuffleMap(){
        
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * *
     * ボードを描画
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

    public static final class Piece extends Parent {
        private final double correctX;
        private final double correctY;
        private final int number;

        private final Shape pieceStroke;
        private final Color piece_color;
        private final int PANE_SIZE = 100;
        
        
        public Piece(final double correctX, final double correctY,int number) {
            this.correctX = correctX;
            this.correctY = correctY;
            this.number=number;
            
            if(this.number>0){
                piece_color=Color.rgb(0, 0, 255);
            }else{
                piece_color=Color.rgb(255, 0, 0);
            }
            
            // 枠線
            pieceStroke = createPiece();
            pieceStroke.setFill(piece_color);
            setFocusTraversable(true);
            
            //文字
            Text text=new Text();
            text.setFont(new Font(30));
            text.setText(Integer.toString(Math.abs(this.number)));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setLayoutX(this.correctX+PANE_SIZE/2-10);
            text.setLayoutY(this.correctY+PANE_SIZE/2+10);
            text.setFill(Color.WHITE);
           
            
            getChildren().addAll(pieceStroke,text);
            // キャッシュ有効化
            setCache(true);
            setActive();
        }
        
        
        
        //ピースを描画
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
        
        public void setActive() {
            setDisable(false);
            setEffect(new DropShadow());
            toFront();
        }
        
        public double getCorrectX() {
            return correctX;
        }

        public double getCorrectY() {
            return correctY;
        }
    }

}
