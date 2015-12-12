package org.snapscript.interpret;


//introductory program to demonstrate Monte Carlo simulation under Java

//simulates the tossing of a coin 10 times, calculating various
//probabilistic quantities:

// P(6 heads)
// P(2 heads in the first 4 tosses | 6 heads total)
// E(square of the total number of heads)

public class MonteCarlo  {

static final int NREPS = 100000;  // number of times the 10-toss
                                 // experiment is to be repeated 

public static void main(String[] Args) 

{  int n6 = 0;  // # of repetitions in which there are 6 heads
       int n246 = 0;  // # of reps in which the 1st 4 tosses yield 2 heads,
                  // among reps in which there are 6 heads total
       int sumH2 = 0;  // sum of the squares of heads counts
       int heads;
       int outcome;
       int rep;
   boolean event24;

   for (rep=0; rep < NREPS; rep++)  {
      heads = 0;
      event24 = false;
      for (int toss=0; toss < 10; toss++)  {
         outcome = (int) (2 * random());
         if (outcome == 1)  {  // heads
            heads++;
         }
         if (toss == 3 && heads == 2) event24 = true;
      }
      if (heads == 6)  {
         n6++;
         if (event24) n246++;
      }
      sumH2 += heads*heads;
   }

   System.out.println("P(6 heads) = "+(float) n6/NREPS);
   System.out.println("P(2 of 1st 4 | 6 heads) = "+(float) n246/n6);
   System.out.println("E(heads squared) = "+(float) sumH2/NREPS);
}

// U(0,1) random variable generator
static float random()

{  return (float) Math.random(); }

}