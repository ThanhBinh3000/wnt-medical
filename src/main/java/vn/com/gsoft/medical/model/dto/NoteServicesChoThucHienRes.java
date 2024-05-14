package vn.com.gsoft.medical.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class NoteServicesChoThucHienRes {
    private Long id;
    private String storeCode;
    private Date noteDate;
    private Long noteNumber;
    private Long idCus;
    private Long idDoctor;
    private Long performerId;
    private Boolean isDeb;
    private Boolean idStatus;
    private Long drugId;
}
