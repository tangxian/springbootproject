/**
 * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.boot.core.kernel_core.aop;


import com.boot.core.kernel_core.context.RequestDataHolder;
import com.boot.core.kernel_core.reqres.request.RequestData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import static com.boot.core.kernel_model.constants.AopSortConstants.REQUEST_DATA_AOP_SORT;


/**
 * 对控制器调用过程中,提供一种RequestData便捷调用的aop
 *
 * @author fengshuonan
 * @date 2016年11月13日 下午10:15:42
 */
@Aspect
@Order(REQUEST_DATA_AOP_SORT)
public class RequestDataAop {

    @Pointcut("execution(* *..controller.*.*(..))")
    public void cutService() {
    }

    @Around("cutService()")
    public Object sessionKit(ProceedingJoinPoint point) throws Throwable {
        Object[] params = point.getArgs();

        //如果方法有参数并且参数中有RequestData对象，就临时保存RequestData到RequestDataHolder
        if (params != null && params.length > 0) {
            if (params[0] instanceof RequestData) {
                RequestDataHolder.put((RequestData) params[0]);
            }
        }

        //是否需要删除ThreadLocal的标识，如果proceed()过程中有异常
        //会执行ExceptionHandler，并在ExceptionHandler中清除ThreadLocal中的内容
        boolean needToRemove = true;
        try {
            return point.proceed();
        } catch (Throwable exception) {
            needToRemove = false;
            throw exception;
        } finally {
            if (needToRemove) {
                RequestDataHolder.remove();
            }
        }
    }
}
