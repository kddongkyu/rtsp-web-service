package data.object.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BusResponseDto {
    private List<BusDto> busList;
    private String memberId;
    private String companyName;
}
