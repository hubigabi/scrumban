package pl.utp.scrumban.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {

    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
