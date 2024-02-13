package data.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
    private String managerName;
    private String managerEmail;
    private String managerPhonenumber;
    private String companyName;
    private String companyNumber;
    private String password;
}
