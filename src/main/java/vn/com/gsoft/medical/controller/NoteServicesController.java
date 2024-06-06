package vn.com.gsoft.medical.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.medical.constant.PathContains;
import vn.com.gsoft.medical.model.dto.NoteServicesReq;
import vn.com.gsoft.medical.model.system.BaseResponse;
import vn.com.gsoft.medical.service.NoteServicesService;
import vn.com.gsoft.medical.util.system.ResponseUtils;


@Slf4j
@RestController
@RequestMapping(PathContains.URL_PHIEU_DICH_VU)
public class NoteServicesController {
	
  @Autowired
  NoteServicesService service;


  @PostMapping(value = PathContains.URL_SEARCH_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(@RequestBody NoteServicesReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchPage(objReq)));
  }

  @PostMapping(value = PathContains.URL_SEARCH_PAGE+"-lieu-trinh", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colectionLieuTrinh(@RequestBody NoteServicesReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchPageLieuTrinh(objReq)));
  }

  @PostMapping(value = PathContains.URL_SEARCH_PAGE+"-cho-thuc-hien", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colectionChoThucHien(@RequestBody NoteServicesReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchPageChoThucHien(objReq)));
  }

  @PostMapping(value = PathContains.URL_SEARCH_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colectionList(@RequestBody NoteServicesReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchList(objReq)));
  }


  @PostMapping(value = PathContains.URL_CREATE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> insert(@Valid @RequestBody NoteServicesReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.create(objReq)));
  }

  @PostMapping(value = PathContains.URL_INIT, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> init(@Valid @RequestBody NoteServicesReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.init(objReq)));
  }

  @GetMapping(value = PathContains.URL_BAR_CODE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> generateBarCode() throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.generateBarCode()));
  }


  @PostMapping(value = PathContains.URL_UPDATE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<BaseResponse> update(@Valid @RequestBody NoteServicesReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.update(objReq)));
  }


  @GetMapping(value = PathContains.URL_DETAIL, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> detail(@PathVariable("id") Long id) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.detail(id)));
  }


  @PostMapping(value = PathContains.URL_DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> delete(@Valid @RequestBody NoteServicesReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.delete(idSearchReq.getId())));
  }

  @PostMapping(value = PathContains.URL_LOCK, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> lock(@Valid @RequestBody NoteServicesReq idSearchReq) throws Exception {
    service.lockNoteService(idSearchReq);
    return ResponseEntity.ok(ResponseUtils.ok(true));
  }


  @PostMapping(value = PathContains.URL_DELETE_DATABASE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> deleteDatabase(@Valid @RequestBody NoteServicesReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.deleteForever(idSearchReq.getId())));
  }

  @PostMapping(value = PathContains.URL_UPDATE_STATUS_MULTI, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> updStatusMulti(@Valid @RequestBody NoteServicesReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.updateMultiple(idSearchReq)));
  }

  @PostMapping(value = PathContains.URL_RESTORE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> restore(@Valid @RequestBody NoteServicesReq idSearchReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.restore(idSearchReq.getId())));
  }

  @PostMapping(value = PathContains.URL_SEARCH_LIST+"-by-customer", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> searchByCustomer(@Valid @RequestBody NoteServicesReq req) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchByCustomer(req)));
  }
}
