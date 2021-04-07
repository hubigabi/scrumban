package pl.utp.scrumban.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnDto {

    private Long id;
    private String name;
    private String description;
    private Boolean isWIP;
    private Integer numberWIP;
    private Integer numberOrder;
}

