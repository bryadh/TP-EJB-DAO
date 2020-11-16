package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {

    private String url;
    private String username;
    private String password;


    public DaoFactory(String url, String username, String password) {
        super();
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance(){
        //Chargement du driver
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        DaoFactory instance = new DaoFactory("jdbc:mysql://localhost:3306/jee?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,username,password);
    }

    //Récupération du DAO
    public MonnaieDao getMonnaieDao(){
        return new MonnaieDaoImpl(this);
    }
}
