package org.jeecg.modules.system.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.Filterelement;
import org.jeecg.modules.system.entity.FilterelementReplace;
import org.jeecg.modules.system.entity.Relationship;
import org.jeecg.modules.system.service.EquipmentDbService;
import org.jeecg.modules.system.service.IFilterelementReplaceService;
import org.jeecg.modules.system.vo.EquipmentVO;
import org.jeecg.modules.system.vo.RelationshipVO;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 滤芯安装记录表
 * @Author: jeecg-boot
 * @Date:   2019-07-12
 * @Version: V1.0
 */
@Slf4j
@Api(tags="滤芯安装记录表")
@RestController
@RequestMapping("/dome/filterelementReplace")
public class FilterelementReplaceController {
	@Autowired
	private IFilterelementReplaceService filterelementReplaceService;


     @AutoLog(value = "滤芯更换")
     @ApiOperation(value="滤芯更换", notes="滤芯更换")
     @PutMapping(value = "/relation")
     public Result<RelationshipVO> EquipmentDb(@RequestBody RelationshipVO filterelementReplace){
         Result<RelationshipVO> result = new Result<RelationshipVO>();
         LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
         filterelementReplace.setUpdateBy(sysUser.getRealname());
         boolean ok = filterelementReplaceService.UpdfiletereMent(filterelementReplace.getUpdateBy(),filterelementReplace.getRecordId());
         if (ok) {
			 List<EquipmentVO> equipmentID= filterelementReplaceService.UpdZT();
            //判断是否有异常设备id
            if (equipmentID.size()==0){//id为空，修改设备状态
                //通過滤芯记录id得到设备id
                filterelementReplaceService.UpdZC(filterelementReplaceService.findbyid(filterelementReplace.getRecordId()));
            }
             result.success("更换成功!");
         }else{
             result.error500("更换失败!");
         }
         return result;
     }
	
	/**
	  * 分页列表查询
	 * @param filterelementReplace
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "滤芯安装记录表-分页列表查询")
	@ApiOperation(value="滤芯安装记录表-分页列表查询", notes="滤芯安装记录表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<FilterelementReplace>> queryPageList(FilterelementReplace filterelementReplace,
                                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                             HttpServletRequest req) {
		Result<IPage<FilterelementReplace>> result = new Result<IPage<FilterelementReplace>>();
		QueryWrapper<FilterelementReplace> queryWrapper = QueryGenerator.initQueryWrapper(filterelementReplace, req.getParameterMap());
		Page<FilterelementReplace> page = new Page<FilterelementReplace>(pageNo, pageSize);
		IPage<FilterelementReplace> pageList = filterelementReplaceService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	/**
	  *   添加
	 * @param filterelementReplace
	 * @return
	 */
	@AutoLog(value = "滤芯安装记录表-添加")
	@ApiOperation(value="滤芯安装记录表-添加", notes="滤芯安装记录表-添加")
	@PostMapping(value = "/add")
	public Result<FilterelementReplace> add(@RequestBody FilterelementReplace filterelementReplace) {
		Result<FilterelementReplace> result = new Result<FilterelementReplace>();
		try {
			filterelementReplaceService.save(filterelementReplace);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param filterelementReplace
	 * @return
	 */
	@AutoLog(value = "滤芯安装记录表-编辑")
	@ApiOperation(value="滤芯安装记录表-编辑", notes="滤芯安装记录表-编辑")
	@PutMapping(value = "/edit")
	public Result<FilterelementReplace> edit(@RequestBody FilterelementReplace filterelementReplace) {
		Result<FilterelementReplace> result = new Result<FilterelementReplace>();
		FilterelementReplace filterelementReplaceEntity = filterelementReplaceService.getById(filterelementReplace.getRecordId());
		if(filterelementReplaceEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = filterelementReplaceService.updateById(filterelementReplace);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@AutoLog(value = "滤芯安装记录表-通过id删除")
	@ApiOperation(value="滤芯安装记录表-通过id删除", notes="滤芯安装记录表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<FilterelementReplace> delete(@RequestParam(name="id",required=true) String id) {
		Result<FilterelementReplace> result = new Result<FilterelementReplace>();
		FilterelementReplace filterelementReplace = filterelementReplaceService.getById(id);
		if(filterelementReplace==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = filterelementReplaceService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "滤芯安装记录表-批量删除")
	@ApiOperation(value="滤芯安装记录表-批量删除", notes="滤芯安装记录表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<FilterelementReplace> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<FilterelementReplace> result = new Result<FilterelementReplace>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.filterelementReplaceService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "滤芯安装记录表-通过id查询")
	@ApiOperation(value="滤芯安装记录表-通过id查询", notes="滤芯安装记录表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<FilterelementReplace> queryById(@RequestParam(name="id",required=true) String id) {
		Result<FilterelementReplace> result = new Result<FilterelementReplace>();
		FilterelementReplace filterelementReplace = filterelementReplaceService.getById(id);
		if(filterelementReplace==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(filterelementReplace);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param request
   * @param response
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<FilterelementReplace> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              FilterelementReplace filterelementReplace = JSON.parseObject(deString, FilterelementReplace.class);
              queryWrapper = QueryGenerator.initQueryWrapper(filterelementReplace, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<FilterelementReplace> pageList = filterelementReplaceService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "滤芯安装记录表列表");
      mv.addObject(NormalExcelConstants.CLASS, FilterelementReplace.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("滤芯安装记录表列表数据", "导出人:Jeecg", "导出信息"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
  }

  /**
      * 通过excel导入数据
   *
   * @param request
   * @param response
   * @return
   */
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<FilterelementReplace> listFilterelementReplaces = ExcelImportUtil.importExcel(file.getInputStream(), FilterelementReplace.class, params);
              for (FilterelementReplace filterelementReplaceExcel : listFilterelementReplaces) {
                  filterelementReplaceService.save(filterelementReplaceExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listFilterelementReplaces.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }
}
