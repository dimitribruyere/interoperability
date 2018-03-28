/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability;

import fr.ptdq.interoperabilty.wikidata.Query;
import org.json.JSONObject;

/**
 *
 */
public class Program
{

    /**
     * Function to compute a question and send a response
     *
     * @param question
     * @return
     */
    public static String question(String question)
    {
        System.out.println("Question = " + question);
        JSONObject json = Query.query(question);
        String toDisplay = "<p>Valeur = " + Query.valueOfResponse(json) + ""
                + "<br />Type = " + Query.typeOfResponse(json) + ""
                + "<br />Item lié = " + Query.itemOfResponse(json)+"</p>"
                + "<br /><br /><pre><code>Requête reçu = "+json.toString(2)+"</pre></code>";
        return toDisplay;
    }
}
