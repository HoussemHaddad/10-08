package com.gestionformation.web.rest;


import com.gestionformation.domain.*;
import com.gestionformation.repository.*;
import com.gestionformation.service.NotificationService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailException;
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
public class ControllerReservation {
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
        List<String> jr = new ArrayList<String>();
        for(int i=1;i<=31;i++)
            jr.add(""+i);


        model.addAttribute("jr", jr);


        List<String> mois = new ArrayList<String>();
        for(int i=1;i<=12;i++)
            mois.add(""+i);


        model.addAttribute("mois", mois);

        return "Catalogue/DetailFormationRF";
    }


    @RequestMapping(value = "/CommentaireRF")
    public String saveRole(Model model, String motCle,Long idFormation) {
        Commentaire com =new Commentaire();
        com.setContenu(motCle);
        Optional<Formation> f=formationRepository.findById(idFormation);
        com.setCommentaire(f.get());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user=userRepository.findOneByLogin(userName);
        com.setUtilisateur(user.get());



       commentaireRepository.save(com);



        return "redirect:/DetailFormationRF?idFormation=" +idFormation;
    }




    /***Reservation RF**/
    @RequestMapping(value = "/ReservationRF")
    public String ReservationRF(Long idFormation,Model model,String jr,String mois,String annee1,
                                @RequestParam(name = "page", defaultValue = "0") int p,
                                @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {
String dateReservation=jr+"/"+mois+"/"+annee1;

String dateCreation = new Date().toString();

Reservation reservation=new Reservation();
reservation.setDateCreation(dateCreation);
reservation.setEtat("CONFIRMED");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user=userRepository.findOneByLogin(userName);
        reservation.setUtilisateur(user.get());
        Optional<Formation> f=formationRepository.findById(idFormation);
        reservation.setFormation(f.get());

        reservationRepository.save(reservation);

        AutresInformations autresInfo= new AutresInformations();
        autresInfo.setNomInfo("DateFormation");
        autresInfo.setContenuInfo(dateReservation);
        autresInfo.setReservation(reservation);
        autresInformationsRepository.save(autresInfo);

        model.addAttribute("Titre", f.get().getNomFormation());
        model.addAttribute("Date", dateReservation);
        model.addAttribute("Etat", "CONFIRMED");

        String msg = "Bonjour Mr "+userName+" vous avez une reservation le:"+dateReservation+" pour la formation "+f.get().getNomFormation();
        try {
            /**remplacer mail par mail du RF qu'on va notifier---------**/
           //------------->changer email par: user.get().getEmail();
            notificationService.sendNotification("h.h138907@gmail.com",msg);
        }
        catch(MailException e)
        {}


TypeDeNotification typeNotif=new TypeDeNotification();
        typeNotif.setNomType("Validation RF");
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

        return "RF/ConfirmationReservation";
    }

/*****Notification RF***/
@RequestMapping(value = "/NotificationRF")
public String NotificationRF(Model model,
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


    return "RF/NotificationRF";

}


    /**changer etat de notification marquer comme lu**/
    @RequestMapping(value = "/MarquerCommeLu", method = RequestMethod.GET)
    public String MarquerCommeLu(Model model,Long id, int page, int size,Long idReservation,Long idUser) {

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
                 return "redirect:/NotificationRF?page=0&size=5";
    }

}
