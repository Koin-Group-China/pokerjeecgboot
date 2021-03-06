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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.system.entity.Client;
import org.jeecg.modules.system.entity.Equipment;
import org.jeecg.modules.system.service.IClientService;
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
 * @Description: 客户表
 * @Author: jeecg-boot
 * @Date:   2019-07-03
 * @Version: V1.0
 */
@Slf4j
@Api(tags="客户表")
@RestController
@RequestMapping("/demo/client")
public class ClientController {
	@Autowired
	private IClientService clientService;


	/**
	  * 分页列表查询
	 * @param client
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "客户表-分页列表查询")
	@ApiOperation(value="客户表-分页列表查询", notes="客户表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Client>> queryPageList(Client client,
                                               @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                               @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                               HttpServletRequest req) {
		Result<IPage<Client>> result = new Result<IPage<Client>>();
		QueryWrapper<Client> queryWrapper = QueryGenerator.initQueryWrapper(client, req.getParameterMap());
		Page<Client> page = new Page<Client>(pageNo, pageSize);
		IPage<Client> pageList = clientService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	 /**
	  * 判断手机号码与短信验证码
	  * @return
	  */
	 @GetMapping(value = "/selbyphone")
	public  Result<Client> selbyphone( String phone,String cody,
	 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize){
		 Result<Client> result = new Result<Client>();
		 Page<Client> page = new Page<Client>(pageNo, pageSize);
	 	if(cody.equals("123456")){//判断短信验证码是否正确
	 	Client client =  clientService.ClientByphone(phone);
			if(client!=null){//判断手机号码是否存在
				result.setSuccess(true);
				result.setResult(client);
				result.success("欢迎您");//登陆成功
			}else{
				result.error500("手机号码没有注册客户信息！");
			}
		}else{
			result.error500("验证码错误，请重新点击获取");
		}
		return result;
	 }


     @AutoLog(value = "查询用户下的设备")
     @ApiOperation(value="查询用户下的设备", notes="查询用户下的设备")
     @GetMapping(value = "/selectEqu")
     public Result<IPage<Equipment>> queryPageList(Equipment equipment,
                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                HttpServletRequest req) {
         Result<IPage<Equipment>> result = new Result<IPage<Equipment>>();
         QueryWrapper<Equipment> queryWrapper = QueryGenerator.initQueryWrapper(equipment, req.getParameterMap());
         Page<Equipment> page = new Page<Equipment>(pageNo, pageSize);
         IPage<Equipment> pageList = clientService.SelectByEquId(page,equipment.getClientId());
         result.setSuccess(true);
         result.setResult(pageList);
         return result;
     }
	 /*查询客户*/
	 @GetMapping(value = "/selClient")
	 public List<Client> selClient(){
		 //获取滤芯表数据
		 List<Client> records = clientService.list();
		 System.out.println(records);
		 return records;
	 }


	 /**
	  *   添加
	 * @param client
	 * @return
	 */
	@AutoLog(value = "客户表-添加")
	@ApiOperation(value="客户表-添加", notes="客户表-添加")
	@PostMapping(value = "/add")
	public Result<Client> add(@RequestBody Client client) {
		Result<Client> result = new Result<Client>();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		try {
			client.setUpdateBy(sysUser.getRealname());
			/*//需要判断手机号码是否已经存在
			int count = clientService.countphone(client.getPhone());
			if(count==0){*/
				clientService.save(client);
				Client client1 = new Client().setClientId(client.getClientId());
				result.setResult(client1);//添加成功后返回客户实体
				result.success("添加客户信息成功！");
			/*}else{
				result.error500("您输入的手机号码已经被使用，请换一个手机号码！");
			}*/
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	 /**
	  *  判断手机号码是否唯一
	  * @param client
	  * @return
	  */
	 @AutoLog(value = "客户表-手机号码唯一验证")
	 @ApiOperation(value="客户表-手机号码唯一验证", notes="客户表-手机号码唯一验证")
	 @RequestMapping(value = "/countphone", method = RequestMethod.GET)
	 public Result<Boolean> checkUsername(Client client) {
		 Result<Boolean> result = new Result<>();
		 result.setResult(true);//如果此参数为false则程序发生异常
		 try {
			 Client count = clientService.countphone(client.getPhone());//根据前端传回的手机号码得到实体，如果==null，表示手机可用
			 if (count!=null) {//不为空，则判断，是新增还是修改
				 //如果根据传入信息查询到用户了，那么就需要做校验了。
				 if (client.getClientId() == null) {
					 //前端传回的id为空=>新增模式=>只要根据用户信息存在则返回false
					 result.setSuccess(false);
					 result.setMessage("该手机号码已被使用,请更换一个手机号码!");
					 return result;
				 } else if (!client.getClientId().equals(count.getClientId())) {
					 //前端id不为空=>编辑模式=>判断两者ID是否一致-不一致则手机号码不可用
					 result.setSuccess(false);
					 result.setMessage("该手机号码已被使用,请更换一个手机号码!");
					 return result;
				 }
			 }

		 } catch (Exception e) {
			 result.setSuccess(false);
			 result.setResult(false);
			 result.setMessage(e.getMessage());
			 return result;
		 }
		 result.setSuccess(true);
		 return result;
	 }
	
	/**
	  *  编辑
	 * @param client
	 * @return
	 */
	@AutoLog(value = "客户表-编辑")
	@ApiOperation(value="客户表-编辑", notes="客户表-编辑")
	@PutMapping(value = "/edit")
	public Result<Client> edit(@RequestBody Client client) {
		Result<Client> result = new Result<Client>();
		Client clientEntity = clientService.getById(client.getClientId());
		if(clientEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = clientService.updateById(client);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param clientId
	 * @return
	 */
	@AutoLog(value = "客户表-通过id删除")
	@ApiOperation(value="客户表-通过id删除", notes="客户表-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<Client> delete(@RequestParam(name="id",required=true) String clientId) {
		Result<Client> result = new Result<Client>();
		Client client = clientService.getById(clientId);
		if(client==null) {
			result.error500("未找到对应实体");
		}else {
            Integer integer = clientService.ClientById(clientId);
            if (integer>0){
                result.error500("该客户名下有已安装设备不能删除");
            }else {
                boolean ok = clientService.removeById(clientId);
                if(ok) {
                    result.success("删除成功!");
                }
            }
		}
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "客户表-批量删除")
	@ApiOperation(value="客户表-批量删除", notes="客户表-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<Client> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<Client> result = new Result<Client>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.clientService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@AutoLog(value = "客户表-通过id查询")
	@ApiOperation(value="客户表-通过id查询", notes="客户表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Client> queryById(@RequestParam(name="id",required=true) String id) {
		Result<Client> result = new Result<Client>();
		Client client = clientService.getById(id);
		if(client==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(client);
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
      QueryWrapper<Client> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              Client client = JSON.parseObject(deString, Client.class);
              queryWrapper = QueryGenerator.initQueryWrapper(client, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<Client> pageList = clientService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "客户表列表");
      mv.addObject(NormalExcelConstants.CLASS, Client.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("客户表列表数据", "导出人:Jeecg", "导出信息"));
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
              List<Client> listClients = ExcelImportUtil.importExcel(file.getInputStream(), Client.class, params);
              for (Client clientExcel : listClients) {
                  clientService.save(clientExcel);
              }
              return Result.ok("文件导入成功！数据行数:" + listClients.size());
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
