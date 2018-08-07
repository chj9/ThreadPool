/**
 * 
 */
package com.thread;

/**   
* Copyright: Copyright (c) 2018 
* 
* @ClassName: Test.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月7日 下午5:37:16 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月7日     chenhj          v1.0.0               修改原因
*/
public class Test {
	public static void main(String[] args) {
		ThreadPoolManager pool = null;
		try {
			//默认连接池最大线程数量4
			//pool = ThreadPoolManager.newInstance().build();
			//设置连接池线程数量10,代码里还有很多自定义设置可以自行设置
			pool = ThreadPoolManager.newInstance(10).build();
			//执行任务
			TaskDemo task = new TaskDemo();
			pool.addExecuteTask(task);
			System.out.println("连接池激活数:"+pool.getNumActive());
			System.out.println("队列等待数:"+pool.getNumQueue());
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			//关闭连接池
			if(pool!=null)
				pool.shutdown();
		}

	}
}
