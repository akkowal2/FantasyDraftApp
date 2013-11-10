package application;

public class OrderList extends Thread{
	DraftController manager;
	
	public OrderList(DraftController manager){
		this.manager=manager;
		start();
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			manager.rotateDraftOrder();	
		}
		
	}
	
}
