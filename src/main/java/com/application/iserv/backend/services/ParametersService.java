package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.ParametersRepository;
import com.application.iserv.ui.parameters.models.ParametersModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.PARAMETER_ALREADY_EXIST;
import static com.application.iserv.ui.utils.Constants.SUCCESSFUL;

@Service
public class ParametersService {

    private final ParametersRepository parametersRepository;

    @Autowired
    public ParametersService(ParametersRepository parametersRepository) {
        this.parametersRepository = parametersRepository;
    }

    public String addParameter(ParametersModel parametersModel) {
        String response;
        List<Object[]> checkParameter = parametersRepository.checkForParameter(parametersModel);

        if (checkParameter.isEmpty()) {
            parametersRepository.addParameter(parametersModel);
            response = SUCCESSFUL;
        }
        else {
            response = PARAMETER_ALREADY_EXIST;
        }

        return response;
    }

    public List<ParametersModel> getParameters() {
        List<Object[]> allParameters = parametersRepository.getParameters();

        List<ParametersModel> parameters = new ArrayList<>();

        for(Object[] row : allParameters) {

            ParametersModel parametersModel = new ParametersModel(
                    row[0].toString(),
                    Double.parseDouble(row[1].toString())
            );

            parameters.add(parametersModel);

        }

        return parameters;

    }

    public String updateParameter(ParametersModel parametersModel) {
        String response = SUCCESSFUL;
        List<Object[]> checkParameter = parametersRepository.checkForParameter(parametersModel);

        if (checkParameter.isEmpty()) {
            parametersRepository.updateParameter(parametersModel);
            response = SUCCESSFUL;
        } else {

            List<String> parameters = new ArrayList<>();

            for (Object[] row : checkParameter) {

                parameters.add(row[0].toString());
                
            }

            if (!parameters.contains(parametersModel.getPosition())) {
                response = PARAMETER_ALREADY_EXIST;
            }
            else {
                response = SUCCESSFUL;
            }

        }

        return response;

    }

}

