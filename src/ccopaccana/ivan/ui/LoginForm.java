package ccopaccana.ivan.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Formulario de inicio de sesión de la plataforma de subastas.
 * Contiene campos de correo y contraseña, botón de ingreso
 * y enlace para navegar al formulario de registro.
 * <p>
 * NOTA: Este formulario es únicamente visual, no tiene funcionalidad de negocio.
 * </p>
 */
public class LoginForm {

    /** Escenario principal de la aplicación */
    private final Stage stage;

    /**
     * Constructor del formulario de inicio de sesión.
     *
     * @param stage Escenario principal donde se mostrará el formulario
     */
    public LoginForm(Stage stage) {
        this.stage = stage;
    }

    /**
     * Construye y retorna el nodo raíz del formulario de inicio de sesión.
     *
     * @return Parent con la vista del formulario
     */
    public Parent getView() {
        // Título
        Label titulo = new Label("Iniciar Sesión");
        titulo.getStyleClass().add("titulo");

        // Campo correo
        Label lblCorreo = new Label("Correo electrónico:");
        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("ejemplo@correo.com");
        txtCorreo.getStyleClass().add("campo");

        // Campo contraseña
        Label lblContrasena = new Label("Contraseña:");
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Ingrese su contraseña");
        txtContrasena.getStyleClass().add("campo");

        // Botón ingresar
        Button btnIngresar = new Button("Ingresar");
        btnIngresar.getStyleClass().add("boton-principal");
        btnIngresar.setMaxWidth(Double.MAX_VALUE);

        // Separador
        Separator sep = new Separator();

        // Link a registro
        Text txtRegistro = new Text("¿No tiene cuenta? ");
        Hyperlink linkRegistro = new Hyperlink("Regístrese aquí");
        linkRegistro.setOnAction(e -> abrirRegistro());
        HBox hRegistro = new HBox(4, txtRegistro, linkRegistro);
        hRegistro.setAlignment(Pos.CENTER);

        // Layout principal
        VBox vbox = new VBox(12,
                titulo,
                lblCorreo, txtCorreo,
                lblContrasena, txtContrasena,
                btnIngresar,
                sep,
                hRegistro
        );
        vbox.setPadding(new Insets(30, 40, 30, 40));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getStyleClass().add("contenedor");

        return vbox;
    }

    /**
     * Abre el formulario de registro de clientes en el mismo escenario.
     */
    private void abrirRegistro() {
        RegistroClienteForm registroForm = new RegistroClienteForm(stage);
        Scene scene = new Scene(registroForm.getView(), 460, 520);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Plataforma de Subastas - Registro de Cliente");
        stage.setScene(scene);
    }
}
