/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.HTMLparser;

import java.util.List;

/**
 *
 */
public class Publications
{
    String title;
    String date;
    List<String> authors;
    String lab;

    public Publications()
    {
    }

    
    
    public Publications(String title, String date, List<String> authors)
    {
        this.title = title;
        this.date = date;
        this.authors = authors;
    }
    
    @Override
    public String toString()
    {
        String output="\n";
        output+="Titre : "+this.title+" ("+this.date+")\n";
        output+=this.authors+"\n";
        return output;
    }
    
}
