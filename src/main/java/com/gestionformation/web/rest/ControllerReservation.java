package com.gestionformation.web.rest;


import com.gestionformation.domain.CategorieFormation;
import com.gestionformation.domain.Formation;
import com.gestionformation.repository.CategorieFormationRepository;
import com.gestionformation.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ControllerReservation {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private CategorieFormationRepository categorieFormationRepository;

/******Consuler Catalogue pour RF**/
    @RequestMapping(value = "/ConsulterCatalogueRF")
    public String CatalogueRF(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int p,
                            @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {
        Page<Formation> pageFormation =
            formationRepository.chercher("%" + mc + "%", new PageRequest(p, s));

        List<CategorieFormation> categories=categorieFormationRepository.chercherCategorieF();
CategorieFormation ca=new CategorieFormation();
categories.add(ca);
        model.addAttribute("categories", categories);


        model.addAttribute("ListFormation", pageFormation.getContent());
        int[] pages = new int[pageFormation.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);
        model.addAttribute("motCle", mc);
        return "Catalogue/ConsulterCatalogueRF";
    }


    /******Consuler details d'une formation pour RF**/

    @RequestMapping(value = "/DetailFormationRF")
    public String DetailRF(Model model, Long idFormation ) {


       Optional<Formation> formation= formationRepository.findById(idFormation);


        model.addAttribute("formation", formation.get());



        return "Catalogue/DetailFormationRF";
    }
}
