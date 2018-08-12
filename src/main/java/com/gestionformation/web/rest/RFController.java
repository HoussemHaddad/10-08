package com.gestionformation.web.rest;

import com.gestionformation.domain.Authority;
import com.gestionformation.domain.CategorieFormation;
import com.gestionformation.domain.CentreDeFormation;
import com.gestionformation.domain.Formation;
import com.gestionformation.repository.CategorieFormationRepository;
import com.gestionformation.repository.CentreDeFormationRepository;
import com.gestionformation.repository.FormationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
//@EnableGlobalAuthentication
//@Profile("DEV_STANDALONE_H2_TEST_LDAP")

public class RFController {

    @Autowired
    private CategorieFormationRepository categorieFormationRepository;

    @Autowired
    private CentreDeFormationRepository centreDeFormation;

    @Autowired
    private FormationRepository formationRepository;


    /** 1er  page a afficher pour le RF**/

    @RequestMapping(value="/Page1RF")
    public String homeRF(Model model) {
String nbr="44";

        model.addAttribute("nbr",nbr);

        return "RF/Page1RF";
    }

/*************************************Gestion des Categorie ****************************************/

    /** Formulaire des Categorie a ajouter**/
    @RequestMapping(value = "/FormCategorie", method = RequestMethod.GET)
    public String formCategorie(Model model) {
        model.addAttribute("Categorie", new CategorieFormation());
        return "RF/FormCategorieFormation";
    }


    /** ajout du categorie **/
    @RequestMapping(value = "/saveCategorie")
    public String saveCategorie(Model model, String name) {


        CategorieFormation cf = new CategorieFormation();
        cf.setNomCategorie(name);


        categorieFormationRepository.save(cf);

        return "redirect:ListeCategorie?page=0&size=5&motCle=";

    }


    /** Afficher les categories pour le RF**/
    @RequestMapping(value = "/ListeCategorie")
    public String Categorie(Model model,
                       @RequestParam(name = "page", defaultValue = "0") int p,
                       @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {
        Page<CategorieFormation> pageCategorie =
            categorieFormationRepository.chercher("%" + mc + "%", new PageRequest(p, s));

        model.addAttribute("ListCategorie", pageCategorie.getContent());
        int[] pages = new int[pageCategorie.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);
        model.addAttribute("motCle", mc);
        return "RF/ListeCategorie";
    }
/****Supprimer categorie**/
    @RequestMapping(value = "/deleteCategorie", method = RequestMethod.GET)
    public String deleteCategorie(Long id, String motCle, int page, int size) {
        categorieFormationRepository.deleteById(id);

        return "redirect:/ListeCategorie?page=" + page + "&size=" + size + "&motCle=" + motCle;
    }


    /*************************************Gestion des Centres de formation ****************************************/

    /** Formulaire des Centres a ajouter**/
    @RequestMapping(value = "/FormCentre", method = RequestMethod.GET)
    public String formCentre(Model model) {
        model.addAttribute("Centre", new CentreDeFormation());
        return "RF/FormCentreFormation";
    }

    /** ajout du centre de formation **/
    @RequestMapping(value = "/saveCentre")
    public String saveCentre(Model model, String nomCentre,String adresse,String websiteUrl,String gpsCoodinates) {


        CentreDeFormation centre = new CentreDeFormation();
       centre.setAdresse(adresse);
       centre.setGpsCoodinates(gpsCoodinates);
       centre.setNomCentre(nomCentre);
       centre.setWebsiteUrl(websiteUrl);


        centreDeFormation.save(centre);

        return "redirect:ListeCentre?page=0&size=5&motCle=";

    }


    /** Afficher les centres pour RF**/
    @RequestMapping(value = "/ListeCentre")
    public String Centre(Model model,
                            @RequestParam(name = "page", defaultValue = "0") int p,
                            @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {
        Page<CentreDeFormation> pageCentre =
            centreDeFormation.chercher("%" + mc + "%", new PageRequest(p, s));

        model.addAttribute("ListCentre", pageCentre.getContent());
        int[] pages = new int[pageCentre.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);
        model.addAttribute("motCle", mc);
        return "RF/ListeCentre";
    }

    @RequestMapping(value = "/deleteCentre", method = RequestMethod.GET)
    public String deleteCentre(Long id, String motCle, int page, int size) {
        centreDeFormation.deleteById(id);

        return "redirect:/ListeCentre?page=" + page + "&size=" + size + "&motCle=" + motCle;
    }

    /*************************************Gestion du Catalogue des formations ****************************************/

    /** Formulaire des formations a ajouter**/
    @RequestMapping(value = "/FormFormation", method = RequestMethod.GET)
    public String formFormation(Model model) {
        model.addAttribute("Formation", new Formation());

        List<CentreDeFormation> centres = centreDeFormation.chercherCentre();
        model.addAttribute("centres", centres);

        List<CategorieFormation> categories = categorieFormationRepository.chercherCategorieF();
        model.addAttribute("categories", categories);
        return "RF/FormFormation";
    }

    /** ajout d'une formation **/
    @RequestMapping(value = "/saveFormation")
    public String saveFormation(Model model, String nomFormation,String description,Long idCentre,Long idCategorie) {

Optional<CentreDeFormation> centre= centreDeFormation.findById(idCentre);

        Optional<CategorieFormation> categorie= categorieFormationRepository.findById(idCategorie);

        Formation formation = new Formation();
        formation.setNomFormation(nomFormation);
        formation.setDescription(description);
        formation.setActive(true);
        formation.setCategorieFormation(categorie.get());
        formation.setCentreDeFormation(centre.get());
        String date=new Date().toString();
        formation.setDateCreation(date);



        formationRepository.save(formation);

        return "redirect:ListeFormation?page=0&size=5&motCle=";

    }


    /** Afficher les Formations pour RF**/
    @RequestMapping(value = "/ListeFormation")
    public String Formation(Model model,
                         @RequestParam(name = "page", defaultValue = "0") int p,
                         @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {
        Page<Formation> pageFormation =
            formationRepository.chercher("%" + mc + "%", new PageRequest(p, s));

        model.addAttribute("ListFormation", pageFormation.getContent());
        int[] pages = new int[pageFormation.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);
        model.addAttribute("motCle", mc);
        return "RF/ListeFormation";
    }
/**changer etat de formation Activer/Desactiver**/
    @RequestMapping(value = "/etatFormation", method = RequestMethod.GET)
    public String etatFormation(Long id, String motCle, int page, int size) {
      Optional<Formation> formation=  formationRepository.findById(id);
      if(formation.get().getActive()==true)
          formation.get().setActive(false);
      else
          formation.get().setActive(true);

        formationRepository.save(formation.get());
        return "redirect:/ListeFormation?page=" + page + "&size=" + size + "&motCle=" + motCle;
    }


    //edit formation
    @RequestMapping(value="/EditFormation",method=RequestMethod.GET)
    public String EditUser(Model model,Long idFormation) {
        Optional<Formation>	f=formationRepository.findById(idFormation);
        model.addAttribute("formation",f.get());


        List<CentreDeFormation> centres = centreDeFormation.chercherCentre();
        model.addAttribute("centres", centres);

        List<CategorieFormation> categories = categorieFormationRepository.chercherCategorieF();
        model.addAttribute("categories", categories);
        return "RF/EditFormation";
    }



    /** Mise a jour d'une formation **/
    @RequestMapping(value = "/MiseajrFormation")
    public String MiseajrFormation(Model model,Long id, String nomFormation,String description,Long idCentre,Long idCategorie) {

        Optional<CentreDeFormation> centre= centreDeFormation.findById(idCentre);

        Optional<CategorieFormation> categorie= categorieFormationRepository.findById(idCategorie);

        Formation formation = formationRepository.findById(id).get();
        formation.setNomFormation(nomFormation);
        formation.setDescription(description);
        formation.setActive(true);
        formation.setCategorieFormation(categorie.get());
        formation.setCentreDeFormation(centre.get());
        String date=new Date().toString();
        formation.setDateCreation(date);



        formationRepository.save(formation);

        return "redirect:ListeFormation?page=0&size=5&motCle=";

    }
}
