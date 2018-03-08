package fr.ptdq.interoperability.DBparser;

import java.sql.*;

public class DBManager
{

    /**
     * Connect to database
     *
     * @return the Connection object
     */
    public static Connection connect()
    {
        // SQLite connection string
        String url = "jdbc:sqlite:src/main/java/fr/ptdq/interoperability/DBparser/inter";
        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * Send a request to SQLite database
     *
     * @param request SQL request
     */
    public static ResultSet executeQuery(Connection conn, String request)
    {
        try
        {
            Statement statement = conn.createStatement();
            return statement.executeQuery(request);

        } catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void displayResultSet(ResultSet rs)
    {
        // loop through the result set
        try
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("querying SELECT * FROM XXX");
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next())
            {
                for (int i = 1; i <= columnsNumber; i++)
                {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception
    {
        Connection conn = connect();

        if (conn != null)
            System.out.println("Connection to DB : Success.");

        ResultSet rs = executeQuery(conn, "SELECT * FROM Personne");

        displayResultSet(rs);
    }


}
