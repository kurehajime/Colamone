/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package colamone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
    private Scene scene;
    private StackPane root;
    private Desk desk;
    private Timeline timeline;
    private SubPanel subPanel;
    private int level = 3;

    /**
     * *
     * 現在のマップ
     */
    private Board board = new Board(true);
    /**
     * *
     * ピースオブジェクトを格納
     */
    final List<Piece> pieces = new ArrayList<>();
    /**
     * *
     * 持ち上げているコマ
     */
    private Piece hoverPiece = null;
    /**
     * *
     * 今のターン
     */
    //private final  AtomicInteger thisTurn = new AtomicInteger(0);

    /***
     * 考え中
     */
    //private  final  AtomicBoolean thinking = new AtomicBoolean(false);
    
    
    
    /**
     * main
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * *
     * 初期表示
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        scene = new Scene(root, 600 + 300, 600);

        desk = new Desk();
        //コマを生成
        for (int i = 1; i <= 8; i++) {
            pieces.add(new Piece(0, 0, i));
            pieces.add(new Piece(0, 0, -1 * i));
        }
        desk.getChildren().addAll(pieces);

        //コマを配置
        board.turn=1;
        this.board = new Board(true);
        drawPieaceAll(this.board);

        //ステータスを配置
        this.subPanel = new SubPanel();

        //横並びに配置
        HBox hBox = new HBox(2);
        hBox.getChildren().addAll(desk, subPanel);
        root.getChildren().addAll(hBox);
        desk.setOnMouseClicked((MouseEvent event) -> {
            clickEvent(event);
        });
        desk.setOnMouseMoved((event) -> {
            moveEvent(event);
        });
        drawSubPanel();

        primaryStage.setTitle("Colamone");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * *
     * コマをすべて描画
     *
     * @param wkMap
     */
    private void drawPieaceAll(Map<Integer, Integer> wkMap) {
        wkMap.entrySet().stream().filter((entry) -> (entry.getValue() != 0)).forEach((entry) -> {
            int x = ((int) Math.ceil(entry.getKey() / 10)) * PANE_SIZE;
            int y = ((int) Math.ceil(entry.getKey() % 10)) * PANE_SIZE;
            Piece p = getPieceByNumber(entry.getValue());
            p.setLayoutX(x);
            p.setLayoutY(y);
            int target_y = (int) Math.floor(entry.getKey() % 10);
            if (entry.getValue() > 0 & target_y == 0 || entry.getValue() < 0 & target_y == 5) {
                //ゴールした。
                p.setGoal();
            } else {
                //標準に戻す。
                p.setDefault();
            }
        });
        pieces.stream().filter((p) -> (board.getPosiotionByNumber(p.number) == -1)).forEach((p) -> {
            p.setNoActive();
        });

    }
    
    

    /**
     * *
     * サブパネルを管理
     */
    private void drawSubPanel() {

        if (board.turn == 1) {
            subPanel.setTitle("Blue Turn", Color.BLUE);
        } else if (board.turn == -1) {
            subPanel.setTitle("Red Turn", Color.RED);
        }
        int end=board.isEnd();
        if (end==1) {
            subPanel.setTitle("BLUE WIN !", Color.BLUE);
            board.turn=0;
        } else if (end==-1) {
            subPanel.setTitle("RED  WIN !", Color.RED);
            board.turn=0;
        }
        
       subPanel.setScore("Blue:"+Integer.toString(this.board.getScore(1))
               +"  Red:"
               +Integer.toString(this.board.getScore(-1))
               ,Color.BLACK);
       
        
        
    }

    /**
     * *
     * クリックイベント
     *
     * @param event
     */
    void clickEvent(MouseEvent event) {

        double x = event.getSceneX() - desk.getLayoutX();
        double y = event.getSceneY() - desk.getLayoutY();
        int target_position = (int) (Math.floor(x / PANE_SIZE) * 10 + Math.floor(y / PANE_SIZE));

        if (this.board.turn == 0||this.board.lock==true) {
            return;
        }

        if (hoverPiece == null) {
            //コマを持ち上げる
            if (this.board.get(target_position) != 0 && this.board.get(target_position) * this.board.turn > 0) {
                hoverPiece = getPieceByPosition(this.board, target_position);
                hoverPiece.setHover();
                hoverPiece.toFront();
                moveEvent(event);
            }
        } else {
            boolean putflg;
            //コマを置き換え
            int prevPisition = board.getPosiotionByNumber(hoverPiece.number);
            if (board.checkBoard(prevPisition, target_position)) {
                this.board = board.putBoard(prevPisition, target_position);
                this.board.turn*=-1;
                putflg = true;                
                drawSubPanel();
            } else {
                putflg = false;
            }

            hoverPiece = null;
            drawPieaceAll(this.board);
            drawSubPanel();
            

            //AIのターン
            if (putflg && this.board.turn != 0) {
                final Task<Boolean> task1 = getAITask();  // ★1Taskを作成 
                //task1.run();                
                Executors.newSingleThreadExecutor().submit(task1);                   //タスクを実行
                                
            }

        }

    }

    /**
     * *
     * AIが考える
     *
     * @return
     */
    private  Task<Boolean> getAITask() {
        return new Task<Boolean>() {
            @Override
            protected  Boolean call() throws Exception {
                board.lock =true;
                AI ai = new AI(new EvalParam());
                int depth = level + 1;
                HandWithPoint hwp = ai.deepThinkAllAB(board, board.turn, depth, 0, 0);
                board = board.putBoard(hwp.hand[0], hwp.hand[1]);
                Piece p = getPieceByPosition(board, hwp.hand[1]);
                int x2 = ((int) Math.ceil(hwp.hand[1] / 10)) * PANE_SIZE;
                int y2 = ((int) Math.ceil(hwp.hand[1] % 10)) * PANE_SIZE;
                timeline = new Timeline();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(0.2),
                                new KeyValue(p.layoutXProperty(), x2),
                                new KeyValue(p.layoutYProperty(), y2)));
                timeline.playFromStart();
                board.turn*=-1;
                board.lock=false;
                Platform.runLater(()->{
                    drawSubPanel();
                    drawPieaceAll(board);
                    Runtime.getRuntime().gc();
                });

                return true;
            }
        };
    }

    /**
     * *
     * マウス移動イベント
     *
     * @param event
     */
    void moveEvent(MouseEvent event) {
        if (hoverPiece != null) {
            double x = event.getSceneX() - desk.getLayoutX();
            double y = event.getSceneY() - desk.getLayoutY();
            hoverPiece.setLayoutX(x - PANE_SIZE / 2);
            hoverPiece.setLayoutY(y - PANE_SIZE / 2);
        }
    }

    /**
     * *
     * 番号からピースを取得
     *
     * @param number
     * @return
     */
    Piece getPieceByNumber(int number) {
        for (Piece p : this.pieces) {
            if (p.number == number) {
                return p;
            }
        }
        return null;
    }

    /**
     * *
     * 位置からピースを取得
     *
     * @param map
     * @param position
     * @return
     */
    Piece getPieceByPosition(Map<Integer, Integer> map, int position) {
        return getPieceByNumber(map.get(position));
    }

    /**
     * *
     * 盤面をリセット
     */
    void reStart() {
        this.board = new Board(true);
        drawPieaceAll(this.board);
        drawSubPanel();
    }

    /**
     * *
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

    /**
     * *
     * サブパネル
     */
    class SubPanel extends Pane {

        private final Label topLabel;
        private final Label scoreLabel;
        private final Slider slider;

        @Override
        public Bounds localToParent(Bounds localBounds) {
            return super.localToParent(localBounds); //To change body of generated methods, choose Tools | Templates.
        }

        void setTitle(String s, Color c) {
            topLabel.setText(s);
            topLabel.setTextFill(c);
        }

        void setScore(String s, Color c) {
            scoreLabel.setText(s);
            scoreLabel.setTextFill(c);
        }

        /**
         * *
         * サブパネル初期化
         */
        SubPanel() {
            double DESK_WIDTH = PANE_SIZE * PANE_COUNT;
            setPrefSize(300, DESK_WIDTH);
            setMaxSize(300, DESK_WIDTH);
            VBox vBox = new VBox(8);
            //タイトルラベル
            topLabel = new Label();
            topLabel.setText("...");
            topLabel.setFont(new Font(30));
            topLabel.setAlignment(Pos.CENTER);
            topLabel.setTextFill(Color.rgb(0, 0, 255));
            //セパレータ
            Separator separator0 = new Separator();
            separator0.setMinWidth(300);
            separator0.setOrientation(Orientation.HORIZONTAL);

            //レベル スタイダー
            Label sLabel = new Label();
            sLabel.setText("CPU Level:");
            slider = new Slider(1, 5, 3);
            slider.setOrientation(Orientation.HORIZONTAL);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setBlockIncrement(1);
            slider.setMajorTickUnit(1f);
            slider.valueProperty().addListener((event) -> {
                level = (int) Math.round(slider.getValue());
            });

            //セパレータ
            Separator separator1 = new Separator();
            separator1.setMinWidth(300);
            separator1.setOrientation(Orientation.HORIZONTAL);
            //SCORE
            scoreLabel = new Label();
            scoreLabel.setText("...");
            scoreLabel.setFont(new Font(20));
            scoreLabel.setAlignment(Pos.CENTER);
            scoreLabel.setTextFill(Color.rgb(0, 0, 0));
            //セパレータ
            Separator separator2 = new Separator();
            separator2.setMinWidth(300);
            separator2.setOrientation(Orientation.HORIZONTAL);
            Button button = new Button("ReStart");
            button.setMinWidth(300);
            button.setOnMouseClicked((event) -> {
                reStart();
            });
            //セパレータ
            Separator separator3 = new Separator();
            separator3.setMinWidth(300);
            separator3.setOrientation(Orientation.HORIZONTAL);
            //説明
            Label manuLabel = new Label();
            manuLabel.setText("【ルール】\n"
            +"・各コマは、丸が付いている方向に進めます。\n"
            +"・一番奥の陣地まで進めると、コマに\n"
            +"　書かれている数字が得点になります。\n"
            +"・得点が8点以上になれば勝利です。\n"
            +"・どちらか片方の動かせるコマがなくなった場合\n"
            +"　その時点で点数の高い方が勝利です。\n"
            +"・ただし同点の場合は後手勝利になります。\n"
            );
            
            //垂直に配置
            vBox.getChildren().addAll(topLabel, separator0,
                    sLabel, slider, separator1, scoreLabel, separator2, button,
                    separator3,manuLabel);
            this.getChildren().add(vBox);
            autosize();
        }
    }

    /**
     * *
     * コマクラス
     */
    class Piece extends Parent {

        private final double correctX;
        private final double correctY;
        public final int number;
        private final Color RED = Color.rgb(255, 0, 0);
        private final Color BLUE = Color.rgb(0, 0, 255);
        private final Color RED_HOVER = Color.rgb(255, 60, 60);
        private final Color BLUE_HOVER = Color.rgb(60, 60, 255);
        private final Color RED_GOAL = Color.rgb(100, 0, 0);
        private final Color BLUE_GOAL = Color.rgb(0, 0, 100);
        private final int PANE_SIZE = 100;
        private Color piece_color;

        /**
         * *
         * コントラスタ
         *
         * @param correctX
         * @param correctY
         * @param number
         */
        public Piece(final double correctX, final double correctY, int number) {
            this.correctX = correctX;
            this.correctY = correctY;
            this.number = number;
            if (this.number > 0) {
                piece_color = BLUE;
            } else {
                piece_color = RED;
            }
            draw();
        }

        /**
         * *
         * 描画
         */
        private void draw() {
            //いったん消す。
            getChildren().removeAll();

            //文字
            Text text = new Text();
            text.setFont(new Font(30));
            text.setText(Integer.toString(Math.abs(this.number)));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setLayoutX(this.correctX + PANE_SIZE / 2 - 10);
            text.setLayoutY(this.correctY + PANE_SIZE / 2 + 10);
            text.setFill(Color.WHITE);

            // 枠線
            Shape pieceStroke = createPieceLine();
            pieceStroke.setFill(piece_color);
            setFocusTraversable(true);

            //コマと文字を貼り付け
            getChildren().addAll(pieceStroke, text);

            int[] move = Board.getMoveArray(number);
            for (int i = 0; i < move.length; i++) {
                if (move[i] == 0) {
                    continue;
                }
                double x = this.correctX + 0.25 * PANE_SIZE + (Math.floor(PANE_SIZE - 0.25 * PANE_SIZE) / 3) * Math.floor(i % 3.0);
                double y = this.correctY + 0.25 * PANE_SIZE + (Math.floor(PANE_SIZE - 0.25 * PANE_SIZE) / 3) * Math.floor(i / 3.0);
                Circle c1 = new Circle(PANE_SIZE * 0.04, Color.WHITE);
                c1.setLayoutX(x);
                c1.setLayoutY(y);
                getChildren().addAll(c1);
            }

            // キャッシュ有効化
            setCache(true);
            setActive();
        }

        /**
         * *
         * コマ枠を描画
         *
         * @return
         */
        private Shape createPieceLine() {
            Polygon polygon = new Polygon();
            polygon.setLayoutX(correctX);
            polygon.setLayoutY(correctY);
            polygon.getPoints().addAll(new Double[]{
                0.1 * PANE_SIZE, 0.1 * PANE_SIZE,
                0.9 * PANE_SIZE, 0.1 * PANE_SIZE,
                0.9 * PANE_SIZE, 0.9 * PANE_SIZE,
                0.1 * PANE_SIZE, 0.9 * PANE_SIZE});
            return polygon;
        }

        /**
         * *
         * 有効化
         */
        public void setActive() {
            setDisable(false);
            setVisible(true);
            setEffect(new DropShadow());
            toFront();
        }

        /**
         * *
         * 無効化
         */
        public void setNoActive() {
            setDisable(true);
            setVisible(false);
            toBack();
        }

        /**
         * *
         * 掴んだ時。
         */
        public void setHover() {
            if (number > 0) {
                piece_color = BLUE_HOVER;
            } else {
                piece_color = RED_HOVER;
            }
            draw();
        }

        /**
         * *
         * 標準。
         */
        public void setDefault() {
            if (number > 0) {
                piece_color = BLUE;
            } else {
                piece_color = RED;
            }
            draw();
        }

        /**
         * *
         * ゴールした時。
         */
        public void setGoal() {
            if (number > 0) {
                piece_color = BLUE_GOAL;
            } else {
                piece_color = RED_GOAL;
            }
            draw();
        }

    }

}
