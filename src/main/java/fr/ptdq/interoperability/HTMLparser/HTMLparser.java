/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.HTMLparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 */
public class HTMLparser
{

    /**
     * @return List<String>
     */
    public static List<String> getTeams()
    {
        String url = "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams.html";
        return getTeamsWithUrl(url);
    }
    

    /**
     * @param url String
     * @return List<String>
     */
    private static List<String> getTeamsWithUrl(String url)
    {
        List<String> teamList = new ArrayList<>();
        try
        {
            Document doc = Jsoup.connect(url).get();

            Elements teamSet = doc.select(".simple");
            for (int i = 0; i < teamSet.size(); i++)
            {
                String team = teamSet.get(i).select("strong").text();
                
                team = team.split("[(]")[0];
                
                teamList.add(team);
            }
            return teamList;
        } catch (IOException e)
        {
            System.err.println("La recherche avec cet URL n'a rien trouvé");
        }
        return null;
    }
}