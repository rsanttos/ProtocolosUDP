package br.com.imd.protocolos.udp.multicast.camera.view;

import java.io.File;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("restriction")
public class WebCamApp extends Application {
	private AnchorPane pane;
	private ImageView imagemItem;
	private static Stage stage;
	private static int index;
	private static String imgPokemons[];

	@Override
	public void start(Stage stage) throws Exception {
		initComponents();
		initLayout();
		initTransition();
		Scene scene = new Scene(this.pane);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Imagem da WebCam");
		stage.show();
		WebCamApp.stage = stage;
		//initLayout();
	}

	public void initTransition(){
		FadeTransition ft = new FadeTransition(Duration.millis(3000), this.imagemItem);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.play();
		
//		ParallelTransition pt = new ParallelTransition();
//		pt.getChildren().addAll(ft, st);
//		pt.play();
		
		SequentialTransition seqt = new SequentialTransition();
		seqt.getChildren().addAll(ft);
		seqt.play();
	}
	
	public void initTimeline(){
		Timeline t = new Timeline();
		KeyValue kv = new KeyValue(imagemItem.opacityProperty(), 0.0);
		KeyFrame kf = new KeyFrame(Duration.millis(2000), kv);
		t.getKeyFrames().add(kf);
		t.setCycleCount(Timeline.INDEFINITE);
		t.setAutoReverse(true);
		t.play();
	}
	
	public void initLayout(){
		this.imagemItem.setX(50);
		this.imagemItem.setY(50);
		this.imagemItem.setFitHeight(300);
		this.imagemItem.setFitWidth(200);
		this.imagemItem.setEffect(new Reflection());
		InnerShadow is = new InnerShadow();
		is.setColor(Color.GREEN);
	}
	
	public void initComponents() {
		this.pane = new AnchorPane();
		this.pane.setPrefSize(600, 400);
		File arquivo = new File("/ProtocolosUDP/img/quasela.png");
		Image image = new Image(arquivo.getName());
		this.imagemItem = new ImageView();
		this.imagemItem.setImage(image);
		
		this.pane.getChildren().addAll(this.imagemItem);
	}

	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		WebCamApp.stage = stage;
	}	

	public static int getIndex() {
		return index;
	}

	public static void setIndex(int index) {
		WebCamApp.index = index;
	}
	
	public AnchorPane getPane() {
		return pane;
	}

	public void setPane(AnchorPane pane) {
		this.pane = pane;
	}

	public ImageView getImagemItem() {
		return imagemItem;
	}

	public void setImagemItem(ImageView imagemItem) {
		this.imagemItem = imagemItem;
	}

	
	public static String[] getImgPokemons() {
		return imgPokemons;
	}

	public static void setImgPokemons(String[] imgPokemons) {
		WebCamApp.imgPokemons = imgPokemons;
	}

	

}
