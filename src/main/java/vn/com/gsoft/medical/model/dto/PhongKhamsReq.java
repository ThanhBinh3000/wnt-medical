package vn.com.gsoft.medical.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.medical.entity.BaseEntity;
import vn.com.gsoft.medical.model.system.BaseRequest;

@Data
public class PhongKhamsReq extends BaseRequest {

    private String maPhongKham;
    private String tenPhongKham;
    private String maNhaThuoc;
    private Integer recordStatusId;
    private String description;
}

