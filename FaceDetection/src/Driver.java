import java.lang.reflect.Field;
import java.net.URISyntaxException;

import org.opencv.core.Core;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Driver extends Application {

	private static String URL = "resources/1.jpg";

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root);
		stage.setScene(scene);

		try {
			URL = getClass().getResource("1.jpg").toURI().toString();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		Image image = new Image(URL);
		ImageView imageView = new ImageView(image);
		root.setCenter(imageView);

		Button button = new Button();
		root.setBottom(button);
		stage.setWidth(800);
		stage.setHeight(800);

		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FaceDetector f = new FaceDetector(image);
				Image processedImage = f.detectFaces();
				ImageView processedImageView = new ImageView(processedImage);

				root.setCenter(processedImageView);
			}

		});

		stage.show();
	}
}