package data.object.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name="member")
@AllArgsConstructor
@NoArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId; // 타입을 Long으로 변경
    private String password;
    private String companyName;
    private String companyNumber;
    private String managerName;
    private String managerPhonenumber;
    private String managerEmail;
    private int individualPort;

    public MemberEntity(String managerEmail, String password, String companyName, String managerPhonenumber, String companyNumber, String managerName) {
        this.managerName = managerName;
        this.managerEmail = managerEmail;
        this.managerPhonenumber = managerPhonenumber;
        this.companyName = companyName;
        this.companyNumber = companyNumber;
        this.password = password;
    }
}
