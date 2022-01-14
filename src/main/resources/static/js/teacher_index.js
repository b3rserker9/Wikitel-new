	let text;
	let students = [];
	let students_id = [];
	let result = [];
	let suggestion = [];
	let current_modal;
	let current_rule;
	let r;
	
	
	
	
	function update() {
				var select = document.getElementById('new-rule-type');
				var option = select.options[select.selectedIndex];
				text=option.text;
				console.log(option.value);
				switch(option.value){
					case '2':
					document.getElementById('url').style.display="block";
					document.getElementById('textarea').style.display="none";
					document.getElementById('file').style.display="none";
					break;
					case '1':
					document.getElementById('url').style.display="none";
					document.getElementById('textarea').style.display="block";
					document.getElementById('file').style.display="none";
					break;
					case '3':
					document.getElementById('url').style.display="none";
					document.getElementById('textarea').style.display="none";
					document.getElementById('file').style.display="none";
					break;
					case '4':
					document.getElementById('url').style.display="none";
					document.getElementById('textarea').style.display="none";
					document.getElementById('file').style.display="block";
					
					break;
				}
				
}



	function New_rule() {
		
console.log("PPPPP3");
				// PREPARE FORM DATA
				 
				var model = {				
		
			model_name: $("#new-model-name").val(),
			rule_name : $("#new-rule-name").val(),
			rule_type : "Testo",
			rule_url :$("#new-rule-url").val(),
			rule_text : $("#rule_Textarea").val()
		
				}
					 var form = $("#formFile")[0].files[0];
					console.log(form);
   					 var data = new FormData();
    				data.append("uploadfile", form);
console.log("PPPPP4");
				// DO POST
				$.ajax({
						
					type : "POST",
					contentType: "application/json",
					url :"Newrule",
					data : JSON.stringify(model),
					dataType: "json",
					
					
					success : function(data) {						
						console.log("SUCCESS : ", data);
				 var form = $("#formFile")[0].files[0];
console.log(form);
    var data = new FormData();
    data.append("uploadfile", form);
    console.log(data);
  $.ajax({
            url: "/uploadFileText",
            type: "POST",
            data: data,
            DataType:'json',
            processData: false,
            contentType: false,
            cache: false,
            success: function (res) {
	console.log("SUCCESS : ", data);
			document.getElementById('load').style.display="none";
						document.getElementById('h3').style.opacity="1";
            },
            error: function (err) {
                console.error(err);
            }
        });
					//	Postsuggetion(data)
						
						
						
						
					},
					error : function(e) {
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
						
					type : "POST",
					contentType: "application/json",
					url :"http://localhost:7000/NewModel",
					data : $("#new-model-name").val(),
					
					
					
					success : function(data) {
						$("#new_lesson1").modal('hide');
						add(data.data7)
						New_rule()
						console.log("SUCCESS : ", data);
						
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			
			
				

			}
			
			function Create_new_precondition() {
				
					var itemForm = document.getElementById('suggested-preconditions-list');
  		var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]');
  		 // this function will get called when the save button is clicked
            result = [];
            checkBoxes.forEach(item => { // loop all the checkbox item
                if (item.checked) {  //if the check box is checked
                    	var precondition = {
			model_id : current_model,	
			rule_id : current_rule,			
			rule_name : item.name,
				}
console.log(precondition);
				$.ajax({
						
					type : "POST",
					contentType: "application/json",
					url :"NewPrecondition",
					data : JSON.stringify(precondition),
					dataType: "json",
					success : function(data) {
						console.log("SUCCESS : ", data);
						document.getElementById("notification").innerHTML+='<div class="toast show" role="alert" aria-live="assertive" id="' + item.name + '" aria-atomic="true">' +
      '<div class="toast-header">'+
        '<strong class="me-auto">Nuovo obiettivo trovato</strong>'+
        '<small class="text-muted">just now</small>'+
        '<button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button></div>'+
      '<div class="toast-body"> ' + item.name +' è stato trovato </div></div>'
   									time(item.name);
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
				
       
                }
            })

			}
			function time(name){
				setTimeout(() => {  document.getElementById(name).remove(); }, 5000);
				
			}
			
			function Postsuggetion(id) {
				console.log("id: " +id)
			current_model=id;
				$.ajax({	
										
				type : "POST",
				contentType: "application/json",
					url :"findsuggestion",
					dataType: "json",
					data :JSON.stringify({ids: id}),		
					
												
					success : function(data) {
						$("#suggested-preconditions-list").empty();
						console.log("SUCCESS : ", data);
						precondition_setup(data);
						/*s.forEach(function(l){		
							document.getElementById("suggested-preconditions-list").innerHTML+='<div class="form-check col-6">'+
 ' <input class="form-check-input" type="checkbox" name="'+l.suggestion.page+'" id="flexRadioDefault1">'
 +' <label class="form-check-label" for="flexRadioDefault1">'+ l.suggestion.page +' </label></div>'
						})*/
						
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			
			
				

			}
			
			function precondition_setup(data){
				var x = document.getElementById("rule_list");
				 r = [];
				console.log();
				let suggestion=[];
				data.forEach(function(l){
					suggestion[l.id]=data.suggestions;
				r[l.id]=l;
				console.log(l);
    var x = document.getElementById("rule_list");
  var option = document.createElement("option");
  option.text = l.name;
   option.value = l.id;
  x.add(option);
});
var x = document.getElementById("rule_list").options[0].value;
  
$('#rule_list').on('change', function (e) {
	$('#suggested-preconditions-list').empty();
        x=this.value
         console.log(x);
          let rule = r[x];
  console.log(rule);
  $("#rule-length").val(rule.length);
  let suggestions = rule.suggestions;
  console.log(suggestions);
  suggestions.forEach(function(l){		
							document.getElementById("suggested-preconditions-list").innerHTML+='<div class="form-check col-6">'+
 ' <input class="form-check-input" type="checkbox" name="'+l.suggestion.page+'" id="flexRadioDefault1">'
 +' <label class="form-check-label" for="flexRadioDefault1">'+ l.suggestion.page +' </label></div>'
						})
        });
  let rule = r[x];
  console.log(rule.length);
  $("#rule-length").val(rule.length);
  let suggestions = rule.suggestions;
  console.log(suggestions);
  suggestions.forEach(function(l){		
							document.getElementById("suggested-preconditions-list").innerHTML+='<div class="form-check col-6">'+
 ' <input class="form-check-input" type="checkbox" name="'+l.suggestion.page+'" id="flexRadioDefault1">'
 +' <label class="form-check-label" for="flexRadioDefault1">'+ l.suggestion.page +' </label></div>'
						})
						current_rule=rule[0].id;
			}

			
				
			
			function GetSuggestion(id) {
				$.ajax({
						
					type : "GET",
					contentType: "application/json",
					url :"GetSuggestion",
					dataType: "json",
					data: JSON.stringify({ids : id}) ,
					
					success : function(data) {
						
						console.log("SUCCESS : ", data);
						
						data.forEach(function(l){		
							document.getElementById("collapse").innerHTML+='<div class="form-check">'+
 ' <input class="form-check-input" type="radio" name="flexRadioDefault" id="flexRadioDefault1">'
 +' <label class="form-check-label" for="flexRadioDefault1">'+ l.page +' </label></div>'
						})
						
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}
			
			 function ajaxGet() {
console.log("PPPPP3");
				// PREPARE FORM DATA
				
console.log("PPPPP4");
				// DO POST
				$.ajax({
						
					type : "GET",
					contentType: "application/json",
					url :"Getlessons",
					dataType: "json",
					
					success : function(data) {
						
						console.log("SUCCESS : ", data);
							a=data;
						
						data.forEach(function(l){		
							if(document.getElementById("rows") == null){
								document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/'+ l.id + '">'+ l.name + '</a></div>'
							}
							else{
								document.getElementById("rows").innerHTML +='<div class="columns animate__animated animate__bounce"><div class="cards">  <button type="button" class="btn btn-link config" style="border:none;color:black;" data-bs-toggle="modal" data-bs-target="#exampleModal" onclick="Postsuggetion('+ l.id +')"><i class="fas fa-cog"></i></button><a href="/Argomento/'+ l.id + '"> <h3 >'+ l.name + '</h3></a> </div></div>'
								document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/'+ l.id + '">'+ l.name + '</a></div>'
							
							}
						})
						
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}
			
			let a = [];

			function add(model){
				current_modal= model.id
				if(document.getElementById("rows") == null){
							document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/'+ model.id + '">'+ model.name + '</a></div>'
							}else{
						
						document.getElementById("rows").innerHTML +='<div class="columns animate__animated animate__bounce"><div class="cards">  <button type="button" class="btn btn-link config" style="border:none;color:black;" data-bs-toggle="modal" data-bs-target="#exampleModal" "><i class="fas fa-cog"></i></button><a href="/Argomento/'+ model.id + '"> <h3 >'+ model.name + '</h3></a> </div></div>'
								document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/Argomento/'+ model.id + '">'+ model.name + '</a></div>'
						}
						a.push(model);
						$('#new_lesson1').modal('hide');	
			}
$(document).ready(
		function() {
		
	 
				prep_modal();
					
		
			
			$( "#pip" ).click(function() {
				
				
	console.log(Prova);
	
});
			
			
			$("#new_precondition").submit(function() {
				console.log("PPPPP2");
				event.preventDefault();
				
				Create_new_precondition();
				
				
			});
			
	
			
				
			 
			})
			
			function prep_modal()
{
  $(".modal").each(function() {

  var element = this;
	var pages = $(this).find('.modal-split');

  if (pages.length != 0)
  {
    	pages.hide();
    	pages.eq(0).show();

    	var b_button = document.createElement("button");
                b_button.setAttribute("type","button");
          			b_button.setAttribute("class","btn btn-primary");
          			b_button.setAttribute("style","display: none;");
          			b_button.innerHTML = "Back";

    	var n_button = document.createElement("button");
                n_button.setAttribute("type","button");
          			n_button.setAttribute("class","btn btn-primary");
          			n_button.innerHTML = "Next";

    	$(this).find('.modal-footer').append(b_button).append(n_button);


    	var page_track = 0;

    	$(n_button).click(function() {
        if(document.getElementById("new-model-name").value.length == 0){
	document.getElementById("new-model-name").setAttribute("class","form-control is-invalid")
	
}else{
	document.getElementById("new-model-name").setAttribute("class","form-control valid")
        this.blur();

    		if(page_track == 0)
    		{
    			$(b_button).show();
    		}

    		if(page_track == pages.length-2)
    		{
    			$(n_button).text("Submit");
    		}

        if(page_track == pages.length-1)
        {
          n_button.setAttribute("type","submit");
          $("#new_models").submit(function() {
				console.log("PPPPP2");
				console.log(a);
				// Prevent the form from submitting via the browser.
				event.preventDefault();
				if(a.some(a => a.name === $("#new-model-name").val())){
					alert("Argomento gia esistente");
				}else{
				Create_model();
				}
				
			});
        }

    		if(page_track < pages.length-1)
    		{
    			page_track++;

    			pages.hide();
    			pages.eq(page_track).show();
    		}

}
    	});

    	$(b_button).click(function() {

    		if(page_track == 1)
    		{
    			$(b_button).hide();
    		}

    		if(page_track == pages.length-1)
    		{
    			$(n_button).text("Next");
    		}

    		if(page_track > 0)
    		{
    			page_track--;

    			pages.hide();
    			pages.eq(page_track).show();
    		}


    	});

  }

  });
}