package com.application.iserv.ui.utils;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import static com.application.iserv.ui.utils.Constants.*;

public class SessionManager {

    private final Preferences preferences = Preferences.userRoot().node(APPLICATION_USER);

    public SessionManager() {

    }

    public void saveApplicationUser(String username, String district, String village, String service) {
        preferences.put(USER_NAME, username);
        preferences.put(DISTRICT, district);
        preferences.put(VILLAGE, village);
        preferences.put(SERVICE, service);

    }

    public void clearApplicationUser() {
        try {
            preferences.clear();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }
    }

    public ApplicationUserDataModel getApplicationUserData() {

        ApplicationUserDataModel applicationUserDataModel = new ApplicationUserDataModel(
                preferences.get(USER_NAME, null),
                preferences.get(DISTRICT, null),
                preferences.get(VILLAGE, null),
                preferences.get(SERVICE, null)
        );

        if (applicationUserDataModel.getUsername() == null
                || applicationUserDataModel.getDistrict() == null
                || applicationUserDataModel.getVillage() == null
                || applicationUserDataModel.getService() == null) {
            throw new RuntimeException("application user is null");
        }
        return applicationUserDataModel;
    }

}
