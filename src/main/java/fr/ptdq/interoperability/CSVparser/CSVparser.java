/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.CSVparser;

import fr.ptdq.wikidataPrivate.PropertyIDs;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.wikidata.wdtk.datamodel.helpers.Datamodel;
import org.wikidata.wdtk.datamodel.helpers.ItemDocumentBuilder;
import org.wikidata.wdtk.datamodel.helpers.StatementBuilder;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.ItemIdValue;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.QuantityValue;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataEditor;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;


/**
 *
 * @author Clément
 */
public class CSVparser {
    
    public static void main(String[] args) 
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/csv/Classeur1.csv"));
            String line;
            
            String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";

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
            ItemDocument LaboHC = (ItemDocument) wbdf.getEntityDocument(PropertyIDs.LaboHC);
            ItemDocument Stage = (ItemDocument) wbdf.getEntityDocument(PropertyIDs.Stage);

            WikibaseDataEditor wbde = new WikibaseDataEditor(con, siteIri);

            PropertyDocument InstanceDe = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.InstanceDe);
            PropertyDocument ProposePar = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Proposepar);
            PropertyDocument Duree = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Duree);            
            PropertyDocument Gratification = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Gratification);            
            PropertyDocument Competence = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Competence);            
            PropertyDocument Description = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Description);            
            PropertyDocument Intitule = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Intitule);            
            PropertyDocument Adresse = (PropertyDocument) wbdf.getEntityDocument(PropertyIDs.Adresse);            
            
            
            
            
            
            ItemIdValue itemStageID = ItemIdValue.NULL; //personnelHC.getItemId();

            int i = 0;
            while ((line = br.readLine()) != null)
            {
                if (i == 1)
                {
                    String[] stage = line.split(";");
                   
                    String lieu = stage[0];
                    String titre = stage[1];
                    String desc = stage[2];
                    String[] compt = stage[3].split("/");
                    String duree = stage[4];
                    String gratification = stage[5];
                   
                    System.out.println("Lieu du stage : " + lieu);
                    System.out.println("titre: " + titre);
                    System.out.println("Description : " + desc + "\rListe des compétences requises : ");
                    for (int j = 0; j < compt.length; j++)
                    {
                        System.out.println("\t- " +compt[j]);                             
                    }
                    System.out.println("Durée du stage " + duree + " mois");
                    System.out.println("Gratification : " + gratification + " euros");
                    for(int j = 0; j < 120; j++)
                    {
                        System.out.print("-");
                    }
                    System.out.println("");
                    
                    
                   // System.out.println("ItemIdValue = " + itemPersonnelID);
                    String[] sub;



                    org.wikidata.wdtk.datamodel.interfaces.Statement statement1 = StatementBuilder
                               .forSubjectAndProperty(itemStageID, InstanceDe.getPropertyId())
                               .withValue(Stage.getItemId()).build();
                    org.wikidata.wdtk.datamodel.interfaces.Statement statement2 = StatementBuilder
                               .forSubjectAndProperty(itemStageID, ProposePar.getPropertyId())
                               .withValue(LaboHC.getItemId()).build();
                    org.wikidata.wdtk.datamodel.interfaces.Statement statement3 = StatementBuilder
                               .forSubjectAndProperty(itemStageID, Adresse.getPropertyId())
                               .withValue(Datamodel.makeStringValue("18 Rue Professeur Benoît Lauras, 42000 Saint-Étienne")).build();
                    org.wikidata.wdtk.datamodel.interfaces.Statement statement4 = StatementBuilder
                               .forSubjectAndProperty(itemStageID, Duree.getPropertyId())
                               .withValue(Datamodel.makeStringValue(duree)).build();

                    org.wikidata.wdtk.datamodel.interfaces.Statement statement5 = StatementBuilder
                               .forSubjectAndProperty(itemStageID, Gratification.getPropertyId())
                               .withValue(Datamodel.makeStringValue(gratification)).build();
                    org.wikidata.wdtk.datamodel.interfaces.Statement statement6 = StatementBuilder
                               .forSubjectAndProperty(itemStageID, Description.getPropertyId())
                               .withValue(Datamodel.makeStringValue(desc)).build();
                    
                    org.wikidata.wdtk.datamodel.interfaces.Statement statement7 = StatementBuilder
                               .forSubjectAndProperty(itemStageID, Intitule.getPropertyId())
                               .withValue(Datamodel.makeStringValue(titre)).build();
                    

                    
                    
                    org.wikidata.wdtk.datamodel.helpers.StatementBuilder builder = StatementBuilder
                               .forSubjectAndProperty(itemStageID, Competence.getPropertyId())
                               .withValue(Datamodel.makeStringValue(titre))             
                               .withValue(Datamodel.makeStringValue(titre));

                    
                    for (int j = 0; j < compt.length; j++)
                    {
                        builder.withValue(Datamodel.makeStringValue(compt[j]));
                    }
                     
                    org.wikidata.wdtk.datamodel.interfaces.Statement statement8 = builder.build();
                    


                    
                    
                    
                    



                    ItemDocument itemDocument = ItemDocumentBuilder.forItemId(itemStageID)
                                .withLabel(titre, "fr")
                                .withStatement(statement1)
                                .withStatement(statement2)
                                .withStatement(statement3)
                                .withStatement(statement4)
                                .withStatement(statement5)
                                .withStatement(statement6)
                                .withStatement(statement7)
                                .withStatement(statement8).build();

                    try
                    {
                        ItemDocument newItemDocument = wbde.createItemDocument(itemDocument,"Stage du Laboratoire Hubert Curien.");
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }    
                }
                itemStageID = ItemIdValue.NULL;
                i++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Fichier non lu");
        } catch (MediaWikiApiErrorException ex) {
            Logger.getLogger(CSVparser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CSVparser.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
