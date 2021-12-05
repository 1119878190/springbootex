package com.demo.aspect;

import com.demo.annotions.Log;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Aspect
@Component
public class MyAspect {

    @Pointcut("@annotation(com.demo.annotions.Log)")
    public void operationLog() {
    }


    @Around(value = "operationLog()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //result为调用方法返回的结果，这是一个进入方法和退出方法的一个分界
        Object result = joinPoint.proceed();

        System.out.println("切面：testLog返回的数据是" + result.toString());
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取注解上的属性
        Log annotation = method.getAnnotation(Log.class);
        String desc = annotation.operDesc();
        System.out.println("切面，@Log中的描述是：" + desc);

        System.out.println("切面 调用的方法是："+method.getName());

        // 获取前端传的参数
        getParameter(joinPoint, method);


        // url http://127.0.0.1:8080/testLog
        System.out.println("切面 request.getRequestURL(): " + request.getRequestURL());
        // uri /testLog
        System.out.println("切面 request.getRequestURI():  " + request.getRequestURI());
        System.out.println("切面 request.getRemoteUser():  " + request.getRemoteUser());
        // ip
        System.out.println("切面 request.getRemoteHost():  " + request.getRemoteHost());
        // get post 请求方法
        System.out.println("切面 request.getMethod()"+request.getMethod());

        // 一定要返回方法的结果，否则前端不会接受到
        return result;
    }

    /**
     * 获取前端传的参数
     *
     * @param joinPoint
     * @param method
     */
    private void getParameter(ProceedingJoinPoint joinPoint, Method method) {
        List<Object> argList = new ArrayList<>();

        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < args.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>();
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
    }


}
