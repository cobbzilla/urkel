package urkel.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.cobbzilla.util.collection.NameAndValue;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

@NoArgsConstructor @AllArgsConstructor @Accessors(chain=true) @EqualsAndHashCode
public class UrkelCommand {

    @Getter @Setter private UrkelProfile profile;
    @Getter @Setter private UrkelCommandDriver template;
    @Getter @Setter private NameAndValue[] params;
    public boolean hasParams () { return !empty(params); }

}
