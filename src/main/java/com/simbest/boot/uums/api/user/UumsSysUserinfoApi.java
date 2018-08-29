/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */

package com.simbest.boot.uums.api.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mzlion.easyokhttp.HttpClient;
import com.simbest.boot.base.web.response.JsonResponse;
import com.simbest.boot.config.AppConfig;
import com.simbest.boot.constants.AuthoritiesConstants;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.SimplePermission;
import com.simbest.boot.security.SimpleUser;
import com.simbest.boot.security.UserOrgTree;
import com.simbest.boot.util.encrypt.RsaEncryptor;
import com.simbest.boot.util.json.JacksonUtils;
import com.simbest.boot.util.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <strong>Title : SysAppController</strong><br>
 * <strong>Description : </strong><br>
 * <strong>Create on : 2018/5/26/026</strong><br>
 * <strong>Modify on : 2018/5/26/026</strong><br>
 * <strong>Copyright (C) Ltd.</strong><br>
 *
 * @author LM liumeng@simbest.com.cn
 * @version <strong>V1.0.0</strong><br>
 *          <strong>修改历史:</strong><br>
 *          修改人 修改日期 修改描述<br>
 *          -------------------------------------------<br>
 */

@Component
@Slf4j
public class UumsSysUserinfoApi {
    private static final String USER_MAPPING = "/action/user/user/";
    private static final String SSO = "/sso";
    @Autowired
    private AppConfig config;
    //private String uumsAddress="http://localhost:8080/uums";
    @Autowired
    private RsaEncryptor encryptor;

    /**
     * 不登录更新用户信息
     * @param keyword
     * @param keyType
     * @param appcode
     * @param simpleUser
     * @return
     */
    public SimpleUser update(String keyword,IAuthService.KeyType keyType,String appcode,SimpleUser simpleUser){
        String json0=JacksonUtils.obj2json(simpleUser);
        String keyword1=encryptor.encrypt(keyword);
        String keyword2=keyword1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(config.getUumsAddress() + USER_MAPPING + "update"+SSO+"?keyword="+keyword2+"&keytype="+keyType
                +"&appcode="+appcode)
                .json( json0 )
                .asBean(JsonResponse.class );
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        SimpleUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
        return auth;
    }

    /**
     * 根据id查找
     * @param id
     * @param appcode
     * @return
     */
    public SimpleUser findById(String id, String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findById"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("id", String.valueOf(id))
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        SimpleUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
        return auth;
    }


    /**
     * 单表条件查询并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param sysUserinfoMap
     * @return
     */
    public JsonResponse findAll(int page, int size,String direction,String properties,String appcode, Map sysUserinfoMap ) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysUserinfoMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(config.getUumsAddress() + USER_MAPPING + "findAll"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .json( json0 )
                .asBean(JsonResponse.class );
        return response;
    }

    /**
     * 根据用户名查询用户信息(BPS专用)
     * @param username
     * @param appcode
     * @return
     */
    public SimpleUser findByUsername(String username,String appcode) {
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findByUsername"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME,encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("username",username)
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        SimpleUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
        return auth;
    }


    /**
     * 根据群组sid查询用户信息并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param groupSid
     * @return
     */
    public JsonResponse findUserByGroup(int page,  int size, String direction,  String properties,String appcode, String groupSid ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findUserByGroup"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("id", String.valueOf(groupSid))
                .asBean(JsonResponse.class);
        return response;
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
    public JsonResponse findUserByOrg(int page,  int size,  String direction,  String properties,String appcode, String orgCode ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findUserByOrg"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("orgCode", orgCode)
                .asBean(JsonResponse.class);
        return response;
    }


    /**
     * 根据职位名获取用户并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param positionName
     * @return
     */
    public JsonResponse findUserByPosition( int page, int size, String direction, String properties,String appcode, String positionName ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findUserByPosition"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("positionName", positionName)
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     * 根据角色id获取用户但不分页
     * @param roleId
     * @return
     */
    public List<SimpleUser> findUserByRoleNoPage(String roleId ,String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findUserByRoleNoPage"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode )
                .param("roleId", String.valueOf(roleId))
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        List<SimpleUser> userList=JacksonUtils.json2Type(json, new TypeReference<List<SimpleUser>>(){});
        return userList;
    }

    /**
     * 根据角色id获取用户并分页
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param appcode
     * @param roleId
     * @return
     */
    public JsonResponse findUserByRole(int page, int size, String direction,  String properties,String appcode,String roleId ){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findUserByRole"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("roleId", String.valueOf(roleId))
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     * 根据过滤条件获取决策下的用户
     * @param appcode
     * @param sysAppDecisionmap
     * @return
     */
    public List<UserOrgTree> findUserByDecisionNoPage(String appcode,Map sysAppDecisionmap){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionmap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(config.getUumsAddress() + USER_MAPPING + "findUserByDecisionNoPage"+SSO+"?loginuser="+username2+"&appcode="+appcode
        +"&username="+username)
                .json( json0 )
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        List<UserOrgTree> userList=JacksonUtils.json2Type(json, new TypeReference<List<UserOrgTree>>(){});
        return userList;
    }

    /**
     * 根据用户返回用户以及用户的的组织树
     * @param appcode
     * @param username
     * @return
     */
    public List<UserOrgTree> findUserByUsernameNoPage(String appcode,String username){
        String loginUser = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", loginUser);
        JsonResponse response= HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findUserByUsernameNoPage"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(loginUser))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("username",username)
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        List<UserOrgTree> userList=JacksonUtils.json2Type(json, new TypeReference<List<UserOrgTree>>(){});
        return userList;
    }

    /**
     * 一层层去查询全部组织和人
     * @param appcode
     * @param orgCode
     * @return
     */
    public List<UserOrgTree> findOneStep(String appcode,String orgCode){
        String loginUser = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", loginUser);
        JsonResponse response= HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findOneStep"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(loginUser))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("orgCode",orgCode)
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        List<UserOrgTree> userList=JacksonUtils.json2Type(json, new TypeReference<List<UserOrgTree>>(){});
        return userList;
    }

    /**
     * 根据用户中文姓名以及主数据首先移动号码模糊查询并分页
     * @param appcode
     * @param page
     * @param size
     * @param direction
     * @param properties
     * @param truename
     * @param preferredMobile
     * @return
     */
    public JsonResponse findRoleNameIsARoleDim(int page,int size,String direction,String properties,String appcode,String truename, String preferredMobile ) {
        String loginUser = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", loginUser);
        JsonResponse response= HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findRoleNameIsARoleDim"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(loginUser))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("direction", direction)
                .param("properties", properties)
                .param("truename", truename)
                .param("preferredMobile", preferredMobile)
                .asBean(JsonResponse.class);
        return response;
    }

    /**
     * 检测用户是否有app的权限
     * 应用向UUMS发送单点请求时使用
     * @param username
     * @param appcode
     * @return
     */
    public Boolean checkUserAccessApp(String username,String appcode) {
       return checkUserAccessAppNoramal(SecurityUtils.getCurrentUserName(),username,appcode);
    }

    /**
     * 检测用户是否有app的权限。当前应用无session时使用，如portal。
     * 门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用
     * @param username
     * @param appcode
     * @return
     */
    public Boolean checkUserAccessAppNoSession(String username,String appcode) {
        return checkUserAccessAppNoramal(username,username,appcode);
    }

    private Boolean checkUserAccessAppNoramal(String loginUser, String username, String appcode){
        log.debug("Http remote request user by username: {}", loginUser);
        JsonResponse response= HttpClient.post(config.getUumsAddress() + USER_MAPPING + "checkUserAccessApp"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(loginUser))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("username", username)
                .param("appCode", appcode)
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        Boolean auth = JacksonUtils.json2obj(json, Boolean.class);
        return auth;
    }

    /**
     * 查询某个人在某一应用下的全部权限。普通应用使用
     * 应用向UUMS发送单点请求时使用
     * @param appcode
     * @param username
     * @return
     */
    public Set<SimplePermission> findPermissionByAppUser( String username, String appcode) {
        return findPermissionByAppUserNormal(SecurityUtils.getCurrentUserName(), username, appcode);
    }

    /**
     * 查询某个人在某一应用下的全部权限。当前应用无session时使用，如portal
     * 门户Portal向应用发送单点请求，应用再向UUMS发送单点请求时使用
     * @param username
     * @param appcode
     * @return
     */
    public Set<SimplePermission> findPermissionByAppUserNoSession( String username, String appcode) {
        return findPermissionByAppUserNormal(username, username, appcode);
    }

    private Set<SimplePermission> findPermissionByAppUserNormal(String loginUser, String username, String appcode){
        log.debug("Http remote request user by username: {}", loginUser);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findPermissionByAppUser"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(loginUser))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .param("username",username)
                .param( "appCode",appcode )
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof List)){
            log.error("--uums接口返回的类型不为List--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        Set<SimplePermission> permissions=JacksonUtils.json2Type(json, new TypeReference<Set<SimplePermission>>(){});
        return permissions;
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
    public SimpleUser findByKey(String keyword,IAuthService.KeyType keyType,String appcode) {
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findByKey"+SSO)
                .param(AuthoritiesConstants.SSO_API_KEYWORD, encryptor.encrypt(keyword))
                .param(AuthoritiesConstants.SSO_API_KEYTYPE, keyType.name())
                .param(AuthoritiesConstants.SSO_API_APP_CODE, appcode)
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        SimpleUser auth = JacksonUtils.json2obj(json, SimpleUser.class);
        return auth;
    }

    //增加用户的权限
   /* public SimpleUser addAppAuthorities(String appcode,IUser authUser, Set<? extends IPermission> permissions) {
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionmap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(config.getUumsAddress() + USER_MAPPING + "addAppAuthorities"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&username="+username)
                .json( json0 )
                .asBean(JsonResponse.class);
        if(response==null){
            log.error("--response对象为空!--");
            return null;
        }
        if(!(response.getData() instanceof ArrayList)){
            log.error("--uums接口返回的类型不为ArrayList--");
            return null;
        }
        String json = JacksonUtils.obj2json(response.getData());
        List<UserOrgTree> userList=JacksonUtils.json2list(json, new TypeReference<List<UserOrgTree>>(){});
        return userList;
    }*/

    /*
    public JsonResponse findUserByApp(@RequestParam int page,@RequestParam int size, @RequestParam String direction,@RequestParam String properties,@RequestParam String appcode, Map sysAppDecisionMap){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(config.getUumsAddress() + USER_MAPPING + "findUserByApp"+SSO+"?loginuser="+username2+"&appcode="+appcode
                +"&page="+page+"&size="+size+"&direction="+direction+"&properties="+properties)
                .json( js
    }on0 ).
                        asBean(JsonResponse.class );
        return response;
    }

    //查询多个应用下参与的全部用户


    public JsonResponse findUserByAppNoPage(String appcode, Map sysAppDecisionMap){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppDecisionMap);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(config.getUumsAddress() + USER_MAPPING + "findUserByAppNoPage"+SSO+"?loginuser="+username2+"&appcode="+appcode)
                .json( json0 ).
                        asBean(JsonResponse.class);
        return response;
    }

    public JsonResponse findUserByAllAppNoPage(@RequestParam String appcode){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        JsonResponse response =  HttpClient.post(config.getUumsAddress() + USER_MAPPING + "findUserByAllAppNoPage"+SSO)
                .param(AuthoritiesConstants.SSO_API_USERNAME, encryptor.encrypt(username))
                .param(AuthoritiesConstants.SSO_API_APP_CODE,appcode)
                .asBean(JsonResponse.class);
        return response;
    }


    public JsonResponse findUserByAppPermission( @RequestParam(required = false, defaultValue = "1") int page, //
                                                 @RequestParam(required = false, defaultValue = "10") int size, //
                                                 @RequestParam(required = false) String direction, //
                                                 @RequestParam(required = false) String properties,
                                                 @RequestParam String appcode,
                                                 List<Map> sysAppGroups){
        String username = SecurityUtils.getCurrentUserName();
        log.debug("Http remote request user by username: {}", username);
        String json0=JacksonUtils.obj2json(sysAppGroups);
        String username1=encryptor.encrypt(username);
        String username2=username1.replace("+","%2B");
        JsonResponse response= HttpClient.textBody(config.getUumsAddress() + USER_MAPPING + "findUserByAppPermission"+SSO+"?loginuser="+username2+"&appcode="+appcode)
                .json( json0 )
                .asBean(JsonResponse.class);
        return response;
    }*/

}

