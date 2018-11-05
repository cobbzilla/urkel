package urkel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.cobbzilla.wizard.model.Identifiable;
import org.cobbzilla.wizard.model.NamedIdentityBase;
import org.cobbzilla.wizard.model.entityconfig.EntityFieldConfig;
import org.cobbzilla.wizard.model.entityconfig.annotations.ECType;
import org.cobbzilla.wizard.model.entityconfig.annotations.ECTypeFields;
import org.cobbzilla.wizard.model.entityconfig.annotations.ECTypeURIs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;
import static org.cobbzilla.util.json.JsonUtil.json;
import static org.cobbzilla.util.reflect.ReflectionUtil.copy;

@ECType(root=true)
@ECTypeURIs(listFields={"name"})
@ECTypeFields(list={"name", "description"})
@Entity @NoArgsConstructor @Accessors(chain=true)
public class UrkelCommandDriver extends NamedIdentityBase {

    public static final String[] VALUE_FIELDS = {"description", "script", "inParamsJson", "outParamsJson"};

    public UrkelCommandDriver(UrkelCommandDriver request) { update((Identifiable) request); setName(request.getName()); }
    @Override public void update(Identifiable request) { copy(this, request, VALUE_FIELDS); }

    @Column(length=1000)
    @Size(max=1000, message="err.description.length")
    @Getter @Setter protected String description;

    @Column(length=10_000)
    @Size(max=10_000, message="err.script.length")
    @Getter @Setter protected String script;

    @Column(length=10_000)
    @Size(max=10_000, message="err.inParamsJson.length")
    @JsonIgnore @Getter @Setter protected String inParamsJson;

    @Transient public EntityFieldConfig[] getInParams() { return json(inParamsJson, EntityFieldConfig[].class); }
    public UrkelCommandDriver setInParams(EntityFieldConfig[] params) { return setInParamsJson(empty(params) ? null : json(params)); }
    public boolean hasInParams () { return !empty(getInParams()); }

    @Column(length=10_000)
    @Size(max=10_000, message="err.outParamsJson.length")
    @JsonIgnore @Getter @Setter protected String outParamsJson;

    @Transient public EntityFieldConfig[] getOutParams() { return json(outParamsJson, EntityFieldConfig[].class); }
    public UrkelCommandDriver setOutParams(EntityFieldConfig[] params) { return setOutParamsJson(empty(params) ? null : json(params)); }
    public boolean hasOutParams () { return !empty(getOutParams()); }
}
