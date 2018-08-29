package com.simbest.boot.uums.web.user;

import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.SimpleUser;
import com.simbest.boot.uums.api.user.UumsSysUserinfoApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Api (description = "系统用户操作相关接口")
@Slf4j
@RestController
@RequestMapping("/uums/sys/userinfo")
public class UumsSysUserInfoController {

    @Autowired
    private UumsSysUserinfoApi uumsSysUserinfoApi;

    /**
     * 不登录更新用户信息
     * @param keyword
     * @param keyType
     * @param appcode
     * @param simpleUser
     * @return
     */
    @ApiOperation(value = "不登录更新用户信息", notes = "不登录更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "keyType", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用编码", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/update")
    public JsonResponse update( @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) IAuthService.KeyType keyType,
                                @RequestParam(required = false) String appcode,
                                @RequestBody(required = false) SimpleUser simpleUser) {
        return JsonResponse.success( uumsSysUserinfoApi.update(keyword,keyType,appcode,simpleUser));
    }

    /**
     * 获取角色信息列表并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param map
     * @return
     */
    @ApiOperation(value = "获取角色信息列表并分页", notes = "获取角色信息列表并分页")
    @ApiImplicitParams ({ //
            @ApiImplicitParam (name = "page", value = "当前页码", dataType = "int", paramType = "query", //
                    required = true, example = "1"), //
            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
                    required = true, example = "10"), //
            @ApiImplicitParam(name = "direction", value = "排序规则（asc/desc）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "properties", value = "排序规则（属性名称）", dataType = "String", //
                    paramType = "query") //
    })
    @PostMapping("/findAll")
    public JsonResponse findAll( @RequestParam(required = false, defaultValue = "1") int page, //
                                 @RequestParam(required = false, defaultValue = "10") int size, //
                                 @RequestParam(required = false) String direction, //
                                 @RequestParam(required = false) String properties,
                                 @RequestParam String appcode,
                                 @RequestBody Map map ) {
        return JsonResponse.success(uumsSysUserinfoApi.findAll(page,size,direction,properties,appcode,map));
    }

    /**
     *根据用户名查询用户信息
     * @param username
     * @return
     */
    @ApiOperation(value = "根据用户名查询用户信息", notes = "根据用户名查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "appcode", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/findByUsername")
    public JsonResponse findByUsername(@RequestParam String username,@RequestParam String appcode) {
        return JsonResponse.success(uumsSysUserinfoApi.findByUsername(username,appcode));
    }

    /**
     * 根据组织orgcode获取用户并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param orgCode
     * @return
     */
    @ApiOperation(value = "根据组织orgcode获取用户并分页", notes = "根据组织orgcode获取用户并分页")
    @ApiImplicitParams ({ //
            @ApiImplicitParam (name = "page", value = "当前页码", dataType = "int", paramType = "query", //
                    required = true, example = "1"), //
            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
                    required = true, example = "10"), //
            @ApiImplicitParam(name = "direction", value = "排序规则（asc/desc）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "properties", value = "排序规则（属性名称）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "appcode", value = "当前应用appcode", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "orgCode", value = "组织appcode", dataType = "String", //
                    paramType = "query")
    })
    @PostMapping("/findUserByOrg")
    public JsonResponse findUserByOrg( @RequestParam(required = false, defaultValue = "1") int page, //
                                      @RequestParam(required = false, defaultValue = "10") int size, //
                                      @RequestParam(required = false) String direction, //
                                      @RequestParam(required = false) String properties,
                                      @RequestParam String appcode,
                                       @RequestParam String orgCode) {
        return JsonResponse.success(uumsSysUserinfoApi.findUserByOrg(page,size,direction,properties,appcode,orgCode));
    }

    /**
     * 根据角色id获取用户但不分页
     * @param roleId
     * @return
     */
    @ApiOperation(value = "根据角色id获取用户但不分页", notes = "根据角色id获取用户但不分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "roleId", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "appcode", dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/findUserByRoleNoPage")
    public JsonResponse findUserByRoleNoPage( @RequestParam String roleId , @RequestParam String appcode){
        return JsonResponse.success(uumsSysUserinfoApi.findUserByRoleNoPage(roleId,appcode ));
    }

    /**
     * 根据过滤条件获取决策下的用户
     * @param appcode
     * @param sysAppDecisionMap
     * @return
     */
    @ApiOperation(value = "根据过滤条件获取决策下的用户", notes = "根据过滤条件获取决策下的用户")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "appcode", value = "当前应用appcode", dataType = "String" ,paramType = "query")
    })
    @PostMapping(value ="/findUserByDecisionNoPage")
    public JsonResponse findUserByDecisionNoPage(@RequestParam String appcode,@RequestBody Map sysAppDecisionMap){
        return JsonResponse.success(uumsSysUserinfoApi.findUserByDecisionNoPage(appcode,sysAppDecisionMap));
    }

    /**
     * 根据用户返回用户以及用户的的组织树
     * @param appcode
     * @param username
     * @return
     */
    @ApiOperation(value = "根据用户返回用户以及用户的的组织树", notes = "根据用户返回用户以及用户的的组织树")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "appcode", value = "当前应用appcode", dataType = "String" ,paramType = "query"),
            @ApiImplicitParam(name = "username", value = "要查询的人的username", dataType = "String" ,paramType = "query")
    })
    @PostMapping(value ="/findUserByUsernameNoPage")
    public JsonResponse findUserByUsernameNoPage(@RequestParam String appcode,@RequestParam String username){
        return JsonResponse.success(uumsSysUserinfoApi.findUserByUsernameNoPage(appcode,username));
    }

    /**
     * 一层层去查询全部组织和人
     * @param appcode
     * @param orgCode
     * @return
     */
    @ApiOperation(value = "一层层去查询全部组织和人", notes = "一层层去查询全部组织和人")
    @ApiImplicitParams ({
            @ApiImplicitParam(name = "appcode", value = "当前应用appcode", dataType = "String" ,paramType = "query"),
            @ApiImplicitParam(name = "orgCode", value = "组织编码", dataType = "String" ,paramType = "query")
    })
    @PostMapping(value ="/findOneStep")
    public JsonResponse findOneStep(@RequestParam String appcode,@RequestParam(required = false) String orgCode){
        return JsonResponse.success(uumsSysUserinfoApi.findOneStep(appcode,orgCode));
    }

    /**
     * 根据用户中文姓名以及主数据首先移动号码模糊查询并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param truename
     * @param preferredMobile
     * @return
     */
    @ApiOperation(value = "根据用户中文姓名以及主数据首先移动号码模糊查询并分页", notes = "根据用户中文姓名以及主数据首先移动号码模糊查询并分页")
    @ApiImplicitParams ({ //
            @ApiImplicitParam (name = "page", value = "当前页码", dataType = "int", paramType = "query", //
                    required = true, example = "1"), //
            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
                    required = true, example = "10"), //
            @ApiImplicitParam(name = "direction", value = "排序规则（asc/desc）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "properties", value = "排序规则（属性名称）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "appcode", value = "当前应用appcode", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "truename", value = "用户真实姓名", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "preferredMobile", value = "用户移动电话", dataType = "String", //
                    paramType = "query")
    })
    @PostMapping("/findRoleNameIsARoleDim")
    public JsonResponse findRoleNameIsARoleDim( @RequestParam(required = false, defaultValue = "1") int page, //
                                       @RequestParam(required = false, defaultValue = "10") int size, //
                                       @RequestParam(required = false) String direction, //
                                       @RequestParam(required = false) String properties,
                                       @RequestParam(required = false) String appcode,
                                       @RequestParam(required = false) String truename,
                                                @RequestParam(required = false) String preferredMobile ) {
        return uumsSysUserinfoApi.findRoleNameIsARoleDim(page,size,direction,properties,appcode,truename,preferredMobile);
    }

   /**
     * 检测用户是否有app的权限
    * 应用向UUMS发送单点请求时使用
     * @param username
     * @param appcode
     * @return
     */
    @ApiOperation(value = "检测用户是否有app的权限,应用向UUMS发送单点请求时使用", notes = "检测用户是否有app的权限,应用向UUMS发送单点请求时使用")
    @ApiImplicitParams ({ //
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用编码", dataType = "String", //
                    paramType = "query")
    })
    @PostMapping("/checkUserAccessApp")
    public JsonResponse checkUserAccessApp(@RequestParam(required = false) String username
                                           ,@RequestParam(required = false) String appcode ){
        return JsonResponse.success( uumsSysUserinfoApi.checkUserAccessApp(username,appcode));
    }

    /**
     * 检测用户是否有app的权限。当前应用无session时使用，如portal。门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用
     * @param username
     * @param appcode
     * @return
     */
    @ApiOperation(value = "检测用户是否有app的权限。当前应用无session时使用，如portal。门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用", notes = "检测用户是否有app的权限。当前应用无session时使用，如portal。门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用")
    @ApiImplicitParams ({ //
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用编码", dataType = "String", //
                    paramType = "query")
    })
    @PostMapping("/checkUserAccessAppNoSession")
    public JsonResponse checkUserAccessAppNoSession(@RequestParam(required = false) String username
            ,@RequestParam(required = false) String appcode ){
        return JsonResponse.success( uumsSysUserinfoApi.checkUserAccessAppNoSession(username,appcode));
    }

    /**
     * 查询某个人在某一应用下的全部权限。普通应用使用
     * 应用向UUMS发送单点请求时使用
     * @param username
     * @param appcode
     * @return
     */
    @ApiOperation(value = "查询某个人在某一应用下的全部权限。普通应用使用。应用向UUMS发送单点请求时使用。", notes = "查询某个人在某一应用下的全部权限。普通应用使用。应用向UUMS发送单点请求时使用。")
    @ApiImplicitParams ({ //
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用编码", dataType = "String", //
                    paramType = "query")
    })
    @PostMapping("/findPermissionByAppUser")
    public JsonResponse findPermissionByAppUser(@RequestParam(required = false) String username
            ,@RequestParam(required = false) String appcode ){
        return JsonResponse.success( uumsSysUserinfoApi.findPermissionByAppUser(username,appcode));
    }

    /**
     * 查询某个人在某一应用下的全部权限。当前应用无session时使用，如portal
     * 门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用
     * @param username
     * @param appcode
     * @return
     */
    @ApiOperation(value = "查询某个人在某一应用下的全部权限。当前应用无session时使用，如portal。门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用。", notes = "查询某个人在某一应用下的全部权限。当前应用无session时使用，如portal。门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用。")
    @ApiImplicitParams ({ //
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用编码", dataType = "String", //
                    paramType = "query")
    })
    @PostMapping("/findPermissionByAppUserNoSession")
    public JsonResponse findPermissionByAppUserNoSession(@RequestParam(required = false) String username
            ,@RequestParam(required = false) String appcode ){
        return JsonResponse.success( uumsSysUserinfoApi.findPermissionByAppUserNoSession(username,appcode));
    }

    /**
     * 根据关键字查询用户身份信息，当前支持如下：
     * 登录名     username
     * 人员编号   employeeNumber
     * 手机号码   preferredMobile
     * 邮箱       email
     * 保留关键字 reserve1 可存微信openid
     * @param keyword
     * @param keyType
     * @param appcode
     * @return
     */
    @ApiOperation(value = "根据关键字查询用户身份信息", notes = "根据关键字查询用户身份信息")
    @ApiImplicitParams ({ //
            @ApiImplicitParam(name = "keyword", value = "关键字", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "keyType", value = "类型", dataType = "String", //
                    paramType = "query"),
            @ApiImplicitParam(name = "appcode", value = "应用编码", dataType = "String", //
                    paramType = "query")
    })
    @PostMapping("/findByKey")
    public JsonResponse findByKey(@RequestParam(required = false) String keyword
            ,@RequestParam(required = false) IAuthService.KeyType keyType,@RequestParam(required = false) String appcode){
        return JsonResponse.success( uumsSysUserinfoApi.findByKey(keyword,keyType,appcode));
    }

    /**
     * 增加用户的权限
     * @return
     */
   /* @ApiOperation(value = "增加用户的权限", notes = "增加用户的权限")
    @PostMapping("/addAppAuthorities")
    public JsonResponse addAppAuthorities(@RequestBody Map authoritiesMap ){
        IUser authUser = new SimpleUser();
        Set<? extends IPermission > permissions = new LinkedHashSet<>(  );
        //TODO 从map中取出，放入其中。
        return JsonResponse.success( uumsSysUserinfoApi.addAppAuthorities(authUser,permissions));
    }*/

    /*    @ApiOperation(value = "查询一个应用下参与的全部用户，包含用户所在的组织以及用户的职位信息分页", notes = "查询一个应用下参与的全部用户，包含用户所在的组织以及用户的职位信息分页")
    @ApiImplicitParams ({ //
            @ApiImplicitParam (name = "page", value = "当前页码", dataType = "int", paramType = "query", //
                    required = true, example = "1"), //
            @ApiImplicitParam(name = "size", value = "每页数量", dataType = "int", paramType = "query", //
                    required = true, example = "10"), //
            @ApiImplicitParam(name = "direction", value = "排序规则（asc/desc）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "properties", value = "排序规则（属性名称）", dataType = "String", //
                    paramType = "query"), //
            @ApiImplicitParam(name = "appcode", value = "appcode", dataType = "String", //
                    paramType = "query"), //
    })
    @PostMapping(value ="/findUserByApp")
    public JsonResponse findUserByApp(@RequestParam(required = false, defaultValue = "1") int page, //
                                      @RequestParam(required = false, defaultValue = "10") int size, //
                                      @RequestParam(required = false) String direction, //
                                      @RequestParam(required = false) String properties,
                                      @RequestParam String appcode,
                                      @RequestBody Map map){
        return JsonResponse.success(uumsSysUserinfoApi.findUserByApp(page,size,direction,properties,appcode,map));
    }




    @ApiOperation(value = "查询一个应用下参与的全部用户，包含用户所在的组织以及用户的职位信息不分页", notes = "查询一个应用下参与的全部用户，包含用户所在的组织以及用户的职位信息不分页")
    @ApiImplicitParam(name = "appcode", value = "appcode", dataType = "String" ,paramType = "query")
    @PostMapping(value ="/findUserByAppNoPage")
    public JsonResponse findUserByAppNoPage(@RequestParam String appcode,@RequestBody Map map){
        return JsonResponse.success(uumsSysUserinfoApi.findUserByAppNoPage(appcode,map));
    }*/


}

