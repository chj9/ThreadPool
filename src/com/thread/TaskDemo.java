/**
 * 
 */
package com.thread;

/**   
* Copyright: Copyright (c) 2018 
* 
* @ClassName: Snippet.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月7日 下午5:39:52 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月7日     chenhj          v1.0.0               修改原因
*/
public class TaskDemo implements Runnable{

	
	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("我是测试");
	}

}

