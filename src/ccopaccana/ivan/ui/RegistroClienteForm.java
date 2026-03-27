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
 * Formulario de registro de clientes (coleccionistas y vendedores)
 * de la plataforma de subastas.
 * <p>
 * NOTA: Este formulario es únicamente visual, no tiene funcionalidad de negocio.
 * </p>
 */
public class RegistroClienteForm {

    /** Escenario principal de la aplicación */
    private final Stage stage;

    /**
     * Constructor del formulario de registro de clientes.
     *
     * @param stage Escenario principal donde se mostrará el formulario
     */
    public RegistroClienteForm(Stage stage) {
        this.stage = stage;
    }

    /**
     * Construye y retorna el nodo raíz del formulario de registro.
     *
     * @return Parent con la vista del formulario
     */
    public Parent getView() {
        // Título
        Label titulo = new Label("Registro de Cliente");
        titulo.getStyleClass().add("titulo");

        // Tipo de usuario
        Label lblTipo = new Label("Tipo de usuario:");
        ToggleGroup tgTipo = new ToggleGroup();
        RadioButton rbVendedor = new RadioButton("Vendedor");
        RadioButton rbColeccionista = new RadioButton("Coleccionista");
        rbVendedor.setToggleGroup(tgTipo);
        rbColeccionista.setToggleGroup(tgTipo);
        rbColeccionista.setSelected(true);
        HBox hbTipo = new HBox(16, rbVendedor, rbColeccionista);

        // Nombre completo
        Label lblNombre = new Label("Nombre completo:");
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ej: Juan Pérez Rodríguez");
        txtNombre.getStyleClass().add("campo");

        // Identificación
        Label lblId = new Label("Identificación:");
        TextField txtId = new TextField();
        txtId.setPromptText("Número de cédula o pasaporte");
        txtId.getStyleClass().add("campo");

        // Fecha de nacimiento
        Label lblFecha = new Label("Fecha de nacimiento (dd/MM/yyyy):");
        TextField txtFecha = new TextField();
        txtFecha.setPromptText("Ej: 15/06/1990");
        txtFecha.getStyleClass().add("campo");

        // Correo
        Label lblCorreo = new Label("Correo electrónico:");
        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("ejemplo@correo.com");
        txtCorreo.getStyleClass().add("campo");

        // Contraseña
        Label lblContrasena = new Label("Contraseña:");
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Mínimo 6 caracteres");
        txtContrasena.getStyleClass().add("campo");

        // Dirección
        Label lblDireccion = new Label("Dirección:");
        TextField txtDireccion = new TextField();
        txtDireccion.setPromptText("Dirección de residencia");
        txtDireccion.getStyleClass().add("campo");

        // Botón registrar
        Button btnRegistrar = new Button("Registrarse");
        btnRegistrar.getStyleClass().add("boton-principal");
        btnRegistrar.setMaxWidth(Double.MAX_VALUE);

        // Separador + link login
        Separator sep = new Separator();
        Text txtLogin = new Text("¿Ya tiene cuenta? ");
        Hyperlink linkLogin = new Hyperlink("Inicie sesión aquí");
        linkLogin.setOnAction(e -> volverLogin());
        HBox hLogin = new HBox(4, txtLogin, linkLogin);
        hLogin.setAlignment(Pos.CENTER);

        // Layout principal
        VBox vbox = new VBox(10,
                titulo,
                lblTipo, hbTipo,
                lblNombre, txtNombre,
                lblId, txtId,
                lblFecha, txtFecha,
                lblCorreo, txtCorreo,
                lblContrasena, txtContrasena,
                lblDireccion, txtDireccion,
                btnRegistrar,
                sep,
                hLogin
        );
        vbox.setPadding(new Insets(25, 40, 25, 40));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getStyleClass().add("contenedor");

        ScrollPane scroll = new ScrollPane(vbox);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        return scroll;
    }

    /**
     * Regresa al formulario de inicio de sesión.
     */
    private void volverLogin() {
        LoginForm loginForm = new LoginForm(stage);
        Scene scene = new Scene(loginForm.getView(), 420, 320);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        stage.setTitle("Plataforma de Subastas - Iniciar Sesión");
        stage.setScene(scene);
    }
}
