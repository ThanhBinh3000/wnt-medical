package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.entity.NoteMedicals;
import vn.com.gsoft.medical.model.dto.NoteMedicalsReq;
import vn.com.gsoft.medical.repository.NoteMedicalsRepository;
import vn.com.gsoft.medical.service.NoteMedicalsService;


@Service
@Log4j2
public class NoteMedicalsServiceImpl extends BaseServiceImpl<NoteMedicals, NoteMedicalsReq,Long> implements NoteMedicalsService {

	private NoteMedicalsRepository hdrRepo;
	@Autowired
	public NoteMedicalsServiceImpl(NoteMedicalsRepository hdrRepo) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
	}

}
