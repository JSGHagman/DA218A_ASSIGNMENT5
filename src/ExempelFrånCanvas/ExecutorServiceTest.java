package ExempelFrånCanvas;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceTest
{
	
	public void start () 
	{
		//ExecutorService executorService = Executors.newSingleThreadExecutor();
		ExecutorService executorService = Executors.newFixedThreadPool(2);
	
		System.out.println("Task nr 1:");
		
		Runnable runnable1 = () -> 
		{						    
			System.out.println("Executing first task inside : " + Thread.currentThread().getName());
		};
			    
		System.out.println("Submit the first task specified by the runnable1 to the executor service.");
		executorService.submit(runnable1);

		 System.out.println("Task nr 2:");
		 Runnable runnable2 = () -> 
		 {
			 System.out.println("Executing second task inside : " + Thread.currentThread().getName());
		 };
		 System.out.println("Submit the second task specified by the runnable2 to the executor service.");
		 executorService.submit(runnable2);
		 executorService.shutdown();		 
	}
}
