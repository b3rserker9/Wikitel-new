let students_id = [];
let students = [];
let student = [];
let ids = [];
let dd;
let result = [];


function remove(id){
	for( var i = 0; i < students_id.length; i++){ 
                                   
        if ( students_id[i] === id) { 
	console.log(students_id[i]);
            students_id.splice(i, 1); 
            
            i--; 
        }
    }
document.getElementById(id).style.display='none';
console.log(students_id)
}

function ajaxGet2() {
	
    console.log("PPPPP3");
    console.log("HERE");
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/Getlessonsreal",
        dataType: "json",
        data: JSON.stringify({
            id: document.getElementById("modelid").getAttribute('value')
        }),
        success: function(data) {
            console.log("SUCCESS : ", data);
            a = data;
            data.forEach(function(l) {
                if (document.getElementById("rows") == null) {
                    document.getElementById("collapse").innerHTML += '<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/' + l.id + '">' + l.name + '</a></div>'
                } else {
                    document.getElementById("rows").innerHTML += '<div class="columns animate__animated animate__bounce"><div class="cards">  <button type="button" class="btn btn-link config" style="border:none;color:black;" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="Postsuggetion(' + l.id + ')"><i class="fas fa-cog"></i></button><a href="/Argomento/' + l.id + '"> <h3 >' + l.name + '</h3> <span class="spinner-border spinner-border-sm"></span></a> </div></div>'
                    document.getElementById("collapse").innerHTML += '<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/' + l.id + '">' + l.name + '</a></div>'
                }
            })
        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}


function add(id) {
    console.log(id);
    
    let user = ids[id];
    console.log(user.first_name + ' ' + user.last_name);
    if (!students_id.includes(id)) {
        let div = document.createElement('div');
        let img = document.createElement('img');
        img.src = user.src;
        div.className = 'chip';
        div.innerHTML = `${user.first_name}  ${user.last_name}`;
        let chips = document.getElementById("chips");
        chips.appendChild(div);
        div.appendChild(img);
         let span = document.createElement('span');
    span.setAttribute("onclick",'remove('+ student[$("#students").val()].id +')')
    span.className ="closebtn";
    span.innerHTML="&times;"
    div.appendChild(span);
        toastr.success('Utente ' + user.first_name + ' ' + user.last_name + 'è stato inserito correttamente alla lista')
        students_id.push(id);
        

    } else {
        alert("already added");
    }


}

function searchFunction() {
    // Declare variables
    var input, filter, list, i;
    input = document.getElementById('search');
    filter = input.value.toUpperCase();
    list = document.getElementsByClassName('card');

    // Loop through all list items, and hide those who don't match the search query
    for (i = 0; i < list.length; i++) {
        if (list[i].innerHTML.toUpperCase().indexOf(filter) > -1) {
            console.log(list[i].innerHTML.toUpperCase());
            console.log(filter);
            list[i].style.display = "";
        } else {
            list[i].style.display = "none";
        }
    }
}

function GetStudent() {
    $.ajax({

        type: "GET",
        contentType: "application/json",
        url: "/getstudents",
        dataType: "json",

        success: function(data) {

            console.log("SUCCESS : ", data);
            for (let i = 0; i < data.length; i++) {
	dd=JSON.stringify(data);
                students[i] = data[i].first_name + ' ' + data[i].last_name;
                ids[data[i].id] = data[i];
                student[students[i]] = data[i];
                create_card_profile(data[i]);
                console.log("stud: "+ dd);
            }


        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}

function create_card_profile(student) {
    let root = document.getElementById("root");
    let profile_card = document.createElement('div');
    profile_card.className = "card profile-card-1";
    let prova = document.createElement('div');
    prova.className = "prova";
    let background = document.createElement('img');
    background.src = student.src;
    background.className = "background";
    prova.appendChild(background);
    let profile_img = document.createElement('img');
    profile_img.src = student.src;
    profile_img.className = "profile";
    prova.append(profile_img);
    let card_content = document.createElement('div');
    card_content.className = "card-content";
    prova.appendChild(card_content);
    let h1 = document.createElement('H2');
    let text = document.createTextNode(student.first_name + ' ' + student.last_name);
    let small = document.createElement('small');
    let text_s = document.createTextNode(student.role);
    let icon_b = document.createElement('div');
    card_content.appendChild(icon_b);
    icon_b.className = "icon-block";
    let button = document.createElement('button');
    button.setAttribute('onclick', 'add(this.id)')
    let icon = document.createElement('i');
    icon.className = "bi bi-plus-circle";
    button.appendChild(icon);
    button.className = "btn";
    button.id = student.id;
    icon_b.appendChild(button);
    card_content.appendChild(h1);
    h1.appendChild(text);
    h1.appendChild(small);
    small.appendChild(text_s);
    profile_card.appendChild(prova);
    root.appendChild(profile_card);
   
}


function Create_new_lesson() {
    var itemForm = document.getElementById('lesson_goal');
    var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]');

    function getData() { // this function will get called when the save button is clicked
        result = [];
        checkBoxes.forEach(item => { // loop all the checkbox item
            if (item.checked) { //if the check box is checked
                result.push(item.value); //stored the objects to result array
            }
        })
        return result;
    }
    // PREPARE FORM DATA
    console.log(getData());
    if(students_id.length == 0){
	document.getElementById("error_student").innerText="Please insert at least one student"
}else{
    var lesson = {

        name: $("#new-lesson-name").val(),
        models: document.getElementById('model_name').getAttribute('value'),
        students: JSON.stringify(students_id),
        goals: JSON.stringify(getData()),
        activeor: $('input[name=roles]:checked').val()
    }
    console.log(document.getElementById('model_name').getAttribute('value'));

    console.log(students_id);
    // DO POST
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "/NewLesson",
        data: JSON.stringify(lesson),
        dataType: "json",



        success: function(data) {
            $('#new-lesson-modal').modal('hide');
            console.log("SUCCESS : ", data);
                       
                    document.getElementById("rows").innerHTML += '<div class="columns animate__animated animate__bounce"><div class="cards"> <a href="/lezione/' + data.data4.id + '"> <h3 >' + data.data4.name + '</h3></a> </div></div>'
                   
        
            students_id = [];
              document.getElementById("chips").innerHTML = '';
  				

        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });


}

}

$(document).ready(
    function() {
        $("#students").autocomplete({
            source: students
        });

        var pathArray = window.location.pathname.split('/');
        console.log(pathArray[2]);

        GetStudent();
        $("#5").submit(function() {
            event.preventDefault();
            Create_new_lesson();


        });


        $("#add").click(function() {

            if (!students_id.includes(student[$("#students").val()].id)) {
                if (students.includes($("#students").val())) {
                    students_id.push(student[$("#students").val()].id);
                    let div = document.createElement('div');
                    let img = document.createElement('img');
                    console.log(student[$("#students").val()])
                    console.log("array: "+ students_id);
                    img.src = student[$("#students").val()].src;
                    div.className = 'chip';
                    div.setAttribute("id",student[$("#students").val()].id);
                    console.log($("#students").val());
                    div.innerHTML = $("#students").val();
                    let chips = document.getElementById("chips");
                    chips.appendChild(div);
                    div.appendChild(img);
                     let span = document.createElement('span');
    span.setAttribute("onclick",'remove('+ student[$("#students").val()].id +')')
    span.className ="closebtn";
    span.innerHTML="&times;"
    div.appendChild(span);
                } else {
                    alert("No match found ");
                }
            } else {
                alert("already added");
            }
            document.getElementById('students').value = '';
        });



    });
   