package ExempelFrånCanvas;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallableTest 
{
		 Callable <String> task = () -> 
		 {
			try 
			{
			    TimeUnit.SECONDS.sleep(1);
			    return "Greetings from Callable";
			}
			catch (InterruptedException e) 
			{
			    throw new IllegalStateException("task interrupted", e);	
			}
		 };
	
	public void start()
	{
		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<String> future = executor.submit(task);
	
		System.out.println("future done? " + future.isDone());
	
		String result = "";
		try 
		{
			result = future.get();
		} 
		catch (InterruptedException e) 
		{
			
			e.printStackTrace();
		} 
		catch (ExecutionException e) 
		{			
			e.printStackTrace();
		}
	
		System.out.println("future done? " + future.isDone());
	    System.out.print("result from the future: " + result);		
	    executor.shutdown();
	 }
}
