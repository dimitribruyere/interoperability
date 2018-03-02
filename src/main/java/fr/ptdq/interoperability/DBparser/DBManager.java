package fr.ptdq.interoperability.DBparser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager
{
    public static Connection createMysqlDatabase(Connection factice, String user,
                                                 String pass, String nomBase)
            throws SQLException
    {
        Connection connection = null;
        Statement statement = null;
        try
        {
            statement = factice.createStatement();
            statement.execute("CREATE DATABASE " + nomBase);
            String url = factice.getMetaData().getURL();
            url = url.substring(0, url.lastIndexOf("/"));
            url += "/" + nomBase;
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e)
        {
            SQLException sqle = new SQLException("Création de la base impossible");
            sqle.setNextException(e);
            throw sqle;
        } finally
        {
            try
            {
                statement.close();
            } catch (Exception e)
            {
            }
        }
        return connection;
    }

    public static void main(String[] args) throws Exception
    {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://host/baseLien";
        String user = "user";
        String pass = "password";
// on commence par se connecter à la base factice
        Connection factice = DriverManager.getConnection(url,user,pass);
// on crée la base et on récupère une Connection
        Connection connection = createMysqlDatabase(factice,user,pass,"NouvelleBase");
// on peut finalement fermer notre Connection factice qui ne nous sert plus à rien
        factice.close();
    }


}
