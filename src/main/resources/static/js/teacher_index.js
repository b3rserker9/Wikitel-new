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
let text;
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

function active(name) {
	console.log(name)
    var span = document.getElementById(name);
    span.style.display="none";

}

function update() {
    var select = document.getElementById('new-rule-type');
    var option = select.options[select.selectedIndex];
    text = option.text;
    console.log(text);
    switch (option.value) {
        case '2':
            document.getElementById('url').style.display = "block";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "none";
            document.getElementById('question').style.display = "none";
            break;
        case '1':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "block";
            document.getElementById('file').style.display = "none";
            document.getElementById('question').style.display = "none";
            break;
        case '3':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "none";
            document.getElementById('question').style.display = "none";
            break;
        case '4':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "block";
            document.getElementById('question').style.display = "block";

            break;
    }

}

function New_rule_file(){
	    var form = $("#formFile")[0].files[0];
    var data = new FormData();
    data.append("uploadfile", form);
      $.ajax({
                    url: "/uploadFileRule",
                    type: "POST",
                    data: data,
                    DataType: 'json',
                    processData: false,
                    contentType: false,
                    cache: false,
                    success: function(res) {
                        console.log("SUCCESS : ", res);
                        active(res+"s");
                    },
                    error: function(err) {
                        console.error(err);
                    }
                });
}



function New_rule() {

    var model = {

        model_name: $("#new-model-name").val(),
        rule_name: $("#new-model-name").val(),
        rule_type: text,
        rule_url: $("#new-rule-url").val(),
        rule_text: $("#rule_Textarea").val(),

    }

    // DO POST
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "Newrule",
        data: JSON.stringify(model),
        dataType: "json",


        success: function(data) {
            active(data+'s');
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


function deleteModal(modal){
	 $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "Newrule",
                data: JSON.stringify(precondition),
                dataType: "json",
                success: function(data) {
                    console.log("SUCCESS : ", data);
                 

                },
                error: function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });
}

function Create_new_precondition() {
    console.log(current_rule);
    var itemForm = document.getElementById('suggested-preconditions-list');
    var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]');
    // this function will get called when the save button is clicked
    result = [];
    checkBoxes.forEach(item => { // loop all the checkbox item
        if (item.checked) { //if the check box is checked
            var precondition = {
                model_id: current_model,
                rule_id: current_rule,
                rule_name: item.name,
                rule_type: "Pagina Wikipedia",
            }
            console.log(precondition);
            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "Newrule",
                data: JSON.stringify(precondition),
                dataType: "json",
                success: function(data) {
                    console.log("SUCCESS : ", data);
                    document.getElementById("notification").innerHTML += '<div class="toast show" role="alert" aria-live="assertive" id="' + item.name + '" aria-atomic="true">' +
                        '<div class="toast-header">' +
                        '<strong class="me-auto">Nuovo obiettivo trovato</strong>' +
                        '<small class="text-muted">just now</small>' +
                        '<button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button></div>' +
                        '<div class="toast-body"> ' + item.name + ' è stato trovato </div></div>'
                    time(item.name);

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
            /*s.forEach(function(l){		
							document.getElementById("suggested-preconditions-list").innerHTML+='<div class="form-check col-6">'+
 ' <input class="form-check-input" type="checkbox" name="'+l.suggestionsuggestion.page+'" id="flexRadioDefault1">'
 +' <label class="form-check-label" for="flexRadioDefault1">'+ l.suggestionsuggestion.page +' </label></div>'
						})*/


        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });




}

function precondition_setup(data,id) {
    let rule = null;
    let rules = data;
    var x = document.getElementById("rule_list");
    var options = document.querySelectorAll('#rule_list option');
    options.forEach(o => o.remove());
    let suggestion = [];
    for (let i = 0; i < rules.length; i++) {
        console.log(rules[i]);
        var option = document.createElement("option");
        option.text = rules[i].name;
        option.value = i;
        x.add(option);
    }
    console.log(document.getElementById("rule_list").options[0]);
    var x = document.getElementById("rule_list").options[0].value;
    
document.getElementById("firstDelete").setAttribute('href','/deletemodel/'+ id );
    $('#rule_list').on('change', function(e) {
        $('#suggested-preconditions-list').empty();
        let i = this.value
        console.log(rules);
        rule = rules[i];
        console.log(rule);
        $("#rule-length").val(rule.length);
        let suggestions = rule.suggestionm;
        console.log(suggestions);
        current_rule = rule.id;
        suggestions.forEach(function(l) {
            document.getElementById("suggested-preconditions-list").innerHTML += '<div class="form-check col-6">' +
                ' <input class="form-check-input" type="checkbox" name="' + l.page + '" id="flexRadioDefault1">' +
                ' <label class="form-check-label" for="flexRadioDefault1">' + l.page + ' </label></div>'
        })
    });
    rule = rules[0];
    console.log(rule.length);
    $("#rule-length").val(rule.length);
    let suggestions = rule.suggestionm;
    console.log(suggestions);
    suggestions.forEach(function(l) {
        document.getElementById("suggested-preconditions-list").innerHTML += '<div class="form-check col-6">' +
            ' <input class="form-check-input" type="checkbox" name="' + l.page + '" id="flexRadioDefault1">' +
            ' <label class="form-check-label" for="flexRadioDefault1">' + l.page + ' </label></div>'
    })
    current_rule = rules[0].id;
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

        document.getElementById("rows").innerHTML += '<div class="columns animate__animated animate__bounce" id="' + model.id + '"><div class="cards" id="' + model.id + 'c">  <button type="button" id="but_conf" class="btn btn-link config" style="border:none;color:black;" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="Postsuggetion(' + model.id + ')"><i class="fas fa-cog"></i></button><a id="' + model.name + 'a" href="/Argomento/' + model.id + '" class="disabled"> <h3 >' + model.name + '</h3></a> <span id="' + model.id + 's" class="spinner-border"></span> </div></div>'
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
                console.log();
                if (document.getElementById("new-model-name").value.length == 0) {
                    document.getElementById("new-model-name").setAttribute("class", "form-control is-invalid")

                } else {
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

