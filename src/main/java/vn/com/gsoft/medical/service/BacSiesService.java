package vn.com.gsoft.medical.service;


import org.springframework.web.multipart.MultipartFile;
import vn.com.gsoft.medical.entity.BacSies;
import vn.com.gsoft.medical.entity.Process;
import vn.com.gsoft.medical.model.dto.BacSiesReq;

import java.util.List;

public interface BacSiesService extends BaseService<BacSies, BacSiesReq, Long> {

    Process importExcel(MultipartFile file) throws Exception;


}