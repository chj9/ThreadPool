# ThreadPool
java线程连接池写法和使用方法

其实如果不需要任何特殊设置的话，我们通常使用 Executors 工厂方法来配置ThreadPoolExecutor。
方法使用如下
```
//池大小
 int poolSize = ComConfig.poolSize;
 ExecutorService exec = Executors.newFixedThreadPool(poolSize);   
 //执行任务
for(int i=0;i<poolSize;i++){
  TaskDemo task = new TaskDemo();
   exec.execute(task);
 }
 //关闭线程池
if(exec!=null)
	exec.shutdown();	
```
想具体了解线程池可以看下这篇文章
https://my.oschina.net/xionghui/blog/494698