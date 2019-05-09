/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.boot.security.auth.filter;

import com.google.common.collect.Sets;
import com.simbest.boot.security.IAuthService;
import com.simbest.boot.security.IUser;
import com.simbest.boot.security.auth.provider.sso.service.SsoAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;

/**
 * 用途：所有单点认证Token注册器
 * 作者: lishuyi
 * 时间: 2018/4/26  15:28
 */
@Slf4j
@Component
public class SsoAuthenticationRegister {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private IAuthService authService;

    public SortedSet<SsoAuthenticationService> getSsoAuthenticationService() {
        SortedSet sortedSet = Sets.newTreeSet();
        Map<String, SsoAuthenticationService> auths = appContext.getBeansOfType(SsoAuthenticationService.class);
        auths.values().forEach( a -> sortedSet.add(a));
        return sortedSet;
    }

    public String decodeKeyword(String encodeKeyword, IAuthService.KeyType keyType){
        String decodeKeyword = null;
        for(SsoAuthenticationService decryptService : getSsoAuthenticationService()) {
            decodeKeyword = decryptService.decryptKeyword(encodeKeyword);
            if(StringUtils.isNotEmpty(decodeKeyword)) {
                log.debug("通过关键字【{}】解密后为【{}】", encodeKeyword, decodeKeyword);
                IUser iUser = authService.findByKey(decodeKeyword, keyType);
                if (null != iUser) {
                    //成功返回
                    break;
                } else {
                    decodeKeyword = null;
                    log.warn("请注意关键字【{}】拉取用户信息为NULL！", decodeKeyword);
                }
            }
        }
        if(StringUtils.isEmpty(decodeKeyword)) {
            log.error("遍历完所有SSO解密服务器，关键字【{}】和关键字类型【{}】依旧为空，请立即检查！", encodeKeyword, keyType);
        }
        return decodeKeyword;
    }
}
