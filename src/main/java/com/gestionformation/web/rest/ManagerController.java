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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ManagerController {
    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private CategorieFormationRepository categorieFormationRepository;

    @Autowired
    private CommentaireRepository commentaireRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AutresInformationsRepository autresInformationsRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TypeDeNotificationRepository typeDeNotificationRepository;

    @Autowired
    private NotificationService notificationService;


    /** 1er  page a afficher pour le Manager**/

    @RequestMapping(value="/Page1Manager")
    public String homeRF(Model model) {
        String nbr="44";

        model.addAttribute("nbr",nbr);

        return "Manager/Page1Manager";
    }

    /*****Notification Collaborateur***/
    @RequestMapping(value = "/NotificationManager")
    public String NotificationManager(Model model,
                                  @RequestParam(name = "page", defaultValue = "0") int p,
                                  @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user=userRepository.findOneByLogin(userName);
        Page<Notification> Notification = notificationRepository.chercher(user.get().getId(),new PageRequest(p, s));
        model.addAttribute("Notification", Notification.getContent());
        int[] pages = new int[Notification.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);

        return "Manager/NotificationManager";

    }
    /**changer etat de notification marquer comme lu notif collaborateur**/
    @RequestMapping(value = "/MarquerCommeLuManager", method = RequestMethod.GET)
    public String MarquerCommeLuManager(Model model,Long id, int page, int size,Long idReservation,Long idUser) {

        Optional<Notification> notification=  notificationRepository.findById(id);




        /** changer l'etat lu de la notification**/
        notification.get().setEtatDeVue(true);

        notificationRepository.save(notification.get());

        if(notification.get().getTypeDeNotification().getNomType().equals("formulaire d'avis"))
        //**redirection vers le formulaire d'avis pour le remplir et il faut ajouter un model contiet les info sur la formation et user
        {
            model.addAttribute("idReservation", idReservation);
            model.addAttribute("idUser", idUser);


            return "redirect:/FormulaireAvis?idReservation=" + idReservation + "&idUser=" + idUser;

        }

        else
            return "redirect:/NotificationManager?page=0&size=5";
    }


    /******Consuler Catalogue pour Manager**/
    @RequestMapping(value = "/ConsulterCatalogueManager")
    public String CatalogueManager(Model model,
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
        return "Catalogue/ConsulterCatalogueManager";
    }

    /******Consuler details d'une formation pour Collaborateur**/

    @RequestMapping(value = "/DetailFormationManager")
    public String DetailRF(Model model, Long idFormation,
                           @RequestParam(name = "page", defaultValue = "0") int p,
                           @RequestParam(name = "size", defaultValue = "5") int s ) {


        Optional<Formation> formation= formationRepository.findById(idFormation);


        model.addAttribute("formation", formation.get());



        Page<Commentaire> commentaires = commentaireRepository.chercher(idFormation, new PageRequest(p, s));
        model.addAttribute("commentaires", commentaires.getContent());
        int[] pages = new int[commentaires.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);
        model.addAttribute("idFormation", idFormation);


/***pour la date enreservation**/





        return "Catalogue/DetailFormationManager";
    }
/**Commentaire Manager**/
    @RequestMapping(value = "/CommentaireManager")
    public String CommentaireManager(Model model, String motCle,Long idFormation) {
        Commentaire com =new Commentaire();
        com.setContenu(motCle);
        Optional<Formation> f=formationRepository.findById(idFormation);
        com.setCommentaire(f.get());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user=userRepository.findOneByLogin(userName);
        com.setUtilisateur(user.get());



        commentaireRepository.save(com);



        return "redirect:/DetailFormationManager?idFormation=" +idFormation;
    }




    /***Reservation Manager**/
    @RequestMapping(value = "/ReservationManager")
    public String ReservationManager(Long idFormation,Model model,
                                 @RequestParam(name = "page", defaultValue = "0") int p,
                                 @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {


        String dateCreation = new Date().toString();

        Reservation reservation=new Reservation();
        reservation.setDateCreation(dateCreation);
        reservation.setEtat("WAITING_FOR_VALIDATION_TR");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user=userRepository.findOneByLogin(userName);
        reservation.setUtilisateur(user.get());
        Optional<Formation> f=formationRepository.findById(idFormation);
        reservation.setFormation(f.get());

        reservationRepository.save(reservation);



        model.addAttribute("Titre", f.get().getNomFormation());

        model.addAttribute("Etat", "CONFIRMED");

        String msg = "Bonjour Mr "+userName+" vous avez réserver la formation "+f.get().getNomFormation();
//        try {
//            /**remplacer mail par mail du collaborateur qu'on va notifier---------**/
//            //------------->changer email par: user.get().getEmail();
//            notificationService.sendNotification("h.h138907@gmail.com",msg);
//        }
//        catch(MailException e)
//        {}


        TypeDeNotification typeNotif=new TypeDeNotification();
        typeNotif.setNomType("nouvelle Reservation Manager");
        typeNotif.setContenuNotification(msg);
        typeDeNotificationRepository.save(typeNotif);

        Notification notif=new Notification();
        notif.setDateDeCreation(dateCreation);
        notif.setReservation(reservation);
        notif.setUtilisateur(user.get());
        /**0= non lu 1=lu--**/
        notif.setEtatDeVue(false);
        notif.setTypeDeNotification(typeNotif);
        notificationRepository.save(notif);


/** notifier RF**/
        User RF=userRepository.chercherRF();

        System.out.println(RF.getFirstName()+"RF RF RF");

        String msgRF = "Bonjour Mr "+RF.getFirstName()    +"le collaborateur:"+userName+" a réservé la formation "+f.get().getNomFormation();
//        try {
//            /**remplacer mail par mail du collaborateur qu'on va notifier---------**/
//            // ------------->changer email par: RF.getEmail();
//            notificationService.sendNotification("h.h138907@gmail.com",msgRF);
//        }
//        catch(MailException e)
//        {}


        TypeDeNotification typeNotif2=new TypeDeNotification();
        typeNotif2.setNomType("nouvelle Reservation Manager");
        typeNotif2.setContenuNotification(msgRF);
        typeDeNotificationRepository.save(typeNotif2);

        Notification notif2=new Notification();
        notif2.setDateDeCreation(dateCreation);
        notif2.setReservation(reservation);
        notif2.setUtilisateur(RF);
        /**0= non lu 1=lu--**/
        notif2.setEtatDeVue(false);
        notif2.setTypeDeNotification(typeNotif2);
        notificationRepository.save(notif2);

        return "Manager/ConfirmationReservation";
    }


    /**Consulter les reservation du Manager**/
    @RequestMapping(value = "/EtatReservationManager")
    public String EtatReservationManager(Model model, Long idFormation,
                                     @RequestParam(name = "page", defaultValue = "0") int p,
                                     @RequestParam(name = "size", defaultValue = "5") int s ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user=userRepository.findOneByLogin(userName);

        Page<Reservation> pageReservation =
            reservationRepository.chercherReservation(user.get().getId(), new PageRequest(p, s));


        for(int i=0; i<pageReservation.getTotalPages();i++)
        {
//    if (pageReservation.getContent().get(i).getAutresInformations().size()==0) {
//        AutresInformations autre=new AutresInformations();
//        autre.setNomInfo("Information");
//        autre.setContenuInfo("pas d'info pour le moment");
//        autre.setReservation(pageReservation.getContent().get(i));
//        Set<AutresInformations> lstautre=new HashSet <>();
//        lstautre.add(autre);
//        pageReservation.getContent().get(i).setAutresInformations(lstautre);
//    }

            model.addAttribute("ListReservation", pageReservation.getContent());
            int[] pages = new int[pageReservation.getTotalPages()];

            model.addAttribute("pages", pages);
            model.addAttribute("size", s);
            model.addAttribute("pagecourante", p);
        }


        return "Manager/EtatReservation";


    }

}
