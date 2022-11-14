package com.application.iserv.security.services;

import com.application.iserv.security.Repositories.CredentialsRepository;
import com.application.iserv.security.models.RegisterAgentModel;
import com.opencsv.CSVWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.SUCCESSFUL;

@Service
public class CredentialsService {

    private final CredentialsRepository credentialsRepository;

    @Autowired
    public CredentialsService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    public void registerAgent(RegisterAgentModel registerAgentModel) {
        credentialsRepository.registerAgent(registerAgentModel);
    }

    public String checkForUser(String username) {

        String response = "";
        List<Object[]> checkUser = credentialsRepository.checkForUser(username);

        if (!checkUser.isEmpty()) {
            for(Object[] row : checkUser) {
                response = SUCCESSFUL+"-"+row[0].toString()+"-"+row[1].toString();
            }
        }
        else {
            response = "Failed to load your account";
        }

        return response;

    }

    public void updatePassword(String username, String password) {
        credentialsRepository.updatePassword(username, password);
    }

    /*@Cacheable(cacheNames = {"application_user"}, key = "#application_user_id")
    public RegisterAgentModel getApplicationUser(Long application_user_id) {
        simulateBackendCall();

        return applicationUserRepository.findById(application_user_id).get();
    }

    @Cacheable(cacheNames = {"application_user"}, key = "#application_user_id")
    public RegisterAgentModel getApplicationUser1() {
        return applicationUserRepository.findAll().get(0);
    }
*/
    public RegisterAgentModel getApplicationUserSaved() {
        return new RegisterAgentModel();
    }

    private void simulateBackendCall() {
        try {
            System.err.println("------------- Server going to sleep for 5 seconds ------------");
            Thread.sleep(5 * 1000);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void export(HttpServletResponse httpServletResponse) {

        try {
            //   Path path1 = Paths.get(ClassLoader.getSystemResource("csv/participants.csv").toURI());

            File file = new File(httpServletResponse.getOutputStream().toString());

            FileWriter fileWriter = new FileWriter(file);

            CSVWriter csvWriter = new CSVWriter(fileWriter);

            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Name", "Mark", "Pass/Fail"});
            data.add(new String[]{"Stacy Hart", "80", "Pass"});
            data.add(new String[]{"John Doe", "40", "Fail"});

            csvWriter.writeAll(data);

            csvWriter.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
