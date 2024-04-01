package vn.com.gsoft.medical.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.medical.entity.BaseEntity;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.util.Date;

@Data
@Table(name = "BacSies")
public class BacSiesReq extends BaseRequest {

    private Long id;

    @Column(name = "MaBacSy")
    private Integer maBacSy;
    @Column(name = "TenBacSy")
    private String tenBacSy;
    @Column(name = "DiaChi")
    private String diaChi;
    @Column(name = "DienThoai")
    private String dienThoai;
    @Column(name = "Email")
    private String email;
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc;
    @Column(name = "Created")
    private Date created;
    @Column(name = "Modified")
    private Date modified;
    @Column(name = "CreatedBy_UserId")
    private Integer createdByUserId;
    @Column(name = "ModifiedBy_UserId")
    private Integer modifiedByUserId;
    @Column(name = "Active")
    private Boolean active;
    @Column(name = "RecordStatusID")
    private Integer recordStatusID;
    @Column(name = "StoreId")
    private Integer storeId;
    @Column(name = "MasterId")
    private Integer masterId;
    @Column(name = "MetadataHash")
    private Integer metadataHash;
    @Column(name = "PreMetadataHash")
    private Integer preMetadataHash;
    @Column(name = "Code")
    private String code;
    @Column(name = "ConnectCode")
    private String connectCode;
    @Column(name = "ConnectPassword")
    private String connectPassword;
    @Column(name = "IsConnectivity")
    private Boolean isConnectivity;
    @Column(name = "ResultConnect")
    private String resultConnect;
    @Column(name = "MaNhomBacSy")
    private Integer maNhomBacSy;
}

