package com.gestionformation.security;

import com.gestionformation.domain.Produit;
import com.gestionformation.domain.User;
import com.gestionformation.repository.ProduitRepository;
import com.gestionformation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;
import java.util.Optional;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {
//    @Autowired
//    private static UserRepository userRepository;
@Autowired
private ProduitRepository produitRepository;

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> {
                if (authentication.getPrincipal() instanceof UserDetails) {
                    UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                    System.out.println("iciiiii11111+++!!"+springSecurityUser.getUsername());
//                    /*traitement test ici***********/
//                    /*****************************************************************/
//
//                    String serverIP = "localhost";
//                    String serverPort = "12345";
//                    String serverLogin = "uid=admin,dc=springframework,dc=org";
//                    String serverPass = "secret";
//
//
//                    Hashtable<String, String> env = new Hashtable<String, String>();
//                    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
//
//
//                    env.put(Context.PROVIDER_URL, "ldap://localhost:12345");
//                    env.put(Context.SECURITY_AUTHENTICATION, "simple");
//                    env.put(Context.SECURITY_PRINCIPAL, serverLogin);
//                    env.put(Context.SECURITY_CREDENTIALS, "secret");
//
//                    try {
//                        DirContext ctx = new InitialDirContext(env);
//                        System.out.println("Connexion au serveur : OK");
//
//                        try {
//                            String us =springSecurityUser.getUsername();
//
//                            Attributes attrs = ctx.getAttributes("uid="+us+",ou=otherpeople,dc=springframework,dc=org");
//                            String s = ctx.getNameInNamespace();
//
//                            System.out.println("++++++++++++++++++++++++++++++" + s);
//                            System.out.println("Recuperation de Nom Prenom: OK");
//
//                            System.out.println("+++++++++++++++++++++++" + attrs.get("sn"));
//                            System.out.println("+++++++++++++++++++++++" + attrs.get("manager"));
//                            System.out.println("+++++++++++++++++++++++" + attrs.get("manager"));
//                            System.out.println("+++++++++++++++++++++++" + attrs.get("userPassword"));
//                            Attribute pwd;
//                            pwd= attrs.get("userPassword");
////                            User uss = new User ("aaa","bbb","ccc","ddd");
////                            userRepository.save(uss);
//                            Produit p = new Produit("ppppp",200,20);
//
//
//                            Attribute userPassword = attrs.get("userPassword");
//                            String pwds = new String((byte[]) userPassword.get());
//                            System.out.println("password ++++++:: "+pwds);
//                            if (pwds.equals("test1"))
//                                System.out.println("password equivalent++++++++++");
//                            else
//
//                                System.out.println("password faut");
////                System.out.println(attrs.get("name"));
////                System.out.println(attrs.get("mail"));
////                System.out.println(attrs.get("department"));
//                        } catch (NamingException e) {
//                            System.out.println("Recuperation de  Nom Prenom: KO");
//                            e.printStackTrace();
//                        }
//                    } catch (NamingException e) {
//                        System.out.println("Connexion au serveur : KO");
//                        e.printStackTrace();
//                    }
//



                    /**************************************************/
                    return springSecurityUser.getUsername();
                } else if (authentication.getPrincipal() instanceof String) {
                    System.out.println("iciiiii222222+++!!"+authentication.getName());
                    return (String) authentication.getPrincipal();
                }
                else if (authentication.getPrincipal() instanceof LdapUserDetails) {
                    LdapUserDetails ldapUser = (LdapUserDetails) authentication.getPrincipal();
                    System.out.println("iciiiii+++!!"+ldapUser.getUsername());
                    return ldapUser.getUsername();
                }
                return null;
            });
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
            .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
            .orElse(false);
    }
}
