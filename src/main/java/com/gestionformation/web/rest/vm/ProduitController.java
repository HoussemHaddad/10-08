package com.gestionformation.web.rest.vm;

import com.gestionformation.domain.bot;

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
import java.util.ArrayList;
import java.util.List;


@Controller
public class ProduitController {


	private bot mybot = new bot();

	String r;

	List<String> lstmot = new ArrayList<>();
	




	


		






	//chat
	@RequestMapping(value="/chat")
	public String chat(Model model, String mot) {
//		AddAiml newAiml = new AddAiml();
//		newAiml.getAiml();

		r=mybot.getrep(mot);
//		if (mot.equals(null))
//			mot="HI";
		lstmot.add("user: "+mot);
		lstmot.add(r);
		model.addAttribute("msg",lstmot);
		for(int i=0;i<lstmot.size();i++)
			System.out.println(lstmot.get(i));


System.out.println(mot);
		return "chat";
	}
}
