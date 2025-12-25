import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.HashMap;

class LabelWithNode {
    private int NodeId;
    private TextField Label;
    // private Button hideButton;
    private static ArrayList <Integer> labelledNodeID = new ArrayList<>(); //To store the labelled Node


    private static HashMap<Integer, TextField> labelNodeDict = new HashMap<>(); //Dict key:NodeID  value: Label reference

    private static HashMap<TextField, Button> labelHideButtonDict = new HashMap<>();// key: label ref, value: hidebutton ref

    private static HashMap <String,ArrayList<Double>> edgeNameCoordDict = new HashMap<>();//key: edgeName, value: Coord of midpoint
    
    private static HashMap <String,Button> edgeNameButtonDict = new HashMap<>();//key: edgeName, value: button reference

    private static HashMap <Button,TextField> buttonLabelEdgeDict = new HashMap<>();

    



    public static void reInitLabelHintdata () {
        clearLabelData ();
        
        NextNodeHint.initHintdata(); //Tu
        RandomOrder.initRandomNodedata();//Tu

    }
    public static void clearLabelData () {
        labelledNodeID.clear();
        labelNodeDict.clear();
        labelHideButtonDict.clear();
      
        edgeNameCoordDict.clear();
        edgeNameButtonDict.clear();
        buttonLabelEdgeDict.clear();
    }

    public LabelWithNode (int NodeId, TextField Label ) {
        this.NodeId= NodeId;
        this.Label = Label;
    }
    public int getNodeID(){
        return NodeId;
    }
    public TextField getLabels () {
        return Label;
    }
    public static ArrayList <Integer> getListLabelledNode () {
        return labelledNodeID;
    }
    public static void updateListLabelledNode (int NodeID) {
        labelledNodeID.add(Integer.valueOf(NodeID));
    }



    public static TextField getLabelByNodeID(int nodeId) {
        return labelNodeDict.get(nodeId);
    }

    public static void updateLabelledNode(int nodeId, TextField label) {
        labelNodeDict.put(nodeId, label);
    }

    public static void updateLabelHideButton (TextField label, Button hideButton) {
        labelHideButtonDict.put(label,hideButton);
    }



    public static void updateLabelPosition (DrawnNodes[] nodes, TextField label, int closest_index) {
        label.setLayoutX(nodes[closest_index].x + 20);
        label.setLayoutY(nodes[closest_index].y - 10);

        updateHideButtonPosition (label);

    }

    public static void updateLabelforEgdePosition (){
        //encapsulate everything
        updateMidPointCoord () ; //update midpoint coord first
        updateEdgeButton ();     //update button coord
        updateLabelEdgePosition();//udpate Label coord
        
    }
    private static void createMidPointCoord (int NodeId1, int NodeId2,DrawnNodes node1, DrawnNodes node2) {
        // double angle = Math.toRadians(45); // Example angle in degrees


        double xMid = ((node1.x-node1.radius/2) + (node2.x-node2.radius/2))/ 2;
        double yMid = ((node1.y-node1.radius/2) + (node2.y-node2.radius/2))/ 2;

        

        String EdgeName =  String.valueOf(NodeId1) + String.valueOf(NodeId2);
        ArrayList <Double> tempMidCoord = new ArrayList<>();
        tempMidCoord.add(xMid); //0 is x
        tempMidCoord.add(yMid); //1 is y

        edgeNameCoordDict.put(EdgeName,tempMidCoord);
    }
    private static void updateMidPointCoord () {
        ColEdge[] edges = SharedData.getInstance().getEdges();
        DrawnNodes[] nodes = SharedData.getInstance().getDrawnCircles();
        for (ColEdge edge : edges) {
            createMidPointCoord(edge.u-1,edge.v-1, nodes[edge.u-1], nodes[edge.v-1]);
        }
    }


    public static void createEdgeButtonDict (String edgeName, Button labelButton  ) {
        edgeNameButtonDict.put(edgeName,labelButton);
    }

    private static void updateEdgeButton () {
            updateMidPointCoord ();
            for (HashMap.Entry<String, Button> entry : edgeNameButtonDict.entrySet()){
                String key = entry.getKey();  
                Double xMid = edgeNameCoordDict.get(key).get(0); 
                Double yMid = edgeNameCoordDict.get(key).get(1);
                entry.getValue().setLayoutX(xMid);
                entry.getValue().setLayoutY(yMid);     
            }
    }

    public static void createbuttonLabelEdgeDict (Button labelButton, TextField Label) {
        buttonLabelEdgeDict.put(labelButton,Label);
    }
    private static void updateLabelEdgePosition () {
        for (HashMap.Entry <Button,TextField> entry : buttonLabelEdgeDict.entrySet()) {
           
            //get Button reference
            Button labelButton = entry.getKey();
            
            double xButton = labelButton.getLayoutX();
            double yButton = labelButton.getLayoutY();

            //get Label reference and update coord
            entry.getValue().setLayoutX(xButton - 5);
            entry.getValue().setLayoutY(yButton +15);

        }

    }
    
    //for testing
    public static HashMap <String,ArrayList<Double>> getEdgeNameCoordDict (){
        return edgeNameCoordDict;
    }
    
    //for testing
    public static void printedgeCoord () {
        System.out.println(edgeNameCoordDict);
        System.out.println(edgeNameCoordDict.size());
    }


    public static void updateHideButtonPosition(TextField Label) {

        for (HashMap.Entry<TextField, Button> entry : labelHideButtonDict.entrySet()) {
            if(entry.getKey().equals(Label)) {
                entry.getValue().setLayoutX(Label.getLayoutX() - 15);
                entry.getValue().setLayoutY(Label.getLayoutY() - 5);
            }
        }
    }
    
}

