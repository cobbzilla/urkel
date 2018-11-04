package urkel.auth;

import org.cobbzilla.wizard.filters.auth.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import urkel.dao.SessionDAO;
import urkel.model.UrkelProfile;

@Service
public class UrkelAuthProvider implements AuthProvider<UrkelProfile> {

    @Autowired private SessionDAO sessionDAO;

    @Override public UrkelProfile find(String uuid) {
        final UrkelProfile session = sessionDAO.find(uuid);
        if (session != null) sessionDAO.touch(uuid, session);
        return session;
    }

}
