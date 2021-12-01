package com.exceptionresolve.demo.exception.resolver;

import com.exceptionresolve.demo.entity.ResponseVo;
import com.exceptionresolve.demo.exception.MyException;
import com.exceptionresolve.demo.util.rest.RestUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常处理
 *
 * @author lafe
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception ex) {

        ModelAndView modelAndView = new ModelAndView();
        ResponseVo responseVo = new ResponseVo();
        if (ex instanceof MyException) {
            responseVo.setSuccess(false);
            responseVo.setMsg(ex.getMessage());
            responseVo.setData("自定义异常");
        }

        try {
//            httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
//            httpServletResponse.getWriter().write(JSONObject.toJSONString(responseVo));
            RestUtil.write(httpServletResponse, responseVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }
}
