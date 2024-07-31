package vn.com.gsoft.medical.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.medical.entity.KhachHangs;
import vn.com.gsoft.medical.entity.MedicalFeeReceiptDetails;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class MedicalFeeReceiptsCustomerDebtRes {
    private Long noteId;
    private Long typeNote;
    private String typeNoteText;
    private Long noteNumber;
    private Date noteDate;
    private String noteServiceText;
    private BigDecimal soLan = BigDecimal.ZERO;
    private BigDecimal gia = BigDecimal.ZERO;
    private BigDecimal thanhTien = soLan.multiply(gia);
    private KhachHangs khachHang;

    private Boolean isFirstItem;
    private BigDecimal thanhTienGroup = BigDecimal.ZERO;
    private List<MedicalFeeReceiptsCustomerDebtRes> chiTiets;
}
