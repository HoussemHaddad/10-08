package com.gestionformation.web.rest;

import com.gestionformation.domain.Authority;
import com.gestionformation.domain.Produit;
import com.gestionformation.domain.User;
import com.gestionformation.repository.AuthorityRepository;
import com.gestionformation.repository.ProduitRepository;
import org.apache.commons.codec.binary.Base64;
import com.gestionformation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
@Controller
//@EnableGlobalAuthentication
//@Profile("DEV_STANDALONE_H2_TEST_LDAP")
//@RequestMapping("/")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  AuthorityRepository authorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;




    /******************************************travail a prendre ****************************************************/
    @RequestMapping(value = "/")
    public String index1()
    {
        return  "redirect:login5";
    }

    /** 1er  page a afficher pour l'admin**/
    @RequestMapping(value="/Page1Admin")
    public String home() {
        return "admin/Page1Admin";
    }
    /** Deconnection**/
    @RequestMapping(value = "/logout")

    public String logout(HttpServletRequest request,
                       HttpServletResponse response) {

        SecurityContextHolder.clearContext();
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "login";
    }
    /** Formulaire des roles l'admin**/
    @RequestMapping(value = "/FormRole", method = RequestMethod.GET)
    public String formRole(Model model) {
        model.addAttribute("role", new Authority());
        return "admin/FormRole";
    }
    /** ajout du role **/
    @RequestMapping(value = "/saveRole")
    public String saveRole(Model model, @Valid Authority role,
                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            System.out.println("ici cv po");
            return "admin/FormRole";
        }
//        produitRepository.save(produit);

        authorityRepository.save(role);

        System.out.println("ici cv ");
        model.addAttribute("role",role);
        return "admin/Confirmation";
    }
    /** Afficher les roles pour l'admin**/
    @RequestMapping(value = "/ListeRole")
    public String Role(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int p,
                        @RequestParam(name = "size", defaultValue = "5") int s
        , @RequestParam(name = "motCle", defaultValue = "") String mc
    ) {
        Page<Authority> pageRole =
           authorityRepository.chercher("%" + mc + "%", new PageRequest(p, s));

        model.addAttribute("ListRole", pageRole.getContent());
        int[] pages = new int[pageRole.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);
        model.addAttribute("motCle", mc);
        return "admin/ListeRole";
    }
    /** Supprimer un role pour l'admin**/
    //delete
    @RequestMapping(value = "/deleteRole", method = RequestMethod.GET)
    public String deleteRole(String id, String motCle, int page, int size) {
authorityRepository.deleteById(id);

        return "redirect:/ListeRole?page=" + page + "&size=" + size + "&motCle=" + motCle;
    }

    /** Formulaire des roles l'admin**/
    @RequestMapping(value = "/FormUser", method = RequestMethod.GET)
    public String formUser(Model model) {
        model.addAttribute("user", new User());
        List<String> rol = authorityRepository.chercherRole();
        model.addAttribute("rol", rol);

        List<User> listeUser = userRepository.chercherUser();
        model.addAttribute("manager", listeUser);
        return "admin/FormUser";
    }
    /** ajout du user **/
    @RequestMapping(value = "/saveUser")
    public String saveUser(Model model, String firstName,String lastName,String email,String login,String password,
                           String logM, String authorities) {
System.out.println("niveau ajout+++ "+logM+"+++++"+authorities);

User user= new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);

        user.setEmail(email);
        user.setImageUrl("");
        user.setActivated(true);
        user.setLangKey("en");
        user.setActivationKey("");
        user.setResetKey("");
        user.setCreatedBy("system");
        Optional<User> us = userRepository.findOneByLogin(logM);
        user.setManager(us.get());

        user.setLastModifiedBy("system");
        /**** Ajout du role a new user*****/

        Set<Authority> ls= new HashSet<>();
        Authority a = new Authority();
        a.setName(authorities);
        ls.add(a);
        user.setAuthorities(ls);



        userRepository.save(user);

        System.out.println("ici cv ");
        model.addAttribute("user",user);
        return "redirect:ListeUser?page=0&size=5&motCle=";
    }


    //affichage des users
    @RequestMapping(value="/ListeUser")
    public String ListUser(Model model,
                        @RequestParam(name="page",defaultValue="0") int p,
                        @RequestParam(name="size",defaultValue="5")int s
        ,@RequestParam(name="motCle",defaultValue="")String mc
    ) {
        Page<User> pageUser=
            userRepository.chercher("%"+mc+"%",new  PageRequest(p,s));

        model.addAttribute("ListUser",pageUser.getContent());
        int[] pages= new int[pageUser.getTotalPages()];

        model.addAttribute("pages", pages);
        model.addAttribute("size", s);
        model.addAttribute("pagecourante", p);
        model.addAttribute("motCle",mc);
        Set<Authority> a = new HashSet<Authority>();
        List<String> role=new ArrayList<>();

        return "admin/ListeUser";
    }

//edit user
				@RequestMapping(value="/EditUser",method=RequestMethod.GET)
				public String EditUser(Model model,String login) {
				Optional<User>	u=userRepository.findOneByLogin(login);
					model.addAttribute("user",u.get());
                    List<String> rol = authorityRepository.chercherRole();
                    model.addAttribute("rol", rol);

                    List<User> listeUser = userRepository.chercherUser();
                    model.addAttribute("manager", listeUser);
					return "admin/EditUser";
				}


// Mise a jour
    @RequestMapping(value = "/Miseajr")
    public String MiseajrUser(Model model, String firstName,String lastName,String email,String login,
                           String logM, String authorities) {
        System.out.println("niveau ajout+++ "+logM+"+++++"+authorities);

        Optional<User> usr= userRepository.findOneByLogin(login);
        User user=usr.get();

        user.setFirstName(firstName);
        user.setLastName(lastName);

        user.setEmail(email);
        user.setImageUrl("");
        user.setActivated(true);
        user.setLangKey("en");
        user.setActivationKey("");
        user.setResetKey("");
        user.setCreatedBy("system");
        Optional<User> us = userRepository.findOneByLogin(logM);
        user.setManager(us.get());

        user.setLastModifiedBy("system");
        /**** Ajout du role a new user*****/

        Set<Authority> ls= new HashSet<>();
        Authority a = new Authority();
        a.setName(authorities);
        ls.add(a);
        user.setAuthorities(ls);



        userRepository.save(user);

        System.out.println("ici cv ");
        model.addAttribute("user",user);
        return "redirect:ListeUser?page=0&size=5&motCle=";
    }
    /** test sur le 1er login **/
    @PostMapping("/login1")
    public String index(HttpSession session, HttpServletRequest request, Authentication authentication, String username, String password) throws NoSuchAlgorithmException {



        session.setAttribute("mySessionAttribute"," "+ username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userName = auth.getName();
        String password1 = (String)auth.getCredentials();
        System.out.println(userName+"----+++++++++-----"+password1);

        /****Recuperer un user par son login */
        Optional<User> usr =userRepository.findOneByLogin(userName);

        /****** utilisateur existe en base local*****/
        if (usr.isPresent()) {
            System.out.println("+++++++++++++++++" + usr.get().getEmail());

            Set<Authority> set = new HashSet<Authority>();
            set=usr.get().getAuthorities();
            Iterator<Authority> it = set.iterator();
            while(it.hasNext()){
                Authority d = it.next();
                    if(d.getName().equals("ROLE_ADMIN"))

                return "redirect:/Page1Admin";
//                        System.out.println("ici on va retourner la page acceuil d'admin");
                    else  if(d.getName().equals("ROLE_USER"))
                        System.out.println("ici on va retourner la page acceuil du collaborateur");
                    else  if(d.getName().equals("ROLE_MANAGER"))
                        System.out.println("ici on va retourner la page acceuil du Manager");
                    else  if(d.getName().equals("ROLE_RF"))
                        return "RF/Page1RF";
//                        System.out.println("ici on va retourner la page acceuil du Responsable formation");
                System.out.println("role:++++++"+d.getName());
            }
        }
        /****** utilisateur n'existe pas en base local et existe en Ldap*****/
/********/
       else {


        String serverIP = "localhost";
        String serverPort = "12345";
        String serverLogin = "uid=admin,dc=springframework,dc=org";
        String serverPass = "secret";


        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");


        env.put(Context.PROVIDER_URL, "ldap://localhost:12345");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, serverLogin);
        env.put(Context.SECURITY_CREDENTIALS, "secret");

        try {
            DirContext ctx = new InitialDirContext(env);
            System.out.println("Connexion au serveur : OK");

            try {

                Attributes attrs = ctx.getAttributes("uid="+userName +",ou=otherpeople,dc=springframework,dc=org");
                String s = ctx.getNameInNamespace();

                System.out.println("++++++++++++++++++++++++++++++" + s);
                System.out.println("Recuperation de Nom Prenom: OK");

                System.out.println("+++++++++++++++++++last_name" + attrs.get("sn"));
                String lastName=attrs.get("sn").toString().substring(4);
                System.out.println("+++++++++++++++++++first_name" + attrs.get("cn"));
                String FirstName= attrs.get("cn").toString().substring(4);
                System.out.println("+++Firsttttt++"+FirstName);
                System.out.println("+++++++++++++++++++first_name" + attrs.get("email"));
                String email=attrs.get("email").toString().substring(7);
                System.out.println("++maiillllllllll"+email);
                System.out.println("+++++++++++++++++++first_name" + attrs.get("Role"));
                String role=attrs.get("Role").toString().substring(6);
                System.out.println("+++++++++++++++++++++++" + attrs.get("manager"));
                String manager=attrs.get("manager").toString().substring(9);
                System.out.println("-----------*****"+manager);
                System.out.println("+++++++++++++++++++++++" + attrs.get("userPassword"));
                Attribute pwd;
                pwd= attrs.get("userPassword");

/******** ici on va effectuer ce traitement si c'est un user simple sinon ajout directe et redirection selon le role ----********/
                Attribute userPassword = attrs.get("userPassword");
                String pwds = new String((byte[]) userPassword.get());
                System.out.println("password ++++++:: "+pwds);
                /****Verifier le  password*******/
//                if (pwds.equals(password1)){
                // System.out.println("password equivalent++++++++++");
                if(role.equals("ROLE_USER")) {
                        /*****ici on doit verifier si manager existe en base local ou nn ******/
                    Optional<User> mngr =userRepository.findOneByLogin(manager);
/*****----------ici if user else mngr rf admin------------/
                    /****** manager existe en base local*****/
                    if (mngr.isPresent()) {
                        System.out.println("+++++++++++++++++email le Manager :" + mngr.get().getEmail());
                        /****manager existe en bd local ***/

                        /****Ajout d'un user*/

                        User user = new User();
                        user.setLogin(userName);
                        user.setPassword(pwds);
                        user.setFirstName(FirstName);
                        user.setLastName(lastName);

                        user.setEmail(email);
                        user.setImageUrl("");
                        user.setActivated(true);
                        user.setLangKey("en");
                        user.setActivationKey("");
                        user.setResetKey("");
                        user.setCreatedBy("system");
                        user.setManager(mngr.get());

                        user.setLastModifiedBy("system");
                        /**** Ajout du role a new user*****/

                        Set<Authority> ls= new HashSet<>();
                        Authority a = new Authority();
                        a.setName(role);
                        ls.add(a);
                        user.setAuthorities(ls);
/**** ajout du user au bd local*/////
                        userRepository.save(user);

                    }
                    /*** Manager n'existe pas en base local recuperer a partir Ldap*****/
                    else {


                        String serverLogin1 = "uid=admin,dc=springframework,dc=org";



                        Hashtable<String, String> env1 = new Hashtable<String, String>();
                        env1.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");


                        env1.put(Context.PROVIDER_URL, "ldap://localhost:12345");
                        env1.put(Context.SECURITY_AUTHENTICATION, "simple");
                        env1.put(Context.SECURITY_PRINCIPAL, serverLogin1);
                        env1.put(Context.SECURITY_CREDENTIALS, "secret");

                        try {
                            DirContext ctx1 = new InitialDirContext(env1);
                            System.out.println("Connexion au serveur : OK");

                            try {

                                Attributes attrs1 = ctx1.getAttributes("uid="+manager +",ou=otherpeople,dc=springframework,dc=org");
                                String s1 = ctx1.getNameInNamespace();

                                System.out.println("++++++++++++++++++++++++++++++" + s1);
                                System.out.println("Recuperation de Nom Prenom: OK");

                                System.out.println("+++++++++++++++++++last_name" + attrs1.get("sn"));
                                String lastNameM=attrs1.get("sn").toString().substring(4);
                                System.out.println("+++++++++++++++++++first_name" + attrs1.get("cn"));
                                String FirstNameM= attrs1.get("cn").toString().substring(4);
                                System.out.println("+++Firsttttt++"+FirstName);
                                System.out.println("+++++++++++++++++++first_name" + attrs1.get("email"));
                                String emailM=attrs1.get("email").toString().substring(7);
                                System.out.println("++maiillllllllll"+email);
                                System.out.println("+++++++++++++++++++first_name" + attrs1.get("Role"));
                                String roleM=attrs1.get("Role").toString().substring(6);
                                System.out.println("+++++++++++++++++++++++" + attrs1.get("manager"));
//                                String manager=attrs.get("manager").toString().substring(9);
//                                System.out.println("-----------*****"+manager);
                                System.out.println("+++++++++++++++++++++++" + attrs1.get("userPassword"));
                                Attribute pwdM;
                                pwdM= attrs1.get("userPassword");


                                Attribute userPassword1 = attrs1.get("userPassword");
                                String pwds1 = new String((byte[]) userPassword1.get());
                                System.out.println("password ++++++:: "+pwds1);


                                /****Ajout d'un Manager*/

                                User user = new User();
                                user.setLogin(manager);
                                user.setPassword(pwds1);
                                user.setFirstName(FirstNameM);
                                user.setLastName(lastNameM);

                                user.setEmail(emailM);
                                user.setImageUrl("");
                                user.setActivated(true);
                                user.setLangKey("en");
                                user.setActivationKey("");
                                user.setResetKey("");
                                user.setCreatedBy("system");

                                ;
//                user.setResetDate(null);
                                user.setLastModifiedBy("system");
//                user.setLastModifiedDate(null);
                                /**** Ajout du role a new user*****/

                                Set<Authority> ls= new HashSet<>();
                                Authority a = new Authority();
                                a.setName(roleM);
                                ls.add(a);
                                user.setAuthorities(ls);
/**** ajout du Manager au bd local*/////
                                userRepository.save(user);



                                /****Ajout d'un user apres ajout manager*/

                                User user1 = new User();
                                user1.setLogin(userName);
                                user1.setPassword(pwds);
                                user1.setFirstName(FirstName);
                                user1.setLastName(lastName);

                                user1.setEmail(email);
                                user1.setImageUrl("");
                                user1.setActivated(true);
                                user1.setLangKey("en");
                                user1.setActivationKey("");
                                user1.setResetKey("");
                                user1.setCreatedBy("system");
                                user1.setManager(user);

                                user1.setLastModifiedBy("system");
                                /**** Ajout du role a new user*****/

                                Set<Authority> ls1= new HashSet<>();
                                Authority a1 = new Authority();
                                a1.setName(role);
                                ls1.add(a1);
                                user1.setAuthorities(ls1);
/**** ajout du user au bd local*/////
                                userRepository.save(user1);
                                System.out.println("ici on va retourner la page acceuil du user");

                            } catch (NamingException e) {
                                System.out.println("Recuperation de  Nom Prenom: KO");
                                e.printStackTrace();
                            }
                        } catch (NamingException e) {
                            System.out.println("Connexion au serveur : KO");
                            e.printStackTrace();
                        }

                    }

            }
/******** ajout direct sans chercher manager car c'est un admin,RF,Manager *******/

            else {
               /****Ajout d'un user*/

                User user = new User();
                user.setLogin(userName);
                user.setPassword(pwds);
                user.setFirstName(FirstName);
                user.setLastName(lastName);

                user.setEmail(email);
                user.setImageUrl("");
                user.setActivated(true);
                user.setLangKey("en");
                user.setActivationKey("");
                user.setResetKey("");
                user.setCreatedBy("system");

                  ;
//                user.setResetDate(null);
                user.setLastModifiedBy("system");
//                user.setLastModifiedDate(null);
            /**** Ajout du role a new user*****/

               Set<Authority> ls= new HashSet<>();
                Authority a = new Authority();
                a.setName(role);
                ls.add(a);
                user.setAuthorities(ls);
/**** ajout du user au bd local*/////
                userRepository.save(user);
                    if(role.equals("ROLE_ADMIN"))
                        System.out.println("ici on va retourner la page acceuil d'admin");
                    else  if(role.equals("ROLE_MANAGER"))
                        System.out.println("ici on va retourner la page acceuil du Manager");
                    else  if(role.equals("ROLE_RF"))
//                        System.out.println("ici on va retourner la page acceuil du Responsable formation");
                        return "RF/Page1RF";


                }





//                }
             //   else

                //    System.out.println("password faut");


//                System.out.println(attrs.get("name"));
//                System.out.println(attrs.get("mail"));
//                System.out.println(attrs.get("department"));
            } catch (NamingException e) {
                System.out.println("Recuperation de  Nom Prenom: KO");
                e.printStackTrace();
            }
        } catch (NamingException e) {
            System.out.println("Connexion au serveur : KO");
            e.printStackTrace();
        }




       }



/*******************************/


//        String redirectUrl = request.getScheme() + "://localhost:8080/#/admin/user-management";
//        return "redirect:" + redirectUrl;
       return "page1";


    }


    @RequestMapping(value = "/login5")
    public String login() {
        return "login";
    }


    @GetMapping("/test")
    public String index(String username,String password) {
        if ( username.equals("admin") & password.equals("admin")) {
            Optional<User> u = userRepository.findOneByLogin(username);
            Set<Authority> o = new HashSet<>();
            if (u.isPresent()) {
                System.out.println("+++++++++++++++++"+  u.get().getEmail());

                o= u.get().getAuthorities();

                Iterator iterator = o.iterator();

                while (iterator.hasNext())

                    System.out.println(iterator.next());


            }


            return "page1";

        }
        else
            return "login";
    }



}
