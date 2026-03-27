package ccopaccana.ivan.ui;

import ccopaccana.ivan.dominio.*;
import ccopaccana.ivan.logica.ControladorSubastas;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Interfaz de usuario en consola para la plataforma de subastas.
 * Gestiona la interacción con el usuario a través de menús de texto.
 * NOTA: Esta clase NO instancia objetos de negocio directamente,
 * toda la lógica es delegada al ControladorSubastas.
 */
public class MenuConsola {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ControladorSubastas controlador = new ControladorSubastas();
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Punto de entrada principal de la aplicación.
     *
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        verificarModerador();
        menuPrincipal();
    }

    /**
     * Verifica si existe un moderador y solicita su registro si no existe.
     */
    private static void verificarModerador() {
        if (!controlador.existeModerador()) {
            System.out.println("=== REGISTRO INICIAL DE MODERADOR ===");
            System.out.println("No existe un moderador registrado. Por favor registre uno.");
            Moderador mod = solicitarDatosModerador();
            boolean resultado = controlador.registrarModerador(mod);
            if (resultado) {
                System.out.println("Moderador registrado exitosamente.\n");
            } else {
                System.out.println("Error al registrar el moderador. Verifique que sea mayor de edad.");
                System.exit(0);
            }
        }
    }

    /**
     * Muestra el menú principal y gestiona la navegación.
     */
    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println("\n========== PLATAFORMA DE SUBASTAS ==========");
            System.out.println("1. Registro de usuarios");
            System.out.println("2. Listado de usuarios");
            System.out.println("3. Creación de subastas");
            System.out.println("4. Listado de subastas");
            System.out.println("5. Creación de ofertas");
            System.out.println("6. Listado de ofertas");
            System.out.println("7. Inicio de sesión");
            System.out.println("8. Validar existencia de moderador");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEntero();

            switch (opcion) {
                case 1 -> menuRegistroUsuarios();
                case 2 -> listarUsuarios();
                case 3 -> crearSubasta();
                case 4 -> listarSubastas();
                case 5 -> crearOferta();
                case 6 -> listarOfertas();
                case 7 -> iniciarSesion();
                case 8 -> validarModerador();
                case 0 -> System.out.println("¡Hasta luego!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // =================== USUARIOS ===================

    /**
     * Menú de registro de usuarios: vendedor o coleccionista.
     */
    private static void menuRegistroUsuarios() {
        System.out.println("\n-- Registro de Usuarios --");
        System.out.println("1. Registrar Vendedor");
        System.out.println("2. Registrar Coleccionista");
        System.out.print("Seleccione: ");
        int op = leerEntero();

        if (op == 1) {
            Vendedor v = solicitarDatosVendedor();
            boolean ok = controlador.registrarVendedor(v);
            System.out.println(ok ? "Vendedor registrado." : "Error: debe ser mayor de edad.");
        } else if (op == 2) {
            Coleccionista c = solicitarDatosColeccionista();
            boolean ok = controlador.registrarColeccionista(c);
            System.out.println(ok ? "Coleccionista registrado." : "Error: debe ser mayor de edad.");
        }
    }

    /**
     * Lista todos los usuarios registrados en el sistema.
     */
    private static void listarUsuarios() {
        System.out.println("\n-- Usuarios Registrados --");
        ArrayList<Usuario> lista = controlador.listarUsuarios();
        if (lista.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    // =================== SUBASTAS ===================

    /**
     * Flujo para crear una nueva subasta desde consola.
     */
    private static void crearSubasta() {
        System.out.println("\n-- Crear Subasta --");
        ArrayList<Usuario> usuarios = controlador.listarUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados para crear subastas.");
            return;
        }

        System.out.println("Usuarios disponibles:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + ". " + usuarios.get(i).getNombreCompleto());
        }
        System.out.print("Seleccione el creador: ");
        int idx = leerEntero() - 1;
        if (idx < 0 || idx >= usuarios.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Usuario creador = usuarios.get(idx);

        System.out.print("Precio mínimo: ");
        double precioMin = leerDouble();

        System.out.print("Fecha de vencimiento (dd/MM/yyyy HH:mm): ");
        String fechaStr = scanner.nextLine().trim();
        LocalDateTime fechaVenc;
        try {
            fechaVenc = LocalDateTime.parse(fechaStr,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (Exception e) {
            System.out.println("Formato de fecha inválido.");
            return;
        }

        ArrayList<Objeto> objetos = new ArrayList<>();
        if (creador instanceof Coleccionista) {
            ArrayList<Objeto> coleccion = ((Coleccionista) creador).getColeccion();
            if (coleccion.isEmpty()) {
                System.out.println("El coleccionista no tiene objetos en su colección.");
                return;
            }
            System.out.println("Objetos en colección:");
            for (int i = 0; i < coleccion.size(); i++) {
                System.out.println((i + 1) + ". " + coleccion.get(i).getNombre());
            }
            System.out.print("Seleccione objeto (número): ");
            int oi = leerEntero() - 1;
            if (oi >= 0 && oi < coleccion.size()) {
                objetos.add(coleccion.get(oi));
            }
        } else {
            // Vendedor puede agregar objetos nuevos
            Objeto obj = solicitarDatosObjeto();
            objetos.add(obj);
        }

        Subasta subasta = new Subasta(fechaVenc, creador, precioMin, objetos, "activa");
        String resultado = controlador.crearSubasta(subasta, creador);
        System.out.println(resultado);
    }

    /**
     * Lista todas las subastas del sistema.
     */
    private static void listarSubastas() {
        System.out.println("\n-- Subastas --");
        ArrayList<Subasta> lista = controlador.listarSubastas();
        if (lista.isEmpty()) {
            System.out.println("No hay subastas registradas.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    // =================== OFERTAS ===================

    /**
     * Flujo para crear una nueva oferta en una subasta.
     * Valida que el oferente no sea Moderador, Vendedor ni el creador de la subasta
     * antes de solicitar el precio.
     */
    private static void crearOferta() {
        System.out.println("\n-- Crear Oferta --");
        ArrayList<Subasta> subastas = controlador.listarSubastas();
        if (subastas.isEmpty()) {
            System.out.println("No hay subastas disponibles.");
            return;
        }

        System.out.println("Subastas disponibles:");
        for (int i = 0; i < subastas.size(); i++) {
            System.out.println((i + 1) + ". " + subastas.get(i));
        }
        System.out.print("Seleccione subasta: ");
        int si = leerEntero() - 1;
        if (si < 0 || si >= subastas.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Subasta subasta = subastas.get(si);

        ArrayList<Usuario> usuarios = controlador.listarUsuarios();
        System.out.println("Usuarios disponibles:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println((i + 1) + ". " + usuarios.get(i).getNombreCompleto());
        }
        System.out.print("Seleccione oferente: ");
        int ui = leerEntero() - 1;
        if (ui < 0 || ui >= usuarios.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Usuario oferente = usuarios.get(ui);

        // Validaciones previas al ingreso del precio
        if (oferente instanceof Moderador) {
            System.out.println("Error: El moderador no puede realizar ofertas.");
            return;
        }
        if (oferente instanceof Vendedor) {
            System.out.println("Error: El vendedor no puede realizar ofertas.");
            return;
        }
        if (oferente.equals(subasta.getCreador())) {
            System.out.println("Error: El creador de la subasta no puede realizar ofertas en ella.");
            return;
        }

        System.out.print("Precio a ofertar: ");
        double precio = leerDouble();

        String resultado = controlador.realizarOferta(subasta, oferente, precio);
        System.out.println(resultado);
    }

    /**
     * Lista todas las ofertas de una subasta seleccionada.
     */
    private static void listarOfertas() {
        System.out.println("\n-- Listado de Ofertas --");
        ArrayList<Subasta> subastas = controlador.listarSubastas();
        if (subastas.isEmpty()) {
            System.out.println("No hay subastas.");
            return;
        }
        for (int i = 0; i < subastas.size(); i++) {
            System.out.println((i + 1) + ". " + subastas.get(i).getCreador().getNombreCompleto());
        }
        System.out.print("Seleccione subasta: ");
        int si = leerEntero() - 1;
        if (si < 0 || si >= subastas.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        ArrayList<Oferta> ofertas = controlador.listarOfertas(subastas.get(si));
        if (ofertas.isEmpty()) {
            System.out.println("No hay ofertas en esta subasta.");
        } else {
            ofertas.forEach(System.out::println);
        }
    }

    /**
     * Flujo de inicio de sesión desde consola.
     * Solicita correo y contraseña, e informa si el acceso fue exitoso.
     */
    private static void iniciarSesion() {
        System.out.println("\n-- Inicio de Sesión --");
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine().trim();

        Usuario usuario = controlador.iniciarSesion(correo, contrasena);
        if (usuario != null) {
            System.out.println("Sesión iniciada correctamente. Bienvenido/a, " + usuario.getNombreCompleto() + ".");
            System.out.println("Tipo de usuario: " + usuario.getClass().getSimpleName());
        } else {
            System.out.println("Error: Correo o contraseña incorrectos.");
        }
    }

    /**
     * Verifica y muestra en pantalla si existe un moderador registrado en el sistema.
     */
    private static void validarModerador() {
        System.out.println("\n-- Validación de Moderador --");
        if (controlador.existeModerador()) {
            System.out.println("Existe un moderador registrado: " +
                    controlador.getModerador().getNombreCompleto());
        } else {
            System.out.println("No hay ningún moderador registrado en el sistema.");
        }
    }

    // =================== HELPERS ===================

    /**
     * Solicita los datos para registrar un moderador.
     *
     * @return Moderador con los datos ingresados
     */
    private static Moderador solicitarDatosModerador() {
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Identificación: ");
        String id = scanner.nextLine().trim();
        System.out.print("Fecha de nacimiento (dd/MM/yyyy): ");
        LocalDate fecha = leerFecha();
        System.out.print("Correo electrónico: ");
        String correo = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine().trim();
        return new Moderador(nombre, id, fecha, pass, correo);
    }

    /**
     * Solicita los datos para registrar un vendedor.
     *
     * @return Vendedor con los datos ingresados
     */
    private static Vendedor solicitarDatosVendedor() {
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Identificación: ");
        String id = scanner.nextLine().trim();
        System.out.print("Fecha de nacimiento (dd/MM/yyyy): ");
        LocalDate fecha = leerFecha();
        System.out.print("Correo: ");
        String correo = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Dirección: ");
        String dir = scanner.nextLine().trim();
        return new Vendedor(nombre, id, fecha, pass, correo, 0.0, dir);
    }

    /**
     * Solicita los datos para registrar un coleccionista.
     *
     * @return Coleccionista con los datos ingresados
     */
    private static Coleccionista solicitarDatosColeccionista() {
        System.out.print("Nombre completo: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Identificación: ");
        String id = scanner.nextLine().trim();
        System.out.print("Fecha de nacimiento (dd/MM/yyyy): ");
        LocalDate fecha = leerFecha();
        System.out.print("Correo: ");
        String correo = scanner.nextLine().trim();
        System.out.print("Contraseña: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Dirección: ");
        String dir = scanner.nextLine().trim();
        return new Coleccionista(nombre, id, fecha, pass, correo, 0.0, dir, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Solicita los datos para registrar un objeto.
     *
     * @return Objeto con los datos ingresados
     */
    private static Objeto solicitarDatosObjeto() {
        System.out.print("Nombre del objeto: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Descripción: ");
        String desc = scanner.nextLine().trim();
        System.out.println("Estado (1-Nuevo, 2-Usado, 3-Antiguo sin abrir): ");
        int est = leerEntero();
        String estado = switch (est) {
            case 1 -> "nuevo";
            case 2 -> "usado";
            case 3 -> "antiguo sin abrir";
            default -> "nuevo";
        };
        System.out.print("Fecha de compra (dd/MM/yyyy): ");
        LocalDate fechaCompra = leerFecha();
        return new Objeto(nombre, desc, estado, fechaCompra);
    }

    /**
     * Lee un número entero desde la consola de forma segura.
     *
     * @return Entero ingresado
     */
    private static int leerEntero() {
        try {
            int val = Integer.parseInt(scanner.nextLine().trim());
            return val;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Lee un número decimal desde la consola de forma segura.
     *
     * @return Double ingresado
     */
    private static double leerDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Lee una fecha desde la consola en formato dd/MM/yyyy.
     *
     * @return LocalDate ingresada
     */
    private static LocalDate leerFecha() {
        try {
            return LocalDate.parse(scanner.nextLine().trim(), FORMATO_FECHA);
        } catch (Exception e) {
            System.out.println("Fecha inválida, usando fecha actual.");
            return LocalDate.now();
        }
    }
}