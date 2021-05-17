package MainForm;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application
{
    public static Scene MainWindow;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
        Parent root = loader.load();

        MainForm mainForm = (MainForm) loader.getController();

        MainWindow = new Scene(root, 590, 740);
        primaryStage.setTitle("Tetris. Курсовой Крютченко Н.С.");
        primaryStage.getIcons().add(new Image("file:icon.png"));
        primaryStage.setScene(MainWindow);
        primaryStage.setResizable(false);


        // Инициализация нажатия кнопок
        Main.MainWindow.setOnKeyPressed(e -> {
            mainForm.KeyDown(e.getCode());
        });

        Main.MainWindow.setOnKeyReleased(e -> {
            mainForm.KeyUp(e.getCode());
        });



        primaryStage.show();

        //primaryStage.getIcons().add();
    }

    @Override
    public void stop()
    {

        System.out.println("Главное окно закрыто!");
        // Сохранение...
        System.out.println("Сохранение рекорда...");

        Platform.exit();
        System.exit(0);
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
