 //import org.apache.commons.math3.*;
 
 import java.util.*;
 import java.lang.*;
public class simplex {
	
	//transposes a matrix
	//RealMatrix transpose(); 
	ArrayList<Double> simp = new ArrayList<Double>();
	public simplex(ArrayList<ArrayList<Double>> A)
	{
		simp = PrepareRun(A);
	}
	
	public String toString()
	{
		String ret = "[";
		for(int i=0; i< simp.size()-2; i++)
			ret += simp.get(i) + ", ";
		ret = ret.substring(0, ret.length()-2);
		ret +=" ]";
		return ret;
	}
	
	
	/** hwo to create a matrix*/
	//double[][] matrixData = { {1d,2d,3d}, {2d,5d,3d}};
	//RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
	// works great
	static ArrayList<ArrayList<Double>> transpose(ArrayList<ArrayList<Double>> M)
	{
		ArrayList<ArrayList<Double>> T = new ArrayList<ArrayList<Double>>();
		
		int mNumRows = M.size();
		int mNumCols = M.get(0).size();
		
		for(int mCol=0; mCol<mNumCols; mCol++)
		{
			ArrayList<Double> tRow = new ArrayList<Double>();
			for(int mRow=0; mRow <mNumRows; mRow++)
			{
				tRow.add(M.get(mRow).get(mCol));
			}
			
			T.add(tRow);
		}
		
		return T;
	}
	

	static ArrayList<ArrayList<Double>> SetLinearProgram(ArrayList<ArrayList<Double>> A)
	{
		ArrayList<Double> maxFunc = new ArrayList<Double>();
		ArrayList<Double> b = new ArrayList<Double>();
		
		ArrayList<ArrayList<Double>> T = transpose(A); //transpose input
		//System.out.println(T.size());
		for(int iRow = 0; iRow < T.size(); iRow++)
		{
			//int loop = T.get(iRow).size();
			//System.out.println(loop);
			//System.out.println(T.size());
			for(int iCol = 0; iCol < T.get(iRow).size(); iCol++)
			{
				//System.out.println(T.get(iRow).get(iCol));
				Double temp = (Double) T.get(iRow).get(iCol).doubleValue()*(-1.0); //negate value
				T.get(iRow).set(iCol, temp); //replace it
				//System.out.println(T.get(iRow).get(iCol));
			}
			//System.out.println(T.get(iRow));
			T.get(iRow).add(  1.0 );
			//System.out.println(T.get(iRow));
			T.get(iRow).add( -1.0 );	
		}	
		
		for(int iRow = 0; iRow < T.size(); iRow++)
		{
			b.add(0.0);
		}
		
		int rowSize = T.get(0).size();
		//System.out.println(T.get(0));
		
		//System.out.println(rowSize);
		//double[] rowEq1Temp = new double[rowSize]; //initalize an array
		//Arrays.fill(rowEq1Temp, 1.0);
		//ArrayList<Double> rowEq1 = Arrays.asList(rowEq1Temp); //make it dynamic
		
		ArrayList<Double> rowEq1 = new ArrayList<Double>(rowSize);
		for (int i = 0; i<rowSize; i++)
			rowEq1.add(1.0);
		
		
		rowEq1.set((rowSize-2), 0.0);
		rowEq1.set((rowSize-1), 0.0);

		T.add(rowEq1);
		b.add(1.0);

		ArrayList<Double> rowEq2 = new ArrayList<Double>(rowSize);
		for (int i = 0; i<rowSize; i++)
			rowEq2.add(-1.0);		
		//double[] rowEq2Temp = new double[rowSize]; //initalize an array
		//Arrays.fill(rowEq2Temp, (-1.0));
		//ArrayList<Double> rowEq2 = Arrays.asList(rowEq2Temp); //make it dynamic
		//set the last two elements as 0?
		
		rowEq2.set((rowSize-2), 0.0);
		rowEq2.set((rowSize-1), 0.0);
		
		T.add(rowEq2);
		b.add(-1.0);
		
		for(int i = 0; i< rowSize; i++)
		{
			if(i<rowSize-2)
				maxFunc.add(1.0);
			else if(i<rowSize-1)
				maxFunc.add(1.0);
			else
				maxFunc.add(-1.0);
		}
		
		return SetSimplex(maxFunc, T, b);
	}
	


	public static ArrayList<Double> PrepareRun(ArrayList<ArrayList<Double>> A)
	{
		ArrayList<ArrayList<Double>> simplex = SetLinearProgram(A);
		double max = 0.0;
		ArrayList<Double> result = DoSimplex(simplex, max);
		int size = result.size();
		return result;
	}
	
	
	static ArrayList<ArrayList<Double>> SetSimplex( ArrayList<Double> maxFunction, ArrayList<ArrayList<Double>> A, ArrayList<Double> b)
	{
		ArrayList<ArrayList<Double>> simplex = new ArrayList<ArrayList<Double>>();
		
		int numVariables = maxFunction.size();
		int numEquations = A.size();
		int numCols = numVariables + numEquations + 2;
		
		
		for(int iRow = 0; iRow < numEquations; iRow++)
		{

			ArrayList<Double> row = new ArrayList<Double>(numCols);
			for (int i = 0; i<numCols; i++)
				row.add(0.0);	
				
						
			for(int iCol =0; iCol<numVariables; iCol++)
			{
				row.set(iCol, A.get(iRow).get(iCol));

			}
			row.set((numVariables + iRow), 1D);
			row.set((numCols-1), b.get(iRow));
			
			
			simplex.add(row);
		}

			ArrayList<Double> lastRow = new ArrayList<Double>(numCols);
			for (int i = 0; i<numCols; i++)
				lastRow.add(0.0);			
			
			for(int iVar = 0; iVar < numVariables; iVar++)
			{
				lastRow.set(iVar, (0-maxFunction.get(iVar)));
			}
			int ind = numVariables + numEquations;
			lastRow.set(ind, 1.0);
			simplex.add(lastRow);
			return simplex;
	}
	
	
	static boolean GetPivots(ArrayList<ArrayList<Double>> simplex, Int pivotCol, Int pivotRow, Boolean noSolution)
	{
		int numRows = simplex.size();
		int numCols = simplex.get(0).size();
		int numVariables = numCols - numRows -1;
		
		
		noSolution.equals(false);
		
		double min = 0;
		
		for(int iCol = 0; iCol< (numCols -2); iCol++)
		{
			double value = simplex.get(numRows - 1).get(iCol);
			if(value<min)
			{
				pivotCol.value = iCol;
				min = value;
			}
		}
		
		if(min == 0)
		{
			return false;
		}
		double minRatio = 0.0;
		boolean first = true;
		for(int iRow = 0; iRow<(numRows-1); iRow++)
		{
			double value = simplex.get(iRow).get(pivotCol.value);
			if(value >0.0)
			{
					double colValue = simplex.get(iRow).get(numCols-1);
					double ratio = colValue/value;
				
				
				if((first || ratio < minRatio) && ratio >= 0.0)
				{
					minRatio = ratio;
					pivotRow.value = iRow;
					first = false;
				}	
			}				
		}
		noSolution = first;
		return !noSolution;

	}
	
	static ArrayList<Double> DoSimplex(ArrayList<ArrayList<Double>> simplex, double max)
	{
		 Int pivotCol = new Int(0); 
		 Int pivotRow = new Int(0);
		
		int numRows = simplex.size();
		int numCols = simplex.get(0).size();
		Boolean noSolution = false;
		
		while( GetPivots(simplex, pivotCol, pivotRow, noSolution))
		{
			double pivot = simplex.get(pivotRow.value).get(pivotCol.value);
			
			for(int iCol=0; iCol < numCols; iCol++)
			{
				double temp = simplex.get(pivotRow.value).get(iCol) / pivot;
				simplex.get(pivotRow.value).set(iCol, temp);
			}

			for(int iRow = 0; iRow < numRows; iRow++)
			{
				if(iRow != pivotRow.value)
				{
					double ratio = -1.0 * simplex.get(iRow).get(pivotCol.value);
					for(int iCol = 0; iCol<numCols; iCol++)
					{
						double temp = simplex.get(pivotRow.value).get(iCol) * ratio + simplex.get(iRow).get(iCol);
						simplex.get(iRow).set(iCol, temp);
					}
				}
				
			}
		}

		if(noSolution)
		{
			ArrayList<Double> vec = new ArrayList<Double>();
			System.out.println("no solution:");
			return vec;
		}
		
		//
		max = simplex.get(numRows-1).get(numCols-1);
		int numVariables = numCols-numRows -1;
		ArrayList<Double> x = new ArrayList<Double>(numVariables);
		for (int i = 0; i<numVariables; i++)
			x.add(0.0);		
		
		
		
		
		for(int iCol = 0; iCol < numVariables; iCol++)
		{
			boolean isUnit = true;
			boolean first = true;
			double value = 0.0;
			for(int j= 0; j<numRows; j++)
			{
				if(simplex.get(j).get(iCol) == 1.0 && first)
				{
					first = false;
					value = simplex.get(j).get(numCols-1);
				}
				else if(simplex.get(j).get(iCol) != 0.0)
				{
					isUnit = false;
				}
			}

			if(isUnit && !first)
				x.set(iCol, value);
			else
				x.set(iCol, 0.0);
		}
		
		return x;
	}
	/*
	public static void main (String[] args)
	{
		
		//trantest();
		trantest2();

	}
	*/
	
	static void run1()
	{/*
	
	 A.push_back(commaSeparatedStringToIntVector(" 1.0 ,  2.0 ,  3.0 , -1.0 ,  1.5"));
 A.push_back(commaSeparatedStringToIntVector("-2.0 , -1.5 , -1.0 ,  1.0 ,  3.0"));
 A.push_back(commaSeparatedStringToIntVector(" 0.0 ,  1.0 ,  0.5 , -2.0 ,  2.5"));
 A.push_back(commaSeparatedStringToIntVector(" 2.0 , -1.0 ,  0.5 , -3.0 , -2.5"));
 A.push_back(commaSeparatedStringToIntVector(" 1.5 ,  2.0 , -1.5 ,  2.0 ,  3.5"));
 */
			ArrayList<ArrayList<Double>> normalvect = new ArrayList<ArrayList<Double>>();
			ArrayList<Double> r1 =  new ArrayList<Double>(Arrays.asList(1.0 ,  2.0 ,  3.0 , -1.0 ,  1.5));
			ArrayList<Double> r2 =  new ArrayList<Double>(Arrays.asList(-2.0 , -1.5 , -1.0 ,  1.0 ,  3.0));
			ArrayList<Double> r3 =  new ArrayList<Double>(Arrays.asList(0.0 ,  1.0 ,  0.5 , -2.0 ,  2.5));
			ArrayList<Double> r4 =  new ArrayList<Double>(Arrays.asList(2.0 , -1.0 ,  0.5 , -3.0 , -2.5));
			ArrayList<Double> r5 =  new ArrayList<Double>(Arrays.asList(1.5 ,  2.0 , -1.5 ,  2.0 ,  3.5));
			normalvect.add(r1);
			normalvect.add(r2);		
			normalvect.add(r3);		
			normalvect.add(r4);		
			normalvect.add(r5);		
			
			PrepareRun(normalvect);
		
		
		
	}
	
	
static void trantest2()
	{
			//test case:
			ArrayList<ArrayList<Double>> normalvect = new ArrayList<ArrayList<Double>>();	 //4,8	2,0
																							//6,2	0,8
			ArrayList<Double> r1 =  new ArrayList<Double>(Arrays.asList(5d, -5d));
			ArrayList<Double> r2 =  new ArrayList<Double>(Arrays.asList(6d, 2d));
			ArrayList<Double> r3 =  new ArrayList<Double>(Arrays.asList(6d, 3d));
			normalvect.add(r1);
			normalvect.add(r2);
			normalvect.add(r3);
			PrepareRun(normalvect);
			
			
	}
	static void trantest()
	{
			//test case:
			ArrayList<ArrayList<Double>> normalvect = new ArrayList<ArrayList<Double>>();	 //4,8	2,0
			ArrayList<ArrayList<Double>> normalvect2 = new ArrayList<ArrayList<Double>>();	 //4,8	2,0
																							//6,2	0,8
			ArrayList<Double> r1 =  new ArrayList<Double>(Arrays.asList(4d, 8d));
			ArrayList<Double> r2 =  new ArrayList<Double>(Arrays.asList(2d, 8d));
			normalvect.add(r1);
			normalvect.add(r2);
			PrepareRun(normalvect);
			
			
			ArrayList<Double> rr1 =  new ArrayList<Double>(Arrays.asList(4d, 2d));
			ArrayList<Double> rr2 =  new ArrayList<Double>(Arrays.asList(6d, 0d));
			normalvect2.add(rr1);
			normalvect2.add(rr2);
			
			//ArrayList<ArrayList<Double>> it = transpose(normalvect);
			PrepareRun(normalvect2);

	}

}
	class Int{
		public int value;
		
		public Int(int i){i = value;}
	}