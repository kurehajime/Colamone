/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colamone;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
import javafx.util.Duration;

/**
 *
 * @author gabill
 */
public class Colamone extends Application {

    private final int PANE_SIZE = 100;
    private final int PANE_COUNT = 6;
    private  Scene scene;
    private StackPane root;
    private Desk desk;
    private Timeline timeline;

    /***
     * 現在のマップ
     */
    private Board board = new Board(true);
    /***
     * ピースオブジェクトを格納
     */
    final List<Piece> pieces = new ArrayList<>();
    /***
     * 持ち上げているコマ
     */
    private Piece hoverPiece=null;
    
    /***
     * 今のターン
     */
    private int thisTurn=0;

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
        root = new StackPane();
        scene = new Scene(root, 600+300, 600);
        
        desk = new Desk();
        //コマを生成
        for(int i=1;i<=8;i++){
            pieces.add(new Piece(0,0, i));
            pieces.add(new Piece(0,0, -1*i));
        }
        desk.getChildren().addAll(pieces);
        
        //コマを配置
        this.board=new Board(true);
        drawPieaceAll(this.board);
        
        //ステータスを配置
        SubPanel subpanel=new SubPanel();
        
        //横並びに配置
        HBox hBox = new HBox(2);
        hBox.getChildren().addAll(desk,subpanel);
        root.getChildren().addAll(hBox);
        desk.setOnMouseClicked((MouseEvent event) -> {
            clickEvent(event);
        });
        desk.setOnMouseMoved((MouseEvent event) -> {
            moveEvent(event);
        });
        thisTurn=1;
        
        primaryStage.setTitle("Colamone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /***
     * コマをすべて描画
     * @param wkMap 
     */
    private void drawPieaceAll(Map<Integer, Integer> wkMap){
        wkMap.entrySet().stream().filter((entry) -> (entry.getValue()!=0)).forEach((entry) -> {
            int x=((int) Math.ceil(entry.getKey()/10))*PANE_SIZE;
            int y=((int) Math.ceil(entry.getKey()%10))*PANE_SIZE;
            Piece p=getPieceByNumber(entry.getValue());
            p.setLayoutX(x);
            p.setLayoutY(y);
            int target_y = (int)Math.floor(entry.getKey() % 10);
            if(entry.getValue()>0 & target_y==0||entry.getValue()<0 &target_y==5 ){
                //ゴールした。
                p.setGoal();
            }else{
                //標準に戻す。
                p.setDefault();
            }
        });
        pieces.stream().filter((p) -> (board.getPosiotionByNumber(p.number)==-1)).forEach((p) -> {
            p.setNoActive();
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
     * サブパネル
     */
    class SubPanel extends Pane {

        SubPanel() {
            double DESK_WIDTH = PANE_SIZE * PANE_COUNT;
            setPrefSize(300, DESK_WIDTH);
            setMaxSize(300, DESK_WIDTH);
            autosize();
        }
    }

    /***
     * コマクラス
     */
    public static final class Piece extends Parent {
        private final double correctX;
        private final double correctY;
        public final int number;
        private final Color RED=Color.rgb(255, 0, 0);
        private final Color BLUE=Color.rgb(0, 0, 255);
        private final Color RED_HOVER=Color.rgb(255, 60, 60);
        private final Color BLUE_HOVER=Color.rgb(60, 60, 255);
        private final Color RED_GOAL=Color.rgb(100, 0, 0);
        private final Color BLUE_GOAL=Color.rgb(0, 0, 100);
        private final int PANE_SIZE = 100;
        private Color piece_color;
        
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
                piece_color=BLUE;
            }else{
                piece_color=RED;
            }
            draw();
        }
        
        /***
         * 描画
         */
        private void draw(){
            //いったん消す。
            getChildren().removeAll();
            
            //文字
            Text text=new Text();
            text.setFont(new Font(30));
            text.setText(Integer.toString(Math.abs(this.number)));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setLayoutX(this.correctX+PANE_SIZE/2-10);
            text.setLayoutY(this.correctY+PANE_SIZE/2+10);
            text.setFill(Color.WHITE);
            
            // 枠線
            Shape pieceStroke = createPieceLine();
            pieceStroke.setFill(piece_color);
            setFocusTraversable(true);
            
            
            //コマと文字を貼り付け
            getChildren().addAll(pieceStroke,text);
            
            int[] move=Board.getMoveArray(number);
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
         * コマ枠を描画
         * @return 
         */
        private Shape createPieceLine() {
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
        
        /***
         * 掴んだ時。
         */
        public void setHover(){
            if(number>0){
                piece_color=BLUE_HOVER;
            }else{
                piece_color=RED_HOVER;
            }
            draw();
        }
        /***
         * 掴んだ時。
         */
        public void setDefault(){
            if(number>0){
                piece_color=BLUE;
            }else{
                piece_color=RED;
            }
            draw();
        }
        /***
         * 掴んだ時。
         */
        public void setGoal(){
            if(number>0){
                piece_color=BLUE_GOAL;
            }else{
                piece_color=RED_GOAL;
            }
            draw();
        }

        
    }
    
    /***
     * クリックイベント
     * @param event 
     */
    void clickEvent(MouseEvent event){
        
        double x=event.getSceneX()-desk.getLayoutX();
        double y=event.getSceneY()-desk.getLayoutY();
        int target_position = (int) (Math.floor(x/PANE_SIZE)*10+Math.floor(y/PANE_SIZE));
        
        if(hoverPiece==null){
            //コマを持ち上げる
            if(this.board.get(target_position)!=0&& this.board.get(target_position)*thisTurn>0){
                hoverPiece=getPieceByPosition(this.board,target_position);
                hoverPiece.setHover();
                hoverPiece.toFront();
                moveEvent(event);
            }
        }else{
            boolean putflg;
            //コマを置き換え
            int prevPisition=board.getPosiotionByNumber(hoverPiece.number);
            if(board.checkBoard(prevPisition,target_position)){
                this.board=board.putBoard(prevPisition, target_position);
                thisTurn=thisTurn*-1;
                putflg=true;
            }else{
                target_position=prevPisition;
                putflg=false;
            }
            int target_y = (int)Math.floor(target_position % 10);
            if(this.board.get(target_position)>0 & target_y==0||this.board.get(target_position)<0 &target_y==5 ){
                //ゴールした。
                //hoverPiece.setGoal();
            }else{
                //標準に戻す。
                //hoverPiece.setDefault();
            }
            
            hoverPiece=null;
            drawPieaceAll(this.board);
            
            //AIのターン
            if(putflg){
                AI ai=new AI(new EvalParam());
                HandWithPoint hwp= ai.deepThinkAllAB(board,thisTurn,4, 0, 0);
                this.board=board.putBoard(hwp.hand[0], hwp.hand[1]);
                //drawPieaceAll(this.board);
                Piece p=getPieceByPosition(board, hwp.hand[1]);
                int x2=((int) Math.ceil(hwp.hand[1]/10))*PANE_SIZE;
                int y2=((int) Math.ceil(hwp.hand[1]%10))*PANE_SIZE;
                timeline = new Timeline();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(p.layoutXProperty(), x2  ),
                                new KeyValue(p.layoutYProperty(), y2 )));
                timeline.playFromStart();
                drawPieaceAll(this.board);
                thisTurn=thisTurn*-1;
            }
        }
        
    }
    /***
     * マウス移動イベント
     * @param event 
     */
    void moveEvent(MouseEvent event){
        if(hoverPiece!=null){
            double x=event.getSceneX()-desk.getLayoutX();
            double y=event.getSceneY()-desk.getLayoutY();
            hoverPiece.setLayoutX(x-PANE_SIZE/2);
            hoverPiece.setLayoutY(y-PANE_SIZE/2);
        }
    }
    
    /***
     * 番号からピースを取得
     * @param number
     * @return 
     */
    Piece getPieceByNumber(int number){
         for(Piece p:this.pieces){
             if(p.number==number){
                 return p;
             }
         }
         return null;
    }
    
    /***
     * 位置からピースを取得
     * @param map
     * @param position
     * @return 
     */
    Piece getPieceByPosition(Map<Integer, Integer> map,int position){
        return getPieceByNumber(map.get(position));
    }
    
}
