//Same algorithm with NextNodeHint using Dsatur Algo
// but randomly pick nodes when it comes to the situation that mulitiple nodes have the same dstatur and number of 
// adjacent node
//But the first node to be colored is always random

import java.util.ArrayList;
import java.util.Random;

public class RandomOrder {
    private static ArrayList<Integer> setOfVertices;
    private static int setOfVerticesLength;
    private static ArrayList<ArrayList<Integer>> unColoredVertices;
    private static Random random = new Random();
    public static int [] randomOrder ;
    private static int randomOrderIndex =1;
    private static ArrayList<Integer> flatVerticesConnection ;
    public static int currentNodeID =-1;
    // public static int NodeIDElements=-1;
    
    public static void initRandomNodedata() {
        setOfVertices = NextNodeHint.getSetOfVertices();
        setOfVerticesLength = setOfVertices.size();
        unColoredVertices = NextNodeHint.getunColoredVertices();
        randomOrder= new int [setOfVerticesLength];
        flatVerticesConnection = NextNodeHint.getflatVerticesConnection ();
        currentNodeID=-1;
        generateRandomOrder ();
    }
    public static int getCurrentNodeID () {
        return currentNodeID;
    }

    public static void udpateCurrentNodeID () {
        if (currentNodeID<SharedData.getInstance().getNumberOfVertices())
            currentNodeID+=1;
    }
    public static void undoCurrentNodeID () {
        if (currentNodeID == -1){}
        else if (currentNodeID<SharedData.getInstance().getNumberOfVertices())
            currentNodeID-=1;
    }

    public static void generateRandomOrder () {
        int randomElement = setOfVertices.get(random.nextInt(setOfVerticesLength));

        //first node will be random
        randomOrder[0] =  randomElement;
        randomNextNode (randomElement);
    }

    private static void updateUncoloredVertices (int vertex_input) {
        //nhung thang dc colored trong bang colortrack se chay moethod nay
        ArrayList<Integer> neighborVertices = NextNodeHint.GetNeighborVertices(vertex_input,flatVerticesConnection);
        //update column DSATUR
        for (int i = 0; i<neighborVertices.size();i++) {
            int currentNeighborVertex = neighborVertices.get(i);
            for (int j = 0; j<unColoredVertices.size();j++) {
                if (unColoredVertices.get(j).get(0) == vertex_input) {
                    unColoredVertices.get(j).set(3,1); //update is+colored column
                }

                if (unColoredVertices.get(j).get(0) == currentNeighborVertex) {
                    int currentDSATUR = unColoredVertices.get(j).get(2);
                    unColoredVertices.get(j).set(2,currentDSATUR+1); //update DSATUR column
                    // break;
                }
            }
        }
    }

    private static int randomNextNode (int vertex_input) {
        boolean isLastVertex = true;
        int nextNode =0 ;

        //update table
        updateUncoloredVertices(vertex_input);
        // System.out.println(unColoredVertices);

        for (int i =0;i<unColoredVertices.size();i++){
            if(unColoredVertices.get(i).get(3)==0) {
                isLastVertex=false;
            }
        }
        if (isLastVertex && randomOrderIndex >= setOfVerticesLength) {
            randomOrderIndex =1;
            return 0;
        }
        else   {
            ArrayList<ArrayList<Integer>> HigestDSATURTable =  NextNodeHint.GetHighestDSATUR(unColoredVertices);
            // System.out.println("Highest-DSATUR Table: " +HigestDSATURTable);
            ArrayList<ArrayList<Integer>> HigestRegularTable = NextNodeHint.GetHighestRegular(HigestDSATURTable);
            // System.out.println("Highest-Regular Talbe: " + HigestRegularTable);

            //randomly pick 1 node from the table
            int randomRow = random.nextInt(HigestRegularTable.size());
            nextNode = HigestRegularTable.get(randomRow).get(0);
            // System.out.println(nextNode);

            //update array
            randomOrder[randomOrderIndex] = nextNode ;
            randomOrderIndex++;

            return randomNextNode(nextNode);
        }
    }
    public static void decrementCurrentNodeID() {
        if (currentNodeID > 0) {
            currentNodeID -= 1;
        }
    }
}


