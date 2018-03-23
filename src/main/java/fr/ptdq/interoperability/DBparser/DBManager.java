package fr.ptdq.interoperability.DBparser;

import fr.ptdq.wikidataPrivate.Main;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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

        Connection conn = connect();

        if (conn != null)
            System.out.println("Connection to DB : Success.");

        //getEquipeRecherche(conn, "Muhlenbach","Fabrice");
        //getPersonne(conn, "Alata", "O");
        //registerTeams(conn);
        registerPersons(conn);

        for(int i = 1; i < 7 ; i++)
        {
          //  registerTeamMembers(conn, i);
        }


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
                /* For all entries */
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


        /*** Item documents ***/
        ItemDocument personnelHC = (ItemDocument) wbdf.getEntityDocument(PropertyIDs.Personne);


        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);



        /*** Properties ***/
        PropertyDocument propertyInstanceDe = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.InstanceDe);
        PropertyDocument propertyMembre = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Membre);

        //  PropertyDocument propertyPrenom = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Prenom);

        ItemIdValue itemPersonnelID = ItemIdValue.NULL; //personnelHC.getItemId();

       // System.out.println("ItemIdValue = " + itemPersonnelID);
        String[] sub;



        for(int i = 0 ; i < listPers.size() ; i++)
        {
            sub = listPers.get(i).split("/"); // 0 = nom, 1 = prenom
            System.out.println("ADDING [nom = " + sub[0] + " prenom = " + sub[1] + "]");
            org.wikidata.wdtk.datamodel.interfaces.Statement statement1 = StatementBuilder
                    .forSubjectAndProperty(itemPersonnelID, propertyInstanceDe.getPropertyId())
                    .withValue(personnelHC.getItemId()).build();

            String id = getEquipeID(getEquipeRecherche(conn,sub[0],sub[1]));
            ItemDocument itemDocument;
            if(id .equals(""))
            {
                itemDocument = ItemDocumentBuilder.forItemId(itemPersonnelID)
                        // .withLabel(sub[1] + " " + sub[0], "en")
                        .withLabel(sub[1] + " " + sub[0], "fr")
                        .withStatement(statement1)
                        .build();
            }
            else
            {
                ItemDocument equipeRecherche = (ItemDocument) wbdf.getEntityDocument(id);
                org.wikidata.wdtk.datamodel.interfaces.Statement statement2 = StatementBuilder
                        .forSubjectAndProperty(itemPersonnelID, propertyMembre.getPropertyId())
                        .withValue(equipeRecherche.getItemId()).build();


                itemDocument = ItemDocumentBuilder.forItemId(itemPersonnelID)
                        // .withLabel(sub[1] + " " + sub[0], "en")
                        .withLabel(sub[1] + " " + sub[0], "fr")
                        .withStatement(statement1)
                        .withStatement(statement2).build();
            }

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

    public static void registerTeams(Connection conn) throws Exception
    {
        String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";
        ArrayList<String> listTeam = new ArrayList<>();
        ResultSet rs = executeQuery(conn,"SELECT nom FROM Equipe_Recherche");

        try
        {

            while (rs.next())
            {

                    listTeam.add(rs.getString(1));
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        for(String s : listTeam)
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


        /*** Item documents ***/
        ItemDocument equipeHc = (ItemDocument) wbdf.getEntityDocument(PropertyIDs.EquipeDeRecherche);


        WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);



        /*** Properties ***/
        PropertyDocument propertyInstanceDe = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.InstanceDe);
        ItemIdValue itemEquipeID = ItemIdValue.NULL;

        // System.out.println("ItemIdValue = " + itemPersonnelID);
        String[] sub;



        for(int i = 0 ; i < listTeam.size() ; i++)
        {

            org.wikidata.wdtk.datamodel.interfaces.Statement statement1 = StatementBuilder
                    .forSubjectAndProperty(itemEquipeID, propertyInstanceDe.getPropertyId())
                    .withValue(equipeHc.getItemId()).build();




            ItemDocument itemDocument = ItemDocumentBuilder.forItemId(itemEquipeID)
                    // .withLabel(sub[1] + " " + sub[0], "en")
                    .withLabel(listTeam.get(i), "fr")
                    .withStatement(statement1).build();

            try
            {
                ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,
                                                                       "Equipe de recherche du Laboratoire Hubert Curien.");
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        // TODO : add to wikidata when ID pb will be adressed
    }

    public static void registerTeamMembers(Connection conn, int teamID) throws Exception
    {
        String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";
        ArrayList<String> listPers = new ArrayList<>();
        ResultSet rs = executeQuery(conn,"SELECT nom, prenom FROM Personne, Membre_Equipe_Recherche WHERE Personne.id = Membre_Equipe_Recherche.id_personne AND Membre_Equipe_Recherche.id_equipe_recherche = " + teamID);

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
            System.out.println("Team "+ teamID + " : " + s);
        }
    }

    public static ArrayList<String> getPersonne(Connection conn, String nom, String prenom /* partial */)
    {
        ArrayList<String> result = new ArrayList<>();

        ResultSet rs = executeQuery(conn,"SELECT nom, prenom FROM Personne WHERE nom LIKE '" + nom + "' AND prenom LIKE '" + prenom +"%'");

        try
        {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next())
            {
                String pers = new String("");
                for (int i = 1; i <= columnsNumber; i++)
                {
                    if (i > 1) pers = " " + pers;
                    pers = rs.getString(i) + pers;
                }
                result.add(pers);
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        for(String s : result)
        {
            System.out.println(s);
        }

        return result;
    }

    public static ArrayList<String> getEquipeRecherche(Connection conn, String nom, String prenom)
    {
        ArrayList<String> result = new ArrayList<>();

        ResultSet rs = executeQuery(conn,"SELECT Equipe_Recherche.nom FROM Equipe_Recherche, Personne, Membre_Equipe_Recherche WHERE Personne.nom LIKE '" + nom + "' AND Personne.prenom LIKE '" + prenom
                +"' AND Equipe_Recherche.id = Membre_Equipe_Recherche.id_equipe_recherche AND Personne.id = Membre_Equipe_Recherche.id_personne" );

        try
        {

            while (rs.next())
            {
                String team = rs.getString("nom");

                result.add(team);
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        for(String s : result)
        {
            System.out.println(s);
        }

        return result;
    }

    public static String getEquipeID(ArrayList<String> listeEquipe)
    {
            if(listeEquipe == null )
            {
                System.out.println("[Error] team list null.");
                return "";
            }

            if(listeEquipe.isEmpty())
                return "";

            if(listeEquipe.get(0).equals("Micro & nano structuring team"))
                return PropertyIDs.EquipeMicro;
        if(listeEquipe.get(0).equals("Laser-matter interaction team"))
            return PropertyIDs.EquipeLaser;
        if(listeEquipe.get(0).equals("Image science team"))
            return PropertyIDs.EquipeImage;
        if(listeEquipe.get(0).equals("Data Intelligence team"))
            return PropertyIDs.EquipeData;
        if(listeEquipe.get(0).equals("Connected Intelligence team"))
            return PropertyIDs.EquipeConnected;
        if(listeEquipe.get(0).equals("Secure Embedded Systems & Hardware Architectures team"))
            return PropertyIDs.EquipeSecure;

        return ""; // default
    }
}