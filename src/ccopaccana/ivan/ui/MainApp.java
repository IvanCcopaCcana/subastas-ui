package ccopaccana.ivan.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada principal de la aplicación JavaFX de subastas.
 * Inicia mostrando el formulario de inicio de sesión.
 */
public class MainApp extends Application {

    /**
     * Inicializa y muestra la ventana principal con el formulario de inicio de sesión.
     *
     * @param primaryStage Escenario principal de la aplicación
     */
    @Override
    public void start(Stage primaryStage) {
        LoginForm loginForm = new LoginForm(primaryStage);
        Scene scene = new Scene(loginForm.getView(), 420, 320);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Plataforma de Subastas - Iniciar Sesión");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Método principal que lanza la aplicación JavaFX.
     *
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
