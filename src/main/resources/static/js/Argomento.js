var $jscomp = $jscomp || {};
$jscomp.scope = {};
$jscomp.createTemplateTagFirstArg = function(a) {
  return a.raw = a;
};
$jscomp.createTemplateTagFirstArgWithRaw = function(a, b) {
  a.raw = b;
  return a;
};
var students_id = [], students = [], student = [], ids = [], dd, result = [];
function remove(a) {
  for (var b = 0; b < students_id.length; b++) {
    students_id[b] === a && (console.log(students_id[b]), students_id.splice(b, 1), b--);
  }
  document.getElementById(a).style.display = "none";
  console.log(students_id);
}

function deletelesson(id){
	$.ajax({type:"POST", contentType:"application/json", url:"/deletelesson/"+id,  success:function(a) {
    console.log("SUCCESS : ", a);
    document.getElementById(id).style.display="none"
  }, error:function(a) {
    alert("Error! " + a);
    console.log("ERROR: ", a);
  }});
}

function lessionopt(id){
	document.getElementById("lessenDelete").setAttribute('onclick','deletelesson('+id+')');
}

function add(a) {
  console.log(a);
  var b = ids[a];
  console.log(b.first_name + " " + b.last_name);
  if (students_id.includes(a)) {
    alert("already added");
  } else {
    var c = document.createElement("div"), d = document.createElement("img");
    d.src = b.src;
    c.className = "chip";
    c.innerHTML = b.first_name + "  " + b.last_name;
    c.setAttribute("id", a);
    document.getElementById("chips").appendChild(c);
    c.appendChild(d);
    d = document.createElement("span");
    d.setAttribute("onclick", "remove(" + a + ")");
    d.className = "closebtn";
    d.innerHTML = "&times;";
    c.appendChild(d);
    toastr.success("Utente " + b.first_name + " " + b.last_name + "\u00e8 stato inserito correttamente alla lista");
    students_id.push(a);
  }
}
function searchFunction() {
  var a;
  var b = document.getElementById("search").value.toUpperCase();
  var c = document.getElementsByClassName("card");
  for (a = 0; a < c.length; a++) {
    -1 < c[a].innerHTML.toUpperCase().indexOf(b) ? (console.log(c[a].innerHTML.toUpperCase()), console.log(b), c[a].style.display = "") : c[a].style.display = "none";
  }
}
function GetStudent() {
  $.ajax({type:"GET", contentType:"application/json", url:"/getstudents", dataType:"json", success:function(a) {
    console.log("SUCCESS : ", a);
    for (var b = 0; b < a.length; b++) {
      dd = JSON.stringify(a), students[b] = a[b].first_name + " " + a[b].last_name, ids[a[b].id] = a[b], student[students[b]] = a[b], create_card_profile(a[b]), console.log("stud: " + dd);
    }
  }, error:function(a) {
    alert("Error!");
    console.log("ERROR: ", a);
  }});
}
function create_card_profile(a) {
  document.getElementById("student-row").innerHTML += '<div class="profile-card .col-md-4"> <img class="profile-image " src="' + a.src + '" /> <h2 class="profile-name"> ' + a.first_name + " " + a.last_name + ' </h2><div style="    text-align: center;"><button class="button-62" role="button" onclick="add(' + a.id + ')"> Add</button></div> </div>';
}
function Create_new_lesson() {
  function a() {
    result = [];
    b.forEach(function(d) {
      d.checked && result.push(d.value);
    });
    console.log(result)
    return result;
  }
  var b = document.getElementById("lesson_goal").querySelectorAll('input[type="checkbox"]');
  console.log(a());
  if (0 == students_id.length) {
    document.getElementById("error_student").innerText = "Please insert at least one student";
  } else {
    var c = {name:$("#new-lesson-name").val(), models:document.getElementById("model_name").getAttribute("value"), students:JSON.stringify(students_id), goals:JSON.stringify(a()), activeor:$("input[name=roles]:checked").val()};
    console.log(document.getElementById("model_name").getAttribute("value"));
    console.log(students_id);
    $.ajax({type:"POST", contentType:"application/json", url:"/NewLesson", data:JSON.stringify(c), dataType:"json", success:function(d) {
      $("#new-lesson-modal").modal("hide");
      console.log("SUCCESS : ", d);
      document.getElementById("row").innerHTML += '<div class="col-xs-12 col-md-4" > <div class="color-block-wrapper"> <div class="color-block color-block-lblue color-block-icon-list"> <h4 class="card-title text-right"><button type="button" id="but_conf" class="btn btn-link config" style="border:none;color:black;background-color: #005bb7!important;" attr="onclick=lessionopt('+ d.data4.id +')" data-bs-toggle="modal" data-bs-target="#lezioneopt"   ><i class="fas fa-cog"></i></button></h4><div class="color-block-text" th> <h3 >' + d.data4.name + '</h3> </div> <div class="color-block-head"> N\u00b0 Studenti: ' + d.data4.followed_by.length + ' </div> </div> <div class="color-block-bottom"> <a href="/lezione/' + d.data4.id + '" class="btncard btncard-transparent-lblue">Visita</a> </div> </div> </div>';
      students_id = [];
      document.getElementById("chips").innerHTML = "";
    }, error:function(d) {
      alert("Error!");
      console.log("ERROR: ", d);
    }});
  }
}
$(document).ready(function() {
  $("#students").autocomplete({source:students});
  var a = window.location.pathname.split("/");
  console.log(a[2]);
  GetStudent();
  $("#5").submit(function() {
    event.preventDefault();
    Create_new_lesson();
  });
  $("#add").click(function() {
    if (students_id.includes(student[$("#students").val()].id)) {
      alert("already added");
    } else {
      if (students.includes($("#students").val())) {
        students_id.push(student[$("#students").val()].id);
        var b = document.createElement("div"), c = document.createElement("img");
        console.log(student[$("#students").val()]);
        console.log("array: " + students_id);
        c.src = student[$("#students").val()].src;
        b.className = "chip";
        b.setAttribute("id", student[$("#students").val()].id);
        console.log($("#students").val());
        b.innerHTML = $("#students").val();
        document.getElementById("chips").appendChild(b);
        b.appendChild(c);
        c = document.createElement("span");
        c.setAttribute("onclick", "remove(" + student[$("#students").val()].id + ")");
        c.className = "closebtn";
        c.innerHTML = "&times;";
        b.appendChild(c);
      } else {
        alert("No match found ");
      }
    }
    document.getElementById("students").value = "";
  });
});