package vn.com.gsoft.medical.model.dto;

import lombok.Data;
import vn.com.gsoft.medical.entity.SampleNoteDetail;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class SampleNoteReq extends BaseRequest {
    private String noteName;
    private String barcode;
    private String description;
    private Date createdDateTime;
    private Long createdByUserID;
    private Date modifiedDateTime;
    private Long modifiedByUserID;
    private String drugStoreID;
    private Long storeId;
    private BigDecimal amount;
    private Long patientId;
    private Long doctorId;
    private String doctorComments;
    private Date noteDate;
    private Long idExamination;
    private String typeId;
    private Boolean isConnect;
    private String resultConnect;
    private String codeConnect;
    private Integer statusConnect;
    private Integer noteNumber;
    private Date connectDate;
    private String noteCheckSum;
    private Integer formOfTreatment;
    private Integer typeSampleNote;
    private Long referenceId;

    private List<SampleNoteDetail> chiTiets;
}
