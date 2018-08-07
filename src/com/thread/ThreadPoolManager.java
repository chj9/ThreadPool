/**
 * 
 */
package com.thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**   
* Copyright: Copyright (c) 2018 
* 
* @ClassName: ThreadPoolManager.java
* @Description: 线程池管理(线程统一调度管理)
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年8月7日 上午10:05:54 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年8月7日     chenhj          v1.0.0               修改原因
*/
public class ThreadPoolManager {
	private static ThreadPoolManager sThreadPoolManager = new ThreadPoolManager(4);
	/**线程池维护线程的最少数量   CPU核心数**/
	private  int SIZE_CORE_POOL;
	/**线程池维护线程的最大数量**/
	private  int SIZE_MAX_POOL;
	/**线程池维护线程所允许的空闲时间,超时时间为0,线程运行完后就关闭，而不会再等待超时时间 单位:秒**/
	private static final long TIME_KEEP_ALIVE = 0L;
	/**线程池所使用的缓冲队列大小**/
	private static final int SIZE_WORK_QUEUE = 100;
	/**任务调度周期**/
	private static final int PERIOD_TASK_QOS = 1000;
	/**任务缓冲队列**/
	public final BlockingQueue<Runnable> mTaskQueue = new LinkedBlockingQueue<Runnable>(SIZE_WORK_QUEUE);
	/**
	 * 线程池单例创建方法
	 * @param threadSize 线程池数量
	 * @return
	 */
	public static ThreadPoolManager newInstance(int threadSize) {
		sThreadPoolManager.setThreadSize(threadSize);
		return sThreadPoolManager;
	}
	/**线程池单例创建方法
	 * 连接池中默认4条
	 * */
	public static ThreadPoolManager newInstance() {
		return sThreadPoolManager;
	}
	/**将构造方法访问修饰符设为私有，禁止任意实例化。*/
	private ThreadPoolManager(int threadSize) {
		setThreadSize(threadSize);
	}
	private ThreadPoolManager setThreadSize(int threadSize){
		//一般情况下SIZE_CORE_POOL和SIZE_MAX_POOL是一样的,但也可自行设置,但是SIZE_CORE_POOL必须小于SIZE_MAX_POOL
		this.SIZE_CORE_POOL=threadSize;
		this.SIZE_MAX_POOL =threadSize;
		return this;
	}
	public ThreadPoolManager build() {
	        return build(this);
	}
	/********自定义取名*********/
	private ThreadFactory namedThreadFactory = null;
	/**创建一个调度线程池*/
	private  ScheduledExecutorService scheduler = null;
	/**通过调度线程周期性的执行缓冲队列中任务*/
	ScheduledFuture<?> mTaskHandler = null;
	/**线程池*/
	private  ThreadPoolExecutor mThreadPool = null;
	
	private  ThreadPoolManager build(ThreadPoolManager builder) {
		 namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("mir-msg-montnets").build();
		 scheduler          = new ScheduledThreadPoolExecutor(SIZE_CORE_POOL,namedThreadFactory,mHandler);
		 mTaskHandler       = scheduler.scheduleAtFixedRate(mAccessBufferThread, 0,
							  PERIOD_TASK_QOS, TimeUnit.MILLISECONDS);
		 mThreadPool        =new ThreadPoolExecutor(SIZE_CORE_POOL, SIZE_MAX_POOL,
							TIME_KEEP_ALIVE,TimeUnit.SECONDS,mTaskQueue,namedThreadFactory,mHandler);
		 return this;
	 }
	/**
	 * 线程池超出界线时将任务加入缓冲队列
	 */
	private final RejectedExecutionHandler mHandler = new RejectedExecutionHandler() {
		@Override
		public void rejectedExecution(Runnable task, ThreadPoolExecutor executor) {
			mTaskQueue.offer(task);
		}
	};
	/**
	 * 将缓冲队列中的任务重新加载到线程池
	 */
	private final Runnable mAccessBufferThread = new Runnable() {
		@Override
		public void run() {
			if (hasMoreAcquire()) {
				mThreadPool.execute(mTaskQueue.poll());
			}
		}
	};

	
	public void perpare() {
		if (mThreadPool.isShutdown() && !mThreadPool.prestartCoreThread()) {
			@SuppressWarnings("unused")
			int startThread = mThreadPool.prestartAllCoreThreads();
		}
	}
	/**
	 * 获取线程激活数
	 * @return
	 */
	public int getNumActive(){
		return mThreadPool.getActiveCount();
	}
	/**
	 * 获取队列等待数
	 * @return
	 */
	public int getNumQueue(){
		return mTaskQueue.size();
	}
	/**
	 * 获取线程总数
	 * @return  已激活线程+线程中等等待线程
	 */
	public int getThreadTotal(){
		return getNumActive()+getNumQueue();
	}
	/**
	 * 向线程池中添加任务方法
	 */
	public void addExecuteTask(Runnable task) {
		if (task != null) {
			mThreadPool.execute(task);
		}
	}
	/**
	 * 消息队列检查方法
	 */
	private boolean hasMoreAcquire() {
		return !mTaskQueue.isEmpty();
	}
	protected boolean isTaskEnd() {
		if (mThreadPool.getActiveCount() == 0) {
			return true;
		} else {
			return false;
		}
	}
	public void shutdown() {
		//清空队列的任务
		mTaskQueue.clear();
		//关闭缓存任务
		scheduler.shutdown();
		//关闭线程池
		mThreadPool.shutdown();
}
}
