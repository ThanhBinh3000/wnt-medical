package vn.com.gsoft.medical.model.dto;

import lombok.Data;
import vn.com.gsoft.medical.model.system.BaseRequest;

import java.util.Date;

@Data
public class BacSiesReq extends BaseRequest {

    private String tenBacSy;
    private String diaChi;
    private String dienThoai;
    private String email;
    private String maNhaThuoc;
    private Boolean active;
    private Integer storeId;
    private Integer masterId;
    private Integer metadataHash;
    private Integer preMetadataHash;
    private String code;
    private String connectCode;
    private String connectPassword;
    private Boolean isConnectivity;
    private String resultConnect;
    private Long maNhomBacSy;

    private Boolean dataDelete;
}

