package urkel.dao;

import org.cobbzilla.wizard.dao.AbstractCRUDDAO;
import org.springframework.stereotype.Repository;
import urkel.model.UrkelProfile;

@Repository
public class UrkelProfileDAO extends AbstractCRUDDAO<UrkelProfile> {

    public UrkelProfile findByName(String name) { return findByUniqueField("name", name); }

}
