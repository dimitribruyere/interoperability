/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.HTMLparser;

import static fr.ptdq.interoperability.DBparser.DBManager.executeQuery;
import fr.ptdq.wikidataPrivate.PropertyIDs;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.nashorn.internal.objects.NativeArray;
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
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

/**
 *
 * @author Dimitri
 */
public class TestParser
{

    public static void main(String[] args)
    {
        HashMap<String, String> listPublis = HTMLparser.getPubli();

        List<Publications> infos = HTMLparser.getInfosFromPublis(listPublis);
        
        //System.out.println(infos);
        
        registerPublis();
    }

    public static void registerPublis()
    {
        try
        {
            String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";
            List<Publications> listPublications = HTMLparser.getInfosFromPublis(HTMLparser.getPubli());
            
            /**
             * * WIKI DATA **
             */
            WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");
            
            ApiConnection con = new ApiConnection("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/api.php");
            
            try
            {
                //Put in the first place the user with which you created the bot account
                //Put as password what you get when you create the bot account
                con.login("Root@SamBot", "tcr0kgob5hgjejp2rrga8kocjq3jfc0l");
            } catch (LoginFailedException e)
            {
                e.printStackTrace();
            }
            
            //Example for getting information about an entity, here the example of The Laboratoire Huber Curien, Q900
            //For more examples give a look at: https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/FetchOnlineDataExample.java
            WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
            ItemDocument publiHC = (ItemDocument) wbdf.getEntityDocument(PropertyIDs.Publication);
            
            WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);
            PropertyDocument propertyAuthor = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Auteur);
            PropertyDocument propertyContributor = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Co_auteur);
            PropertyDocument propertyYearPubli = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.AnneePubli);
            PropertyDocument propertyInstanceDe = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.InstanceDe);
            
            ItemIdValue itemPubliID = ItemIdValue.NULL;
            
            
            Publications p = listPublications.get(0);
            System.out.println(p);
            
            /*Instance de */
            org.wikidata.wdtk.datamodel.interfaces.Statement statement1 = StatementBuilder
                .forSubjectAndProperty(itemPubliID, propertyInstanceDe.getPropertyId())
                .withValue(publiHC.getItemId()).build();
            
//            /*Auteurs*/
//            for (int i=0; i<p.getAuthors().size(); i++)
//            {
                org.wikidata.wdtk.datamodel.interfaces.Statement statement2;
                statement2 = StatementBuilder
                    .forSubjectAndProperty(itemPubliID, propertyAuthor.getPropertyId())
                    .withValue(Datamodel.makeStringValue(p.getAuthors().get(0))).build();
//            }

            
            
            /*Date*/
            org.wikidata.wdtk.datamodel.interfaces.Statement statement3 = StatementBuilder
                .forSubjectAndProperty(itemPubliID, propertyYearPubli.getPropertyId())
                .withValue(Datamodel.makeStringValue(p.getDate())).build();
            
            
            
            org.wikidata.wdtk.datamodel.interfaces.Statement statement4;
            statement2 = StatementBuilder
                .forSubjectAndProperty(itemPubliID, propertyContributor.getPropertyId())
                .withValue(Datamodel.makeStringValue(p.getContributors().get(0))).build();
            
            
            ItemDocument itemDocument = ItemDocumentBuilder.forItemId(itemPubliID)
                .withLabel(p.getTitle(), "fr")
                .withStatement(statement1).build();
            
            try
            {
                ItemDocument newItemDocument = wbde.createItemDocument(itemDocument, "Publication of LaHC");
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        } catch (MediaWikiApiErrorException ex)
        {
            Logger.getLogger(TestParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
