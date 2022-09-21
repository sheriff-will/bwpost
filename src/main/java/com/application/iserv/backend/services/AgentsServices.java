package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.AgentsRepository;
import com.application.iserv.ui.agents.models.AgentsModel;
import com.application.iserv.ui.agents.models.AttendanceModel;
import com.application.iserv.ui.agents.models.NomineesModel;
import com.application.iserv.ui.agents.models.ReferenceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@Service
public class AgentsServices {

    private final AgentsRepository agentsRepository;

    @Autowired
    public AgentsServices(AgentsRepository agentsRepository) {
        this.agentsRepository = agentsRepository;
    }


    // Agents
    public List<AgentsModel> getAllAgents() {
        List<Object[]> allAgents = agentsRepository.retrieveAllAgents();

        List<AgentsModel> agents = new ArrayList<>();

        for(Object[] row : allAgents) {

            String dateOfBirth_str = row[4].toString();
            String[] getDateOfBirth = dateOfBirth_str.split("-");

            LocalDate dateOfBirth = LocalDate.of(
                    Integer.parseInt(getDateOfBirth[0]),
                    Integer.parseInt(getDateOfBirth[1]),
                    Integer.parseInt(getDateOfBirth[2])
            );

            String placementDate_str = row[14].toString();
            String[] getPlacementDate = placementDate_str.split("-");

            LocalDate placementDate = LocalDate.of(
                    Integer.parseInt(getPlacementDate[0]),
                    Integer.parseInt(getPlacementDate[1]),
                    Integer.parseInt(getPlacementDate[2])
            );

            String completionDate_str = row[15].toString();
            String[] getCompletionDate = completionDate_str.split("-");

            LocalDate completionDate = LocalDate.of(
                    Integer.parseInt(getCompletionDate[0]),
                    Integer.parseInt(getCompletionDate[1]),
                    Integer.parseInt(getCompletionDate[2])
            );

            AgentsModel agentsModel = new AgentsModel(
                    Long.parseLong(row[0].toString()),
                    LocalDateTime.parse(row[20].toString()),
                    dateOfBirth,
                    placementDate,
                    completionDate,
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[5].toString(),
                    row[6].toString(),
                    row[7].toString(),
                    row[8].toString(),
                    row[9].toString(),
                    row[10].toString(),
                    row[11].toString(),
                    row[12].toString(),
                    row[13].toString(),
                    row[24].toString(),
                    row[16].toString(),
                    row[17].toString(),
                    row[18].toString(),
                    row[19].toString(),
                    row[25].toString(),
                    row[26].toString(),
                    row[27].toString(),
                    row[1].toString()+" "+row[2].toString(),
                    PRESENT
            );

            agents.add(agentsModel);

        }

        return agents;
    }

    public String updateAgent(AgentsModel agentsModel) {
        String response = SUCCESSFUL;
        List<Object[]> checkAgentList = agentsRepository.checkForAgent(agentsModel);

        List<String> recordAgent = new ArrayList<>();

        if (!checkAgentList.isEmpty()) {

            for (int i = 0; i < checkAgentList.size(); i++) {
                BigInteger bigInteger = new BigInteger(String.valueOf(checkAgentList.get(i)));
                String bigIntegerStr = bigInteger.toString();
                Long participantId = Long.parseLong(bigIntegerStr);

                if (agentsModel.getParticipantId() != participantId) {
                    recordAgent.add(AGENT_ALREADY_EXIST);
                }

            }
        }

        if (!recordAgent.isEmpty()) {
            response = AGENT_ALREADY_EXIST;
        }
        else {
            agentsRepository.updateAgentDetails(agentsModel);
        }

        return response;
    }

    public void terminateAgent(Long participantId) {
        agentsRepository.terminateAgentDetails(participantId);
    }

    public String addAgent(AgentsModel agentsModel) {
        String response;
        List<Object[]> checkAgent = agentsRepository.checkForAgent(agentsModel);

        if (checkAgent.isEmpty()) {
            agentsRepository.addNewAgent(agentsModel);
            response = SUCCESSFUL;
        }
        else {
            response = AGENT_ALREADY_EXIST;
        }

        return response;
    }

    public List<AgentsModel> getAllTerminatedAgents() {
        List<Object[]> allAgents = agentsRepository.retrieveAllTerminatedAgents();

        List<AgentsModel> agents = new ArrayList<>();

        for(Object[] row : allAgents) {

            String dateOfBirth_str = row[4].toString();
            String[] getDateOfBirth = dateOfBirth_str.split("-");

            LocalDate dateOfBirth = LocalDate.of(
                    Integer.parseInt(getDateOfBirth[0]),
                    Integer.parseInt(getDateOfBirth[1]),
                    Integer.parseInt(getDateOfBirth[2])
            );

            String placementDate_str = row[14].toString();
            String[] getPlacementDate = placementDate_str.split("-");

            LocalDate placementDate = LocalDate.of(
                    Integer.parseInt(getPlacementDate[0]),
                    Integer.parseInt(getPlacementDate[1]),
                    Integer.parseInt(getPlacementDate[2])
            );

            String completionDate_str = row[15].toString();
            String[] getCompletionDate = completionDate_str.split("-");

            LocalDate completionDate = LocalDate.of(
                    Integer.parseInt(getCompletionDate[0]),
                    Integer.parseInt(getCompletionDate[1]),
                    Integer.parseInt(getCompletionDate[2])
            );

            AgentsModel agentsModel = new AgentsModel(
                    Long.parseLong(row[0].toString()),
                    LocalDateTime.parse(row[20].toString()),
                    dateOfBirth,
                    placementDate,
                    completionDate,
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[5].toString(),
                    row[6].toString(),
                    row[7].toString(),
                    row[8].toString(),
                    row[9].toString(),
                    row[10].toString(),
                    row[11].toString(),
                    row[12].toString(),
                    row[13].toString(),
                    row[24].toString(),
                    row[16].toString(),
                    row[17].toString(),
                    row[18].toString(),
                    row[19].toString(),
                    row[25].toString(),
                    row[26].toString(),
                    row[27].toString(),
                    row[1].toString()+" "+row[2].toString(),
                    PRESENT
            );

            agents.add(agentsModel);

        }

        return agents;
    }

    public List<AgentsModel> searchAgents(String agentNames, Long statusValue) {
        List<Object[]> allAgents = agentsRepository.searchForAgents(agentNames, statusValue);

        List<AgentsModel> agents = new ArrayList<>();

        for(Object[] row : allAgents) {

            String dateOfBirth_str = row[4].toString();
            String[] getDateOfBirth = dateOfBirth_str.split("-");

            LocalDate dateOfBirth = LocalDate.of(
                    Integer.parseInt(getDateOfBirth[0]),
                    Integer.parseInt(getDateOfBirth[1]),
                    Integer.parseInt(getDateOfBirth[2])
            );

            String placementDate_str = row[14].toString();
            String[] getPlacementDate = placementDate_str.split("-");

            LocalDate placementDate = LocalDate.of(
                    Integer.parseInt(getPlacementDate[0]),
                    Integer.parseInt(getPlacementDate[1]),
                    Integer.parseInt(getPlacementDate[2])
            );

            String completionDate_str = row[15].toString();
            String[] getCompletionDate = completionDate_str.split("-");

            LocalDate completionDate = LocalDate.of(
                    Integer.parseInt(getCompletionDate[0]),
                    Integer.parseInt(getCompletionDate[1]),
                    Integer.parseInt(getCompletionDate[2])
            );

            AgentsModel agentsModel = new AgentsModel(
                    Long.parseLong(row[0].toString()),
                    LocalDateTime.parse(row[20].toString()),
                    dateOfBirth,
                    placementDate,
                    completionDate,
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[5].toString(),
                    row[6].toString(),
                    row[7].toString(),
                    row[8].toString(),
                    row[9].toString(),
                    row[10].toString(),
                    row[11].toString(),
                    row[12].toString(),
                    row[13].toString(),
                    row[24].toString(),
                    row[16].toString(),
                    row[17].toString(),
                    row[18].toString(),
                    row[19].toString(),
                    row[25].toString(),
                    row[26].toString(),
                    row[27].toString(),
                    row[1].toString()+" "+row[2].toString(),
                    PRESENT
            );

            agents.add(agentsModel);

        }

        return agents;
    }


    // Nominees
    public List<NomineesModel> getAllNominees() {
        List<Object[]> allNominees = agentsRepository.retrieveAllNominees();

        List<NomineesModel> nominees = new ArrayList<>();

        for(Object[] row : allNominees) {

            NomineesModel nomineesModel = new NomineesModel(
                    Long.parseLong(row[0].toString()),
                    Long.parseLong(row[7].toString()),
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[4].toString(),
                    row[5].toString(),
                    row[6].toString(),
                    row[1].toString()+" "+row[2].toString()
            );

            nominees.add(nomineesModel);

        }

        return nominees;

    }

    public String addNominee(NomineesModel nomineesModel) {
        String response;
        List<Object[]> checkNominee = agentsRepository.checkForNominee(nomineesModel);

        if (checkNominee.isEmpty()) {
            agentsRepository.addNewNominee(nomineesModel);
            response = SUCCESSFUL;
        }
        else {
            response = NOMINEE_ALREADY_EXIST;
        }

        return response;
    }

    public String updateNominee(NomineesModel nomineesModel) {
        String response = SUCCESSFUL;

        List<Object[]> checkNomineeList = agentsRepository.checkForNominee(nomineesModel);

        List<String> recordNominee = new ArrayList<>();

        if (!checkNomineeList.isEmpty()) {

            for (int i = 0; i < checkNomineeList.size(); i++) {
                BigInteger bigInteger = new BigInteger(String.valueOf(checkNomineeList.get(i)));
                String bigIntegerStr = bigInteger.toString();
                Long nomineeId = Long.parseLong(bigIntegerStr);

                if (nomineesModel.getNomineeId() != nomineeId) {
                    recordNominee.add(NOMINEE_ALREADY_EXIST);
                }

            }
        }

        if (!recordNominee.isEmpty()) {
            response = NOMINEE_ALREADY_EXIST;
        }
        else {
            agentsRepository.updateNomineeDetails(nomineesModel);
        }

        return response;
    }

    public void removeNominee(Long nomineeId) {
        agentsRepository.removeNomineeDetails(nomineeId);
    }


    // Reference
    public List<ReferenceModel> getAllReferences() {
        List<Object[]> allReferences = agentsRepository.retrieveAllReferences();

        List<ReferenceModel> references = new ArrayList<>();

        for(Object[] row : allReferences) {

            ReferenceModel referenceModel = new ReferenceModel(
                    Long.parseLong(row[0].toString()),
                    Long.parseLong(row[6].toString()),
                    row[1].toString(),
                    row[2].toString(),
                    row[3].toString(),
                    row[4].toString(),
                    row[5].toString(),
                    row[1].toString()+" "+row[2].toString()
            );

            references.add(referenceModel);

        }

        return references;

    }

    public String addReference(ReferenceModel referenceModel) {
        String response;
        List<Object[]> checkReference = agentsRepository.checkForReference(referenceModel);

        if (checkReference.isEmpty()) {
            agentsRepository.addNewReference(referenceModel);
            response = SUCCESSFUL;
        }
        else {
            response = REFERENCE_ALREADY_EXIST;
        }

        return response;
    }

    public String updateReference(ReferenceModel referenceModel) {
        String response = SUCCESSFUL;

        List<Object[]> checkReferenceList = agentsRepository.checkForReference(referenceModel);

        List<String> recordReference = new ArrayList<>();

        if (!checkReferenceList.isEmpty()) {

            for (int i = 0; i < checkReferenceList.size(); i++) {
                BigInteger bigInteger = new BigInteger(String.valueOf(checkReferenceList.get(i)));
                String bigIntegerStr = bigInteger.toString();
                Long referenceId = Long.parseLong(bigIntegerStr);

                if (referenceModel.getReferenceId() != referenceId) {
                    recordReference.add(REFERENCE_ALREADY_EXIST);
                }

            }
        }

        if (!recordReference.isEmpty()) {
            response = REFERENCE_ALREADY_EXIST;
        }
        else {
            agentsRepository.updateReferenceDetails(referenceModel);
        }

        return response;
    }

    public void removeReference(Long referenceId) {
        agentsRepository.removeReferenceDetails(referenceId);
    }


    // Attendance
    public List<AttendanceModel> getAttendance(Long statusValue) {
        List<Object[]> allAttendance = agentsRepository.retrieveAllAttendance(statusValue);

        List<AttendanceModel> attendance = new ArrayList<>();

        for(Object[] row : allAttendance) {

            String date_str = row[1].toString();
            String[] getDate = date_str.split("-");

            LocalDate date = LocalDate.of(
                    Integer.parseInt(getDate[0]),
                    Integer.parseInt(getDate[1]),
                    Integer.parseInt(getDate[2])
            );

            AttendanceModel attendanceModel = new AttendanceModel(
                    row[2].toString(),
                    date,
                    Long.parseLong(row[0].toString()),
                    Long.parseLong(row[3].toString())
            );

            attendance.add(attendanceModel);

        }

        return attendance;

    }

    // Contract Duration
    public List<String> getContractDuration(String district) {
        return agentsRepository.retrieveDuration(district);

    }

}
