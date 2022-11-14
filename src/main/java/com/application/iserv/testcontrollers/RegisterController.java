package com.application.iserv.testcontrollers;

import com.application.iserv.security.models.RegisterAgentModel;
import com.application.iserv.security.services.CredentialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/v1")
public class RegisterController {

    private final PasswordEncoder passwordEncoder;
    private final CredentialsService credentialsService;

    @Autowired
    public RegisterController(PasswordEncoder passwordEncoder, CredentialsService credentialsService) {
        this.passwordEncoder = passwordEncoder;
        this.credentialsService = credentialsService;
    }

    @GetMapping("/register")
    public void registerAgent() {

        RegisterAgentModel registerAgentModel = new RegisterAgentModel(
                "John",
                "Smith",
                "747319384",
                "76448899",
                "null",
                "ADMIN",
                "Kgatleng",
                "Oodi",
                "Ipelegeng",
                1,
                1,
                1,
                1
        );

     //   credentialsService.registerAgent(registerAgentModel);

      //  System.err.println(credentialsService.getApplicationUser(12L));

       // System.err.println(credentialsService.getDistrict(1L));

    }

    @GetMapping("/download")
    public void district(HttpServletResponse response) {

        /*response.setContentType("application/csv");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=csv.csv";
        response.setHeader(headerKey, headerValue);
        */
        credentialsService.export(response);
        response.setHeader("Content-Disposition", "attachment; filename=participants.csv");


    }

}
