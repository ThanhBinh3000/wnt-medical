package vn.com.gsoft.medical.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.medical.entity.BenhBoYTe;
import vn.com.gsoft.medical.entity.ESDiagnose;
import vn.com.gsoft.medical.model.dto.BenhBoYTeReq;

import java.util.List;

@Repository
public interface BenhBoYTeRepository extends BaseRepository<BenhBoYTe, BenhBoYTeReq, Long> {

    @Query("SELECT c FROM BenhBoYTe c " +
            "WHERE 1=1 "
            + " AND ((:#{#param.textSearch} IS NULL OR lower(c.name) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.code) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.description) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%')))))"
            + " ORDER BY c.id desc"
    )
    Page<BenhBoYTe> searchPage(@Param("param") BenhBoYTeReq param, Pageable pageable);

    @Query("SELECT c FROM BenhBoYTe c " +
            "WHERE 1=1 "
            + " AND ((:#{#param.textSearch} IS NULL OR lower(c.name) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.code) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%'))))"
            + " OR (:#{#param.textSearch} IS NULL OR lower(c.description) LIKE lower(concat('%',CONCAT(:#{#param.textSearch},'%')))))"
            + " ORDER BY c.id desc"
    )
    List<BenhBoYTe> searchList(@Param("param") BenhBoYTeReq param);

    List<BenhBoYTe> findByIdIn(List<Long> ids);
}