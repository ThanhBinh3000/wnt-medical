package vn.com.gsoft.medical.model.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.medical.entity.BaseEntity;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class NoteMedicalsReq extends BaseRequest {

    private Integer noteNumber;
    private Long idPatient;
    private Long idDoctor;
    private Date noteDate;
    private String includingDiseases;
    private String drugAllergy;
    private String diagnostic;
    private String conclude;
    private String storeCode;
    private Date reexaminationDate;
    private BigDecimal totalMoney;
    private Integer sickCondition;
    private String clinicalExamination;
    private Boolean isDeb;
    private String heartbeat;
    private String temperature;
    private String weight;
    private String bloodPressure;
    private String breathing;
    private String height;
    private Integer clinicCode;
    private Integer statusNote;
    private Integer orderWait;
    private String reasonExamination;
    private Integer idServiceExam;
    private Integer idDiagnostic;
    private String testResults;
    private String diagnosticIds;
    private String diagnosticOther;
    private Boolean isLock;
}

