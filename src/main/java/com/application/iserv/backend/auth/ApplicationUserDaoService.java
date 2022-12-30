package com.application.iserv.backend.auth;

import com.application.iserv.ui.utils.SessionManager;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.application.iserv.security.enums.ApplicationUserRole.*;

@Repository
@Transactional
public class ApplicationUserDaoService implements ApplicationUserDao {

    // SessionManager
    SessionManager sessionManager = new SessionManager();

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers(username)
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers(String username) {

        List<Object[]> userCredentials = getCredentialsDetails(username);

        ApplicationUser applicationUser = null;

        for(Object[] row : userCredentials) {

            Set<? extends GrantedAuthority> grantedAuthorities = null;

            if (row[5].toString().equalsIgnoreCase("ADMIN")) {
                grantedAuthorities = ADMIN.getGrantedAuthorities();
            }
            else if (row[5].toString().equalsIgnoreCase("SUPERVISOR")) {
                grantedAuthorities = SUPERVISOR.getGrantedAuthorities();
            }
            else if (row[5].toString().equalsIgnoreCase("ROOT")) {
                grantedAuthorities = ROOT.getGrantedAuthorities();
            }

            sessionManager.saveApplicationUser(
                    row[0].toString()+" "+row[1].toString(),
                    row[10].toString(),
                    row[11].toString(),
                    row[12].toString()
            );

            applicationUser = new ApplicationUser(
                    row[0].toString(),
                    row[1].toString(),
                    Long.parseLong(row[2].toString()),
                    row[3].toString(),
                    row[4].toString(),
                    row[10].toString(),
                    row[11].toString(),
                    row[12].toString(),
                    grantedAuthorities,
                    Boolean.parseBoolean(row[6].toString()),
                    Boolean.parseBoolean(row[7].toString()),
                    Boolean.parseBoolean(row[8].toString()),
                    Boolean.parseBoolean(row[9].toString())
            );

        }

        return Lists.newArrayList(
                applicationUser
        );

    }

    public List<Object[]> getCredentialsDetails(String username) {

        String sql = "SELECT application_user.firstname, application_user.lastname, " +
                "application_user.identity_number, application_user.username, application_user.password, " +
                "application_user.role, application_user.is_account_non_expired, " +
                "application_user.is_account_non_locked, application_user.is_credentials_non_expired, " +
                "application_user.is_enabled, application_user.district, application_user.village, " +
                "application_user.service " +
                "FROM application_user " +
                "WHERE application_user.username = '"+username+"'";

        try {
            Query query = entityManager.createNativeQuery(sql);
            return (List<Object[]>) query.getResultList();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}