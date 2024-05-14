package vn.com.gsoft.medical.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.gsoft.medical.constant.PathContains;
import vn.com.gsoft.medical.model.dto.BenhBoYTeReq;
import vn.com.gsoft.medical.model.system.BaseResponse;
import vn.com.gsoft.medical.service.BenhBoYTeService;
import vn.com.gsoft.medical.util.system.ResponseUtils;


@Slf4j
@RestController
@RequestMapping(PathContains.URL_BENH_BO_Y_TE)
public class BenhBoYTeController {
	
  @Autowired
  BenhBoYTeService service;

  @PostMapping(value = PathContains.URL_SEARCH_PAGE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colection(@RequestBody BenhBoYTeReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchPage(objReq)));
  }


  @PostMapping(value = PathContains.URL_SEARCH_LIST, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BaseResponse> colectionList(@RequestBody BenhBoYTeReq objReq) throws Exception {
    return ResponseEntity.ok(ResponseUtils.ok(service.searchList(objReq)));
  }

}
