package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConexionBD {
    private static Connection conexion;

    private ConexionBD(){}

    public static Connection conectar(){

        try{

            if(conexion==null || conexion.isClosed()){

                Properties props = new Properties();

                InputStream input =
                        ConexionBD.class
                                .getClassLoader()
                                .getResourceAsStream(
                                        "config/database.properties"
                                );

                props.load(input);

                String url=
                        props.getProperty(
                                "db.url"
                        );

                String user=
                        props.getProperty(
                                "db.user"
                        );

                String password=
                        props.getProperty(
                                "db.password"
                        );

                conexion=
                        DriverManager.getConnection(
                                url,
                                user,
                                password
                        );

                System.out.println(
                        "Conexión creada"
                );
            }

        }
        catch(Exception e){

            e.printStackTrace();

        }

        return conexion;

    }
}
