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
                    row[1].toString(),
                    Long.parseLong(row[0].toString()),
                    Double.parseDouble(row[2].toString())
            );

            parameters.add(parametersModel);

        }

        return parameters;

    }

    public String updateParameter(ParametersModel parametersModel) {
        String response = "";
        List<Object[]> checkParameter = parametersRepository.checkUpdateParameter(parametersModel);

        if (!checkParameter.isEmpty()) {

            List<String> positions = new ArrayList<>();
            List<Long> parameterIds = new ArrayList<>();

            for (Object[] row : checkParameter) {
                positions.add(row[1].toString());
                parameterIds.add(Long.parseLong(row[0].toString()));
            }

            int positionIdIndex = -1;
            int parameterIdIndex = -1;

            for (int i = 0; i < parameterIds.size(); i++) {
                if (parameterIds.get(i).equals(parametersModel.getParameterId())) {
                    parameterIdIndex = i;
                }
            }

            for (int i = 0; i < positions.size(); i++) {
                if (positions.get(i).equalsIgnoreCase(parametersModel.getPosition())) {
                    positionIdIndex = i;
                }
            }

            boolean isSuccessful = false;
            if (positionIdIndex == parameterIdIndex) {
                isSuccessful = true;
            }
            else if (positionIdIndex == -1) {
                isSuccessful = true;
            }

            if (isSuccessful) {
                response = SUCCESSFUL;
                parametersRepository.updateParameter(parametersModel);
            }
            else {
                response = PARAMETER_ALREADY_EXIST;
            }

        }

        return response;

    }

    public void deleteParameter(Long parameterId) {
        parametersRepository.deleteParameter(parameterId);
    }
}

