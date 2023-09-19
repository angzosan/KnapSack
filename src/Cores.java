import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class Cores {
    private int[] numberofcores={1,2,7,11};
    public static void main(String[] args) throws IOException {
        Queue<Double> k =new LinkedList<>();                    //////////////making two queues: one for the prices(double) and one for the amount of cores (int)
        Queue<Integer> k2 =new LinkedList<>();
        String path="src/"+args[0];
        BufferedReader text=new BufferedReader(new FileReader(path));
        Scanner myReader;
        myReader = new Scanner(text);
        String data = myReader.nextLine();
        StringBuilder n = new StringBuilder();
        int i=0;
        do {
            n.append(data.charAt(i));
            i++;
        } while (i < data.length());
        int num=(Integer.parseInt(n.toString()));              ///////////////the first line in the file is the amount of available cores
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
             i=0;
             n = new StringBuilder();
            do {
                n.append(data.charAt(i));                     /////////////the first half in every line if the amount of cores every customer needs
                i++;

            } while (data.charAt(i) != ' ');
            k2.add( Integer.parseInt(n.toString()));
                n = new StringBuilder();
                i++;
                do {
                    n.append(data.charAt(i));
                    i++;                                     ///////////////the second half is the amount of money that each customer is willing to give for ONE core

                } while (i < data.length());
                k.add(Double.parseDouble(n.toString()));
        }

        new Cores(k,k2,num);

    }


    private Cores(Queue core,Queue core2,int num){

        int[] k=new int[(core2.size())];                  ////////////// k = number of cores for every customer
        double[] l=new double[(core.size())];             ////////////// l = amount of money for one core

        int i=0;
        while (core2.peek()!=null) {
            k[i] = (int) core2.peek();
            core2.remove();
            i++;
            }

        i=0;
        while (core.peek()!=null) {
            l[i] = (double) core.peek();
            core.remove();
            i++;
        }

        int[] fn =new int[k.length];
        for (i=0;i<k.length;i++) {
            fn[i]=getNumberOfVMs(k[i]);                   /////////in this array we put the minimum number of VMs for every customer
        }
        for (i=0;i<k.length;i++)
            System.out.println("Client "+(i+1)+": " + fn[i]+" VMs");

        System.out.println("Total amount: "+ findTotalAmount(num,k,l));            //////////we print out the total/max amount of money that we can have
    }

        private int getNumberOfVMs(int k){    ///////we find the min amount of VMs for one customer
            int [] fno=new int[k+1];
            fno[0]=0;
            for (int i=1;i<=k;i++){      ////we try find to find the min amount for of VMs for every core for 0 to the number of cores the customer wants
                fno[i]=min(i,fno);
            }
            return fno[k];
       }


        private int min(int fn, int[] fna){
        int min=fn;
            for (int o=0;o<4;o++) {                       /////to find the number of cores (for each number we get when we call this method)
                if (fn>=numberofcores[o]) {               /////we find the min that comes from the cells of the function that are a result from
                    if (fna[fn - numberofcores[o]] < min) {  //// of the current number minus the number of VMs are smaller than
                        min = fna[fn - numberofcores[o]];   ////the number we sent
                    }
                }
            }
        return min+1;
        }

    private double findTotalAmount(int num, int[] k, double[] l)
    {
        double[][] V=new double[k.length+1][num+1];

        if (l.length == 0 || num == 0)           /////////if the array is empty or if we don't have any cores we return 0
            return 0;

        for (int i=0;i<l.length;i++)
            l[i]=l[i]*k[i];                ////////we multiply the price every customer is willing to pay with the number of cores to get the final price for each order


        for (int i=0;i<=num;i++)
            V[0][num]=0;      //////////first row=only 0s
        for (int i=0;i<=k.length;i++)
            V[k.length][0]=0;   ////////////first column= only 0s


        for (int i=1;i<=k.length;i++){
            for (int j=1;j<=num;j++){
                if (j-k[i-1]>=0){                             ////if  the current number of cores minus the amount of the current customer is >=0
                    V[i][j]=Math.max(V[i-1][j],l[i-1]+V[i-1][j-k[i-1]]);    //////// we take the max between the previous and the total price of each order plus the price in the cell previous line
                                                                            ////////and the column of the current number if cores minus the number of cores the current customer wants

                }else                     /////// else
                    V[i][j]=V[i-1][j];   ///////we keep the previous number
            }
        }
        return V[k.length][num];       //////we return the value of the very last cell as it is the greatest amount
    }

}
