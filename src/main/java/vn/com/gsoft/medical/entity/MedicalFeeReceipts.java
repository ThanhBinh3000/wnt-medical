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
@Table(name = "MedicalFeeReceipts")
public class MedicalFeeReceipts extends BaseEntity{
    @Id
    @Column(name = "Id")
    private Long id;
    @Column(name = "NoteDate")
    private Date noteDate;
    @Column(name = "TotalMoney")
    private BigDecimal totalMoney;
    @Column(name = "StoreCode")
    private String storeCode;
    @Column(name = "Discount")
    private BigDecimal discount;
    @Column(name = "NoteNumber")
    private Integer noteNumber;
    @Column(name = "Description")
    private String description;
    @Column(name = "DescriptNotePay")
    private String descriptNotePay;
    @Column(name = "DebtAmount")
    private BigDecimal debtAmount;
    @Column(name = "IdCus")
    private Long idCus;
    @Column(name = "TypePayment")
    private Integer typePayment;

    @Transient
    private String customerName;
}

