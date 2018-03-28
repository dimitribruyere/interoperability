/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperabilty.wikidata;

/**
 *
 * @author Clément
 */
import com.fasterxml.jackson.databind.JsonNode;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.datamodel.interfaces.*;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    final static String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";

    public static void main(String[] args) throws MediaWikiApiErrorException {

        WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");

        ApiConnection con = new ApiConnection("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/api.php");

        try {
            //Put in the first place the user with which you created the bot account
            //Put as password what you get when you create the bot account
            con.login("Root@SamBot", "tcr0kgob5hgjejp2rrga8kocjq3jfc0l");
        } catch (LoginFailedException e) {
            e.printStackTrace();
        }

	Map<String, String> map = new HashMap<String,String>();
	map.put("action","wbsearchentities");
	map.put("search","Pierre maret");
	map.put("language","fr");

	try {
	    JsonNode json = con.sendJsonRequest("GET",map);
	    System.out.println(json.toString());
	} catch (IOException ex) {
	    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
	}

        //Example for getting information about an entity, here the example of The Laboratoire Huber Curien, Q900
        //For more examples give a look at: https://github.com/Wikidata/Wikidata-Toolkit-Examples/blob/master/src/examples/FetchOnlineDataExample.java
        WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
        ItemDocument laboratoireHC = (ItemDocument) wbdf.getEntityDocument("Q900");


        //Example to search for an ID given the label
        List<WbSearchEntitiesResult> entities = wbdf.searchEntities("Hubert curien","fr");
        for (WbSearchEntitiesResult entity : entities){
            System.out.println(entity.getEntityId());
        }

        //Example for writing information about an entity, here the example of creating Florance Garrelie working at Laboratoire Huber Curien
       


    }
}