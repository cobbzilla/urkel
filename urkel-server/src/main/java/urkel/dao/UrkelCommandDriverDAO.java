package urkel.dao;

import org.cobbzilla.wizard.dao.AbstractCRUDDAO;
import org.springframework.stereotype.Repository;
import urkel.model.UrkelCommandDriver;

@Repository
public class UrkelCommandDriverDAO extends AbstractCRUDDAO<UrkelCommandDriver> {

    public UrkelCommandDriver findByName(String name) { return findByUniqueField("name", name); }

}
