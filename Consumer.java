package atomicB;

import java.util.concurrent.Semaphore;

public class Consumer extends Thread{
	private static AtomicB sharedObj=new AtomicB();
	static {
		sharedObj.start();
	}
	private static Semaphore mutex=new Semaphore(1);
	private static int posId;
	private int id=posId++;
	private static int cnt=0;
	public void run() {
		try {
		 while(true) {
			sharedObj.full[id].acquire();// wait for element
		    int a=sharedObj.consume();
		    System.out.println("Consumer "+id + " - element "+a);
		    sleep((long) (Math.random()*1000));
		    mutex.acquire();// mutual exclusion because cnt is shared between consumers
		    cnt++;
		    if (cnt==AtomicB.N) {// wake up producer  - Nth consumer red element
		    	cnt=0;
		    	sharedObj.empty.release();
		    }
		    mutex.release();
		 }
	}
		catch(InterruptedException e) { }
	}
	public static void main(String[] args) {
		Consumer[] array=new Consumer[AtomicB.N];
		for (int i=0;i<AtomicB.N;i++) {
			array[i]=new Consumer();
			array[i].start();
		}
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0;i<AtomicB.N;i++) {
			array[i].interrupt();
		}
		sharedObj.interrupt();
	}

}
