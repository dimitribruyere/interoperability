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
    List<String> contributors;

    public Publications()
    {
    }

    
    
    public Publications(String title, String date, List<String> authors, List<String> contributors)
    {
        this.title = title;
        this.date = date;
        this.authors = authors;        
        this.contributors = contributors;
    }
    
    @Override
    public String toString()
    {
        String output="\n";
        //output+=date+"\n";
        output+="Titre : "+this.title+" ("+this.date+")\n";
        output+=this.authors+"\n";
        return output;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public List<String> getAuthors()
    {
        return authors;
    }

    public void setAuthors(List<String> authors)
    {
        this.authors = authors;
    }

    public List<String> getContributors()
    {
        return contributors;
    }

    
    
}
