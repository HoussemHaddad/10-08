package com.gestionformation.web.rest;

import com.gestionformation.domain.*;
import com.gestionformation.repository.*;
import com.gestionformation.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CentreDeFormationRepository centreDeFormation;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private AutresInformationsRepository autresInformationsRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TypeDeNotificationRepository typeDeNotificationRepository;

    @Autowired
    private NotificationService notificationService;


    /** 1er  page a afficher pour le RF**/

    @RequestMapping(value="/Page1RF")
    public String homeRF(Model model) {
////String nbr="44";
//
//        model.addAttribute("nbr",nbr);

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
            formationRepository.chercherTous("%" + mc + "%", new PageRequest(p, s));

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

    /*****GEstion des reservation par  le RF*************/

    /***Gerer les réservation par le RF**/
    @RequestMapping(value = "/GererReservationRF")
    public String GererReservationManager(Model model, Long idFormation,
                                          @RequestParam(name = "page", defaultValue = "0") int p,
                                          @RequestParam(name = "size", defaultValue = "5") int s ) {
       // User RF=userRepository.chercherRF();



        Page<Reservation> pageReservation =
            reservationRepository.chercherReservationdRF(new PageRequest(p, s));

        model.addAttribute("ListReservation", pageReservation.getContent());
        int[] pages = new int[pageReservation.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);

        return "RF/GererReservationRF";

    }

    /**Confirmer les reservation par le RF**/
    @RequestMapping(value = "/ConfirmationRF")
    public String ConfirmerReservationManager(Model model, Long idReservation)
    {


        model.addAttribute("idReservation", idReservation);

        /***pour la date enreservation**/
        List<String> jr = new ArrayList<String>();
        for(int i=1;i<=31;i++)
            jr.add(""+i);


        model.addAttribute("jr", jr);


        List<String> mois = new ArrayList<String>();
        for(int i=1;i<=12;i++)
            mois.add(""+i);


        model.addAttribute("mois", mois);

        return "RF/ConfirmerReservation";
    }



    /**Confirmer les reservation par le RF apres l'ajoutc du date **/
    @RequestMapping(value = "/ValiderReservationRF")
    public String ValiderReservationRF(Model model, Long idReservation,String jr,String mois,String annee1)
    {
        String dateReservation=jr+"/"+mois+"/"+annee1;
        Optional<Reservation> reservation=reservationRepository.findById(idReservation);
        reservation.get().setEtat("CONFIRMED");
        reservationRepository.save(reservation.get());


        AutresInformations autresInfo= new AutresInformations();
        autresInfo.setNomInfo("DateFormation");
        autresInfo.setContenuInfo(dateReservation);
        autresInfo.setReservation(reservation.get());
        autresInformationsRepository.save(autresInfo);

        /*notifier user*/
        String msg = "Bonjour Mr "+reservation.get().getUtilisateur().getFirstName()+" le responsable de formation a valider votre réservation de la formation "+reservation.get().getFormation().getNomFormation()+" pour la date de: "+dateReservation;
//        try {
//            /**remplacer mail par mail du collaborateur qu'on va notifier---------**/
//            //------------->changer email par: reservation.get().getUtilisateur().getEmail();

//            notificationService.sendNotification("h.h138907@gmail.com",msg);
//        }
//        catch(MailException e)
//        {}
        String dateCreation = new Date().toString();

        TypeDeNotification typeNotif=new TypeDeNotification();
        typeNotif.setNomType("Décision RF");
        typeNotif.setContenuNotification(msg);
        typeDeNotificationRepository.save(typeNotif);

        Notification notif=new Notification();
        notif.setDateDeCreation(dateCreation);
        notif.setReservation(reservation.get());
        notif.setUtilisateur(reservation.get().getUtilisateur());
        /**0= non lu 1=lu--**/
        notif.setEtatDeVue(false);
        notif.setTypeDeNotification(typeNotif);
        notificationRepository.save(notif);




        /*notifier Manager*/
        String msgManager = "Bonjour Mr "+reservation.get().getUtilisateur().getManager().getFirstName()+" le responsabe de formation a valider la réservation du votre collaborateur " +
            ""+reservation.get().getUtilisateur().getFirstName()+" pour la formation "+reservation.get().getFormation().getNomFormation()+" pour la date de: "+dateReservation;
//        try {
//            /**remplacer mail par mail du Manager qu'on va notifier---------**/
//            //------------->changer email par:reservation.get().getUtilisateur().getManager().getEmail();

//            notificationService.sendNotification("h.h138907@gmail.com",msg);
//        }
//        catch(MailException e)
//        {}
        String dateCreation1 = new Date().toString();

        TypeDeNotification typeNotif1=new TypeDeNotification();
        typeNotif1.setNomType("Décision RF");
        typeNotif1.setContenuNotification(msgManager);
        typeDeNotificationRepository.save(typeNotif1);

        Notification notif1=new Notification();
        notif1.setDateDeCreation(dateCreation1);
        notif1.setReservation(reservation.get());
        notif1.setUtilisateur(reservation.get().getUtilisateur().getManager());
        /**0= non lu 1=lu--**/
        notif1.setEtatDeVue(false);
        notif1.setTypeDeNotification(typeNotif1);
        notificationRepository.save(notif1);



        /*notifier TR*/

        User RF=userRepository.chercherRF();

        String msgRF = "Bonjour Mr "+RF.getFirstName()    +" la reservation du :"+reservation.get().getUtilisateur().getFirstName()+" pour la formation "+reservation.get().getFormation().getNomFormation()+" a été valider par vous même pour la date de: "+dateReservation;
//        try {
//            /**remplacer mail par mail du collaborateur qu'on va notifier---------**/
//            // ------------->changer email par: RF.getEmail();
//            notificationService.sendNotification("h.h138907@gmail.com",msgRF);
//        }
//        catch(MailException e)
//        {}


        TypeDeNotification typeNotif2=new TypeDeNotification();
        typeNotif2.setNomType("Décision RF");
        typeNotif2.setContenuNotification(msgRF);
        typeDeNotificationRepository.save(typeNotif2);

        Notification notif2=new Notification();
        notif2.setDateDeCreation(dateCreation);
        notif2.setReservation(reservation.get());
        notif2.setUtilisateur(RF);
        /**0= non lu 1=lu--**/
        notif2.setEtatDeVue(false);
        notif2.setTypeDeNotification(typeNotif2);
        notificationRepository.save(notif2);




        return "redirect:/GererReservationRF?page=0&size=5";
    }



    /**Anuulation du reservation par le RF**/
    @RequestMapping(value = "/AnnulationRF")
    public String AnnulationReservationManager(Model model, Long idReservation)
    {

        model.addAttribute("idReservation", idReservation);

        return "RF/AnnulationReservationRF";
    }


    /**Anuulation du reservation par le RF**/
    @RequestMapping(value = "/saveAnnulationRF")
    public String AnnulerReservationManager(Model model, Long idReservation,String raison)
    {

        Optional<Reservation> reservation=reservationRepository.findById(idReservation);
        reservation.get().setEtat("CANCELED_BY_RF");
        reservationRepository.save(reservation.get());

        /*notifier user*/
        String msg = "Bonjour Mr "+reservation.get().getUtilisateur().getFirstName()+" le résponsable de formation a annuler votre réservation de la formation "+reservation.get().getFormation().getNomFormation()+" pour la raison: "+raison;
//        try {
//            /**remplacer mail par mail du collaborateur qu'on va notifier---------**/
//            //------------->changer email par: reservation.get().getUtilisateur().getEmail();

//            notificationService.sendNotification("h.h138907@gmail.com",msg);
//        }
//        catch(MailException e)
//        {}
        String dateCreation = new Date().toString();

        TypeDeNotification typeNotif=new TypeDeNotification();
        typeNotif.setNomType("Décision RF");
        typeNotif.setContenuNotification(msg);
        typeDeNotificationRepository.save(typeNotif);

        Notification notif=new Notification();
        notif.setDateDeCreation(dateCreation);
        notif.setReservation(reservation.get());
        notif.setUtilisateur(reservation.get().getUtilisateur());
        /**0= non lu 1=lu--**/
        notif.setEtatDeVue(false);
        notif.setTypeDeNotification(typeNotif);
        notificationRepository.save(notif);


        /*notifier TR*/

        User RF=userRepository.chercherRF();

        String msgRF = "Bonjour Mr "+RF.getFirstName()    +" la reservation du :"+reservation.get().getUtilisateur().getFirstName()+" pour la formation "+reservation.get().getFormation().getNomFormation()+" a été annuler par vous même pour la raison suivante: "+raison;
//        try {
//            /**remplacer mail par mail du collaborateur qu'on va notifier---------**/
//            // ------------->changer email par: RF.getEmail();
//            notificationService.sendNotification("h.h138907@gmail.com",msgRF);
//        }
//        catch(MailException e)
//        {}


        TypeDeNotification typeNotif2=new TypeDeNotification();
        typeNotif2.setNomType("Décision RF");
        typeNotif2.setContenuNotification(msgRF);
        typeDeNotificationRepository.save(typeNotif2);

        Notification notif2=new Notification();
        notif2.setDateDeCreation(dateCreation);
        notif2.setReservation(reservation.get());
        notif2.setUtilisateur(RF);
        /**0= non lu 1=lu--**/
        notif2.setEtatDeVue(false);
        notif2.setTypeDeNotification(typeNotif2);
        notificationRepository.save(notif2);


      /**Notifier le manager**/
        String msgManager = "Bonjour Mr "+reservation.get().getUtilisateur().getManager().getFirstName()   +" la reservation du :"+reservation.get().getUtilisateur().getFirstName()+" pour la formation "+reservation.get().getFormation().getNomFormation()+" a été annuler par le RF pour la raison suivante: "+raison;
//        try {
//            /**remplacer mail par mail du collaborateur qu'on va notifier---------**/
//            // ------------->changer email par: reservation.get().getUtilisateur().getManager().getEmail();
//            notificationService.sendNotification("h.h138907@gmail.com",msgRF);
//        }
//        catch(MailException e)
//        {}


        TypeDeNotification typeNotif3=new TypeDeNotification();
        typeNotif3.setNomType("Décision RF");
        typeNotif3.setContenuNotification(msgManager);
        typeDeNotificationRepository.save(typeNotif3);

        Notification notif3=new Notification();
        notif3.setDateDeCreation(dateCreation);
        notif3.setReservation(reservation.get());
        notif3.setUtilisateur(reservation.get().getUtilisateur().getManager());
        /**0= non lu 1=lu--**/
        notif3.setEtatDeVue(false);
        notif3.setTypeDeNotification(typeNotif3);
        notificationRepository.save(notif3);






        return "redirect:/GererReservationRF?page=0&size=5";
    }



}
