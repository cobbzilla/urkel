package urkel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor @Accessors(chain=true)
public class UrkelCommandStatus {

    @Getter @Setter private int status;
    @Getter @Setter private String stdout;
    @Getter @Setter private String stderr;

}
