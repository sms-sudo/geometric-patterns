import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class Testing extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 400, 400);
       
        Circle circle1 = new Circle(100, 100, 50);
        circle1.setFill(Color.RED);
        Circle circle2 = new Circle(300, 200, 70);
        circle2.setFill(Color.WHITE);
        circle2.setStroke(Color.GREEN);
        circle2.setStrokeWidth(4);
        root.getChildren().add(circle1);
        root.getChildren().add(circle2);
       
        Line line1 = new Line(0, 0, 400, 400);
        line1.setStrokeWidth(4);
       
        root.getChildren().add(line1);

        primaryStage.setTitle("Hello");
        primaryStage.setScene(scene);
        primaryStage.show();
       
    }

}