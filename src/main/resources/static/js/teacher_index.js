var question = [
    ["attivo", "riflessivo", "attivo? Preferisci sperimentare e anche confrontare le tue osservazioni con gli altri, anche mentre lo stai pensando; ti piace il lavoro di gruppo. " +
        " " +
        "riflessivo? Preferisci lavorare prima di tutto da solo, organizzare il materiale e studiarlo. " +
        "Non c'è una risposta giusta! Dipende da quali caratteristiche, individualmente, è quello di ottenere il miglior risultato."
    ],
    ["pratico", "intuitivo", "pratico? Senti (con i tuoi sensi) il materiale di apprendimento. intuitivo? Hai un approccio cerebrale.Non c'è una risposta giusta! L'obiettivo è assimilare le informazioni nel migliore dei modi."],
    ["visivo", "verbale", "visivo? Ti piacerebbe vedere diagrammi e studiare su materiali completamente di immage e grafico. verbale? Preferisci leggere le descrizioni scritte a parole.Non c'è una risposta giusta! L'obiettivo è assimilare le informazioni nel migliore dei modi."],
    ["sequenziale", "globale", "sequenziale? Ti piacerebbe avere una sequenza di argomenti all'interno di una lessione che puoi seguire setp per passo . globale? Preferisci avere una descrizione globale della partizione e dopo tutte le specifiche."],
];
let text = "Pagina Wikipedia";
let students = [];
let students_id = [];
let result = [];
let suggestion = [];
let current_modal;
let current_rule;
let first = true;
var clone;
let rules = [];
let close = false;
const map = new Map();

function active(name) {
	console.log(name)
    var span = document.getElementById(name);
    span.style.zIndex=1;
    span.style.backgroundColor="#ffff";
    var loading = document.getElementById(name + 's');
    loading.style.display= "none";

}

function update() {
    var select = document.getElementById('new-rule-type');
    var option = select.options[select.selectedIndex];
    text = option.text;
    console.log(text);
    switch (option.value) {
        case '2':
            document.getElementById('url').style.display = "block";
            document.getElementById('Add_node_times').style.display = "block";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "none";
            document.getElementById('question').style.display = "none";
            break;
        case '1':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "block";
            document.getElementById('Add_node_times').style.display = "block";
            document.getElementById('file').style.display = "none";
            document.getElementById('question').style.display = "none";
            break;
        case '3':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('Add_node_times').style.display = "none";
            document.getElementById('file').style.display = "none";
            document.getElementById('question').style.display = "none";
            break;
        case '4':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "block";
            document.getElementById('Add_node_times').style.display = "block";
            document.getElementById('question').style.display = "none";

            break;
    }

}

function New_rule_file(){
	    var form = $("#formFile")[0].files[0];
    var data = new FormData();
    data.append("uploadfile", form);
      $.ajax({
                    url: "/uploadFileRule/"+ $("#Add_node_time").val(),
                    type: "POST",
                    data: data,
                    DataType: 'json',
                    processData: false,
                    contentType: false,
                    cache: false,
                    success: function(res) {
                        console.log("SUCCESS : ", res);
                        active(res);
                    },
                    error: function(err) {
                        console.error(err);
                    }
                });
}


function wikipedianotfound(i,p){
	document.getElementById("wikinotradio").innerHTML += '<input type="radio" name="select" id="option-'+ i +'" value="'+ p +'" checked>' +
															 '<label for="option-'+ i +'" class="option option-'+ i +'"><div class="dot"></div><span>'+p+'</span></label>'
}


function New_rule() {

if(text!= "Pagina Wikipedia"){
	var url
	if(!$("#new-rule-url").val().match(/^https?:\/\//i)){
		 url = 'http://' + $("#new-rule-url").val();
		
		}else{
			url = $("#new-rule-url").val()
		}
	
	 var model = {

        model_name: $("#new-model-name").val(),
        rule_name: $("#new-model-name").val(),
        rule_type: text,
        rule_length:$("#Add_node_time").val(),
        rule_url: url,
        rule_text: $("#rule_Textarea").val(),

    }
}else{
	 var model = {

        model_name: $("#new-model-name").val(),
        rule_name: $("#new-model-name").val(),
        rule_type: text,
        rule_url: $("#new-rule-url").val(),
        rule_text: $("#rule_Textarea").val(),

    }
}
  
    
    if(document.getElementsByClassName("flexing").length == 0)
           		document.getElementById("Search_container").innerHTML= ''
           		console.log($("#new-model-name").val().toLowerCase())
             document.getElementById("Search_container").innerHTML += '<li class="list-group-item flexing" ><div class="fw-bold text-wrap ricerca" style="width: 85%;overflow-wrap: break-word;word-wrap: break-word;hyphens: auto;">'+$("#new-model-name").val()+' (<small class="w-100" style="text-align: center;" >'+$("#new-model-name").val()+'</small>)</div><div id="'+$("#new-model-name").val().replace(/\s/g, '').toLowerCase()+'" class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div> </li>'
          
	

    // DO POST
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/Newrule",
        data: JSON.stringify(model),
        dataType: "json",
        success: function(data) {
		console.log(data)
		  if(data.status2 == "Error"){
	 alert(data.rule_name);
}
	if(data.status == "delete"){
		 alert(data.e)
	}else{
			if(data.exists || data.exists==null){
				active(data.model);
			}else{
				var myModal = new bootstrap.Modal(document.getElementById('wikipediaNotfound'), {
  keyboard: false
})
var i =1;
document.getElementById("wikinotradio").innerHTML="";
data.maybe.forEach(function(l) {
	wikipedianotfound(i,l)
	i++;
})
myModal.show()
document.getElementById("deletemodal").setAttribute('onclick','deleteModal('+data.model+')');
			}
            
            console.log("SUCCESS : ", data);
            }
        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}


function newSearch(){
	
	 var model = {

        model_name: $("#new-model-name").val(),
        rule_name: $("input[name=select]:checked").val(),
        rule_type: text,
        rule_url: $("#new-rule-url").val(),
        rule_text: $("#rule_Textarea").val(),

    }

    // DO POST
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "/Newrule",
        data: JSON.stringify(model),
        dataType: "json",


        success: function(data) {
	  if(data.status2 == "Error"){
	 alert(data.rule_name);
}
			if(data.exists){
				active(data.model);
			}else{
				var myModal = new bootstrap.Modal(document.getElementById('wikipediaNotfound'), {
  keyboard: false
})
var i =1;
data.maybe.forEach(function(l) {
	wikipedianotfound(i,l)
	i++;
})
myModal.show()

			}
            
            console.log("SUCCESS : ", data);
        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}

function Create_model() {
    console.log("PPPPP3");
    // PREPARE FORM DATA

    console.log("PPPPP4");
    // DO POST
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "NewModel",
        data: $("#new-model-name").val(),



        success: function(data) {

            add(data.data7)
            if(text=="File"){
            New_rule_file()}
            else{
			New_rule()
			
}
			$('#new_lesson1').modal('hide');
            resetformat();
            console.log("SUCCESS : ", data);


        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });




}


function deleteModal(id){
	 $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "/deletemodel/"+id,
               
                success: function(data) {
                    console.log("SUCCESS : ", data);
                 document.getElementById(id).style.display="none"

                },
                error: function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });
}

function remove_spaces(s){
	return s.replace(/\s/g, '');
}

function Create_new_precondition() {
    console.log(current_rule);
    var itemForm = document.getElementById('suggested-preconditions-list');
    var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]');
    result = [];
    checkBoxes.forEach(item => { // loop all the checkbox item
        if (item.checked) { //if the check box is checked
            var precondition = {
                model_id: current_model,
                rule_id: current_rule,
                rule_name: item.name,
                rule_type: "Pagina Wikipedia",
            }
          
          if(document.getElementsByClassName("flexing").length == 0)
           		document.getElementById("Search_container").innerHTML= ''
           		
             document.getElementById("Search_container").innerHTML += '<li class="list-group-item flexing" ><div class="fw-bold text-wrap ricerca" style="width: 85%;overflow-wrap: break-word;word-wrap: break-word;hyphens: auto;">'+item.name+' (<small class="w-100" style="text-align: center;" >'+current_model+'</small>)</div><div id="'+item.name.replace(/\s/g, '').toLowerCase()+'" class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div> </li>'
          
	
            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "/Newrule",
                data: JSON.stringify(precondition),
                dataType: "json",
                success: function(data) {
                    console.log("SUCCESS : ", data);
                   
                    time(item.name);
        
                     if(data.status2 == "Error"){
	 alert(data.rule_name);
}

                },
                error: function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });


        }
    })

}

function time(name) {
    setTimeout(() => {
        document.getElementById(name).remove();
    }, 5000);

}

function Postsuggetion(id) {
    console.log("id: " + id)
    current_model = id;
    document.getElementById("loading_modal").style.display = "flex";
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "findsuggestion",
        dataType: "json",
        data: JSON.stringify({
            ids: id
        }),


        success: function(data) {
            $("#suggested-preconditions-list").empty();
            console.log("SUCCESS : ", data);
            
            precondition_setup(data ,id);
            document.getElementById("loading_modal").style.display = "none";
            document.getElementById("firstDelete").setAttribute('onclick','deleteModal('+id+')');
            var itemForm = document.getElementById('suggested-preconditions-list');
    var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]:checked');
    console.log(checkBoxes)
   
            /*s.forEach(function(l){		
							document.getElementById("suggested-preconditions-list").innerHTML+='<div class="form-check col-6">'+
 ' <input class="form-check-input" type="checkbox" name="'+l.suggestionsuggestion.page+'" id="flexRadioDefault1">'
 +' <label class="form-check-label" for="flexRadioDefault1">'+ l.suggestionsuggestion.page +' </label></div>'
						})*/


        },
        error: function(e) {
            alert("Error!")
            document.getElementById("firstDelete").setAttribute('onclick','deleteModal('+id+')');
            console.log("ERROR: ", e);
        }
    });




}
function precondition_setup(data,id) {
	document.getElementById("firstDelete").setAttribute('onclick','deleteModal('+id+')');
   console.log(data)
     rules = data;
    var x = document.getElementById("rule_list");
    var options = document.querySelectorAll('#rule_list option');
    options.forEach(o => o.remove());
    for (let i = 0; i < rules.length; i++) {
        console.log(rules[i]);
        var option = document.createElement("option");
        option.text = rules[i].name;
        option.value = i;
        x.add(option);
    }
    console.log(document.getElementById("rule_list").options[0]);
    var x = document.getElementById("rule_list").options[0].value;
    console.log(rules)

   
    let rule = rules[0];
     console.log(rule.length/60);
    $("#rule-length").val(Math.round((rule.length/60) * 100) / 100);
    let suggestions = rule.suggestionm;
    console.log(suggestions);
    suggestions.forEach(function(l) {
        document.getElementById("suggested-preconditions-list").innerHTML += '<div class="form-check col-md-6">' +
            ' <input class="form-check-input" type="checkbox" name="' + l.page + '" id="flexRadioDefault1">' +
            ' <label class="form-check-label" for="flexRadioDefault1">' + l.page + ' </label></div>'
    })
    current_rule = rules[0].id;
    console.log(rules)
     $('#rule_list').on('change', function(e) {
	
        $('#suggested-preconditions-list').empty();
        console.log(rules);
        let i = this.value
        console.log(i);
        
       let rule = rules[i];
        console.log(rule.length/60);
        $("#rule-length").val(Math.round((rule.length/60) * 100) / 100);
        let suggestions = rule.suggestionm;
        console.log(suggestions);
        current_rule = rule.id;
        document.getElementById("loading_modal").style.display = "flex";
        suggestions.forEach(function(l) {
            document.getElementById("suggested-preconditions-list").innerHTML += '<div class="form-check col-md-6">' +
                ' <input class="form-check-input" type="checkbox" name="' + l.page + '" id="flexRadioDefault1">' +
                ' <label class="form-check-label" for="flexRadioDefault1">' + l.page + ' </label></div>'
        })
        document.getElementById("loading_modal").style.display = "none";
        console.log("2")
    });
}




function GetSuggestion(id) {
    $.ajax({

        type: "GET",
        contentType: "application/json",
        url: "GetSuggestion",
        dataType: "json",
        data: JSON.stringify({
            ids: id
        }),

        success: function(data) {

            console.log("SUCCESS : ", data);

            data.forEach(function(l) {
                document.getElementById("collapse").innerHTML += '<div class="form-check">' +
                    ' <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1">' +
                    ' <label class="form-check-label" for="flexRadioDefault1">' +l.page + ' </label></div>'
            })


        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}

let a = [];

function add(model) {
    current_modal = model.id
    if (document.getElementById("rows") == null) {
        document.getElementById("collapse").innerHTML += '<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/' + model.id + '">' + model.name + '</a></div>'
    } else {

        document.getElementById("rows").innerHTML += '<div class="col-md-3 col-sm-6 item"> <div class="card item-card card-block" style="z-index:-60;background-color: #aba6a626" id="' + model.id + '"><div class="spinner-border spinner-borders text-primary" role="status" id="'+model.id+'s"> <span class="visually-hidden">Loading...</span> </div> <h4 class="card-title text-right"><button type="button" id="but_conf" class="btn btn-link config" style="border:none;color:black" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="Postsuggetion(' + model.id + ')"><i class="fas fa-cog"></i></button></h4> <a id=" ' + model.name + 'a" href="/Argomento/' + model.id + '"><h5 class="item-card-title mt-3 mb-3" style="text-align:center;font-size:1.75rem!important" >'+ model.name + '</h5></a> </div> </div>'
        document.getElementById("collapse").innerHTML += '<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/' + model.id + '">' + model.name + '</a></div>'
    }
    a.push(model);

}


function resetformat() {

    var element = document.getElementById("clone");
    element.parentNode.removeChild(element);
    document.getElementById("new_lesson1").appendChild(clone);
    prep_modal();
    console.log("fatto");
}

$(document).ready(
    function() {
	
          clone = document.getElementById("clone").cloneNode(true);
        questions();
        prep_modal();
 var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
  return new bootstrap.Popover(popoverTriggerEl)
})
        $("#new_precondition").submit(function() {
            event.preventDefault();

            Create_new_precondition();
        });




    });

function questions() {
    let id = 0;
    //ROOT
    let div = document.getElementById("question");
    for (let i = 0; i < question.length; i++) {
        // LINK
        let al = document.createElement('a');
        al.className = "link text-decoration-none";
         al.setAttribute("data-bs-toggle", "collapse");
        al.setAttribute("href", "#collapseExample" + i);
        al.setAttribute("role", "button");
        al.setAttribute("style", "margin-top:2%;");
        al.setAttribute("aria-controls", "collapseExample");
        al.setAttribute("aria-expanded", "false");
        al.textContent = question[i][0] + " o " + question[i][1];
        let span = document.createElement('span');
        al.appendChild(span);
        let icon = document.createElement('i');
        icon.className = "fas fa-caret-down";
        span.appendChild(icon);

        //Info
        let info = document.createElement('a');
        info.className = "btn btn-link";
        info.setAttribute("tabindex", "0")
        info.setAttribute("role", "button");
        info.setAttribute("data-bs-toggle", "popover");
        info.setAttribute("data-bs-trigger", "focus");
        info.setAttribute("data-bs-content", question[i][2]);
        let span2 = document.createElement('span');
        let icon2 = document.createElement('i');
        icon2.className = "fas fa-info-circle";
        span2.appendChild(icon2);
        info.appendChild(span2);

        let links = document.createElement('div')
        links.className = "links";
        links.appendChild(al);
        links.appendChild(info);
        div.appendChild(links);

        //Collapse
        let collapse = document.createElement('div');
        collapse.className = "collapse"
        collapse.setAttribute("id", "collapseExample" + i);
        div.appendChild(collapse);

        //Testo
        let text = document.createElement('div');
        text.className = "testo";
        let right = document.createElement('p');
        right.className = "right";
        right.textContent = question[i][0];
        let left = document.createElement('p');
        left.className = "left";
        left.textContent = question[i][1]
        text.append(right);
        text.append(left);

        //Card
        let card = document.createElement('div');
        card.className = "card card-body"
        collapse.appendChild(card);
        card.appendChild(text);
        //radio
        let r = document.createElement('div');
        r.setAttribute("id", "radios" + i);
        card.appendChild(r);
        for (let j = 0; j < 15;
            (j++)) {
            let radio = document.createElement('input');
            let label = document.createElement('label');
            radio.setAttribute("id", "option" + j + id);
            radio.setAttribute("name", "options" + i);
            radio.setAttribute("type", "radio");
            radio.setAttribute("value", j - 7);
            label.setAttribute("for", "option" + j + id);
            label.textContent = (j - 7);
            r.appendChild(radio);
            r.appendChild(label);
            id++;
        }

    }

    var one = $("#radios0").radioslider({
        fillOrigin: '0',
        size: 'small',
        fit: true,
    });
    var two = $("#radios1").radioslider({
        fillOrigin: '0',
        size: 'small',
        fit: true,
    });
    var three = $("#radios2").radioslider({
        fillOrigin: '0',
        size: 'small',
        fit: true,
    });
    var four = $("#radios3").radioslider({
        fillOrigin: '0',
        size: 'small',
        fit: true,
    });
    one.radioslider('setValue', '0');
    two.radioslider('setValue', '0');
    three.radioslider('setValue', '0');
    four.radioslider('setValue', '0');

}

function validate(a){
	console.log(a)
	var d = 1;
	var error = true;
	switch(a) {
		case 0:
		 if (document.getElementById("new-model-name").value.length == 0) {
                    document.getElementById("new-model-name").setAttribute("class", "form-control is-invalid")
                    d = 0

                }else{
		 			document.getElementById("new-model-name").setAttribute("class", "form-control valid")

}
		break;
		case 1:
		if(text != "Pagina Wikipedia"){
		console.log(document.getElementById("new-rule-url").value.length)
				if(document.getElementById("new-rule-url").value.length == 0 && text == "Pagina Web"){
					console.log("SL D")
		document.getElementById("error_type").innerText = "Url non inserito";
		d = false
		error = false
	}
	
	if(document.getElementById("rule_Textarea").value.length == 0 && text == "Testo"){
		document.getElementById("error_type").innerText = "Testo non inserito";
		d = false
		error = false
	}
	if(document.getElementById("formFile").files.length == 0 && text == "File"){
		document.getElementById("error_type").innerText = "File non inserito";
		d = false
		error = false
	}
		if(error){
		document.getElementById("error_type").innerText = '';
			
	}
	if (document.getElementById("Add_node_time").value.length == 0){
		document.getElementById("error_time").innerText = "Durata lezione non inserita";
		d = false
	}else{
		document.getElementById("error_time").innerText = '';
			
	}}
		break; 
		}
		console.log(d)
		return d;
}

function prep_modal() {
    $(".new_model").each(function() {

        var element = this;
        var pages = $(this).find('.modal-split');
console.log(pages.length);
        if (pages.length != 0) {
            pages.hide();
            pages.eq(0).show();
            var b_button = document.createElement("button");
            b_button.setAttribute("type", "button");
            b_button.setAttribute("class", "btn btn-primary");
            b_button.setAttribute("style", "display: none;");
            b_button.innerHTML = "Back";
            console.log("iam in");
            var n_button = document.createElement("button");
            n_button.setAttribute("type", "button");
            n_button.setAttribute("class", "btn btn-primary");
            n_button.innerHTML = "Next";

            $(this).find('.modal-footer').append(b_button).append(n_button);


            var page_track = 0;
            $(n_button).click(function() {
			if(validate(page_track)){
                    document.getElementById("new-model-name").setAttribute("class", "form-control valid")
                    this.blur();

                    if (page_track == 0) {
                        $(b_button).show();
                    }

                    if (page_track == pages.length - 2) {

                        $(n_button).text("Submit");

                    }


                    if (page_track == pages.length - 1) {
                        n_button.setAttribute("type", "submit");
                        $("#new_models").submit(function() {
                            // Prevent the form from submitting via the browser.
                            event.preventDefault();
                            if (a.some(a => a.name === $("#new-model-name").val())) {
                                alert("Argomento gia esistente");
                            } else {
                                Create_model();
                               
                            }

                        });
                    }

                    if (page_track < pages.length - 1) {
                        page_track++;
						document.getElementById("model_rule_name").innerText=$("#new-model-name").val();
                        pages.hide();
                        pages.eq(page_track).show();
                    }

                }
            });

            $(b_button).click(function() {

                if (page_track == 1) {
                    $(b_button).hide();
                }

                if (page_track == pages.length - 1) {
                    $(n_button).text("Next");
                }

                if (page_track > 0) {
                    page_track--;

                    pages.hide();
                    pages.eq(page_track).show();
                }


            });

        }

    });
}



function ricerchetot(){
	   $.ajax({

        type: "Get",
        contentType: "application/json",
        url: "/ricerchetot",
        dataType: "json",
        

        success: function(data) {

            console.log("SUCCESS : ", data);
		Object.keys(data).forEach(function(le) {
			console.log(data[le])
			data[le].forEach(function(s){
				if(document.getElementsByClassName("flexing").length == 0)
           		document.getElementById("Search_container").innerHTML= ''
             document.getElementById("Search_container").innerHTML += '<li class="list-group-item flexing" ><div class="fw-bold text-wrap ricerca" style="width: 85%;overflow-wrap: break-word;word-wrap: break-word;hyphens: auto;">'+s+' (<small class="w-100" style="text-align: center;" >'+le+'</small>)</div><div id="'+remove_spaces(s).toLowerCase()+'" class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div> </li>'
			})
		
		})
        


        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}
$(document).ready(function() {
	console.log("ciaosk")
 ricerchetot()
});
