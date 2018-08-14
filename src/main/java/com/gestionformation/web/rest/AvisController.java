package com.gestionformation.web.rest;


import com.gestionformation.domain.*;
import com.gestionformation.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@Controller
public class AvisController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TypeQuestionRepository typeQuestionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormulaireRepository formulaireRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private FeedBackRepository feedbackRepository;

    /**Form d'ajout question**/
    @RequestMapping(value = "/AjoutQuestion", method = RequestMethod.GET)
    public String AjoutQuestion(Model model) {
        List<TypeQuestion> typeQ=new ArrayList<>();
         typeQ=typeQuestionRepository.findAll();
        model.addAttribute("typeQ", typeQ);
        return "GestionAvis/AjoutQuestion";
    }

    /** ajout du question **/
    @RequestMapping(value = "/saveQuestion")
    public String saveQuestion(Model model,String titeQ,Long Type) {
        Optional<TypeQuestion> typeq=typeQuestionRepository.findById(Type);
        Question quest = new Question();
        quest.setTitre(titeQ);
        quest.setTypeQuestion(typeq.get());
        questionRepository.save(quest);
        model.addAttribute("titeQ", titeQ);
        model.addAttribute("Type", typeq.get().getNomType());

        return "GestionAvis/ConfirmationQuestion";
    }



    /**Form d'ajout Formulaire**/
    @RequestMapping(value = "/AjoutFormulaire", method = RequestMethod.GET)
    public String AjoutFormulaire(Model model) {

        List<Question> ListQ=questionRepository.findAll();

        foo foo = new foo();
        List<Long> checkedItems = new ArrayList<Long>();
				/*for(int i = 0 ; i < ListCi.size(); i++)
					checkedItems.add(ListCi.get(i).getCi());
					*/
        foo.setCheckedItems(checkedItems);
        model.addAttribute("foo", foo);


        model.addAttribute("ListQ",ListQ);

        return "GestionAvis/AjoutFormulaire";
    }

/** save formulaire**/
    @RequestMapping(value="/saveFormulaire",method=RequestMethod.POST)
    public String save(Model model ,@ModelAttribute foo foo) {
        Formulaire form = new Formulaire();
        Set<Question> lstQuestion = new HashSet<Question>();
//liste des valeurs coucher
        List<Long> checkedItems  = foo.getCheckedItems();

        for(int i = 0 ; i < checkedItems .size(); i++)
        {System.out.println("valeur coucher ++++++++++"+checkedItems.get(i));
        Optional<Question> q= questionRepository.findById(checkedItems.get(i));

        lstQuestion.add(q.get());
        }
        form.setQuestions(lstQuestion);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        Optional<User> user=userRepository.findOneByLogin(userName);
        form.setUtilisateur(user.get());
formulaireRepository.save(form);
        return "GestionAvis/ConfirmationForm";
    }


    /**Form d'ajout Formulaire**/
    @RequestMapping(value = "/FormulaireAvis", method = RequestMethod.GET)
    public String FormulaireAvis(Model model,Long idReservation,Long idUser) {
        Long max=formulaireRepository.chercherMaxForm();
        List<Question> lstQ= formulaireRepository.chercherQuestions(max);
        System.out.println(lstQ.get(1).getTitre()+"titre+------------------------><");
        List<Question>lstQ1 =new ArrayList<>();
        List<Question>lstQ2 =new ArrayList<>();
        List<Question>lstQ3 =new ArrayList<>();

        for(int i=0;i<lstQ.size();i++){
            if(lstQ.get(i).getTypeQuestion().getNomType().equals("YES_OR_NO_QUESTION"))
            {
                System.out.println(lstQ.get(i).getTitre()+"ici avant ajout en liste");
                lstQ1.add(lstQ.get(i));
                System.out.println(lstQ1.get(i).getTitre()+"ici apres ajout en liste");
            }


           else if(lstQ.get(i).getTypeQuestion().getNomType().equals("EVALUATION_QUESTION"))
        lstQ2.add(lstQ.get(i));

          else  if(lstQ.get(i).getTypeQuestion().getNomType().equals("EDITORIAL_QUESTION"))
        lstQ3.add(lstQ.get(i));
}

            model.addAttribute("lstQ1",lstQ1);
        model.addAttribute("lstQ2",lstQ2);
        model.addAttribute("lstQ3",lstQ3);

        model.addAttribute("idReservation", idReservation);
        model.addAttribute("idUser", idUser);

        return "GestionAvis/FormualireAvis";
    }

    @RequestMapping(value = "/saveFeedback")
    public String saveFeedback(Model model,String rep1,String rep2,String rep3,Long idReservation,Long idUser) {
        Long max=formulaireRepository.chercherMaxForm();
        List<Question> lstQ= formulaireRepository.chercherQuestions(max);
        /*****************************a completer******************************************/
        //ici on utilise sub string pour deviser les reponse , on recupere l'id du question et on save en table feedback
        List<Question>lstQ1 =new ArrayList<>();
        List<Question>lstQ2 =new ArrayList<>();
        List<Question>lstQ3 =new ArrayList<>();

        Optional<Reservation> reservation=reservationRepository.findById(idReservation);

        for(int i=0;i<lstQ.size();i++){
            if(lstQ.get(i).getTypeQuestion().getNomType().equals("YES_OR_NO_QUESTION"))
                lstQ1.add(lstQ.get(i));

            else if(lstQ.get(i).getTypeQuestion().getNomType().equals("EVALUATION_QUESTION"))
                lstQ2.add(lstQ.get(i));

            else  if(lstQ.get(i).getTypeQuestion().getNomType().equals("EDITORIAL_QUESTION"))
                lstQ3.add(lstQ.get(i));
        }



// question de type 1
        StringTokenizer st = new StringTokenizer(rep1,",");
        int i=0;
        while (st.hasMoreTokens()) {
            Feedback feed1=new Feedback();
            feed1.setQuestion(lstQ1.get(i));
            i++;
            feed1.setReponseQuestion(st.nextToken());
          feed1.setReservation(reservation.get());
            feedbackRepository.save(feed1);

        }


        // question de type 2
        StringTokenizer st2 = new StringTokenizer(rep2,",");
        int y=0;
        while (st2.hasMoreTokens()) {
            Feedback feed2=new Feedback();
            feed2.setQuestion(lstQ2.get(y));
            y++;
            feed2.setReponseQuestion(st2.nextToken());
            feed2.setReservation(reservation.get());
            feedbackRepository.save(feed2);

        }


        // question de type 3
        StringTokenizer st3 = new StringTokenizer(rep3,",");
        int j=0;
        while (st3.hasMoreTokens()) {
            Feedback feed3=new Feedback();
            feed3.setQuestion(lstQ3.get(j));
            j++;
            feed3.setReponseQuestion(st3.nextToken());
            feed3.setReservation(reservation.get());
            feedbackRepository.save(feed3);

        }
        System.out.println("rep1111------------> "+rep1);

        System.out.println("rep2+++-----------> "+rep2);




        return "GestionAvis/ConfirmationRemplir";

    }

}
