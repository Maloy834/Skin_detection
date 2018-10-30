package detection;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
     try {
    	 String filepath=("H:\\workspace\\Skin_detection\\TestImage\\mypic.jpg");
		new SkinDetection(filepath);
    	 //SkinDetection sk=new SkinDetection(filepath);
    	 //sk.createMask(filepath);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
