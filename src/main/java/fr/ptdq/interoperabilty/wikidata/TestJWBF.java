/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperabilty.wikidata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import net.sourceforge.jwbf.core.actions.HttpActionClient;
import net.sourceforge.jwbf.core.bots.WikiBot;
import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import net.sourceforge.jwbf.mediawiki.actions.queries.BaseQuery;
import net.sourceforge.jwbf.mediawiki.actions.queries.Search;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;
import org.json.JSONObject;

/**
 *
 * @author chris
 */
public class TestJWBF
{

    /**
     * Sample bot that retrieves and edits an article.
     */
    public static void main(String[] args) throws IOException
    {/*
        //Connexion
        System.out.println("Connexion");
        MediaWikiBot wikiBot = new MediaWikiBot("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/");
        wikiBot.login("Root@SamBot", "tcr0kgob5hgjejp2rrga8kocjq3jfc0l");

        //Récupération d'un object
        System.out.println("Récupération d'un object");
        Article article = wikiBot.getArticle("Item:Q904");

        //Transformation du JSON en objet
        System.out.println("Transformation du JSON en objet");
        JSONObject obj = new JSONObject(article.getText());

        //Récupération des déclarations
        System.out.println("Récupération des déclarations");
        JSONObject declarations = obj.getJSONObject("claims");

        //Lecture des déclarations
        System.out.println("Lecture des déclarations");
        System.out.println(declarations.toString(2));

        //Suppressions de 2 déclarations
        System.out.println("Suppressions de 2 déclarations");
        //obj.getJSONObject("claims").remove("P252");
        obj.getJSONObject("claims").remove("P186");
        obj.getJSONObject("claims").remove("P243");
        //Affichage de l'objet après suppressions
        System.out.println("Affichage de l'objet après suppressions");
        System.out.println(obj.toString(2));

        //Appliquation du JSON à l'objet Article
        System.out.println("Appliquation du JSON à l'objet Article");
        article.setText(obj.toString());

        //Envoi des modifications au wiki
        //article.save();
        //Si il s'agit d'un nouvel article
        System.out.println("Si il s'agit d'un nouvel article on créé un object SimpleArticle");
        SimpleArticle simpleArticle = new SimpleArticle(obj.toString(), "Item:Q999");
        //Envoi du nouvel article au wiki
        //wikiBot.writeContent(simpleArticle);
        //Lancement d'une requete
        System.out.println("Lancement d'une requete");
        HttpURLConnection httpcon = (HttpURLConnection) ((new URL("https://wdaqua-core1.univ-st-etienne.fr/gerbil").openConnection()));
        httpcon.setDoOutput(true);
        httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpcon.setRequestProperty("Accept", "application/json");
        httpcon.setRequestMethod("POST");
        httpcon.connect();
        byte[] outputBytes = "query=pierre Maret sex&lang=en&kb=wikidata".getBytes("UTF-8");
        OutputStream os = httpcon.getOutputStream();
        os.write(outputBytes);
        os.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        JSONObject queryResponse = new JSONObject(response.toString());


        System.out.println("REPONSE REQUETE=" + response.toString());
        System.out.println("REPONSE REQUETE=" + queryResponse.toString(2));
         */
        //https://wdaqua-qanary.univ-st-etienne.fr/gerbil-execute/wdaqua-core1,QueryExecuter/ => ERROR 500
        //https://wdaqua-qanary.univ-st-etienne.fr/gerbil-execute/wdaqua-core1,%20QueryExecuter/ => ERROR 500
        //https://wdaqua-qanary.univ-st-etienne.fr/gerbil/ => Page HTML
        //https://wdaqua-qanary.univ-st-etienne.fr:8080/gerbil-execute/wdaqua-core1,%20QueryExecuter/ => Timeout
        //https://wdaqua-biennale-design.univ-st-etienne.fr/gerbil/ => FileNotFoundException
//TESTS
//        System.out.println("SUMMARY="+article.getEditSummary());
//        System.out.println("TEXT="+article.getText());
//        System.out.println("EDITOR="+article.getEditor());
//        System.out.println("TITLE="+article.getTitle());
//        System.out.println(wikiBot.getWikiType());
//        System.out.println(wikiBot.readDataOpt("Item:Q904"));
    }
}
