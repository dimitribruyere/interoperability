/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperabilty.wikidata;

import static fr.ptdq.interoperabilty.wikidata.Main.siteIri;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import org.json.JSONException;
import org.wikidata.wdtk.util.WebResourceFetcherImpl;
import org.wikidata.wdtk.wikibaseapi.ApiConnection;
import org.wikidata.wdtk.wikibaseapi.LoginFailedException;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;
import org.wikidata.wdtk.wikibaseapi.WikibaseDataFetcher;
import org.wikidata.wdtk.wikibaseapi.apierrors.MediaWikiApiErrorException;

/**
 *
 */
public class Query
{

    public static void main(String[] args)
    {
        System.out.println("Test 1");
        JSONObject json = query("instance de Florence Garrelie");
        System.out.println(valueOfResponse(json));
        System.out.println(typeOfResponse(json));
        System.out.println(itemOfResponse(json));
        System.out.println(apiSearch(json));

        System.out.println("\nTest 2");
        JSONObject json2 = query("instance de jacky lafrite");
        System.out.println(valueOfResponse(json2));
        System.out.println(typeOfResponse(json2));
        System.out.println(itemOfResponse(json2));
        System.out.println(apiSearch(json2));
        System.out.println(json2);

        System.out.println(getValueOfItem("instance de Pierre Maret"));
    }

    public static String valueOfResponse(JSONObject json)
    {
        try
        {
            JSONObject results = new JSONObject(json
                    .optJSONArray("questions")
                    .optJSONObject(0)
                    .optJSONObject("question")
                    .getString("answers"))
                    .optJSONObject("results")
                    .optJSONArray("bindings")
                    .optJSONObject(0);

            if (!results.isNull("o1"))
            {
                return results.optJSONObject("o1").optString("value");
            }
            else
            {
                if (!results.isNull("s0"))
                {
                    return results.optJSONObject("s0").optString("value");
                }
            }
            return "Parsing error";
        }
        catch (JSONException e)
        {
            return "Parsing error";
        }
    }

    public static String typeOfResponse(JSONObject json)
    {
        try
        {
            JSONObject results = new JSONObject(json
                    .optJSONArray("questions")
                    .optJSONObject(0)
                    .optJSONObject("question")
                    .getString("answers"))
                    .optJSONObject("results")
                    .optJSONArray("bindings")
                    .optJSONObject(0);

            if (!results.isNull("o1"))
            {
                return results.optJSONObject("o1").optString("type");
            }
            else
            {
                if (!results.isNull("s0"))
                {
                    return results.optJSONObject("s0").optString("type");
                }
            }

            return "Parsing error";
        }
        catch (JSONException e)
        {
            return "Parsing error";
        }
    }

    public static String itemOfResponse(JSONObject json)
    {
        try
        {
            String value = json
                    .optJSONArray("questions")
                    .optJSONObject(0)
                    .optJSONObject("question")
                    .optJSONArray("language")
                    .optJSONObject(0)
                    .optString("SPARQL")
                    .split("<")[1]
                    .split(">")[0]
                    .split("entity/")[1];

            return value;
        }
        catch (Exception e)
        {
            return "Parsing error";
        }
    }

    public static String getValueOfItem(String item)
    {
        try
        {
            String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";
            WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");
            ApiConnection con = new ApiConnection("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/api.php");

            con.login("Root@SamBot", "tcr0kgob5hgjejp2rrga8kocjq3jfc0l");

            WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
            ArrayList<WbSearchEntitiesResult> entities = (ArrayList<WbSearchEntitiesResult>) wbdf.searchEntities(item, "fr");

            String toSend = "";//entities.size()+" trouvés : ";
            if (entities.size() > 0)
            {
                toSend = toSend.concat(entities.get(0).getLabel());
            }
            return toSend;
        }
        catch (MediaWikiApiErrorException | LoginFailedException ex)
        {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        }
    }
    
    public static String getAllOfItem(String item)
    {
        try
        {
            String siteIri = "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/";
            WebResourceFetcherImpl.setUserAgent("Wikidata Toolkit EditOnlineDataExample");
            ApiConnection con = new ApiConnection("https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/api.php");

            con.login("Root@SamBot", "tcr0kgob5hgjejp2rrga8kocjq3jfc0l");

            WikibaseDataFetcher wbdf = new WikibaseDataFetcher(con, siteIri);
            ArrayList<WbSearchEntitiesResult> entities = (ArrayList<WbSearchEntitiesResult>) wbdf.searchEntities(item, "fr");

            String toSend = "";//entities.size()+" trouvés : ";
            if (entities.size() > 0)
            {
                int i=0;
                for(WbSearchEntitiesResult entity : entities)
                {
                    i++;
                    toSend = toSend.concat("<br/><br/><h4>Element "+i+"</h4>"+
                            "<br/>EntityId = "+entity.getEntityId()+
                            "<br/>Label = "+entity.getLabel()+
                            "<br/>Url = "+entity.getUrl()+
                            "<br/>Title = "+entity.getTitle()+
                            "<br/>Description = "+entity.getDescription()+
                            "<br/>ConceptUri = "+entity.getConceptUri());
                }
            }
            else
                System.out.println("0 élément trouvés avec l'API..");
            return toSend;
        }
        catch (MediaWikiApiErrorException | LoginFailedException ex)
        {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
        }
    }

    public static JSONObject query(String query)
    {
        try
        {
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL("https://wdaqua-core1.univ-st-etienne.fr/gerbil").openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();
            String toSend = "query=" + query + "&lang=fr&kb=student";
            byte[] outputBytes = toSend.getBytes("UTF-8");
            try (OutputStream os = httpcon.getOutputStream())
            {
                os.write(outputBytes);
            }

            StringBuffer response;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream())))
            {
                String inputLine;
                response = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
            }

            return new JSONObject(response.toString());
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Query.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<String> apiSearch(JSONObject json)
    {
        Pattern pattern = Pattern.compile("Q\\d{1,5}");
        Matcher matcher = pattern.matcher(json.toString());
        List<String> matches = new ArrayList<>();
        while (matcher.find())
        {
            matches.add("Item = " + matcher.group(0) + " Label = " + getValueOfItem(matcher.group(0)) + " <a href=\"https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/index.php/Item:" + matcher.group(0) + "\" class=\"badge badge-pill badge-primary\">Lien vers la page Wikidata</a><br />\n");
        }
        return matches;
    }
}
