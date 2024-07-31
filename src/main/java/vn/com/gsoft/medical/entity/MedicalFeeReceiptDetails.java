package vn.com.gsoft.medical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MedicalFeeReceiptDetails")
public class MedicalFeeReceiptDetails extends BaseEntity{
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NoteId")
    private Long noteId;
    @Column(name = "IdBill")
    private Long idBill;
    @Column(name = "StoreCode")
    private String storeCode;
    @Column(name = "TypeNote")
    private Long typeNote;
    @Column(name = "NoteNumber")
    private Long noteNumber;
    @Transient
    private String examinationContent;
    @Transient
    private String extraInfo;
    @Transient
    private String clinicName;
    @Transient
    private String unit;
    @Transient
    private BigDecimal amount;
    @Transient
    private BigDecimal price;
    @Transient
    private BigDecimal totalMoney;
}

