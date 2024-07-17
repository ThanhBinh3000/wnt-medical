package vn.com.gsoft.medical.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.medical.constant.PathContains;
import vn.com.gsoft.medical.model.dto.MedicalFeeReceiptsReq;
import vn.com.gsoft.medical.model.system.BaseResponse;
import vn.com.gsoft.medical.service.MedicalFeeReceiptsService;
import vn.com.gsoft.medical.util.system.ResponseUtils;

import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping("/medical-fee-receipts")
public class MedicalFeeReceiptsController {
	
  @Autowired
  MedicalFeeReceiptsService service;


  @PostMapping(value = PathContains.URL_SEARCH_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(@RequestBody MedicalFeeReceiptsReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchPage(objReq)));
  }


  @PostMapping(value = PathContains.URL_SEARCH_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colectionList(@RequestBody MedicalFeeReceiptsReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchList(objReq)));
  }


  @PostMapping(value = PathContains.URL_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> insert(@Valid @RequestBody MedicalFeeReceiptsReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.create(objReq)));
  }


  @PostMapping(value = PathContains.URL_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> update(@Valid @RequestBody MedicalFeeReceiptsReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.update(objReq)));
  }


  @GetMapping(value = PathContains.URL_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.detail(id)));
  }


  @PostMapping(value = PathContains.URL_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody MedicalFeeReceiptsReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.delete(idSearchReq.getId())));
  }

  @PostMapping(value = PathContains.URL_DELETE_DATABASE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> deleteDatabase(@Valid @RequestBody MedicalFeeReceiptsReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.deleteForever(idSearchReq.getId())));
  }

  @PostMapping(value = PathContains.URL_UPDATE_STATUS_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> updStatusMulti(@Valid @RequestBody MedicalFeeReceiptsReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.updateMultiple(idSearchReq)));
  }

  @PostMapping(value = PathContains.URL_RESTORE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> restore(@Valid @RequestBody MedicalFeeReceiptsReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.restore(idSearchReq.getId())));
  }

  @GetMapping(value = "get-new-note-number", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> getNewNoteNumber() throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.getNewNoteNumber()));
  }

  @PostMapping(value = "get-list-customer-debt", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> getListCustomerDebt(@Valid @RequestBody MedicalFeeReceiptsReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.getListCustomerDebt(objReq.getIdCus(), objReq.getIsDisplayByNote())));
  }

  @PostMapping(value = PathContains.URL_PREVIEW, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> preview(@RequestBody HashMap<String, Object> body) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.preview(body)));
  }
}
