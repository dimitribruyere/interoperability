/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperabilty.wikidata;

import info.bliki.api.User;
import info.bliki.api.query.Query;

/**
 *
 * @author chris
 */
public class TestBLIKI
{

    public static void main(String[] args)
    {
        info.bliki.api.User user = new User("ChristopherJEAMME", "D2rZ9Fk1Sg3K", "https://wdaqua-biennale-design.univ-st-etienne.fr/wikibase/");
        Query query = Query.create();

        System.out.println(query.get("Q878"));
    }
}
