package atomicB;
/*in this problem we have one producer and N consumers
 * when  process producer make one element it waits until all N consumers read that element 
 */
import java.util.concurrent.Semaphore;

public class AtomicB extends Thread{
	int buf;
	static int N=5;
	Semaphore empty=new Semaphore(1);
	Semaphore[] full=new Semaphore[N];// one semaphore for each consumer
	
	{
	 for (int i=0;i<N;i++) {
		 full[i]=new Semaphore(0);
	 }
	}
	public void run() {//process producer
      try {
    	while (!interrupted()) {
		empty.acquire();//wait for all consumers to read the buf

	      int a=produce();
	      System.out.println("Producer  -  element "+a);
		  sleep((long) (Math.random()*1000));
	      for (int i=0;i<N;i++) { // notify consumers that buf is ready
	    	  full[i].release();
	      }
		
	     } 
      }
      catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	public int produce() {
		buf=(int)(Math.random()*100);
		return buf;
	}
	public int consume() {
		return buf;
	}

}
