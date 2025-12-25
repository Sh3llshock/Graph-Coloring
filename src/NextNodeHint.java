import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NextNodeHint {
    private static ArrayList<ArrayList<Integer>> unColoredVertices ;
    private static ArrayList<ArrayList<Integer>> VerticesColorTrack;
    private static ArrayList<Integer> flatVerticesConnection ;
    private static ArrayList<Integer>  setOfVertices;
    private static ArrayList<ArrayList<Integer>> Vertices_Connection;
  

    public static void initHintdata() {
        unColoredVertices = new ArrayList<>();
        VerticesColorTrack = new ArrayList<>();
        flatVerticesConnection = new ArrayList<>();
        setOfVertices = new ArrayList<>();
        Vertices_Connection = new ArrayList<>();

        ColEdge[] edges = SharedData.getInstance().getEdges();
        int numberOfEdges = SharedData.getInstance().getNumberOfEdges();
        
        for (int i = 0; i < numberOfEdges; i++) {
            ArrayList<Integer> edgeConnection = new ArrayList<>();
            edgeConnection.add(edges[i].u - 1);  
            edgeConnection.add(edges[i].v - 1); 
            Vertices_Connection.add(edgeConnection);  
        }

        //flatten Matrix
        flatVerticesConnection = FlattenMatrix(Vertices_Connection);
        //Get Distinct vertices
        setOfVertices = getSetOfVertices(flatVerticesConnection);

        CreateUncoloredTable();
        CreateColorTrackingTable ();
    }
    public static int implementHint () {//when user press hint button will implement this method
        //update UncoloredVertices table based on input at the given period.
        for(int i=0;i<VerticesColorTrack.size();i++) {
            if(VerticesColorTrack.get(i).get(1)==1) {
                int coloredVertex = VerticesColorTrack.get(i).get(0);
                UpdateUncoloredVertices(coloredVertex);
           }     
        }
        int nextNode = RecommendNextNode();

        unColoredVertices.clear();
        CreateUncoloredTable();
        return nextNode;

    }

    public static ArrayList<Integer> getflatVerticesConnection () {
        return flatVerticesConnection;
    }

    public static ArrayList<Integer> getSetOfVertices () {
        
        return setOfVertices;
    }

    public static ArrayList<ArrayList<Integer>> getunColoredVertices () {
        return  unColoredVertices;
    }

    public static ArrayList<ArrayList<Integer>> getVerticesColorTrack () {
        return  VerticesColorTrack;
    }
    
    public static void ColorTracking (int vertex_input) { //be recursive every time user select or unselect
        for (int i = 0; i<VerticesColorTrack.size();i++){
            if(VerticesColorTrack.get(i).get(0)==vertex_input 
            &&  VerticesColorTrack.get(i).get(1) == 0) { // if not colored
                VerticesColorTrack.get(i).set(1,1) ;//then is colored
            }

            // Not use in Classic game mode
            // else if (VerticesColorTrack.get(i).get(0)==vertex_input 
            // &&  VerticesColorTrack.get(i).get(1) == 1){ // if colored
            //     VerticesColorTrack.get(i).set(1,0); //then is uncolored
            // }
        }

    }
    
    private static int RecommendNextNode () { 
        boolean isLastVertex = true;
        for (int i =0;i<unColoredVertices.size();i++){
            if(unColoredVertices.get(i).get(3)==0) {
                isLastVertex=false;
            }
        }

        if (!isLastVertex ) {
        ArrayList<ArrayList<Integer>> HigestDSATURTable =  GetHighestDSATUR(unColoredVertices);
        // System.out.println("aaa" + HigestDSATURTable);
        ArrayList<ArrayList<Integer>> HigestRegularTable = GetHighestRegular(HigestDSATURTable);
        // System.out.println("HEllo"+setOfVertices);
        // System.out.println("Highest-DSATUR Table: " +HigestDSATURTable);
        // System.out.println("Highest-Regular Talbe: " + HigestRegularTable);


        // System.out.println("The next node should be colored is: " + GetLowestNode(HigestRegularTable))  ;
        return GetLowestNode(HigestRegularTable) ;
        }
        else {
            return -999;
        }
        
    }

    private static void UpdateUncoloredVertices (int vertex_input) { 
            //nhung thang dc colored trong bang colortrack se chay moethod nay
       
        ArrayList<Integer> neighborVertices = GetNeighborVertices(vertex_input,flatVerticesConnection);
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
                    // VerticesColorTrack.get(j).set(2,1); 
                    // break;
                }
            }
        }

    }
    //for testing
    public static void SetOfVertices () {
        System.out.println(setOfVertices);
    }
    //for testing
    public static void UncoloredTable () {
        System.out.println(unColoredVertices);
    }
    //for testing
    public static void ColorTrack () {
        System.out.println(VerticesColorTrack);
    }

    private static void CreateUncoloredTable () {
        int [] vertex_degree = ComputeRegularDegree(setOfVertices,flatVerticesConnection);

        for (int i = 0;i<setOfVertices.size();i++) {
            ArrayList<Integer> row = new ArrayList<>();
            row.add(setOfVertices.get(i)); // column 1: node_id
            row.add(vertex_degree[i]); // column 2: regular degree
            row.add(0);              // column 3: saturation degree
            row.add(0);              // column 4: is colored?
            unColoredVertices.add(row);
        }
    }

    private static void CreateColorTrackingTable () {
        for (int i = 0;i<setOfVertices.size();i++) {
            ArrayList<Integer> row = new ArrayList<>();
            row.add(setOfVertices.get(i)); // column 1: node_id
            row.add(0);              // column 2: is colored?
            VerticesColorTrack.add(row);
        }
    }
   
    public static ArrayList<ArrayList<Integer>> GetHighestDSATUR (ArrayList<ArrayList<Integer>> unColoredVertices) {
        ArrayList<ArrayList<Integer>> HighestDSATUR  = new ArrayList<>();
        int highestDSATUR  = 0;

        for (int i = 0; i < unColoredVertices.size(); i++) {
            if (unColoredVertices.get(i).get(3) ==0 && //really important criteria 
                unColoredVertices.get(i).get(2) > highestDSATUR) { 
                highestDSATUR  = unColoredVertices.get(i).get(2);  
            }
        }
        
        //for testing purpose
        // System.out.println ("Highest DSATUR: "+highestDSATUR );
        for (int i =0; i<unColoredVertices.size();i++ ) {
            if (unColoredVertices.get(i).get(3)==0 && //really important criteria 
                unColoredVertices.get(i).get(2) ==highestDSATUR  
             ) {
                HighestDSATUR .add(new ArrayList<>(unColoredVertices.get(i))); // copy rows from unColoredVertices 
            }
        }
        // System.out.println("here" +HighestDSATUR);
        return HighestDSATUR ;
    }

    public static ArrayList<ArrayList<Integer>> GetHighestRegular  (ArrayList<ArrayList<Integer>> HighestDSATUR ) {
        ArrayList<ArrayList<Integer>> HighestRegular = new ArrayList<>();
        int highestRegular = HighestDSATUR.get(0).get(1);

        for (int i = 1; i < HighestDSATUR.size(); i++) {
            if (HighestDSATUR.get(i).get(1) > highestRegular ) {
                highestRegular  = HighestDSATUR.get(i).get(1);  
            }
        }
        //for testing purpose
        // System.out.println ("Highest Regular: "+highestRegular );
        for (int i =0; i<HighestDSATUR.size();i++ ) {
            if (HighestDSATUR.get(i).get(1) ==highestRegular  ) {
                HighestRegular .add(new ArrayList<>(HighestDSATUR.get(i))); // copy rows from unColoredVertices 
            }
        }
        return HighestRegular;
    }

    private static int GetLowestNode (ArrayList<ArrayList<Integer>> HighestRegular) {
        int lowestNode = HighestRegular.get(0).get(0);

        for (int i =0;i<HighestRegular.size();i++) {
            if(HighestRegular.get(i).get(0) < lowestNode) {
                lowestNode = HighestRegular.get(i).get(0);
            }
        }
        return lowestNode;
    }

    public static ArrayList<Integer> GetNeighborVertices (int vertex_input,ArrayList<Integer> flatVerticesConnection) {
        ArrayList<Integer> neighborVertices = new ArrayList<>();

        for (int i = 0;i<flatVerticesConnection.size();i++) {
            if( i%2==0 && flatVerticesConnection.get(i)==vertex_input  ) {
                neighborVertices.add(flatVerticesConnection.get(i+1));
            }
            else if( i%2!=0 && flatVerticesConnection.get(i)==vertex_input) {
                neighborVertices.add(flatVerticesConnection.get(i-1));
            }
        }
        
        //for checking purpose, will be removed later
        // System.out.println("Neighbor Vertices list: "+ neighborVertices);

        return neighborVertices;
 
    }

    public static ArrayList<Integer> FlattenMatrix (ArrayList<ArrayList<Integer>> Vertices_Connection ) { //to reuse in RandomOrder.java
        ArrayList<Integer> flatList = new ArrayList<>();
        
        for (ArrayList<Integer> innerList : Vertices_Connection) {
            // Add all elements of the inner list to the flattened list
            flatList.addAll(innerList);
        }
        return flatList;
    }
    
    public static int [] ComputeRegularDegree (ArrayList<Integer>  setOfVertices, ArrayList<Integer> flatVerticesConnection ) {

       //calculate the number of connections
        int [] vertex_degree = new int[setOfVertices.size()];

        //compute vertex degree, order rely on the order of setOfVertices
         for (int i =0; i<setOfVertices.size();i++) {
            int vertex_count=0; // reset to 0 after every loop of j
            
            for (int j=0;j<flatVerticesConnection.size();j++) {                
                if(flatVerticesConnection.get(j)==setOfVertices.get(i)) 
                    vertex_count++;
            }
            vertex_degree[i] = vertex_count;
        }
        return vertex_degree;
    }
    
    public static ArrayList<Integer> getSetOfVertices(ArrayList<Integer> list) {
        List<Integer> tempList = list.stream().distinct().collect(Collectors.toList());
        return new ArrayList<>(tempList);  
    }

    public static ArrayList<ArrayList<Integer>> getColorTracking(){
        return VerticesColorTrack;
    }



}