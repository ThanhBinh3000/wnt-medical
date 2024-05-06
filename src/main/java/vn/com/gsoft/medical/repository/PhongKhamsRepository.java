package vn.com.gsoft.medical.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.gsoft.medical.entity.PhongKhams;
import vn.com.gsoft.medical.model.dto.PhongKhamsReq;
import vn.com.gsoft.medical.repository.BaseRepository;

import java.util.List;

@Repository
public interface PhongKhamsRepository extends BaseRepository<PhongKhams, PhongKhamsReq, Long> {
  @Query("SELECT c FROM PhongKhams c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.maPhongKham} IS NULL OR lower(c.maPhongKham) LIKE lower(concat('%',CONCAT(:#{#param.maPhongKham},'%'))))"
          + " AND (:#{#param.tenPhongKham} IS NULL OR lower(c.tenPhongKham) LIKE lower(concat('%',CONCAT(:#{#param.tenPhongKham},'%'))))"
//          + " AND (:#{#param.created} IS NULL OR c.created >= :#{#param.createdFrom}) "
//          + " AND (:#{#param.created} IS NULL OR c.created <= :#{#param.createdTo}) "
//          + " AND (:#{#param.modified} IS NULL OR c.modified >= :#{#param.modifiedFrom}) "
//          + " AND (:#{#param.modified} IS NULL OR c.modified <= :#{#param.modifiedTo}) "
//          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
//          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedByUserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.maNhaThuoc} IS NULL OR lower(c.maNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.maNhaThuoc},'%'))))"
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.description} IS NULL OR lower(c.description) LIKE lower(concat('%',CONCAT(:#{#param.description},'%'))))"
          + " ORDER BY c.id desc"
  )
  Page<PhongKhams> searchPage(@Param("param") PhongKhamsReq param, Pageable pageable);
  
  
  @Query("SELECT c FROM PhongKhams c " +
         "WHERE 1=1 "
          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
          + " AND (:#{#param.maPhongKham} IS NULL OR lower(c.maPhongKham) LIKE lower(concat('%',CONCAT(:#{#param.maPhongKham},'%'))))"
          + " AND (:#{#param.tenPhongKham} IS NULL OR lower(c.tenPhongKham) LIKE lower(concat('%',CONCAT(:#{#param.tenPhongKham},'%'))))"
//          + " AND (:#{#param.created} IS NULL OR c.created >= :#{#param.createdFrom}) "
//          + " AND (:#{#param.created} IS NULL OR c.created <= :#{#param.createdTo}) "
//          + " AND (:#{#param.modified} IS NULL OR c.modified >= :#{#param.modifiedFrom}) "
//          + " AND (:#{#param.modified} IS NULL OR c.modified <= :#{#param.modifiedTo}) "
//          + " AND (:#{#param.createdByUserId} IS NULL OR c.createdByUserId = :#{#param.createdByUserId}) "
//          + " AND (:#{#param.modifiedByUserId} IS NULL OR c.modifiedByUserId = :#{#param.modifiedByUserId}) "
          + " AND (:#{#param.maNhaThuoc} IS NULL OR lower(c.maNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.maNhaThuoc},'%'))))"
          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
          + " AND (:#{#param.description} IS NULL OR lower(c.description) LIKE lower(concat('%',CONCAT(:#{#param.description},'%'))))"
          + " ORDER BY c.id desc"
  )
  List<PhongKhams> searchList(@Param("param") PhongKhamsReq param);

}
