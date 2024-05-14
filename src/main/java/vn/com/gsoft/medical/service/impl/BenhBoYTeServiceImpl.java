package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.entity.BenhBoYTe;
import vn.com.gsoft.medical.model.dto.BenhBoYTeReq;
import vn.com.gsoft.medical.repository.BenhBoYTeRepository;
import vn.com.gsoft.medical.service.BenhBoYTeService;

@Service
@Log4j2
public class BenhBoYTeServiceImpl extends BaseServiceImpl<BenhBoYTe, BenhBoYTeReq,Long> implements BenhBoYTeService {

	private BenhBoYTeRepository hdrRepo;

	@Autowired
	public BenhBoYTeServiceImpl(BenhBoYTeRepository hdrRepo) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
	}
}
