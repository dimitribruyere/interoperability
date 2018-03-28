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
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONException;

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
        
        System.out.println("\nTest 2");
        JSONObject json2 = query("Q886");
        System.out.println(valueOfResponse(json2));
        System.out.println(typeOfResponse(json2));
        System.out.println(itemOfResponse(json2));
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
            
            if(!results.isNull("o1"))
                return results.optJSONObject("o1").optString("value");
            else
            {
                if(!results.isNull("s0"))
                    return results.optJSONObject("s0").optString("value");
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

            if(!results.isNull("o1"))
                return results.optJSONObject("o1").optString("type");
            else
            {
                if(!results.isNull("s0"))
                    return results.optJSONObject("s0").optString("type");
            }

            return "Parsing error";
        }
        catch(JSONException e)
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
        catch(Exception e)
        {
            return "Parsing error";
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
            String toSend = "query=" + query + "&lang=en&kb=student";
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
}
