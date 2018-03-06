/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperabilty.wikidata;

import java.util.concurrent.TimeUnit;
import net.sourceforge.jwbf.core.actions.HttpActionClient;
import net.sourceforge.jwbf.core.contentRep.Article;
import net.sourceforge.jwbf.core.contentRep.SimpleArticle;
import net.sourceforge.jwbf.mediawiki.actions.queries.BaseQuery;
import net.sourceforge.jwbf.mediawiki.bots.MediaWikiBot;

/**
 *
 * @author chris
 */
public class TestJWBF
{

    /**
     * Sample bot that retrieves and edits an article.
     */
    public static void main(String[] args)
    {
        //org.apache.http.impl.client.HttpClientBuilder.create();

        HttpActionClient client = HttpActionClient.builder() //
                .withUrl("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/") //
                .withUserAgent("PTDQ_Bot", "1.0", "PTDQ@ptdq.com") //
                .withRequestsPerUnit(10, TimeUnit.MINUTES) //
                .build();
        
        MediaWikiBot wikiBot = new MediaWikiBot("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/");
        wikiBot.login("ChristopherJEAMME", "D2rZ9Fk1Sg3K");
        //SimpleArticle article = wikiBot.readData("Main_Page");
        //System.out.println(wikiBot.getWikiType());

        System.out.println(wikiBot.readDataOpt("Item:Q904")); //Pierre Maret
        
        //applyChangesTo(article);
        //article.save();
    }

    static void applyChangesTo(Article article)
    {
        // edits the article...
    }
}
