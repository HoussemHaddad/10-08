package com.gestionformation.domain;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;

import java.io.File;


public class bot {
    private static final boolean TRACE_MODE = false;
    static String botName = "super";
    String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }






    public static  String getrep(String msg)
    {String rep="";
//        List<String> listrep = new ArrayList<>();


//        if(msg.equals("Hi"))
//            reponse="Hello";
//        else if (msg.equals("how are you"))
//            reponse="fine and you";
//        else if (msg.equals("what is your name?"))
//            reponse="i am Robot!";
//        else
//            reponse="i do not understanr repeat please!";
//                    return reponse;
//AddAiml newAiml = new AddAiml();
//newAiml.getAiml();


        try {

            String resourcesPath = getResourcesPath();
            System.out.println(resourcesPath);
            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot("super", resourcesPath);
            bot.writeAIMLFiles();
            Chat chatSession = new Chat(bot);


            bot.brain.nodeStats();
            String textLine = "";
            textLine=msg;


                    String request = textLine;
//                    if (MagicBooleans.trace_mode)
//                        rep="STATE=" + request + ":THAT=" + ((History) chatSession.thatHistory.get(0)).get(0) + ":TOPIC=" + chatSession.predicates.get("topic");
                    String response = chatSession.multisentenceRespond(request);
                    while (response.contains("&lt;"))
                        response = response.replace("&lt;", "<");
                    while (response.contains("&gt;"))
                        response = response.replace("&gt;", ">");
                    rep="Robot : " + response;
                    System.out.println("Robot : " + response);
//                    listrep.add(rep);


        } catch (Exception e)

    {
        e.printStackTrace();
    }
    return rep;

    }










    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        System.out.println(path);
        String resourcesPath = path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
        return resourcesPath;
    }
}
