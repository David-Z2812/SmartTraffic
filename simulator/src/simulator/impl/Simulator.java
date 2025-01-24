package simulator.impl;

import simulator.impl.Simulator_Publisher;

public class Simulator {
	
	protected Simulator_Publisher publisher = null;
	
	public Simulator() {
		this.publisher = new Simulator_Publisher();
		this.publisher.connect();

	}

	public void Step(int index) {
		this.publisher.publishStep(index);
	 }
	
}
