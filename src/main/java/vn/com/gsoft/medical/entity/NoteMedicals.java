package vn.com.gsoft.medical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NoteMedicals")
public class NoteMedicals extends BaseEntity {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NoteNumber")
    private Long noteNumber;
    @Column(name = "IdPatient")
    private Long idPatient;
    @Column(name = "IdDoctor")
    private Long idDoctor;
    @Column(name = "NoteDate")
    private Date noteDate;
    @Column(name = "IncludingDiseases")
    private String includingDiseases;
    @Column(name = "DrugAllergy")
    private String drugAllergy;
    @Column(name = "Diagnostic")
    private String diagnostic;
    @Column(name = "Conclude")
    private String conclude;
    @Column(name = "StoreCode")
    private String storeCode;
    @Column(name = "ReexaminationDate")
    private Date reexaminationDate;
    @Column(name = "TotalMoney")
    private BigDecimal totalMoney;
    @Column(name = "SickCondition")
    private Integer sickCondition;
    @Column(name = "ClinicalExamination")
    private String clinicalExamination;
    @Column(name = "IsDeb")
    private Boolean isDeb;
    @Column(name = "Heartbeat")
    private String heartbeat;
    @Column(name = "Temperature")
    private String temperature;
    @Column(name = "Weight")
    private String weight;
    @Column(name = "BloodPressure")
    private String bloodPressure;
    @Column(name = "Breathing")
    private String breathing;
    @Column(name = "Height")
    private String height;
    @Column(name = "ClinicCode")
    private Integer clinicCode;
    @Column(name = "StatusNote")
    private Integer statusNote;
    @Column(name = "OrderWait")
    private Integer orderWait;
    @Column(name = "ReasonExamination")
    private String reasonExamination;
    @Column(name = "IdServiceExam")
    private Long idServiceExam;
    @Column(name = "IdDiagnostic")
    private Integer idDiagnostic;
    @Column(name = "TestResults")
    private String testResults;
    @Column(name = "DiagnosticIds")
    private String diagnosticIds;
    @Column(name = "DiagnosticOther")
    private String diagnosticOther;
    @Column(name = "IsLock")
    private Boolean isLock;
    @Transient
    private String createdByUseText;
    @Transient
    private String patientName;
    @Transient
    private String customerAddress;
    @Transient
    private String customerPhoneNumber;
    @Transient
    private String customerEmail;
    @Transient
    private String customerGender;
    @Transient
    private Integer customerAge;
    @Transient
    private KhachHangs customer;
    @Transient
    private String doctorName;
    @Transient
    private String TitleLamSang;
    @Transient
    private String nameCilinic;
    @Transient
    private String pharmacyName;
    @Transient
    private String pharmacyAddress;
    @Transient
    private String pharmacyPhoneNumber;
    @Transient
    private List<BenhBoYTe> diagnostics;
    @Transient
    private List<SampleNoteDetail> lichSuKeDons;
}

