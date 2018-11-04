package urkel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.cobbzilla.wizard.filters.auth.TokenPrincipal;
import org.cobbzilla.wizard.model.Identifiable;
import org.cobbzilla.wizard.model.IdentifiableBase;
import org.cobbzilla.wizard.model.entityconfig.annotations.ECType;
import org.cobbzilla.wizard.model.entityconfig.annotations.ECTypeFields;
import org.cobbzilla.wizard.model.entityconfig.annotations.ECTypeURIs;
import org.cobbzilla.wizard.validation.HasValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import static org.cobbzilla.util.reflect.ReflectionUtil.copy;
import static org.cobbzilla.wizard.model.NamedEntity.NAME_MAXLEN;

@ECType(root=true)
@ECTypeURIs(listFields={"name"})
@ECTypeFields(list={"uuid", "name", "admin"})
@Entity @NoArgsConstructor @Accessors(chain=true)
public class UrkelProfile extends IdentifiableBase implements TokenPrincipal {

    @HasValue(message="err.name.empty")
    @Column(length=NAME_MAXLEN, unique=true, nullable=false)
    @Size(min=2, max=NAME_MAXLEN, message="err.name.length")
    @Getter @Setter protected String name;

    @Transient @JsonIgnore private String apiToken;

    private static final String[] VALUE_FIELDS = {};
    public UrkelProfile(UrkelProfile request) { update(request); setName(request.getName()); }
    @Override public void update(Identifiable thing) { copy(this, thing, VALUE_FIELDS); }

    @Override public String getApiToken() { return apiToken; }
    @Override public void setApiToken(String token) { this.apiToken = token; }

}
