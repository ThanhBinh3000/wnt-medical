package vn.com.gsoft.medical.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.ESDiagnose;
import vn.com.gsoft.medical.model.dto.ESDiagnoseReq;

import java.util.List;

@Repository
public interface ESDiagnoseRepository extends CrudRepository<ESDiagnose, Long> {
    @Query("SELECT c FROM ESDiagnose c " +
            "WHERE 1=1 "
            + " AND ((:#{#param.textSearch} IS NULL OR lower(c.maChanDoan) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.tenChanDoan) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.ketLuan) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%')))))"
            + " ORDER BY c.id desc"
    )
    List<ESDiagnose> searchList(@Param("param") ESDiagnoseReq param);

    @Query("SELECT c FROM ESDiagnose c " +
            "WHERE 1=1 "
            + " AND ((:#{#param.textSearch} IS NULL OR lower(c.maChanDoan) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.tenChanDoan) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.ketLuan) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%')))))"
            + " ORDER BY c.id desc"
    )
    Page<ESDiagnose> searchPage(@Param("param") ESDiagnoseReq param, Pageable pageable);

    List<ESDiagnose> findByIdIn(List<Long> ids);
}