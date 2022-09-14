import java.util.ArrayList;

class moduleInfo{
    public String moduleName;
    public int gradeNum;
    public char gradeLetter;

    public moduleInfo(String mN, long gN, char gL){
        this.moduleName = mN;
        this.gradeNum = Math.toIntExact(gN);
        this.gradeLetter = gL;
    }
}

public class student {
    public int studentNum;
    public String studentName;
    public String studentSurname;
    public String dateOfBirth;
    public int studentAge;
    public String mobileNum;
    public String studentAddress;
    public String studentGender;
    public ArrayList<moduleInfo> modules = new ArrayList<>();

    public student(Long studentnum, String studentname, String studentsurname, String DOB, Long Age, String mobile, String address, String gender){
        this.studentNum = Math.toIntExact(studentnum);
        this.studentName = studentname;
        this.studentSurname = studentsurname;
        this.dateOfBirth = DOB;
        this.studentAge = Math.toIntExact(Age);
        this.mobileNum = mobile;
        this.studentAddress = address;
        this.studentGender = gender;
    }

    public void addModule(moduleInfo module){
        module.gradeLetter = this.calcgradeCHAR(module.gradeNum);
        this.modules.add(module);
    }
    private char calcgradeCHAR(int gradenum){
        if(gradenum >= 90 && gradenum <= 100)return 'A';
        else if(gradenum >= 80 && gradenum <= 89)return 'a';
        else if(gradenum >= 70 && gradenum <= 79)return 'B';
        else if(gradenum >= 60 && gradenum <= 69)return 'C';
        else if(gradenum >= 50 && gradenum <= 59)return 'D';
        else if(gradenum >= 40 && gradenum <= 49)return 'E';
        else if(gradenum >= 30 && gradenum <= 39)return 'F';
        else return 'F';
    }

    /*public void displayStudent(){
        System.out.println("    STUDENTNUMBER: " + this.studentNum);
        System.out.print("    STUDENTNAME: " + this.studentName);
        System.out.print("    STUDENTSURNAME: " + this.studentSurname);
        System.out.print("    DATEOFBIRTH: " + this.dateOfBirth);
        System.out.print("    STUDENTAGE: " + this.studentAge);
        System.out.println("    MOBILENUMBER: " + this.mobileNum);
        System.out.print("    STUDENTADDRESS: " + this.studentAddress);
        System.out.print("    STUDENTGENDER: " + this.studentGender);
        System.out.print("    MODULES:");
        for(int i = 0; i < this.modules.size(); ++i){
            System.out.print("  " + this.modules.get(i).moduleName + " : " + this.modules.get(i).gradeNum + "/" + this.modules.get(i).gradeLetter);
        }
        System.out.println();
        System.out.println();
    }*/
}