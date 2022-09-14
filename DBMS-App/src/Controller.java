import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import java.io.FileReader;
import java.io.IOException;

public class Controller{
    private final BPlusTree tree;
    private int numOfNodes = 0;
    public Controller(){
        this.tree = new BPlusTree();
    }
    public String init(){
        student StudentNode = this.startApplication();
        if(StudentNode == null){
            return "{" +
                    "\"occupiedLeafNodesVal\": "+this.getTreeFillRate()+"," +
                    "\"personsCount\": "+this.numOfNodes+"," +
                    "\"tree_height\": "+this.getreeHeight(this.tree.get_ROOT(), 1)+"," +
                    "\"recent_insert_name\": \"no_name\"," +
                    "\"recent_insert_studentNumber\": "+ 0 +
                    "}";
        }
        else {
            return "{" +
                    "\"occupiedLeafNodesVal\": " + this.getTreeFillRate() + "," +
                    "\"personsCount\": " + this.numOfNodes + "," +
                    "\"tree_height\": " + this.getreeHeight(this.tree.get_ROOT(), 1) + "," +
                    "\"recent_insert_name\": \"" + StudentNode.studentName + " " + StudentNode.studentSurname + "\"," +
                    "\"recent_insert_studentNumber\": " + StudentNode.studentNum +
                    "}";
        }
    }
    private student startApplication(){
        try{
            JSONObject StudentDatabase = (JSONObject)JSONValue.parse(new FileReader("src/StudentDatabase.json"));
            JSONArray jsonSTUDENTSObject = (JSONArray)StudentDatabase.get("students");
            for (int i = 0; i < jsonSTUDENTSObject.size(); ++i) {
                student StudentNode = parseStudentObject((JSONObject)jsonSTUDENTSObject.get(i));
                tree.insert(StudentNode.studentNum, StudentNode);
                ++this.numOfNodes;
                if(i == jsonSTUDENTSObject.size() - 1)return StudentNode;
            }
            return null;
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    public String AddNewStudent(String data){
        JSONObject studentdata = (JSONObject)JSONValue.parse(data);
        student StudentNode = parseStudentObject(studentdata);

        try{
            this.tree.insert(StudentNode.studentNum, StudentNode);
            ++this.numOfNodes;
            return "{" +
                    "\"occupiedLeafNodesVal\": "+this.getTreeFillRate()+"," +
                    "\"personsCount\": "+this.numOfNodes+"," +
                    "\"tree_height\": "+this.getreeHeight(this.tree.get_ROOT(), 1)+"," +
                    "\"recent_insert_name\": \""+ StudentNode.studentName + " " + StudentNode.studentSurname+"\"," +
                    "\"recent_insert_studentNumber\": "+ StudentNode.studentNum +
                    "}";
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            return "null";
        }
    }
    public String SearchStudent(int key){
        student StudentNode = this.tree.findThisKey(key);

        StringBuilder studentdata = new StringBuilder("{\"count\": 1, \"results\": [{"
                + "\"STUDENTNUMBER\": " + StudentNode.studentNum + ","
                + "\"STUDENTNAME\": \"" + StudentNode.studentName + "\","
                + "\"STUDENTSURNAME\": \"" + StudentNode.studentSurname + "\","
                + "\"DATEOFBIRTH\": \"" + StudentNode.dateOfBirth + "\","
                + "\"STUDENTAGE\": " + StudentNode.studentAge + ","
                + "\"MOBILENUMBER\": \"" + StudentNode.mobileNum + "\","
                + "\"STUDENTADDRESS\": \"" + StudentNode.studentAddress + "\","
                + "\"STUDENTGENDER\": \"" + StudentNode.studentGender + "\","
                + "\"MODULECOUNT\": " + StudentNode.modules.size() + ","
                + "\"MODULES\": [");

        for(int i = 0; i < StudentNode.modules.size(); ++i){
            if(i == StudentNode.modules.size() - 1){
                studentdata.append("{\"moduleName\": \"").append(StudentNode.modules.get(i).moduleName).append("\",\"gradeNUM\": ").append(StudentNode.modules.get(i).gradeNum).append("}");
            }
            else studentdata.append("{\"moduleName\": \"").append(StudentNode.modules.get(i).moduleName).append("\",\"gradeNUM\": ").append(StudentNode.modules.get(i).gradeNum).append("},");
        }
        studentdata.append("]}]}");

        return studentdata.toString();
    }
    public String deleteStudent(int key){
        try{
            this.tree.delete(key);
            --this.numOfNodes;
            return "{" +
                    "\"occupiedLeafNodesVal\": "+this.getTreeFillRate()+"," +
                    "\"personsCount\": "+this.numOfNodes+"," +
                    "\"tree_height\": "+this.getreeHeight(this.tree.get_ROOT(), 1)+"," +
                    "\"recent_insert_name\": \"null\"," +
                    "\"recent_insert_studentNumber\": 0" +
                    "}";
        }
        catch(IOException e){
            System.out.println(e.getMessage());
            return "null";
        }
    }
    private static moduleInfo readModule(JSONObject module){
        String modulename = (String)module.get("moduleName");
        long gradeint = (long)module.get("gradeNUM");
        return new moduleInfo(modulename, gradeint, 'h');
    }
    private static student parseStudentObject(JSONObject studentsobj){
        Long studentnumber = (Long)studentsobj.get("STUDENTNUMBER");
        String name = (String)studentsobj.get("STUDENTNAME");
        String surname = (String)studentsobj.get("STUDENTSURNAME");
        String dob = (String)studentsobj.get("DATEOFBIRTH");
        Long age = (Long)studentsobj.get("STUDENTAGE");
        String phonenumber = (String)studentsobj.get("MOBILENUMBER");
        String address = (String)studentsobj.get("STUDENTADDRESS");
        String gender = (String)studentsobj.get("STUDENTGENDER");
        JSONArray modules = (JSONArray)studentsobj.get("MODULES");

        student newStudent = new student(studentnumber, name, surname, dob, age, phonenumber, address, gender);

        for (Object module : modules) newStudent.addModule(readModule((JSONObject) module));
        return newStudent;
    }


    //helper functions
    private int getreeHeight(BPlusTreeNode treeNodeobj, int depth){
        if(treeNodeobj.references[0] == null){return depth;}
        else return this.getreeHeight(treeNodeobj.references[0], depth + 1);
    }
    private int getTreeFillRate(){
        BPlusTreeNode leafnode = tree.getFirstLeaf(tree.get_ROOT());

        int number_of_leaf_nodes = 0;
        BPlusTreeNode runner = leafnode;
        while(runner != null){
            ++number_of_leaf_nodes;
            runner = runner.nextLeaf;
        }

        double val = ((double)this.numOfNodes / ((double)number_of_leaf_nodes * (double)tree.get_ROOT().referenceArraySize)) * (double)100;
        return (int)val;
    }
}
