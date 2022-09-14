import javafx.scene.web.WebEngine;
public class Interface {
    private final WebEngine webEngine;
    private final Controller StudentDatabaseManagementSystem;
    private final String startval;
    public Interface(WebEngine webengine){
        this.webEngine = webengine;
        this.StudentDatabaseManagementSystem = new Controller();
        startval = this.StudentDatabaseManagementSystem.init();
    }
    public void addNewStudent(){
        String newstudent = (String) webEngine.executeScript("JAVA__READABLE__TEXT;");
        String JSstudentobjJSON = this.StudentDatabaseManagementSystem.AddNewStudent(newstudent);
        webEngine.executeScript("assignDashBoardValues('"+ JSstudentobjJSON +"')");
    }
    public void searchForStudentNumber(){
        String studentnum = (String) webEngine.executeScript("JAVA__READABLE__TEXT;");
        String JSstudentobjJSON = this.StudentDatabaseManagementSystem.SearchStudent(Integer.parseInt(studentnum));
        webEngine.executeScript("initSEARCHRESULTS('"+ JSstudentobjJSON +"')");
    }
    public void deleteThisStudent(){
        Integer studentnum = (Integer) webEngine.executeScript("JAVA__READABLE__TEXT;");
        String JSstudentobjJSON = this.StudentDatabaseManagementSystem.deleteStudent(studentnum);
        webEngine.executeScript("assignDashBoardValues('"+ JSstudentobjJSON +"')");
    }
    public String getStartValues(){return startval;}
}
