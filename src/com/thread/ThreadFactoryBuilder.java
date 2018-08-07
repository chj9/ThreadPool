/**
 * 
 */
package com.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**   
* Copyright: Copyright (c) 2018 
* 
* @ClassName: ThreadFactoryBuilder.java
* @Description: 自定义线程池中的名称
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月7日 上午10:46:47 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月7日     chenhj          v1.0.0               修改原因
*/
class ThreadFactoryBuilder {
	private String namePrefix = null;
    public ThreadFactoryBuilder setNameFormat(String namePrefix) {
        if (namePrefix == null) {
            throw new NullPointerException();
        }
        this.namePrefix = namePrefix;
        return this;
    }
    public ThreadFactory build() {
        return build(this);
    }
    private static ThreadFactory build(ThreadFactoryBuilder builder) {
        final String namePrefix = builder.namePrefix;
        final AtomicLong count = new AtomicLong(0);
        //jdk8中还是优先使用lamb表达式
        return (Runnable runnable) -> {
            Thread thread = new Thread(runnable);
            if (namePrefix != null) {
                thread.setName(namePrefix + "-" + count.getAndIncrement());
            }
            return thread;
        };
    }
}
