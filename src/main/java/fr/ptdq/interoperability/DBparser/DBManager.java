package fr.ptdq.interoperability.DBparser;

import fr.ptdq.wikidataPrivate.PropertyIDs;
import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

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
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next())
            {
                for (int i = 1; i <= columnsNumber; i++)
                {
                    if (i > 1) System.out.print("\t");
                    String columnValue = rs.getString(i);
                    System.out.print("[" + rsmd.getColumnName(i) + "]" + columnValue);
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


        /**
         * Personne     P
         * Nom          P
         * Prénom       P
         * Fonction     P258
         * Adresse      P257
         * Equipe de recherche  P289
         * Membre de    P292
         *
         */
        Connection conn = connect();

        if (conn != null)
            System.out.println("Connection to DB : Success.");

        registerPersons(conn);


    }

    public static void registerPersons(Connection conn) throws Exception
    {
        String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";
        ArrayList<String> listPers = new ArrayList<>();
        ResultSet rs = executeQuery(conn,"SELECT nom, prenom FROM Personne");

        try
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next())
            {
                String pers = new String("");
                for (int i = 1; i <= columnsNumber; i++)
                {
                    if (i > 1) pers += "/";
                    pers += rs.getString(i);
                }
                listPers.add(pers);
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        for(String s : listPers)
        {
            System.out.println(s);
        }

        /*** WIKI DATA ***/

        WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

        ApiConnection con = new ApiConnection("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/api.php");

        try {
            //Put in the first place the user with which you created the bot account
            //Put as password what you get when you create the bot account
            con.login("Root@SamBot", "tcr0kgob5hgjejp2rrga8kocjq3jfc0l");
        } catch (LoginFailedException e) {
            e.printStackTrace();
        }

        //Example for getting information about an entity, here the example of The Laboratoire Huber Curien, Q900
        //For more examples give a look at: https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/FetchOnlineDataExample.java
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
        ItemDocument personnelHC = (ItemDocument) wbdf.getEntityDocument(PropertyIDs.Personne);

        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);
        PropertyDocument propertyNom = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Nom);
        PropertyDocument propertyPrenom = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Prenom);

        ItemIdValue itemPersonnelID = personnelHC.getItemId();
        System.out.println("ItemIdValue = " + itemPersonnelID);
        String[] sub;

        for(int i = 0 ; i < listPers.size() ; i++)
        {
            sub = listPers.get(i).split("/"); // 0 = nom, 1 = prenom
            System.out.println("ADDING [nom = " + sub[0] + " prenom = " + sub[1] + "]");
            org.wikidata.wdtk.datamodel.interfaces.Statement statement1 = StatementBuilder
                    .forSubjectAndProperty(itemPersonnelID, propertyNom.getPropertyId())
                    .withValue(Datamodel.makeStringValue(sub[0])).build();
            org.wikidata.wdtk.datamodel.interfaces.Statement statement2 = StatementBuilder
                    .forSubjectAndProperty(itemPersonnelID, propertyPrenom.getPropertyId())
                    .withValue(Datamodel.makeStringValue(sub[1])).build();

            ItemDocument itemDocument = ItemDocumentBuilder.forItemId(itemPersonnelID)
                    .withLabel(sub[1] + " " + sub[0], "en")
                    .withLabel(sub[1] + " " + sub[0], "fr")
                    .withStatement(statement1)
                    .withStatement(statement2).build();

            try
            {
                ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                                                                       "Personnel du Laboratoire Hubert Curien.");
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

}
