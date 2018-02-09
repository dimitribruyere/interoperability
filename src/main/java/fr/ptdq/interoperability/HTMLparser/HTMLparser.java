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
     * @param url String
     * @return List<String>
     */
    static List<String> getTeams()
    {
        String url = "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams.html";
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

    /**
     * @param url String
     *
     * @return List<String>
     */
    private static List<String> getMember()
    {

        String listUrl[] =
        {
            "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams/micro-nano-structuring/staff.html",
            "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams/radiation-matter-interaction/staff.html",
            "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams/image-science-computer-vision/staff.html",
            "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams/data-intelligence/staff.html",
            "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams/connected-intelligence/staff.html",
            "https://laboratoirehubertcurien.univ-st-etienne.fr/en/teams/secure-embedded-systems-hardware-architectures/staff.html"
        };

        List<String> teamList = new ArrayList<>();

        for (int j = 0; j <listUrl.length; j++)
        {
            String url = listUrl[j];
            try
            {
                Document doc = Jsoup.connect(url).get();

//                Elements members = doc.select(".simple");
//                for (int i = 0; i < members.size(); i++)
//                {
//                    String team = members.get(i).select("strong").text();
//
//                    team = team.split("[(]")[0];
//
//                    teamList.add(team);
//                }
                return teamList;
            } catch (IOException e)
            {
                System.err.println("La recherche avec cet URL n'a rien trouvé");
            }
            return null;
        }
        return null;
    }

}
