package de.hoegertn.demo.cxfsimple;

public class DemoStart {
	
	public static void main(String[] args) {
		SpringStarter starter = new SpringStarter() {
			// nothing to do here
		};
		
		try {
			starter.doStart();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Call this to stop the service
		
		// try {
		// starter.doStop();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
	
}
