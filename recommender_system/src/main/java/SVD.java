import Jama.EigenvalueDecomposition;
import Jama.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Soamya Agrawal
 */
public class SVD {

    /**
     * @param rows    the number of rows
     * @param columns the number of columns
     * @throws java.io.IOException
     */

    public double getSVDError(int rows, int columns) throws IOException {

        double[][] a = new double[rows][columns];

        BufferedReader br = new BufferedReader(new FileReader("/Users/soamya.agrawal/Desktop/BITS_Assignment/Information_Retrieval/Recommender_System/recommender_system/src/main/resources/movies.csv"));
        HashMap<Integer, Integer> hmap = new HashMap<>();
        int i;
        br.readLine();

        //read movie file , store values in the array.
        for (i = 0; i < columns; i++) {
            String line = br.readLine();
            String[] line1 = line.split(",");
            hmap.put(Integer.parseInt(line1[0]), i);
        }

        br.close();

        // read user,movie and rating from file 
        BufferedReader br1 = new BufferedReader(new FileReader("/Users/soamya.agrawal/Desktop/BITS_Assignment/Information_Retrieval/Recommender_System/recommender_system/src/main/resources/ratings20.csv"));
        br1.readLine();
        int l;
        int m;
        int j;
        double r;
        int numMovies = 0;
        int[] rowsToNumMovies = new int[] {0,0,5,81,101,172,198,205,274,300,315,333,343,358,375,377,646,651,733,784,1120,1170};
        numMovies = rowsToNumMovies[rows];
        for (i = 0; i < numMovies; i++) {
            String line = br1.readLine();
            String[] line2 = line.split(",");
            l = Integer.parseInt(line2[0]);

            m = hmap.get(Integer.parseInt(line2[1]));
            r = Double.parseDouble(line2[2]);
            a[l][m] = r;

        }

        br1.close();

        //matrix a Obtained from the given data
        /*System.out.println();
        System.out.println("**********************************************************");
        System.out.println("A Matrix");
        System.out.println("**********************************************************");
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
*/
        //let's make U matrix
        //transpose of A matrix
        double[][] at = new double[columns][rows];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                at[j][i] = a[i][j];
            }
        }

        /*
        // printing a transpose matrix
        for (i = 0; i < columns; i++) {
            for (j = 0; j < rows; j++) {
                System.out.print(at[i][j] + " ");
            }
            System.out.println();
        }*/

        //multiply a (rowsxcolumns) with atranspose (columnsxrows) to Matrix U
        double[][] u = new double[rows][rows];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                for (int k = 0; k < columns; k++) {
                    u[i][j] += a[i][k] * at[k][j];
                }
            }
        }

       /* for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                System.out.print(u[i][j] + " ");
            }
            System.out.println();
        }*/

        Matrix mat = new Matrix(u);
        EigenvalueDecomposition evd = new EigenvalueDecomposition(mat);
        Matrix sigma = evd.getD();
        double[][] sigmaa = sigma.getArray();

        Matrix m1 = evd.getV();
        double[][] b = m1.getArray();

//        System.out.println("**********************************************************");
//        System.out.println("**********************************************************");
//        System.out.println("Sigma matrix");
        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                if (sigmaa[i][j] < 0) {
                    sigmaa[i][j] = 0;

                }
                //System.out.print(sigmaa[i][j] + " ");
            }

//            System.out.println();
        }

        //multiply at (columnsxrows) with a (rowsxcolumns) to obtain U transpose matrix
        double[][] uT = new double[columns][columns];
        for (i = 0; i < columns; i++) {
            for (j = 0; j < columns; j++) {
                for (int k = 0; k < rows; k++) {
                    uT[i][j] += at[i][k] * a[k][j];
                }
            }
        }

        Matrix mNew = new Matrix(uT);
        EigenvalueDecomposition evd1 = new EigenvalueDecomposition(mNew);

        Matrix m11 = evd1.getV();
        double[][] b1 = m11.getArray();

        double[][] b1t = new double[columns][columns];
        for (i = 0; i < columns; i++) {
            for (j = 0; j < columns; j++) {
                b1t[j][i] = b1[i][j];
            }
        }

        double temp;
        for (i = 0; i < columns; i++) {
            for (j = 0; j < columns / 2; j++) {
                temp = b1[i][j];
                b1[i][j] = b1[i][columns - j - 1];
                b1[i][columns - j - 1] = temp;
            }
        }

        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows / 2; j++) {
                temp = b[i][j];
                b[i][j] = b[i][rows - j - 1];
                b[i][rows - j - 1] = temp;
            }
        }
        
        /*System.out.println("**********************************************************");
        System.out.println("**********************************************************");
        System.out.println("U matrix");
        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                System.out.print(b[i][j] + "    ");
            }
            System.out.println();
        }*/

        //for sigma matrix Arranging the terms in decreasing order
        for (i = 0; i < rows / 2; i++) {
            temp = sigmaa[i][i];
            sigmaa[i][i] = sigmaa[rows - i - 1][rows - i - 1];
            sigmaa[rows - i - 1][rows - i - 1] = temp;
        }

        double[][] sigmaFinal = new double[rows][columns];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                sigmaa[i][j] = Math.sqrt(sigmaa[i][j]);
                sigmaFinal[i][j] = sigmaa[i][j];
            }
        }

     /*   System.out.println();
        System.out.println("**********************************************************");
        System.out.println("Sigma Final matrix");
        System.out.println("**********************************************************");
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                System.out.print(sigmaFinal[i][j] + "     ");
            }
            System.out.println();
        }
*/
        //for v transpose matrix arranging the terms in Decreasing order
        for (i = 0; i < columns / 2; i++) {
            for (j = 0; j < columns; j++) {
                temp = b1t[i][j];
                b1t[i][j] = b1t[columns - i - 1][j];
                b1t[columns - i - 1][j] = temp;
            }
        }

        /*System.out.println("V Transpose matrix");
        for (i = 0; i < columns; i++) {
            for (j = 0; j < columns; j++) {
                System.out.print(b1t[i][j] + "    ");
            }
            System.out.println();
        }*/

        double[][] temp1 = new double[rows][columns];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                for (int k = 0; k < columns; k++) {
                    temp1[i][j] += (a[i][k] * b1[k][j]);
                }
            }
        }
/*System.out.println();
        System.out.println("**********************************************************");
        System.out.println("Calculated A*V matrix");
        System.out.println("**********************************************************");
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                System.out.print(temp1[i][j] + "    ");
            }
            System.out.println();
        }*/

        double[][] finalU = new double[rows][rows];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                if (sigmaFinal[j][j] != 0) {
                    finalU[i][j] = temp1[i][j] / sigmaFinal[j][j];
                }
            }
        }

        double[][] temp2 = new double[rows][columns];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                for (int k = 0; k < rows; k++) {
                    temp2[i][j] += sigmaa[i][k] * temp1[k][j];
                }
            }
        }

        /**Calulating U matrix for known V matrix so that we get unique eigen values**/
/*System.out.println();
        System.out.println("**********************************************************");
        System.out.println("Calculated U matrix");
        System.out.println("**********************************************************");
        for (i = 0; i < rows; i++) {
            for (j = 0; j < rows; j++) {
                System.out.print(finalU[i][j] + "    ");
            }
            System.out.println();
        }*/

        double error = 0;
        int k = 0;

        // Calculating U * Sigma Matrix
        double[][] usigcal = new double[rows][columns];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                for (k = 0; k < rows; k++) {
                    usigcal[i][j] += finalU[i][k] * sigmaFinal[k][j];
                }
            }
        }

        // Calcuating U * Sigma * V Transpose matrix
        double[][] amatcal = new double[rows][columns];
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                for (k = 0; k < columns; k++) {
                    amatcal[i][j] += usigcal[i][k] * b1t[k][j];
                }
            }
        }

        //error calculation
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                error += Math.pow((a[i][j] - amatcal[i][j]), 2);
            }
        }

      /*  System.out.println();
        System.out.println("**********************************************************");
        System.out.println("Calculated finally the A matrix");
        System.out.println("**********************************************************");
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                System.out.print(amatcal[i][j] + "    ");
            }
            System.out.println();
        }*/
        error = Math.sqrt(error);
       /* System.out.println();
        System.out.println("**********************************************************");
        System.out.println("Error is  " + error);
        System.out.println("**********************************************************");*/
        hmap.clear();
        return error;
    }

}
