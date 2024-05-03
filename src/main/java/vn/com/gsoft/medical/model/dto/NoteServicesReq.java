package vn.com.gsoft.medical.model.dto;

import lombok.Data;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NoteServicesReq extends BaseRequest {

    private Long noteNumber;
    private String clinicalExamination;
    private String object;
    private Date noteDate;
    private Integer idCus;
    private String storeCode;
    private BigDecimal totalMoney;
    private String description;
    private Boolean isModified;
    private Integer idDoctor;
    private String barCode;
    private Boolean isDeb;
    private Integer idNoteMedical;
    private BigDecimal score;
    private BigDecimal preScore;
    private BigDecimal paymentScore;
    private BigDecimal paymentScoreAmount;
    private Integer performerId;
    private String templateDocument;
    private Boolean isLock;
}

