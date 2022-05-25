package ExempelFrånCanvas;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolTest 
{
	public void DriveCars()
	{
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		  
		Double speed = 50.0;
		System.out.println("Maximum threads inside the pool " + executor.getActiveCount());
	
		for (Integer i = 0; i <= 10; i++)
	    {
	  	  	Task task = new Task("Car no " + i.toString(), (i+1)*speed);  
	  	  	executor.execute(task);
	    }
		System.out.println("After the loop:  Number threads inside the pool " + executor.getActiveCount());          	  	  
	    executor.shutdown();
	}
}
