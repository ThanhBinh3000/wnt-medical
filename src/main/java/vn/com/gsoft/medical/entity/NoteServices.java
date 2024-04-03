package vn.com.gsoft.medical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NoteServices")
public class NoteServices extends BaseEntity {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NoteNumber")
    private Long noteNumber;
    @Column(name = "ClinicalExamination")
    private String clinicalExamination;
    @Column(name = "Object")
    private String object;
    @Column(name = "NoteDate")
    private Date noteDate;
    @Column(name = "IdCus")
    private Integer idCus;
    @Column(name = "StoreCode")
    private String storeCode;
    @Column(name = "TotalMoney")
    private BigDecimal totalMoney;
    @Lob
    @Column(name = "Description")
    private String description;
    @Column(name = "IsModified")
    private Boolean isModified;
    @Column(name = "IdDoctor")
    private Integer idDoctor;
    @Column(name = "BarCode")
    private String barCode;
    @Column(name = "IsDeb")
    private Boolean isDeb;
    @Column(name = "IdNoteMedical")
    private Integer idNoteMedical;
    @Column(name = "Score")
    private BigDecimal score;
    @Column(name = "PreScore")
    private BigDecimal preScore;
    @Column(name = "PaymentScore")
    private BigDecimal paymentScore;
    @Column(name = "PaymentScoreAmount")
    private BigDecimal paymentScoreAmount;
    @Column(name = "PerformerId")
    private Integer performerId;
    @Column(name = "TemplateDocument")
    private String templateDocument;
    @Column(name = "IsLock")
    private Boolean isLock;
}

