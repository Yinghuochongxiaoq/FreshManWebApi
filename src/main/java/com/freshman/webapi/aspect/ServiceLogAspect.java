package com.freshman.webapi.aspect;

import com.alibaba.fastjson.JSON;
import com.freshman.webapi.util.convertutil.TConverter;
import com.freshman.webapi.util.dateUtil.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class ServiceLogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.freshman.webapi.controller..*(..))")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinpoint) {
        System.out.println("方法执行前处理,"+joinpoint.toString());
    }

    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    public void doAfterReturning(Object ret) {
        System.out.println("方法返回后处理"+JSON.toJSON(ret));
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Date startTime = new Date();
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //方法名
        String path = request.getRequestURI();
        //请求参数
        String paramsString = "";
        int elapsedTime;

        //TODO：处理参数
        try {
            Map<String, String[]> params = request.getParameterMap();
            for (String pkey : params.keySet()) {
                if (pkey.equals("file"))
                    continue;
                if (!StringUtils.isEmpty(paramsString))
                    paramsString += "&";
                paramsString += pkey + "=" + TConverter.toSafeString(params.get(pkey)[0]);
            }
        } catch (Exception ex) {
            logger.error("记录接口日志请求时出错", ex);
        }
        System.out.println(DateHelper.date2Str(startTime) + "开始执行：" + path + " 参数：" + paramsString);

        //TODO：执行方法，result 为方法的返回值
        Object result = pjp.proceed();

        //TODO：执行完成
        Date endTime = new Date();
        System.out.println(DateHelper.date2Str(endTime) + "执行完毕");
        System.out.println("执行结果：" + JSON.toJSONString(result));
        try {
            elapsedTime = TConverter.ObjectToInt(endTime.getTime() - startTime.getTime());
            //TODO：写访问日志
            System.out.println("处理最后的事宜……耗时："+elapsedTime+"毫秒");
        } catch (Exception ex) {
            logger.error("记录接口日志结果时出错", ex);
        }
        return result;
    }

    @AfterThrowing(value = "logPointCut()",throwing = "e")
    public void afterThorwingMethod(JoinPoint joinPoint,NullPointerException  e){
        String methodName = joinPoint.getSignature().getName();
        System.out.println("【异常通知】the method 【" + methodName + "】 occurs exception: " + e);
    }
}
