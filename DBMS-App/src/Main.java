import javafx.application.Application;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import java.util.Objects;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		try{
			WebView webView = new WebView();
			final WebEngine webEngine = webView.getEngine();
			Interface interfaceobj = new Interface(webEngine);

			webEngine.load(Objects.requireNonNull(getClass().getResource("dbms_frontend/index.html")).toExternalForm());
			webEngine.getLoadWorker().stateProperty().addListener(
					(ov, oldState, newState) -> {
						if (newState == State.SUCCEEDED){
							webEngine.executeScript("initSTART__VALUES('"+ interfaceobj.getStartValues() +"')");
							JSObject window = (JSObject) webEngine.executeScript("window");
							window.setMember("InterfaceOBJ", interfaceobj);
						}
					});

			VBox vBox = new VBox(webView);
			Scene scene = new Scene(vBox, 1191, 600);
			primaryStage.setTitle("ECHO dbms");
			primaryStage.getIcons().add(new Image("dbms_frontend/img/DBMS_LOGO.png"));
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}