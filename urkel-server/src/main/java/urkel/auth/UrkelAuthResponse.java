package urkel.auth;

import lombok.NoArgsConstructor;
import org.cobbzilla.wizard.auth.AuthResponse;
import urkel.model.UrkelProfile;

@NoArgsConstructor
public class UrkelAuthResponse extends AuthResponse<UrkelProfile> {

    public UrkelAuthResponse(String token, UrkelProfile profile) {
        super(token, profile);
    }

}
