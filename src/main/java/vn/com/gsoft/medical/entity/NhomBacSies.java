package vn.com.gsoft.medical.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NhomBacSies")
public class NhomBacSies extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String tenNhomBacSy;
    private String ghiChu;
    private String maNhaThuoc;


}
