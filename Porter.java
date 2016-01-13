import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.PrintWriter;


/*	Developer Helper
*		Copies the files in the array from this location to the location of android project
* 		A crude way to keep everything sychronized and let me work in the local environment
		without worrying about syncing with the other stuff
*/

class Porter{
	
	static String prefix = "package com.xanderfehsenfeld.flippycakes;";
	static String[] files_to_port = new String[]{
		"Cell.java",
		"CellularItem.java",
		"Circle.java",
		"Drawable.java",
		"FaceComparator.java",
		"FryingPan.java",
		"HoneyComb.java",
		"Item.java",
		"Pancake.java",
		"PhysicsPresence.java",
		"Quadrilateral.java",
		"Region3D.java",
		"ThreeDTransform.java",
		"Triangle.java",
		"VectorOps.java",
		"GameEnv.java",
		"NumberGenerator.java"
		
		
	};
	
	
	public static void main(String[] args){
		String currFile = "";
		
		
		//List<Integer> list = new ArrayList<Integer>();
		File file;
		// = new File("file.txt");
		BufferedReader reader = null;
		PrintWriter writer = null; 
		
		int linesRead = 0;
		
		for (int i = 0; i < files_to_port.length; i ++ ){
			file = new File( files_to_port[i] );

			try {
				writer = new PrintWriter("../../app/src/main/java/com/xanderfehsenfeld/flippycakes/" + files_to_port[i]);
				writer.println( prefix );
				reader = new BufferedReader(new FileReader(file));
				String text = null;

				while ((text = reader.readLine()) != null) {
					//list.add(Integer.parseInt(text));
					writer.println( text );
					linesRead++;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
					if (writer != null){
						writer.close();
					}
				} catch (IOException e) {
				}
			}
		}
		
		System.out.println( "linesRead: " + linesRead );
	}



}