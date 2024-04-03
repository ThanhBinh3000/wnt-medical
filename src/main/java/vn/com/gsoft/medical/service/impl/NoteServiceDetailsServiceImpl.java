package vn.com.gsoft.medical.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.gsoft.medical.entity.NoteServiceDetails;
import vn.com.gsoft.medical.model.dto.NoteServiceDetailsReq;
import vn.com.gsoft.medical.repository.NoteServiceDetailsRepository;
import vn.com.gsoft.medical.service.NoteServiceDetailsService;


@Service
@Log4j2
public class NoteServiceDetailsServiceImpl extends BaseServiceImpl<NoteServiceDetails, NoteServiceDetailsReq,Long> implements NoteServiceDetailsService {

	private NoteServiceDetailsRepository hdrRepo;
	@Autowired
	public NoteServiceDetailsServiceImpl(NoteServiceDetailsRepository hdrRepo) {
		super(hdrRepo);
		this.hdrRepo = hdrRepo;
	}

}
