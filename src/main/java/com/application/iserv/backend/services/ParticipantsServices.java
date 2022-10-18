package com.application.iserv.backend.services;

import com.application.iserv.backend.repositories.ParticipantsRepository;
import com.application.iserv.ui.participants.models.ParticipantsModel;
import com.application.iserv.ui.participants.models.NomineesModel;
import com.application.iserv.ui.participants.models.ReferenceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.application.iserv.ui.utils.Constants.*;

@Service
public class ParticipantsServices {

    private final ParticipantsRepository participantsRepository;

    @Autowired
    public ParticipantsServices(ParticipantsRepository participantsRepository) {
        this.participantsRepository = participantsRepository;
    }

    // Agents
    public List<ParticipantsModel> getAllAgents() {

        List<Object[]> allAgents = participantsRepository.retrieveAllAgents();

        List<ParticipantsModel> agents = new ArrayList<>();

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
            String completionYear = getCompletionDate[0];
            String completionMonth = getCompletionDate[1];

            LocalDate completionDate = LocalDate.of(
                    Integer.parseInt(getCompletionDate[0]),
                    Integer.parseInt(getCompletionDate[1]),
                    Integer.parseInt(getCompletionDate[2])
            );

            LocalDate todayLocalDate = LocalDate.now();
            String[] todayLocalDate_str = todayLocalDate.toString().split("-");
            String todayYear = todayLocalDate_str[0];
            String todayMonth = todayLocalDate_str[1];

            String compareTodayDate_str = todayYear+todayMonth;
            String compareCompletionDate_str = completionYear+completionMonth;

            int dateDifference = Integer.parseInt(compareTodayDate_str)
                    - Integer.parseInt(compareCompletionDate_str);

            if (dateDifference <= 0) {
                ParticipantsModel participantsModel = new ParticipantsModel(
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
                        row[1].toString()+" "+row[2].toString()
                );

                agents.add(participantsModel);

            }

        }

        return agents;
    }

    public String updateAgent(ParticipantsModel participantsModel) {
        String response = SUCCESSFUL;
        List<Object[]> checkAgentList = participantsRepository.checkForAgent(participantsModel);

        List<String> recordAgent = new ArrayList<>();

        if (!checkAgentList.isEmpty()) {

            for (int i = 0; i < checkAgentList.size(); i++) {
                BigInteger bigInteger = new BigInteger(String.valueOf(checkAgentList.get(i)));
                String bigIntegerStr = bigInteger.toString();
                Long participantId = Long.parseLong(bigIntegerStr);

                if (participantsModel.getParticipantId() != participantId) {
                    recordAgent.add(PARTICIPANT_ALREADY_EXIST);
                }

            }
        }

        if (!recordAgent.isEmpty()) {
            response = PARTICIPANT_ALREADY_EXIST;
        }
        else {
            participantsRepository.updateAgentDetails(participantsModel);
        }

        return response;
    }

    public void terminateAgent(Long participantId) {
        participantsRepository.terminateAgentDetails(participantId);
    }

    public String addAgent(ParticipantsModel participantsModel, List<String> contractDates) {
        String response;
        List<Object[]> checkAgent = participantsRepository.checkForAgent(participantsModel);

        if (checkAgent.isEmpty()) {
            participantsRepository.addNewAgent(participantsModel, contractDates);
            response = SUCCESSFUL;
        }
        else {
            response = PARTICIPANT_ALREADY_EXIST;
        }

        return response;
    }

    public List<ParticipantsModel> getAllTerminatedAgents() {
        List<Object[]> allAgents = participantsRepository.retrieveAllTerminatedAgents();

        List<ParticipantsModel> agents = new ArrayList<>();

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

            ParticipantsModel participantsModel = new ParticipantsModel(
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
                    row[1].toString()+" "+row[2].toString()
            );

            agents.add(participantsModel);

        }

        return agents;
    }

    public List<ParticipantsModel> searchAgents(String agentNames, Long statusValue, boolean isExpired) {
        List<Object[]> allAgents = participantsRepository.searchForAgents(agentNames, statusValue);

        List<ParticipantsModel> agents = new ArrayList<>();

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
            String completionYear = getCompletionDate[0];
            String completionMonth = getCompletionDate[1];

            LocalDate completionDate = LocalDate.of(
                    Integer.parseInt(getCompletionDate[0]),
                    Integer.parseInt(getCompletionDate[1]),
                    Integer.parseInt(getCompletionDate[2])
            );

            LocalDate todayLocalDate = LocalDate.now();
            String[] todayLocalDate_str = todayLocalDate.toString().split("-");
            String todayYear = todayLocalDate_str[0];
            String todayMonth = todayLocalDate_str[1];

            String compareTodayDate_str = todayYear+todayMonth;
            String compareCompletionDate_str = completionYear+completionMonth;

            int dateDifference = Integer.parseInt(compareTodayDate_str)
                    - Integer.parseInt(compareCompletionDate_str);

            if (isExpired) {
                if (dateDifference > 0) {
                    ParticipantsModel participantsModel = new ParticipantsModel(
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
                            row[1].toString()+" "+row[2].toString()
                    );

                    agents.add(participantsModel);

                }
            }
            else {
                if (dateDifference <= 0) {
                    ParticipantsModel participantsModel = new ParticipantsModel(
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
                            row[1].toString()+" "+row[2].toString()
                    );

                    agents.add(participantsModel);

                }
            }

        }

        return agents;
    }

    public List<String> getContractDuration(String district) {
        return participantsRepository.retrieveDuration(district);
    }

    public List<ParticipantsModel> getAllExpiredAgents() {

        List<Object[]> allAgents = participantsRepository.retrieveAllAgents();

        List<ParticipantsModel> agents = new ArrayList<>();

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
            String completionYear = getCompletionDate[0];
            String completionMonth = getCompletionDate[1];

            LocalDate completionDate = LocalDate.of(
                    Integer.parseInt(getCompletionDate[0]),
                    Integer.parseInt(getCompletionDate[1]),
                    Integer.parseInt(getCompletionDate[2])
            );

            LocalDate todayLocalDate = LocalDate.now();
            String[] todayLocalDate_str = todayLocalDate.toString().split("-");
            String todayYear = todayLocalDate_str[0];
            String todayMonth = todayLocalDate_str[1];

            String compareTodayDate_str = todayYear+todayMonth;
            String compareCompletionDate_str = completionYear+completionMonth;

            int dateDifference = Integer.parseInt(compareTodayDate_str)
                    - Integer.parseInt(compareCompletionDate_str);

            if (dateDifference > 0) {
                ParticipantsModel participantsModel = new ParticipantsModel(
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
                        row[1].toString()+" "+row[2].toString()
                );

                agents.add(participantsModel);

            }

        }

        return agents;
    }


    // Nominees
    public List<NomineesModel> getAllNominees() {
        List<Object[]> allNominees = participantsRepository.retrieveAllNominees();

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
        List<Object[]> checkNominee = participantsRepository.checkForNominee(nomineesModel);

        if (checkNominee.isEmpty()) {
            participantsRepository.addNewNominee(nomineesModel);
            response = SUCCESSFUL;
        }
        else {
            response = NOMINEE_ALREADY_EXIST;
        }

        return response;
    }

    public String updateNominee(NomineesModel nomineesModel) {
        String response = SUCCESSFUL;

        List<Object[]> checkNomineeList = participantsRepository.checkForNominee(nomineesModel);

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
            participantsRepository.updateNomineeDetails(nomineesModel);
        }

        return response;
    }

    public void removeNominee(Long nomineeId) {
        participantsRepository.removeNomineeDetails(nomineeId);
    }


    // Reference
    public List<ReferenceModel> getAllReferences() {
        List<Object[]> allReferences = participantsRepository.retrieveAllReferences();

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
        List<Object[]> checkReference = participantsRepository.checkForReference(referenceModel);

        if (checkReference.isEmpty()) {
            participantsRepository.addNewReference(referenceModel);
            response = SUCCESSFUL;
        }
        else {
            response = REFERENCE_ALREADY_EXIST;
        }

        return response;
    }

    public String updateReference(ReferenceModel referenceModel) {
        String response = SUCCESSFUL;

        List<Object[]> checkReferenceList = participantsRepository.checkForReference(referenceModel);

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
            participantsRepository.updateReferenceDetails(referenceModel);
        }

        return response;
    }

    public void removeReference(Long referenceId) {
        participantsRepository.removeReferenceDetails(referenceId);
    }


    // Attendance
    public List<ParticipantsModel> getAttendance(String date) {
        List<Object[]> allAgents = participantsRepository.retrieveAttendance(date);

        List<ParticipantsModel> agents = new ArrayList<>();

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

            ParticipantsModel participantsModel = new ParticipantsModel(
                    Integer.parseInt(row[28].toString()),
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
                    row[1].toString()+" "+row[2].toString()
            );

            agents.add(participantsModel);

        }

        return agents;
    }

    public List<ParticipantsModel> searchAttendance(String date, String agentNames, Long statusValue) {
        List<Object[]> allAgents = participantsRepository.searchAttendance(date, agentNames, statusValue);

        List<ParticipantsModel> agents = new ArrayList<>();

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

            ParticipantsModel participantsModel = new ParticipantsModel(
                    Integer.parseInt(row[28].toString()),
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
                    row[1].toString()+" "+row[2].toString()
            );

            agents.add(participantsModel);

        }

        return agents;
    }

    public void updateAttendance(Integer daysWorked, Long participantId, String date) {
        participantsRepository.updateAttendance(daysWorked, participantId, date);
    }

}
