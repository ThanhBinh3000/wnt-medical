package vn.com.gsoft.medical.model.dto;

import lombok.Data;
import vn.com.gsoft.medical.entity.MedicalFeeReceiptDetails;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MedicalFeeReceiptsReq extends BaseRequest {
    private Date noteDate;
    private BigDecimal totalMoney;
    private String storeCode;
    private BigDecimal discount;
    private Integer noteNumber;
    private String description;
    private String descriptNotePay;
    private BigDecimal debtAmount;
    private Long idCus;
    private Integer typePayment;
    private List<MedicalFeeReceiptDetails> chiTiets = new ArrayList<>();
    private Boolean isDisplayByNote;

    private Long idMedical;
}
