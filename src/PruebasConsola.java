import dao.UsuarioDAO;
import model.Usuario;

import java.util.ArrayList;
import java.util.Scanner;

public class PruebasConsola {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        int opcion;

        do {

            System.out.println("\n========== HELP DESK MANAGER ==========");
            System.out.println("1. Registrar usuario");
            System.out.println("2. Listar usuarios");
            System.out.println("3. Salir");

            System.out.print("Seleccione una opción: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch(opcion) {

                case 1:

                    System.out.println("\n=== REGISTRO USUARIO ===");

                    System.out.print("Nombre: ");
                    String nombre = sc.nextLine();

                    System.out.print("Correo: ");
                    String correo = sc.nextLine();

                    System.out.print("Password: ");
                    String password = sc.nextLine();

                    System.out.print("Rol (ADMIN/TECNICO/CLIENTE): ");
                    String rol = sc.nextLine().toUpperCase();

                    Usuario usuario = new Usuario(
                            nombre,
                            correo,
                            password,
                            rol
                    );

                    usuarioDAO.insertar(usuario);

                    break;

                case 2:

                    System.out.println("\n=== LISTA USUARIOS ===");

                    ArrayList<Usuario> usuarios =
                            usuarioDAO.obtenerTodos();

                    if(usuarios.isEmpty()) {

                        System.out.println(
                                "No existen usuarios"
                        );

                    } else {

                        for(Usuario u : usuarios) {

                            System.out.println(u);
                        }
                    }

                    break;

                case 3:

                    System.out.println(
                            "Saliendo del sistema..."
                    );

                    break;

                default:

                    System.out.println(
                            "Opción inválida"
                    );
            }

        } while(opcion != 3);

        sc.close();
    }
}