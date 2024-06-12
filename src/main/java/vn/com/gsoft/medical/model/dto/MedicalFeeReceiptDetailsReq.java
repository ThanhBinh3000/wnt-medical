package vn.com.gsoft.medical.model.dto;

import jakarta.persistence.Column;
import lombok.Data;
import vn.com.gsoft.medical.entity.MedicalFeeReceiptDetails;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MedicalFeeReceiptDetailsReq extends BaseRequest {
    private Long noteId;
    private Long idBill;
    private String storeCode;
    private Long typeNote;
    private Long noteNumber;
}
