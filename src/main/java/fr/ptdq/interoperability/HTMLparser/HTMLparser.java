/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.HTMLparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    public static List<String> getTeams()
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
        }
        catch (IOException e)
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
    public static HashMap<String, String> getMembers()
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

        HashMap<String, String> memberList = new LinkedHashMap<String, String>();

        for (int j = 0; j <listUrl.length; j++)
        {
            String url = listUrl[j];
            try
            {
                String team ="";
                switch (j)
                {
                    case 0 :
                        team = "Micro & Nano Structuring";
                        break;
                    case 1 :
                        team = "Radiation-Matter Interaction";
                        break;
                    case 2 :
                        team = "Image Science & Computer Vision";
                        break;
                    case 3 :
                        team = "Data Intelligence";
                        break;
                    case 4 :
                        team = "Connected Intelligence";
                        break;
                    case 5 :
                        team = "Secure Embedded Systems Hardware Architectures";
                        break;
                }
                
                Document doc = Jsoup.connect(url).get();

                Elements membersContent = doc.select(".ametys-cms-content");
                Element members = membersContent.select("ul").get(0);
                
                for (int i = 0; i<members.childNodeSize(); i++)
                { 
                    String member = members.select("li").get(i).text();
                
                    memberList.put(member, team);
                }
                

            } catch (IOException e)
            {
                System.err.println("La recherche avec cet URL n'a rien trouvé");
            }
        }
        return memberList;
    }
    
    public static HashMap<String, String> getPubli()
    {
        HashMap<String, String> publis = new LinkedHashMap<>();
        
        String url = "https://dossier.univ-st-etienne.fr/ltsi/www/LabMetry/publi_labo_chronological_All.html";
        try
        {
            Document doc = Jsoup.connect(url).get();

            Elements publiSet = doc.getElementsByTag("li");
            
            for (int i = 0; i < publiSet.size(); i++)
            {
                String title = publiSet.get(i).getElementsByTag("a").text();
                String bordel = publiSet.get(i).text();
                
                publis.put(title, bordel);

            }
            return publis;
        } catch (IOException e)
        {
            System.err.println("La recherche avec cet URL n'a rien trouvé");
        }
        return null;
        
    }
}
