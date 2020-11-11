package pl.utp.scrumban.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {

    private String name;
    private String email;
    private String confirmPassword;
}
