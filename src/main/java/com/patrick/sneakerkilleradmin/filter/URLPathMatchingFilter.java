package com.patrick.sneakerkilleradmin.filter;

import com.patrick.sneakerkilleradmin.entity.Permission;
import com.patrick.sneakerkilleradmin.service.PermissionService;
import com.patrick.sneakerkilleradmin.util.SpringContextUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class URLPathMatchingFilter extends PathMatchingFilter {
    private static final Logger logger = LoggerFactory.getLogger(URLPathMatchingFilter.class);

    @Autowired
    PermissionService permissionService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (HttpMethod.OPTIONS.toString().equals((httpServletRequest).getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }

        if (null == permissionService) {
            permissionService = SpringContextUtils.getContext().getBean(PermissionService.class);
        }

        String requestAPI = getPathWithinApplication(request);

        Subject subject = SecurityUtils.getSubject();

        //用户未登录
        if (!subject.isAuthenticated()) {
            logger.info("用户未登录状态");
            return false;
        }

        // 判断访问接口是否需要过滤（数据库中是否有对应信息）
        boolean needFilter = permissionService.needFilter(requestAPI);
        if (!needFilter) {
            return true;
        } else {
            // 判断当前用户是否有相应权限
            boolean hasPermission = false;
            String username = subject.getPrincipal().toString();
            List<Permission> permissions = permissionService.listAllPermissions(username);
            Set<String> permissionAPIs = permissions.stream().map(Permission::getUrl).collect(Collectors.toSet());
            for (String api : permissionAPIs) {
                // 匹配前缀
                if (requestAPI.startsWith(api)) {
                    hasPermission = true;
                    break;
                }
            }
            if (hasPermission) {
                logger.info("user "+username+" 有权访问"+requestAPI);
                return true;
            } else {
                logger.info("user "+username+" 无权限访问"+requestAPI);
                return false;
            }
        }
    }
}
