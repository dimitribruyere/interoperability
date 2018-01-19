/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ptdq.interoperability.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import static fr.ptdq.interoperability.Program.question;

/**
 *
 */
@Controller
public class MainController
{

    @RequestMapping("/")
    public String questionInterface()
    {
        return "index";
    }

    @RequestMapping("/response")
    public String response(@RequestParam(required = false) String questiontext, Model m)
    {
        if (questiontext != null)
        {
            String response = question(questiontext);
            m.addAttribute("questiontext", questiontext);
            m.addAttribute("response", response);
            return "response";
        }
        return "redirect:/";
    }
}
