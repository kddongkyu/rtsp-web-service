package data.object.entity;

import data.object.dto.BusDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.type.TypeHelper;

@Data
@Entity(name = "Bus")
@AllArgsConstructor
@NoArgsConstructor
public class BusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;

    @ManyToOne
    @JoinColumn(name = "member_id") // 'bus_id'에서 'member_id'로 변경
    private MemberEntity member;

    public BusEntity(String name,String url, MemberEntity member) {
        this.name = name;
        this.url = url;
        this.member = member;
    }
}
