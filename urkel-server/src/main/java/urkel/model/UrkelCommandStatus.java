package urkel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor @Accessors(chain=true)
public class UrkelCommandStatus {

    public UrkelCommandStatus (String id) { this.id= id; }

    public UrkelCommandStatus(String id, String output) {
        this.id = id;
        this.stdout = output;
        this.status = 0;
    }

    @Getter @Setter private String id;
    @Getter @Setter private Integer status;
    @Getter @Setter private String stdout;
    @Getter @Setter private String stderr;

}
