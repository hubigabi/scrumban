package pl.utp.scrumban.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {

    private String name;
    private String email;

    @NotEmpty
    private String confirmPassword;
}
