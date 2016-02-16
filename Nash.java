/*Thomas Veale Homework 1:
Purpose: Calculate pure and mixed strat. nash eq.

Input: A plain text file with tab septate outcomes, and comma separate player payout (row player first, then column player).
Output: The pure strategy nash eq. (if any), and the mixxed strat otherwise.


Crrated 2/14/2016
 */
 import java.util.*;
 import java.io.*;
 //import org.apache.commons.math3.*;
 
 public class Nash
 {
	 
	protected static String game;
	protected static String line;
	protected static ArrayList<ArrayList<Payout>> normForm;
	protected static ArrayList<ArrayList<Double>> rowp;
	protected static ArrayList<ArrayList<Double>> colp;
	protected static ArrayList<Payout> column;
	
	

		
	 public static void main(String[] args)
	 {
		 ArrayList<ArrayList<Payout>> normForm = new ArrayList<ArrayList<Payout>>(); //create our normal form game matrix
		 ArrayList<ArrayList<Double>> rowp = new ArrayList<ArrayList<Double>>();
		 ArrayList<ArrayList<Double>> colp = new ArrayList<ArrayList<Double>>();
		 try
		 {
			 game = new String(args[0]); //take input argument from main args
			
			 FileReader fr = new FileReader(game); //open the input file
			 
			 BufferedReader br = new BufferedReader(fr); //wrap it in a buffer
			 
			 while((line = br.readLine()) != null) { //while the file is not empty
					 
					 
				 Scanner scan = new Scanner(line); //make a new tokenizer for the line
				
				column = new ArrayList<Payout>(); //a temp arraylist to be appended to normalForm
					ArrayList<Double> row = new ArrayList<Double>(); //a temp arraylist to be appended to normalForm
					ArrayList<Double> col = new ArrayList<Double>(); //a temp arraylist to be appended to normalForm
				 while(scan.hasNext()){
						 String payout = scan.next();
						 
						 //System.out.println(payout);
						 
						 String[] parts = payout.split(",");
						 int rowPayout = Integer.parseInt(parts[0]); // get row payoff
						 int colPayout = Integer.parseInt(parts[1]); // get column payoff
						 Payout temp = new Payout(rowPayout, colPayout); //<p1, p2>
						 row.add(rowPayout/(Double)1.0);
						 col.add(colPayout/(Double)1.0);
						 //System.out.println(temp);
						 //System.out.println(column.size());
						 column.add(temp); //add the payout cell to row array	 <p1, p2>   <p2, p2> ...  <p1, p2>				 
						 
						 /*
						 for(int j = 0; j<column.size(); j++)
						 {
							 System.out.println(column.get(j).getP1() + " " +  column.get(j).getp2());
						 }
						 */
						 //System.out.println(parts[0] + " and " + parts[1]);

				 
				 } //end column loop			 
				 normForm.add(column); //append the column array list to normForm  <p1, p2>   <p2, p2> ...  <p1, p2>	
																				// <p1, p2>   <p2, p2> ...  <p1, p2>	
																				//   ...			...		...
																				// <p1, p2>   <p2, p2> ...  <p1, p2>	
																				
				 rowp.add(row);
				 colp.add(col);
				 //column.clear();
				 
				 scan.close(); //close the scanner
			 
			 }//end row loop
			
			/* 
			for(int i = 0; i<normForm.size(); i++)
			 {
				 ArrayList<Payout> temp  = normForm.get(i);
				 for(int j = 0; j<temp.size(); j++)
				 {
					 System.out.println(temp.get(j).getP1() + " " +  temp.get(j).getp2());
				 }
			 }*/
			 
			 br.close();	 
		 } //end try
		 catch(FileNotFoundException ex)
		 {
			 System.out.println( "Unable to open file `" + game + "'");
		 }//end catch1
		 catch(IOException ex) {
			 System.out.println("Error reading file `" + game + "'");
		 } //end catch2
		 
		 
		 
		 Payout[][] norm = new Payout[normForm.size()][]; //define new normal form game array
		 
		 /**convert the arraylist to array */
		 for(int i = 0; i<normForm.size(); i++)
		 {
			 column = normForm.get(i);
			 /*
			for(int j = 0; j<column.size(); j++)
			{
				System.out.println(column.get(j).getP1() + " " +  column.get(j).getp2());
			}*/
			 norm[i] = column.toArray(new Payout[column.size()]);
		 }
		 
		 Game g = new Game(norm, rowp, colp); //create the game
		 
		 
		 g.pureNash();
		 
		 //System.out.println(g);
		 //g.pureNash();
		 
		 
		
	     
	 } //end main
 } //end class
 
 
 //The payout field of each game
 class Payout
 {
	 int player1;
	 int player2;
	 boolean bestp1;
	 boolean bestp2;
	 
	 public Payout(int p1, int p2)
	 {
		 player1 = p1;
		 player2 = p2;
		 bestp1 = bestp2 = false;
	 }
	 
	 int getp1()
	 {
		 return player1;
	 }
	 
	 int getp2()
	 {
		 return player2;
	 }
	 
	 void p1best()
	 {
		 bestp1 = true;
	 }
	 
	 void p2best()
	 {
		 bestp2 = true;;
	 }
	 
	 
	 boolean p1nash()
	 {
		 return bestp1;
	 }
	 
	 boolean p2nash()
	 {
		 return bestp2;
	 }
	 
	 
	 public String toString()
	 {
		 String ret = new String("The payout for this cell is:" + "\n" + "row player:    " + player1 + "\n" + "column player: " + player2 + "\n");
		 return ret;
	 }
 }
 
 //the 2D matrix game wrapper
 class Game
 {
		//protected static ArrayList<ArrayList<Payout>> normForm;
		public Payout[] getColumn(Payout[][] array, int index){
			Payout[] column = new Payout[normalForm.length];
			for(int i=0; i<column.length; i++){
			   column[i] = array[i][index];
			}
			return column;
		}
		protected Payout[][] normalForm;
		protected Payout[] column;
		protected ArrayList<ArrayList<Double>> colp;
		protected ArrayList<ArrayList<Double>> rowp;
		
		public Game( Payout[][] norm, ArrayList<ArrayList<Double>> rowp, ArrayList<ArrayList<Double>> colp )
		{
			normalForm = norm;
			this.colp = colp;
			this.rowp = rowp;
			findNash();
		}
		//temp[j].getP2()
		public void findNash(){
			
			//calculates pure strat column player best choice
			 for(int i = 0; i<normalForm.length; i++) //extract rows
			 {
				Payout[] row  = normalForm[i];
				Payout max = row[0];
				for(int j = 1; j<row.length; j++)
				 {
					 //System.out.println(temp[j].getp2());
					if(row[j].getp2() > max.getp2())
						max = row[j];
				 }
				 
				 for(int j = 0; j<row.length; j++)
				 {
					 //System.out.println(temp[j].getp2());
					if(row[j].getp2() == max.getp2())
						row[j].p2best(); //make it true
						//System.out.println(temp[j].getp2());
				 }
				 
			 }


			 //calculates pure strat row player dominant strat
					  for(int i = 0; i<normalForm.length; i++) //extract rows
					 {
						    column = new Payout[normalForm.length];
							//Payout[] temp  = normalForm[i];
							column = getColumn(normalForm, i);	//new Payout[normalForm.length]; //how many rows
							//column[i] = normalForm[i][j];
							Payout max = column[0];
							//for(int k = 0; i<column.length;i++)
								//System.out.println(column[k]);
						for(int j =1; j<column.length; j++)
						{
							if(column[j].getp1() > max.getp1())
								max = column[j];					
						}
						for(int j =0; j<column.length; j++)
						{
							if(column[j].getp1() == max.getp1())
								column[j].p1best(); //make it true					
						}

						
					 
			 }
		}
		
		
		/**calculates and prints pure strat nash eq */
		public void pureNash(){	
			String ret = new String("\n");
			int count = 0;
			for(int i = 0; i<normalForm.length; i++)
			 {
				Payout[] temp  = normalForm[i];
				for(int j = 0; j<temp.length; j++)
				 { //output : row 2 col 1 6,1
					 if(temp[j].p1nash() && temp[j].p2nash()) //if they are both flagged as best options. this is a pure strat nash eq.
					 {
						 count++;
						 int ti = i+1;
						 int tj = j+1;
						 ret += "\n row " + ti + " col " + tj + " " + temp[j].getp1() + ","+ temp[j].getp2(); //add it to the list to print
					 }
				 }
				 
		
			 }
			 
			 if(count > 1){
				System.out.println("There are " + count + " pure-strategy Nash equilibria located at: \n" + ret);
			 }
			 else if(count == 1){
				System.out.println("There is one pure strategy Nash equilibrium located at: \n" + ret);
			 }
			 
			 else{
				// System.out.println("There is no pure strategy Nash equilibrium.. do something else?");
				 mixNash();
			 }
		}
		
		
		public void mixNash(){
		System.out.println("There is a mixed start Nash Equilibrium at + \n");
			
			
			simplex row = new simplex(rowp);
			simplex col = new simplex(colp);
			
			System.out.println("Row player: " + row);
			System.out.println("Col player: " + col);
			
			
		}
	 
		void columnMix(){
			
			
		}
		
		void rowMix(){
			for(int i = 0; i<normalForm.length; i++) //extract rows
			 {
			/*	 
				Payout[] row  = normalForm[i]; //extract a row
				//Payout max = row[0];
				for(int j = 1; j<row.length; j++)
				 {
					 //System.out.println(temp[j].getp2());
					if(row[j].getp2() > max.getp2())
						max = row[j];
				 }
				 
				 for(int j = 0; j<row.length; j++)
				 {
					 //System.out.println(temp[j].getp2());
					if(row[j].getp2() == max.getp2())
						row[j].p2best(); //make it true
						//System.out.println(temp[j].getp2());
				 }*/

			 }
			
		}

		public String toString(){
			 String ret = new String("normalForm.length : " + normalForm.length + "\n");
			 
			 for(int i = 0; i<normalForm.length; i++)
			 {
				 Payout[] temp  = normalForm[i];
				 ret += "Row length is: " + temp.length + "\n";
				 for(int j = 0; j<temp.length; j++)
				 {
					 ret += temp[j].getp1() + " " +  temp[j].getp2() + "\t";
				 }
				 ret += "\n";
			 }
			 
			 
			 return ret;
				 
		 }
	 
 }