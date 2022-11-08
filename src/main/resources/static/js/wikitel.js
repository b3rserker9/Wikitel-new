var question = [["attivo", "riflessivo", "attivo? Preferisci sperimentare e anche confrontare le tue osservazioni con gli altri, anche mentre lo stai pensando; ti piace il lavoro di gruppo.  riflessivo? Preferisci lavorare prima di tutto da solo, organizzare il materiale e studiarlo. Non c'\u00e8 una risposta giusta! Dipende da quali caratteristiche, individualmente, \u00e8 quello di ottenere il miglior risultato."], ["pratico", "intuitivo", "pratico? Senti (con i tuoi sensi) il materiale di apprendimento. intuitivo? Hai un approccio cerebrale.Non c'\u00e8 una risposta giusta! L'obiettivo \u00e8 assimilare le informazioni nel migliore dei modi."], 
["visivo", "verbale", "visivo? Ti piacerebbe vedere diagrammi e studiare su materiali completamente di immage e grafico. verbale? Preferisci leggere le descrizioni scritte a parole.Non c'\u00e8 una risposta giusta! L'obiettivo \u00e8 assimilare le informazioni nel migliore dei modi."], ["sequenziale", "globale", "sequenziale? Ti piacerebbe avere una sequenza di argomenti all'interno di una lessione che puoi seguire setp per passo . globale? Preferisci avere una descrizione globale della partizione e dopo tutte le specifiche."],
], src = "https://i.imgur.com/tq1CpHn.png", ruolo = !1;
function ruolos(c) {
  var d = document.getElementById("stepper1"), b = document.getElementById("stepper2");
 if( "STUDENT" === c)  {d.style.display = "block"
  b.style.display = "block",ruolo = 0
   document.getElementById("next").innerText = "Next"
    document.getElementById("next").setAttribute("data-bs-dismiss","modaldff"), console.log("!")} 
    else{ d.style.display = "none"
     b.style.display = "none", ruolo = !0
      document.getElementById("next").innerText = "Submit"
 document.getElementById("next").setAttribute("data-bs-dismiss","modal"),console.log("!d") ;}
}
function image() {
  "plus" != document.querySelector('input[name="drone"]:checked').id && (document.getElementById("myImg").src = document.getElementById(document.querySelector('input[name="drone"]:checked').id.slice(-1)).src, src = document.getElementById(document.querySelector('input[name="drone"]:checked').id.slice(-1)).src);
}

function getData() {
  var c = document.getElementById("interests").querySelectorAll('input[type="checkbox"]'), d = [];
  d = [];
  c.forEach(function(b) {
    b.checked && d.push(b.value);
  });
  return d;
}
function ajaxPost() {

  var c = {profile:JSON.stringify(getData()), first_name:$("#first_name").val(), last_name:$("#last_name").val(), email:$("#email_r").val(), password:$("#password_r").val(), src:src, role:$("input[name=roles]:checked").val(), one:JSON.stringify({one:$("input[name=options0]:checked").val(), two:$("input[name=options1]:checked").val(), three:$("input[name=options2]:checked").val(), four:$("input[name=options3]:checked").val()})};

  $.ajax({type:"POST", contentType:"application/json", url:"register", data:JSON.stringify(c), dataType:"json", success:function(d) {
    console.log("SUCCESS : ", d);
    $("#modal3").modal("show");
    $("#modal2").modal("hide");
    document.getElementById("modal2").style.display="none";
    "plus" == document.querySelector('input[name="drone"]:checked').id && (event.preventDefault(), uploadFile());
   	
  }, error:function(d) {
    alert("Error!");
    console.log("ERROR: ", d);
  }});
  $("#ok").click(function() {
    $("#modal1").modal("show");
    $("#modal3").modal("hide");
  });
}



function uploadFile(c) {
  c = document.querySelector('input[name="drone"]:checked');
  if (null != c) {
    console.log(c.id);
    c = $("#photo-upload-button")[0].files[0];
    console.log(c);
    var d = new FormData();
    d.append("uploadfile", c);
    console.log(d);
    $.ajax({url:"/uploadFile", type:"POST", data:d, DataType:"json", processData:!1, contentType:!1, cache:!1, success:function(b) {
      $("#exampleModal").modal("hide");
      document.getElementById("myImg").src = document.getElementById("file-ip-1-preview").src;
    }, error:function(b) {
      console.log(b);
    }});
  } else {
    document.write("Nothing has been selected");
  }
}
function showPreview() {
  document.getElementById("photo-upload-button").click();
  var c = document.getElementById("photo-upload-button"), d = document.getElementById("file-ip-1-preview");
  c.addEventListener("change", function() {
    var b = c.files[0];
    if (b) {
      var a = new FileReader();
      a.readAsDataURL(b);
      a.addEventListener("load", function() {
        d.style.display = "block";
        document.getElementById("backg").style.background = "#a5a1a100";
        d.src = this.result;
        file = b;
        document.getElementById("myImg").src = this.result;
      });
    }
  });
}
function validateForm(c) {
  var d = !0;
  switch(c) {
    case 0:
      0 == document.getElementById("first_name").value.length || 0 == document.getElementById("last_name").value.length ? (document.getElementById("first_name").setAttribute("class", "form-control is-invalid"), document.getElementById("last_name").setAttribute("class", "form-control is-invalid"), d = !0) : (document.getElementById("first_name").setAttribute("class", "form-control valid"), document.getElementById("last_name").setAttribute("class", "form-control valid"), d = !1);
      break;
    case 1:
      c = /^([A-Za-z0-9_\-\.])+@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
      var b = document.getElementById("email_r");
      if (0 == b.value.length || 0 == document.getElementById("password_r").value.length) {
        b.setAttribute("class", "form-control is-invalid"), document.getElementById("password_r").setAttribute("class", "form-control is-invalid"), d = !0;
      } else {
        if ("email_r" == b.name) {
          if (b.value.match(c)) {
            var a = {email:$("#email_r").val(),};
            $.ajax({type:"POST", contentType:"application/json", url:"/getEmail", data:JSON.stringify(a), dataType:"json", async:!1, success:function(e) {
              console.log("SUCCESS : ", e);
              "Done" == e.status && (b.setAttribute("class", "form-control valid"), document.getElementById("password_r").setAttribute("class", "form-control valid"), d = !1);
              document.getElementById("error").innerHTML = "Email already in use";
            }, error:function(e) {
              alert("Error!");
              console.log("ERROR: ", a);
            }});
          } else {
            document.getElementById("error").innerHTML = "Wrong Format";
          }
        }
      }
      break;
    case 2:
      d = $("input[name=roles]:checked").val() ? !1 : !0;
      break;
    case 3:
      d = !1;
      break;
    case 4:
      d = !1;
  }
  console.log(d);
  return d;
}
$(document).ready(function() {

  document.getElementById("errorlog").getAttribute("value") && ($("#modal1").modal("show"), (new mdb.Collapse(document.getElementById("loginErrormes"))).show());
  prep_modal();
  questions();
  [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]')).map(function(c) {
    return new bootstrap.Popover(c);
  });
});
function prep_modal() {
  $(".register").each(function() {
    var c = $(this).find(".modal-split");
    console.log("prova1: " + c);
    if (0 != c.length) {
      c.hide();
      c.eq(0).show();
      var d = document.createElement("button");
      d.setAttribute("type", "button");
      d.setAttribute("class", "btn btn-primary");
      d.setAttribute("style", "display: none;");
      d.innerHTML = "Back";
      var b = document.createElement("button");
      b.setAttribute("type", "button");
      b.setAttribute("class", "btn btn-primary");
      b.setAttribute("style", "display: block;");
      b.setAttribute("id", "next");
      b.innerHTML = "Next";
      $(this).find(".modal-footer").append(d).append(b);
      var a = 0;
      $(b).click(function() {
        if (!validateForm(a)) {
          console.log("page_track= " + a);
          this.blur();
          0 == a && $(d).show();
          a == c.length - 2 && $(b).text("Submit");
          if (a == c.length - 1 || a == c.length - 3 && ruolo) {
				document.getElementById("next").setAttribute("data-bs-dismiss","modal")
             a = 4, b.setAttribute("type", "submit"), $("#register_form").submit(function() {

              event.preventDefault();
         
              ajaxPost();
            });
          }
          a < c.length - 1 && (a++, console.log(a), $("#progressbar li:nth-child(" + (a + 1) + ")").addClass("active"), c.hide(), c.eq(a).show());
        }
      });
      $(d).click(function() {
        1 == a && $(d).hide();
        a == c.length - 1 && $(b).text("Next");
        0 < a && ($("#progressbar li:nth-child(" + (a + 1) + ")").removeClass("active"), a--, console.log(a), c.hide(), c.eq(a).show());
      });
    }
  });
}
function questions() {
  for (var c = 0, d = document.getElementById("question"), b = 0; b < question.length; b++) {
    var a = document.createElement("a");
    a.className = "link text-decoration-none";
    a.setAttribute("data-bs-toggle", "collapse");
    a.setAttribute("href", "#collapseExample" + b);
    a.setAttribute("role", "button");
    a.setAttribute("style", "margin-top:2%;");
    a.setAttribute("aria-controls", "collapseExample");
    a.setAttribute("aria-expanded", "false");
    a.textContent = question[b][0] + " o " + question[b][1];
    var e = document.createElement("span");
    a.appendChild(e);
    var f = document.createElement("i");
    f.className = "fas fa-caret-down";
    e.appendChild(f);
    e = document.createElement("a");
    e.className = "btn btn-link";
    e.setAttribute("tabindex", "0");
    e.setAttribute("role", "button");
    e.setAttribute("data-bs-toggle", "popover");
    e.setAttribute("data-bs-trigger", "focus");
    e.setAttribute("data-bs-content", question[b][2]);
    f = document.createElement("span");
    var g = document.createElement("i");
    g.className = "fas fa-info-circle";
    f.appendChild(g);
    e.appendChild(f);
    f = document.createElement("div");
    f.className = "links";
    f.appendChild(a);
    f.appendChild(e);
    d.appendChild(f);
    a = document.createElement("div");
    a.className = "collapse";
    a.setAttribute("id", "collapseExample" + b);
    d.appendChild(a);
    e = document.createElement("div");
    e.className = "testo";
    f = document.createElement("p");
    f.className = "right";
    f.textContent = question[b][0];
    g = document.createElement("p");
    g.className = "left";
    g.textContent = question[b][1];
    e.append(f);
    e.append(g);
    f = document.createElement("div");
    f.className = "card card-body";
    a.appendChild(f);
    f.appendChild(e);
    a = document.createElement("div");
    a.setAttribute("id", "radios" + b);
    f.appendChild(a);
    for (e = 0; 15 > e; e++) {
      f = document.createElement("input"), g = document.createElement("label"), f.setAttribute("id", "option" + e + c), f.setAttribute("name", "options" + b), f.setAttribute("type", "radio"), f.setAttribute("value", e - 7), g.setAttribute("for", "option" + e + c), g.textContent = e - 7, a.appendChild(f), a.appendChild(g), c++;
    }
  }
  c = $("#radios0").radioslider({fillOrigin:"0", size:"small", fit:!0,});
  d = $("#radios1").radioslider({fillOrigin:"0", size:"small", fit:!0,});
  b = $("#radios2").radioslider({fillOrigin:"0", size:"small", fit:!0,});
  a = $("#radios3").radioslider({fillOrigin:"0", size:"small", fit:!0,});
  c.radioslider("setValue", "0");
  d.radioslider("setValue", "0");
  b.radioslider("setValue", "0");
  a.radioslider("setValue", "0");
}
;