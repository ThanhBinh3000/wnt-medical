package vn.com.gsoft.medical.model.dto;

import lombok.Data;
import vn.com.gsoft.medical.model.system.BaseRequest;

@Data
public class NhomThuocsReq extends BaseRequest {

    private String tenNhomThuoc;
    private String kyHieuNhomThuoc;
    private String maNhaThuoc;
    private Boolean active;
    private Boolean referenceId;
    private Long archivedId;
    private Long storeId;
    private Long typeGroupProduct;
}
