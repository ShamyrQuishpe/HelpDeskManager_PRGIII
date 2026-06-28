package dao;

import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UsuarioDAO {

    private Connection conexion;

    public UsuarioDAO(){

        conexion= ConexionBD.conectar();

    }

    //CREATE
    public void insertar(
            Usuario usuario
    ){

        try{

            String sql= "INSERT INTO usuarios"+ "(nombre,correo,password,rol)"+ "VALUES(?,?,?,?)";

            PreparedStatement ps= conexion.prepareStatement(sql);

            ps.setString(1, usuario.getNombre());

            ps.setString(2, usuario.getCorreo());

            ps.setString(3, usuario.getPassword());

            ps.setString(4, usuario.getRol());

            ps.executeUpdate();

            System.out.println("Usuario registrado");

        }

        catch(Exception e){

            e.printStackTrace();

        }

    }

    //READ
    public ArrayList<Usuario> obtenerTodos(){

        ArrayList<Usuario> lista= new ArrayList<>(); //Estructura de datos Lista

        try{

            String sql= "SELECT * FROM usuarios";

            PreparedStatement ps= conexion.prepareStatement(sql);

            ResultSet rs= ps.executeQuery();

            while(rs.next()){

                Usuario u = new Usuario(

                                rs.getInt("id_usuario"),

                                rs.getString("nombre"),

                                rs.getString("correo"),

                                rs.getString("password"),

                                rs.getString("rol")
                        );

                lista.add(u);

            }

        }

        catch(Exception e){

            e.printStackTrace();

        }

        return lista;

    }

    // OBTENER TECNICOS
    public ArrayList<Usuario> obtenerTecnicos() {
        ArrayList<Usuario> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM usuarios WHERE rol = 'TECNICO'";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
                lista.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // LOGIN
    public Usuario login(
            String correo,
            String password
    ) {

        Usuario usuario = null;

        try {

            String sql = "SELECT * FROM usuarios " + "WHERE correo=? AND password=?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, correo);

            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {

                usuario = new Usuario(

                        rs.getInt("id_usuario"),

                        rs.getString("nombre"),

                        rs.getString("correo"),

                        rs.getString("password"),

                        rs.getString("rol")
                );
            }

        }

        catch(Exception e) {

            e.printStackTrace();
        }

        return usuario;
    }

    // UPDATE
    public void actualizar(Usuario usuario) {

        try {

            String sql = "UPDATE usuarios " + "SET nombre=?, correo=?, password=? " + "WHERE id_usuario=?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, usuario.getNombre());

            ps.setString(2, usuario.getCorreo());

            ps.setString(3, usuario.getPassword());

            ps.setInt(4, usuario.getIdUsuario());

            ps.executeUpdate();

            System.out.println("Usuario actualizado");

        }

        catch(Exception e) {

            e.printStackTrace();
        }
    }
    //Actualziar completo
    public void actualizarAdmin(Usuario usuario){

        try{

            String sql = "UPDATE usuarios SET " + "nombre = ?, " + "correo = ?, " + "password = ?, " + "rol = ? " + "WHERE id_usuario = ?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, usuario.getNombre());

            ps.setString(2, usuario.getCorreo());

            ps.setString(3, usuario.getPassword());

            ps.setString(4, usuario.getRol());

            ps.setInt(5, usuario.getIdUsuario());

            ps.executeUpdate();

            System.out.println(
                    "Usuario actualizado por admin"
            );
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }
    // BUSCAR POR ID
    public Usuario buscarPorId(int idUsuario){

        Usuario usuario = null;

        try{

            String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idUsuario);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                usuario = new Usuario(

                        rs.getInt("id_usuario"),

                        rs.getString("nombre"),

                        rs.getString("correo"),

                        rs.getString("password"),

                        rs.getString("rol")
                );
            }

        }
        catch(Exception e){

            e.printStackTrace();
        }

        return usuario;
    }

    //Eliminar por ID
    public void eliminar(int idUsuario){

        try{

            String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idUsuario);

            ps.executeUpdate();

            System.out.println("Usuario eliminado");
        }
        catch(Exception e){

            e.printStackTrace();
        }
    }

}