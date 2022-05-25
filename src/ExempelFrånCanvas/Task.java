package ExempelFrånCanvas;


//Code Example:  Farid Naisan, 2015-12-09

import java.util.concurrent.TimeUnit;

public class Task implements Runnable
{
    private String carName;
    private Double speed;
 
    public Task(String name, Double speed)
    {
        this.carName = name;
        this.speed = speed;
    }
        
    @Override
    public void run()
    {
    	Drive();
    }
    private void Drive()
    {
        try
        {
            Long drivingTime = (long) (Math.random() * 10.0);
            System.out.println( carName + " is driving at speed " + speed + ".");
            TimeUnit.SECONDS.sleep(drivingTime);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}


