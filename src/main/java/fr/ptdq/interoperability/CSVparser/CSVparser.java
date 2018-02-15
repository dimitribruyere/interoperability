/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.CSVparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Clément
 */
public class CSVparser {
    
    public static void main(String[] args) throws IOException
    {
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/csv/Classeur1.csv"));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null)
            {
                if (i != 0)
                {
                   String[] stage = line.split(";");
                   String lieu = stage[0];
                   String titre = stage[1];
                   String desc = stage[2];
                   String[] compt = stage[3].split("/");
                   String duree = stage[4];
                   String gratification = stage[5];
                   
                    System.out.println("Lieu du stage : " + lieu);
                    System.out.println("titre: " + titre);
                    System.out.println("Description : " + desc + "\rListe des compétences requises : ");
                    for (int j = 0; j < compt.length; j++)
                    {
                        System.out.println("\t- " +compt[j]);                             
                    }
                    System.out.println("Durée du stage " + duree + " mois");
                    System.out.println("Gratification : " + gratification + " euros");
                    for(int j = 0; j < 120; j++)
                    {
                        System.out.print("-");
                    }
                    System.out.println("");
                }
                
                i++;
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Fichier non lu");
        }
        
    }
}
