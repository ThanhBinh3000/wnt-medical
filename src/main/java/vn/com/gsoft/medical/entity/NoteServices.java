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
import java.util.List;

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
    private Long idCus;
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
    private Long idDoctor;
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
    private Long performerId;
    @Column(name = "TemplateDocument")
    private String templateDocument;
    @Column(name = "IsLock")
    private Boolean isLock;
    @Transient
    private String createdByUseText;
    @Transient
    private String performerText;
    @Transient
    private String customerName;
    @Transient
    private String customerAddress;
    @Transient
    private String customerPhoneNumber;
    @Transient
    private Integer customerAge;
    @Transient
    private String customerGender;
    @Transient
    private String customerEmail;
    @Transient
    private Date customeBirthDate;
    @Transient
    private KhachHangs customer;
    @Transient
    private String doctorName;
    @Transient
    private String doctorPhoneNumber;
    @Transient
    private List<NoteServiceDetails> chiTiets;
    @Transient
    private Thuocs dichVu;
    @Transient
    private Long drugId;
    @Transient
    private Integer countNumbers;
    @Transient
    private BigDecimal amount;
    @Transient
    private Integer lastCountNumbers;
    @Transient
    private BigDecimal retailOutPrice;
    @Transient
    private Boolean idStatus;
    @Transient
    private String tenThuoc;
    @Transient
    private String pharmacyName;
    @Transient
    private String pharmacyAddress;
    @Transient
    private String pharmacyPhoneNumber;
}

