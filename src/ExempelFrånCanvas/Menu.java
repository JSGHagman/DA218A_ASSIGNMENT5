package ExempelFrånCanvas;

import java.util.Scanner;

public class Menu 
{
	public int ShowMenu()
	{
		  System.out.println();
		  System.out.println("Enter one of the following commands:");
		  System.out.println("1  A simple Executors example.");
		  System.out.println("2  Test ThreadPoolExecutor.");
		  System.out.println("3  Test ExecutorService");
		  System.out.println("4  Test Callable");
		  System.out.println("0  Exit");
		  
		  Scanner scanner = new Scanner(System.in);
		  System.out.println();
		  int choice = scanner.nextInt();
		  return choice;
	  }
	  
	  
	  
	  public void start()
	  {
		  int choice = -1;
	
		  do 
		  {
		      choice = ShowMenu();
	
		      switch (choice)
		      {
		      	case 0:
		      		System.out.println("Pleaz kam back!");
		      		break; //exit
		      		
		      	case 1:
				      System.out.println("0. A simple Executors example!");           
				      ExecutorsTest executorEx = new ExecutorsTest();
				      executorEx.start();
				      break;
		      
		          case 2:
		              //2. Test ThreadPoolExecutor
		        	  System.out.println("1. Example ThreadPoolExecutor is starting! ");  
		        	  ThreadPoolTest threadPool = new ThreadPoolTest();
		        	  threadPool.DriveCars();
		              break;
		              
		          case 3: 
		        	  //3. Test ExecutorService   
		              System.out.println("2. Example ExecutorService pool is starting! ");       
		              ExecutorServiceTest  exService = new ExecutorServiceTest();
		              exService.start();
		              break;
		              
		          case 4: 
		        	  //4. Test Callable   
		              System.out.println("3. Example using a Callable ");       
		              CallableTest  callable = new CallableTest();
		              callable.start();
		              break;
		              
		          default:
		              System.out.println("Choice must be a value between 1 and 4 or 0 to exit!");
		      }   
		  } while (choice != 0);
	  }

}
