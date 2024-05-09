package vn.com.gsoft.medical.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.medical.entity.KhachHangs;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class NoteServicesLieuTrinhRes {
    private Long id;
    private String storeCode;
    private Date noteDate;
    private Long idCus;
    private String tenDichVu;
    private Integer countNumbers;
    private BigDecimal amount;
    private Integer lastCountNumbers;
    private BigDecimal retailOutPrice;
}
