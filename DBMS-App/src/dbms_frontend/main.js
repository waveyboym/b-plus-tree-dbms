let currentlyselectedPage = "home";
let alreadySelecetedAPage = false;

const nav_size = 4;
const squirclepath = ["img/SquircleW.svg", "img/Squircle.svg"];
const icon = ["Home", "Search", "Settings-alt", "Location-question"];
const entryfields = ["studentnumber", "name", "surname", "dateofbirth", "age", "mobilenumber", "address", "gender"];
const pathholders = ["eg '1234' or '12345'", "eg 'John'",
                    "eg 'Doe'", "eg '2100-01-01'",
                    "age '70'", "eg '+1-234-567-890'",
                    "eg: '23-Brown-Street-Purple-Road'", "eg 'Male'"];
const entryfieldsEnum = {"studentnumber":0, "name":1, "surname":2, "dateofbirth":3, "age":4, "mobilenumber":5, "address":6, "gender":7};
Object.freeze(entryfieldsEnum);

//modifiable
let occupiedLeafNodesVal = 0;
let personsCount = 0;
let tree_height = 0;
let recent_insert_name = "";
let recent_insert_studentNumber = 0;
let JAVA__READABLE__TEXT = "";

window.onload = function(){initSQUIRCLES();}

function initSTART__VALUES(data){
    let initDATAJSON = JSON.parse(data);

    occupiedLeafNodesVal = initDATAJSON.occupiedLeafNodesVal;
    personsCount = initDATAJSON.personsCount;
    tree_height = initDATAJSON.tree_height;
    recent_insert_name = initDATAJSON.recent_insert_name;
    recent_insert_studentNumber = initDATAJSON.recent_insert_studentNumber;

    initTOOLBOXES();
    initStudentDatabaseEntry();
    initPROGRESS_BAR();
}

function initPROGRESS_BAR(){
    let BarProgress = document.querySelector(".progress"),
        progressValue = document.querySelector(".progress-value");

        //nax 235px so interval increase is 2.35pxx
    
    let progressStartValue = 0,    
        progressEndValue = occupiedLeafNodesVal * 2.35,    
        speed = 40;
        
    let progress = setInterval(() => {
        progressStartValue += 2.35;

        let num = progressStartValue/2.35;
        //num -= 1;
        progressValue.innerHTML = num.toPrecision(2) + "%";
        BarProgress.style.width = progressStartValue + "px";
    
        if(progressStartValue >= progressEndValue)clearInterval(progress);  
    }, speed);
}

function initSQUIRCLES(){
    let side_nav_bar = document.querySelector(".side-nav-bar");
    while (side_nav_bar.hasChildNodes()) {
        side_nav_bar.removeChild(side_nav_bar.firstChild);
    }

    for(let i = 0; i < nav_size; ++i){
        if(!alreadySelecetedAPage && currentlyselectedPage == "home" && i == 0){
            side_nav_bar.innerHTML += '<nav class="icon">'
                                +'<nav class="currently-selected selected">'

                                +'</nav>'
                                +'<nav class="squircle" '+getfunctionCall(icon[i])+'>'
                                    +'<nav class="squircle-icon">'
                                        +'<img src="'+ squirclepath[0] +'" alt="squircle" id="squircleID">'
                                    +'</nav>'
                                    +'<nav class="desc-icon">'
                                        +'<img src="img/HomeR.svg" alt="home" id="'+ icon[0] +'ID">'
                                    +'</nav>'
                                +'</nav>'
                            +'</nav>';
            alreadySelecetedAPage = true;
        }
        else if(!alreadySelecetedAPage && currentlyselectedPage == "search" && i == 1){
            side_nav_bar.innerHTML += '<nav class="icon">'
                                +'<nav class="currently-selected selected">'

                                +'</nav>'
                                +'<nav class="squircle" '+getfunctionCall(icon[i])+'>'
                                    +'<nav class="squircle-icon">'
                                        +'<img src="'+ squirclepath[0] +'" alt="squircle" id="squircleID">'
                                    +'</nav>'
                                    +'<nav class="desc-icon">'
                                        +'<img src="img/SearchR.svg" alt="home" id="'+ icon[1] +'ID">'
                                    +'</nav>'
                                +'</nav>'
                            +'</nav>';
            alreadySelecetedAPage = true;
        }
        else{
            side_nav_bar.innerHTML += '<nav class="icon">'
                                +'<nav class="currently-selected">'

                                +'</nav>'
                                +'<nav class="squircle" '+getfunctionCall(icon[i])+'>'
                                    +'<nav class="squircle-icon">'
                                        +'<img src="'+ squirclepath[1] +'" alt="squircle" id="squircleID">'
                                    +'</nav>'
                                    +'<nav class="desc-icon">'
                                        +'<img src="img/'+ icon[i] +'.svg" alt="home" id="'+ icon[i] +'ID">'
                                    +'</nav>'
                                +'</nav>'
                            +'</nav>';
        }
    }
}

function initTOOLBOXES(){
    let dashboard = document.querySelector(".dashboard");

    dashboard.innerHTML = '<nav class="tool-boxes">'
                            +'<nav class="toolbox">'
                                +'<nav class="toolbox-one-text">'
                                    +'<nav class="icon-class">'
                                        +'<img src="img/User.svg" alt="user-icon" id="userID">'
                                    +'</nav>'
                                    +'<nav class="persons-count-text-img"></nav>'
                                +'</nav>'
                                +'<h1>'+ personsCount +'</h1>'
                            +'</nav>'
                            +'<nav class="toolbox">'
                                +'<nav class="toolbox-two-text"></nav>'
                                +'<nav class="progress-bar">'
                                    +'<div class="progress"></div>'
                                +'</nav>'
                                +'<nav class="tree-details">'
                                    +'<nav class="tree-height">'
                                        +'<nav class="tree-height-text"></nav>'
                                        +'<nav class="tree-height-number">'+ tree_height +'</nav>'
                                    +'</nav>'
                                    +'<span class="progress-value">0%</span>'
                                +'</nav>'
                            +'</nav>'
                            +'<nav class="toolbox">'
                                +'<nav class="recentinsert-text"></nav>'
                                +'<nav class="recent-name">'+ recent_insert_name +'</nav>'
                                +'<nav class="recent-id">'+ recent_insert_studentNumber +'</nav>'
                            +'</nav>'
                        +'</nav>';
}

function initStudentDatabaseEntry(){
    let dashboard = document.querySelector(".dashboard");
    let student_nodes = "";

    for(let i = 0; i < entryfields.length; ++i){
        if(i == 6){
            student_nodes +=    '<nav class="student-details-node long-node">'
                                    +'<nav class="'+entryfields[i]+'-img"></nav>'
                                    +'<input type="text" id="'+entryfields[i]+'" placeholder="'+pathholders[i]+'">'
                                +'</nav>';
        }
        else{
            student_nodes +=    '<nav class="student-details-node">'
                                    +'<nav class="'+entryfields[i]+'-img"></nav>'
                                    +'<input type="text" id="'+entryfields[i]+'" placeholder="'+pathholders[i]+'">'
                                +'</nav>';
        }
        
    }

    dashboard.innerHTML += '<nav class="student-entry-todatabase">'
                                +'<nav class="student-entry-details">'
                                    +'<nav class="all-entries-section">'
                                        + student_nodes
                                    +'</nav>'
                                    +'<nav class="modules-section">'
                                        +'<nav class="modules-text-img"></nav>'
                                        +'<textarea placeholder="Expected text form:    '
                                            +'modulename:grade;    '
                                            +'modulename:grade;    '
                                            +'etc" id="ModulesEntryBox">'
                                        +'</textarea>'
                                    +'</nav>'
                                +'</nav>'
                                +'<nav class="student-entry-button" onclick="addStudent()">'
                                    +'<nav class="squircle">'
                                        +'<nav class="squircle-icon">'
                                            +'<img src="'+squirclepath[1]+'" alt="squircle" id="squircleID">'
                                        +'</nav>'
                                        +'<nav class="desc-icon">'
                                            +'<img src="img/Plus.svg" alt="home" id="plusID">'
                                        +'</nav>'
                                    +'</nav>'
                                +'</nav>'
                                +'<nav class="error-message"><h1></h1></nav>'
                            +'</nav>';

}

function getfunctionCall(type){
    if(type == "Home")return 'onclick="changeToHome()"';
    else if(type == "Search")return 'onclick="changeToSearch()"';
    else return '';
}

function changeToSearch(){
    if(currentlyselectedPage == "search")return;

    let dashboard = document.querySelector(".dashboard");
    document.querySelector(".intro-text").style.opacity = 0;
    
    currentlyselectedPage = "search";
    alreadySelecetedAPage = false;
    initSQUIRCLES();

    dashboard.innerHTML = "";
    dashboard.innerHTML += '<nav class="search-query-box">'
                                +'<nav class="search-text"></nav>'
                                +'<input type="text" id="searchquery" placeholder="search for student number in range 1000-99999">'
                                +'<nav class="student-query-request-button" onclick="searchFor()">'
                                    +'<nav class="squircle">'
                                        +'<nav class="squircle-icon">'
                                            +'<img src="img/Squircle.svg" alt="squircle" id="squircleID">'
                                        +'</nav>'
                                        +'<nav class="desc-icon">'
                                            +'<img src="img/Search.svg" alt="home" id="SearchID">'
                                        +'</nav>'
                                    +'</nav>'
                                +'</nav>'
                            +'</nav>'
                            +'<nav class="search-results">'
                                +'<nav class="no-results"></nav>'
                            +'</nav>';
}

function changeToHome(){
    if(currentlyselectedPage == "home")return;

    document.querySelector(".dashboard").innerHTML = "";
    document.querySelector(".intro-text").style.opacity = 1;
    currentlyselectedPage = "home";
    alreadySelecetedAPage = false;
    initSQUIRCLES();
    initTOOLBOXES();
    initStudentDatabaseEntry();
    initPROGRESS_BAR();
}

function determine(type){
    if(type == "Male")return "M";
    else if(type == "Female")return "F";
    else return "N-B";
}

function createModules(object, index){
    let studentsJSON = JSON.parse(object);
    let modulesSTR = '';

    for(let i = 0; i < studentsJSON.results[index].MODULECOUNT; ++i){
        modulesSTR +=  '<nav class="module">'
                            +'<h2>'+studentsJSON.results[index].MODULES[i].moduleName+'</h2>'
                            +'<h3>'+studentsJSON.results[index].MODULES[i].gradeNUM+'</h3>'
                        +'</nav>';
    }

    return modulesSTR;
}

function initSEARCHRESULTS(object){
    let studentsJSON = JSON.parse(object);
    let searchresults = document.querySelector(".search-results");
    if(studentsJSON.count == 0){
        searchresults.innerHTML = '<nav class="no-results"></nav>';
        return;
    }

    searchresults.innerHTML = "";
    for(let i = 0; i < studentsJSON.count; ++i){
        searchresults.innerHTML += '<nav class="search-node" id='+studentsJSON.results[i].STUDENTNUMBER+'>'
                                        +'<nav class="search-box-node">'
                                            +'<nav class="top-layer-box">'
                                                +'<h1>'+studentsJSON.results[i].STUDENTNUMBER+'</h1>'
                                                +'<nav class="student-data-one">'
                                                    +'<h2>'+studentsJSON.results[i].STUDENTNAME + ' ' +studentsJSON.results[i].STUDENTSURNAME +'</h2>'
                                                    +'<h2>Age: '+studentsJSON.results[i].STUDENTAGE+' y/o</h2>'
                                                    +'<h3>Gender: '+determine(studentsJSON.results[i].STUDENTGENDER)+'</h3>'
                                                +'</nav>'
                                                +'<nav class="student-data-two">'
                                                    +'<h2>Date of birth: '+studentsJSON.results[i].DATEOFBIRTH+'</h2>'
                                                    +'<h2>Mobile Number: '+studentsJSON.results[i].MOBILENUMBER+'</h2>'
                                                    +'<h2>Address: '+studentsJSON.results[i].STUDENTADDRESS+'</h2>'
                                                +'</nav>'
                                            +'</nav>'
                                            +'<nav class="bottom-layer-box">'
                                                +'<h1>Modules</h1>'
                                                +'<nav class="student-modules">'+ createModules(object, i)+'</nav>'
                                            +'</nav>'
                                        +'</nav>'
                                        +'<nav class="delete-box">'
                                            +'<nav class="squircle" onclick="deleteStudent('+studentsJSON.results[i].STUDENTNUMBER+')">'
                                                +'<nav class="squircle-icon">'
                                                    +'<img src="img/Squircle.svg" alt="squircle" id="squircleID">'
                                                +'</nav>'
                                                +'<nav class="desc-icon">'
                                                    +'<img src="img/Delete.svg" alt="home" id="DeleteID">'
                                                +'</nav>'
                                            +'</nav>'
                                        +'</nav>'
                                    +'</nav>';
    }
}

/*
RegEx	Description
^	The password string will start this way
(?=.*[a-z])	The string must contain at least 1 lowercase alphabetical character
(?=.*[A-Z])	The string must contain at least 1 uppercase alphabetical character
(?=.*[0-9])	The string must contain at least 1 numeric character
(?=.*[!@#$%^&*])	The string must contain at least one special character, but we are escaping reserved RegEx characters to avoid conflict
(?=.{8,})	The string must be eight characters or longer
*/
function addStudent(){
    let studentNum = new RegExp("^((?=.{4,5})(?=.*[0-9]))");
    let studentNameANDsurname = new RegExp("^((?=.{2,})(?=.*[A-Z])(?=.*[a-z]))");
    let studentDOB = new RegExp("^((?=.{10,10})(?=.*[0-9])(?=.*[-]))");
    let studentAge = new RegExp("^((?=.{2,3})(?=.*[0-9]))");
    let studentMOBILE = new RegExp("^((?=.{10,})(?=.*[0-9])(?=.*[-+]))");
    let studentAddress = new RegExp("^((?=.{5,})(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[-]))");
    let studentGender = new RegExp("^((?=.{4,})(?=.*[a-z]))");

    let getstudentNum = document.getElementById(entryfields[entryfieldsEnum.studentnumber]).value;
    let getstudentName = document.getElementById(entryfields[entryfieldsEnum.name]).value;
    let getstudentSurname = document.getElementById(entryfields[entryfieldsEnum.surname]).value;
    let getstudentDOB = document.getElementById(entryfields[entryfieldsEnum.dateofbirth]).value;
    let getstudentAge = document.getElementById(entryfields[entryfieldsEnum.age]).value;
    let getstudentMOBILE = document.getElementById(entryfields[entryfieldsEnum.mobilenumber]).value;
    let getstudentAddress = document.getElementById(entryfields[entryfieldsEnum.address]).value;
    let getstudentGender = document.getElementById(entryfields[entryfieldsEnum.gender]).value;
    let getModulesText = document.getElementById("ModulesEntryBox").value;

    if(getstudentNum == "" || getstudentName == "" || getstudentSurname == "" || getstudentDOB == "" || getstudentAge == "" || 
        getstudentMOBILE == "" || getstudentAddress == "" || getstudentGender == "" || getModulesText == ""){
            document.querySelector(".error-message h1").innerHTML = "please fill in all of the fields before adding a new student";
            return;
    }

    if(!studentNum.test(getstudentNum)){
        document.querySelector(".error-message h1").innerHTML = "expected a number with min length 4 and max length 5 with no letters or special characters";
        return;
    }
    if(!studentNameANDsurname.test(getstudentName)){
        document.querySelector(".error-message h1").innerHTML = "expected a name greater than length 2 with no numbers or special characters";
        return;
    }
    if(!studentNameANDsurname.test(getstudentSurname)){
        document.querySelector(".error-message h1").innerHTML = "expected a surname greater than length 2 with no numbers or special characters";
        return;
    }
    if(!studentDOB.test(getstudentDOB)){
        document.querySelector(".error-message h1").innerHTML = "expected a date of birth 10 characters long with no letters but only numbers and '-' special character";
        return;
    }
    if(!studentAge.test(getstudentAge)){
        document.querySelector(".error-message h1").innerHTML = "expected a number with no letters or special characters";
        return;
    }
    if(!studentMOBILE.test(getstudentMOBILE)){
        document.querySelector(".error-message h1").innerHTML = "expected a number at least 10 characters long with no letters but only numbers and '-,+' special characters";
        return;
    }
    if(!studentAddress.test(getstudentAddress)){
        document.querySelector(".error-message h1").innerHTML = "expected an address at least 5 characters long";
        return;
    }
    if(!studentGender.test(getstudentGender)){
        document.querySelector(".error-message h1").innerHTML = "expected a gender at least 4 characters long with no numbers";
        return;
    }

    let studentdata = "{"
                        +"\"STUDENTNUMBER\": "+ getstudentNum +","
                        +"\"STUDENTNAME\": \""+ getstudentName +"\","
                        +"\"STUDENTSURNAME\": \""+ getstudentSurname +"\","
                        +"\"DATEOFBIRTH\": \""+ getstudentDOB +"T00:00:00.000Z\","
                        +"\"STUDENTAGE\": "+ getstudentAge +","
                        +"\"MOBILENUMBER\": \""+ getstudentMOBILE +"\","
                        +"\"STUDENTADDRESS\": \""+ getstudentAddress +"\","
                        +"\"STUDENTGENDER\": \""+ getstudentGender +"\","
                        +"\"MODULES\": [";
    
        
    while(!(getModulesText.length <= 0)){
        let modulename__lastindex = getModulesText.indexOf(':');
        let modulename = getModulesText.slice(0, modulename__lastindex);
        let grade__lastindex = getModulesText.indexOf(';');
        let grade = getModulesText.slice(modulename__lastindex + 1, grade__lastindex);
        let replaceThisString = getModulesText.slice(0, grade__lastindex + 1);
        getModulesText = getModulesText.replace(replaceThisString, "");

        if(getModulesText.length <= 0){
            studentdata += "{\"moduleName\": \""+modulename+"\",\"gradeNUM\": "+grade+"}";
        }
        else studentdata += "{\"moduleName\": \""+modulename+"\",\"gradeNUM\": "+grade+"},";
    }

    studentdata +="]}";
    clearFields();
    JAVA__READABLE__TEXT = "";
    JAVA__READABLE__TEXT = studentdata;
    InterfaceOBJ.addNewStudent();
    JAVA__READABLE__TEXT = "";
    return;
}

function searchFor(){
    let searchText = document.getElementById("searchquery").value;

    if(searchText < 1000 || searchText >= 100000){
        document.getElementById("searchquery").value = "";
        return;
    }
    //search whatever
    JAVA__READABLE__TEXT = "";
    JAVA__READABLE__TEXT = searchText;
    InterfaceOBJ.searchForStudentNumber();
    JAVA__READABLE__TEXT = "";
}

function deleteStudent(studentID){
    let studentNode = document.getElementById(studentID);
    studentNode.remove();

    JAVA__READABLE__TEXT = "";
    JAVA__READABLE__TEXT = studentID;
    InterfaceOBJ.deleteThisStudent();
    JAVA__READABLE__TEXT = "";
}

function clearFields(){
    document.getElementById(entryfields[entryfieldsEnum.studentnumber]).value = "";
    document.getElementById(entryfields[entryfieldsEnum.name]).value = "";
    document.getElementById(entryfields[entryfieldsEnum.surname]).value = "";
    document.getElementById(entryfields[entryfieldsEnum.dateofbirth]).value = "";
    document.getElementById(entryfields[entryfieldsEnum.age]).value = "";
    document.getElementById(entryfields[entryfieldsEnum.mobilenumber]).value = "";
    document.getElementById(entryfields[entryfieldsEnum.address]).value = "";
    document.getElementById(entryfields[entryfieldsEnum.gender]).value = "";
    document.getElementById("ModulesEntryBox").value = "";
}

function assignDashBoardValues(data){
    let initDATAJSON = JSON.parse(data);

    if(document.querySelector(".toolbox h1") != null)document.querySelector(".toolbox h1").innerHTML = initDATAJSON.personsCount;
    if(document.querySelector(".tree-height-number") != null)document.querySelector(".tree-height-number").innerHTML = initDATAJSON.tree_height;
    if(document.querySelector(".progress-value") != null)document.querySelector(".progress-value").innerHTML = initDATAJSON.occupiedLeafNodesVal + "%";
    if(document.querySelector(".recent-name") != null)document.querySelector(".recent-name").innerHTML = initDATAJSON.recent_insert_name;
    if(document.querySelector(".recent-id"))document.querySelector(".recent-id").innerHTML = initDATAJSON.recent_insert_studentNumber;
    if(document.querySelector(".progress"))document.querySelector(".progress").style.width = initDATAJSON.occupiedLeafNodesVal * 2.35 + "px";

    occupiedLeafNodesVal = initDATAJSON.occupiedLeafNodesVal;
    personsCount = initDATAJSON.personsCount;
    tree_height = initDATAJSON.tree_height;
    if(initDATAJSON.recent_insert_name != "null" && initDATAJSON.recent_insert_studentNumber != 0){
        recent_insert_name = initDATAJSON.recent_insert_name;
        recent_insert_studentNumber = initDATAJSON.recent_insert_studentNumber;
    }
}