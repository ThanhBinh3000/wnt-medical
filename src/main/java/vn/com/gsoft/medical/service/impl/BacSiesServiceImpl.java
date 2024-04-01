package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.entity.BacSies;
import vn.com.gsoft.medical.model.dto.BacSiesReq;
import vn.com.gsoft.medical.repository.BacSiesRepository;
import vn.com.gsoft.medical.service.BacSiesService;


@Service
@Log4j2
public class BacSiesServiceImpl extends BaseServiceImpl<BacSies, BacSiesReq,Long> implements BacSiesService {

	private BacSiesRepository hdrRepo;
	@Autowired
	public BacSiesServiceImpl(BacSiesRepository hdrRepo) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
	}

}
