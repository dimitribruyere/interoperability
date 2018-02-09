/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.HTMLparser;

import java.util.List;
import jdk.nashorn.internal.objects.NativeArray;

/**
 *
 * @author Dimitri
 */
public class TestParser
{

    public static void main(String[] args)
    {
        List<String> listTeam = HTMLparser.getTeams();

        listTeam.forEach(i ->
        {
            System.out.println(i + "\n");
        });

    }
}
