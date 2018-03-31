/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.sys.web;

import com.simbest.boot.base.repository.Condition;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.sys.model.SysCustomFieldValue;
import com.simbest.boot.sys.model.SysCustomFieldValueDto;
import com.simbest.boot.sys.service.ISysCustomFieldService;
import com.simbest.boot.sys.service.ISysCustomFieldValueService;
import com.simbest.boot.sys.service.ISysDictService;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用途：实体自定义字段值控制器
 * 作者: lishuyi
 * 时间: 2017/12/22  15:51
 */
@Slf4j
@RestController
@RequestMapping("/sys/sysfieldvalue")
public class SysCustomFieldValueController {

    @Autowired
    private ISysCustomFieldService fieldService;

    @Autowired
    private ISysCustomFieldValueService fieldValueService;


    @Autowired
    private ISysDictService dictService;

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping(value = "getEntityValues")
    public JsonResponse getEntityValues(@RequestParam(required = true) String fieldClassify, //
                                        @RequestParam(required = true) Long fieldEntityId) {
        Condition c = new Condition();
        c.eq("fieldClassify", fieldClassify);
        c.eq("fieldEntityId", fieldEntityId);
        return JsonResponse.builder() //
                .errcode(JsonResponse.SUCCESS_CODE) //
                .errmsg("查询成功！") //
                .data(fieldValueService.findAll(fieldValueService.getSpecification(c)))
                .build();
    }
//
//    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
//    @RequestMapping(value = "getById", method = RequestMethod.GET)
//    public ModelAndView getById(@RequestParam(required = false, defaultValue = "-1") final long id, Model model) {
//        Optional<SysCustomField> userOptional = fieldService.findById(id);
//        SysCustomField field = fieldService.findById(id).orElse(new SysCustomField());
//        model.addAttribute("field", field);
//        model.addAttribute("fieldClassifyMap", fieldService.getFieldClassifyMap());
//        model.addAttribute("dictList", dictService.findByEnabled(true));
//        SysCustomFieldType[] types = SysCustomFieldType.values();
//        Map<String,String> typeMap = Maps.newTreeMap();
//        for (SysCustomFieldType type : types){
//            typeMap.put(type.name(), type.getValue());
//        }
//        model.addAttribute("fieldTypes", typeMap);
//        return new ModelAndView("sys/sysfield/form", "fieldModel", model);
//    }



    /*      field.setCreator(SecurityUtils.getCurrentUserName());
            field.setCreatedTime(new Date());
            field.setModifier(SecurityUtils.getCurrentUserName());
            field.setModifiedTime(new Date());
            field.setEnabled(true);
            field.setRemoved(false);*/

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping(value = "/create")
    public JsonResponse create( @RequestBody SysCustomFieldValueDto fieldValues) {

        List<SysCustomFieldValue> fieldValuess = fieldValues.getSysfieldvalue();
        List<SysCustomFieldValue> fields =  new ArrayList<>(  );
        for(SysCustomFieldValue field  :fieldValuess){
            field.setCreator( SecurityUtils.getCurrentUserName());
            field.setCreatedTime(new Date());
            field.setModifier(SecurityUtils.getCurrentUserName());
            field.setModifiedTime(new Date());
            field.setEnabled(true);
            field.setRemoved(false);
            fields.add( field );
        }
          fieldValueService.saveAll(fields);
        return JsonResponse.defaultSuccessResponse();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
    @PostMapping(value = "/update")
    public JsonResponse update(@RequestBody SysCustomFieldValueDto fieldValues) {
        List<SysCustomFieldValue> fieldValuess = fieldValues.getSysfieldvalue();
        List<SysCustomFieldValue> fields =  new ArrayList<>(  );
        for(SysCustomFieldValue field  :fieldValuess){
            field.setModifier(SecurityUtils.getCurrentUserName());
            field.setModifiedTime(new Date());
            if(null == field.getId()){
                field.setCreator( SecurityUtils.getCurrentUserName());
                field.setCreatedTime(new Date());
                field.setEnabled(true);
                field.setRemoved(false);
            }
            fields.add( field );
        }
        fieldValueService.saveAll(fields);
        return JsonResponse.defaultSuccessResponse();
    }
//
//    @PreAuthorize("hasAnyAuthority('ROLE_SUPER','ROLE_ADMIN')")
//    @PostMapping(value = "/delete")
//    public JsonResponse delete(@RequestParam Long id, Model model) {
//        fieldService.deleteById(id);
//        return JsonResponse.defaultSuccessResponse();
//    }
//
//    @ApiOperation(value = "获取自定义字段列表", notes = "通过此接口来获取某实体类型自定义字段")
//    @ApiImplicitParams({ //
//            @ApiImplicitParam(name = "page", value = "当前页码", dataType = "int", paramType = "query", //
//                    required = true, example = "1"), //
//            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
//                    required = true, example = "10"), //
//    })
//    @PostMapping(value = "getSysCustomFieldsByFieldClassify")
//    public JsonResponse getSysCustomFieldsByFieldClassify(@RequestParam(required = false, defaultValue = "1") int page, //
//                                       @RequestParam(required = false, defaultValue = "10") int size, //
//                                       @RequestParam(required = true) String fieldClassify, //
//                                       @RequestParam(required = false, defaultValue = "-1") long fieldEntityId //
//    ) {
//        // 获取分页规则
//        Pageable pageable = fieldRepository.getPageable(page, size, null, null);
//
//        // 获取查询条件
//        Condition condition = new Condition();
//        condition.eq("fieldClassify", fieldClassify);
//
//        Specification<SysCustomField> s = fieldRepository.getSpecification(condition);
//
//        // 获取查询结果
//        Page<SysCustomField> pages = fieldRepository.findAll(s, pageable);
//
//        // 构成返回信息
//        Map<String, Object> searchD = new HashMap<>();
//        searchD.put("fieldClassify", fieldClassify);
//        SysCustomField param = new SysCustomField();
//        param.setFieldClassify(fieldClassify);
//        searchD.put("fieldClassifyCn", param.getFieldClassifyCn());
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("totalSize", pages.getTotalElements());
//        map.put("totalPage", pages.getTotalPages());
//        map.put("list", pages.getContent());
//        map.put("size", pages.getSize());
//        map.put("page", page);
//        map.put("searchD", searchD);
//
//        if (fieldEntityId != -1) {
//            Condition condition1 = new Condition();
//            condition1.eq("fieldClassify", fieldClassify);
//            condition1.eq("fieldEntityId", fieldEntityId);
//            Specification<SysCustomFieldValue> s1 = fieldValueService.getSpecification(condition1);
//            List<SysCustomFieldValue> values = fieldValueService.findAll(s1);
//            map.put("values", values);
//        }
//
//        JsonResponse res = JsonResponse.builder() //
//                .errcode(JsonResponse.SUCCESS_CODE) //
//                .errmsg("查询成功！") //
//                .data(map)
//                .build();
//        return res;
//    }
}