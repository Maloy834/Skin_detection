package detection;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;

public class SkinDetection {

	File sourceimage = null;

	public SkinDetection(String filepath) throws IOException {

		final File dir = new File("H:/workspace/Skin_detection/image");
		final File dir1 = new File("H:/workspace/Skin_detection/mask");
		// File fout = new File("H:/workspace/Skin_detection/image.txt");
		// FileOutputStream fos = new FileOutputStream(fout);
		int skin[][][] = new int[256][256][256];
		int nonskin[][][] = new int[256][256][256];
		String[] image_path = new String[87];
		String[] mask_path = new String[87];
		if (dir.isDirectory()) {

			int count = 0;
			for (final File f : dir.listFiles()) {

				BufferedImage img = null;

				try {
					img = ImageIO.read(f);
					image_path[count] = f.getName();
					count++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (dir1.isDirectory()) {
			int cnt = 0;
			for (final File f1 : dir1.listFiles()) {
				try {
					BufferedImage img = ImageIO.read(f1);
					mask_path[cnt] = f1.getName();
					cnt++;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < mask_path.length; i++) {
			File f = new File("H:/workspace/Skin_detection/image/" + image_path[i]);
			File f1 = new File("H:/workspace/Skin_detection/mask/" + mask_path[i]);
			try {
				BufferedImage img = ImageIO.read(f);
				BufferedImage img1 = ImageIO.read(f1);
				int height = img.getHeight();
				int width = img.getWidth();
				for (int k = 0; k < height; k++) {
					for (int j = 0; j < width; j++) {
						Color c = new Color(img.getRGB(j, k));
						Color c1 = new Color(img1.getRGB(j, k));
						// System.out.println(c1.getRed()+" "+c1.getGreen()+"
						// "+c1.getBlue());
						if (c1.getRed() > 240 && c1.getGreen() > 240 && c1.getBlue() > 240) {
							skin[c.getRed()][c.getGreen()][c.getBlue()]++;
						} else
							nonskin[c.getRed()][c.getGreen()][c.getBlue()]++;

					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
      training(skin,nonskin);
		createMask(filepath);
	}

	public void training(int skin[][][], int nonskin[][][]) throws IOException {
		File fout1 = new File("H:/workspace/Skin_detection/mask.txt");
		FileOutputStream fos1 = new FileOutputStream(fout1);

		BufferedWriter bw1 = new BufferedWriter(new OutputStreamWriter(fos1));

		for (int p = 0; p < 256; p++) {
			for (int j = 0; j < 256; j++) {
				for (int k = 0; k < 256; k++) {
					// System.out.println("Skin:"+skin[p][j][k]+"
					// Nonskin:"+nonskin[p][j][k]);
					try {
						bw1.write(p+" "+j+" "+k+" "+skin[p][j][k] + " " + nonskin[p][j][k]);
						bw1.write("\n");

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		bw1.close();
	}
	
	public void createMask(String filepath) throws IOException
	{
		BufferedReader br=null;
		FileReader fr=null;
		int [][][] arr=new int[256][256][256];
		int [][][] arr1=new int[256][256][256];
		try {
			fr=new FileReader("H:/workspace/Skin_detection/mask.txt");
			br=new BufferedReader(fr);
			String currentLine;
			
			//String [] str=new String[2];
			while((currentLine = br.readLine()) != null)
			{
				String str[]=currentLine.split(" ");
				int a=Integer.parseInt(str[0].trim());
				int b=Integer.parseInt(str[1].trim());
				int c=Integer.parseInt(str[2].trim());
				int d=Integer.parseInt(str[3].trim());
				int e=Integer.parseInt(str[4].trim());
				arr[a][b][c]=d;
				arr1[a][b][c]=e;
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File f = new File(filepath);
		
		BufferedImage img = ImageIO.read(f);
		int height=img.getHeight();
		int width=img.getWidth();
		
		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				Color c=new Color(img.getRGB(j, i));
				double skincount=(double)arr[c.getRed()][c.getGreen()][c.getBlue()] * 1.0;
				double nonskincount=(double)arr1[c.getRed()][c.getGreen()][c.getBlue()] * 1.0;
				System.out.print(".");
				if(nonskincount!=0.0 && skincount/nonskincount >0.25)
				{
					Color white=new Color(255, 255, 255);
					img.setRGB(j, i, white.getRGB());
				}
				else{
					Color black=new Color(0, 0, 0);
					img.setRGB(j, i, black.getRGB());
				}
			}
		}
		try {
			ImageIO.write(img, "jpg", new File("H:/workspace/Skin_detection/TestMask/mask.jpg"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
