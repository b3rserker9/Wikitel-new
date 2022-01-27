	var question = [
   ["attivo", "riflessivo","attivo? Preferisci sperimentare e anche confrontare le tue osservazioni con gli altri, anche mentre lo stai pensando; ti piace il lavoro di gruppo. "+
   " "+ 
"riflessivo? Preferisci lavorare prima di tutto da solo, organizzare il materiale e studiarlo. "+
"Non c'è una risposta giusta! Dipende da quali caratteristiche, individualmente, è quello di ottenere il miglior risultato."],
   ["pratico", "intuitivo","pratico? Senti (con i tuoi sensi) il materiale di apprendimento. intuitivo? Hai un approccio cerebrale.Non c'è una risposta giusta! L'obiettivo è assimilare le informazioni nel migliore dei modi."],
   ["visivo", "verbale","visivo? Ti piacerebbe vedere diagrammi e studiare su materiali completamente di immage e grafico. verbale? Preferisci leggere le descrizioni scritte a parole.Non c'è una risposta giusta! L'obiettivo è assimilare le informazioni nel migliore dei modi."],
   ["sequenziale", "globale","sequenziale? Ti piacerebbe avere una sequenza di argomenti all'interno di una lessione che puoi seguire setp per passo . globale? Preferisci avere una descrizione globale della partizione e dopo tutte le specifiche."],
];
let src = "https://e7.pngegg.com/pngimages/84/165/png-clipart-united-states-avatar-organization-information-user-avatar-service-computer-wallpaper.png";





function image(){
	if(document.querySelector( 'input[name="drone"]:checked').id != "plus"){
	document.getElementById("myImg").src = document.getElementById(document.querySelector( 'input[name="drone"]:checked').id.slice(-1)).src
	src = document.getElementById(document.querySelector( 'input[name="drone"]:checked').id.slice(-1)).src
	}
}
function close(){
	console.log("fatto");
	 $('#modal1').modal('hide');

}
	 
function getData() { 
	var itemForm = document.getElementById('interests'); // getting the parent container of all the checkbox inputs
        var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]'); // get all the check box
        let result = [];// this function will get called when the save button is clicked
            result = [];
            checkBoxes.forEach(item => { // loop all the checkbox item
                if (item.checked) {  //if the check box is checked
                    let data =  item.value
                    
                    result.push(data); //stored the objects to result array
                }
            })
            return result;
        }

	function ajaxPost() {
console.log("PPPPP3");
				// PREPARE FORM DATA
				var user = {
			profile: JSON.stringify(getData()),					
			first_name : $("#first_name").val(),
			last_name: $("#last_name").val(),
			email: $("#email_r").val(),
			password : $("#password_r").val(),
			src : src,
			role : $('input[name=roles]:checked').val(),
			one :JSON.stringify({one:$('input[name=options0]:checked').val(),two:$('input[name=options1]:checked').val(),three:$('input[name=options2]:checked').val(),four:$('input[name=options3]:checked').val()})
				}
console.log("PPPPP4");
				// DO POST
				$.ajax({
						
					type : "POST",
					contentType: "application/json",
					url :"register",
					data : JSON.stringify(user),
					dataType: "json",
					success : function(data) {
						
						console.log("SUCCESS : ", data);
						if(document.querySelector( 'input[name="drone"]:checked').id =="plus"){
						event.preventDefault();
						uploadFile();
			
						}
						
						 
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
					$("#ok").click(function(){
    			$("#modal1").modal('show');
    			$('#modal3').modal('hide');
  });
			
				

			}
			
			function uploadFile(e) {

	var getSelectedValue = document.querySelector( 'input[name="drone"]:checked');   
if(getSelectedValue != null) {   
         console.log(getSelectedValue.id);
		 var form = $("#photo-upload-button")[0].files[0];
console.log(form);
    var data = new FormData();
    data.append("uploadfile", form);
    console.log(data);
  $.ajax({
            url: "/uploadFile",
            type: "POST",
            data: data,
            DataType:'json',
            processData: false,
            contentType: false,
            cache: false,
            success: function (res) {
				$('#exampleModal').modal('hide');
                document.getElementById("myImg").src = document.getElementById("file-ip-1-preview").src;
              
            },
            error: function (err) {
                console.log(err);
            }
        });
  }
  else {  
         document.write("Nothing has been selected");  
         }
}

 function showPreview(){
	document.getElementById('photo-upload-button').click()
	const chooseFile = document.getElementById("photo-upload-button");
const imgPreview = document.getElementById("file-ip-1-preview");
console.log(imgPreview.src);
chooseFile.addEventListener("change", function () {
  const files = chooseFile.files[0];
 
  if (files) {
    const fileReader = new FileReader();
    fileReader.readAsDataURL(files);
    fileReader.addEventListener("load", function () {
      imgPreview.style.display = "block";
      document.getElementById('backg').style.background="#a5a1a100"
      imgPreview.src=this.result ;
      file=files;
      document.getElementById("myImg").src = this.result;
      console.log(imgPreview.src);
    });    
  }
   });	
  }

function validateForm(i) {
	
	let valid = true;
switch(i){
case 0:
   if(document.getElementById("first_name").value.length == 0 || document.getElementById("last_name").value.length == 0 ){
	document.getElementById("first_name").setAttribute("class","form-control is-invalid")
	document.getElementById("last_name").setAttribute("class","form-control is-invalid")
	valid = true;
	
}else{
	document.getElementById("first_name").setAttribute("class","form-control valid")
	document.getElementById("last_name").setAttribute("class","form-control valid")
	valid = false;
	}
break;
case 1:
  var x, y, i;
  let text;
  var mailformat = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
  

  let e = document.getElementById("email_r")
  // A loop that checks every input field in the current tab:
if(e.value.length == 0 || document.getElementById("password_r").value.length == 0 ){
	e.setAttribute("class","form-control is-invalid")
	document.getElementById("password_r").setAttribute("class","form-control is-invalid")
	valid = true;
	
}else{     
	if(e.name =="email_r") {
		if( !(e.value.match(mailformat) ) ){
	console.log("dsds");
			document.getElementById("error").innerHTML="Wrong Format";
} else {
	console.log("PPPPP8");
				// PREPARE FORM DATA
				var email = {				
		
			email: $("#email_r").val(),
		
				}
console.log("PPPPP9");
				// DO POST
				$.ajax({
						
					type : "POST",
					contentType: "application/json",
					url :"/mauro",
					data : JSON.stringify(email),
					dataType: "json",
					async: false,
					success : function(data) {
						console.log("SUCCESS : ", data);
						
						if(data.status == "Done" ){
							e.setAttribute("class","form-control valid");
						document.getElementById("password_r").setAttribute("class","form-control valid");
						valid = false;							
						}
						document.getElementById("error").innerHTML="Email already in use";
							console.log(valid);
						
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", email);
					}
				});
			
	
}
}
}
  
 break;
  case 2:
  console.log("nope");
 if($('input[name=roles]:checked').val()){
	console.log("try");
	valid= false;
}else{
 valid = true;
 }
 break;
 case 3:
 valid = false;
 break;

  case 4:
 valid = false;
 break;
 }
  console.log(valid);
  return valid; 
}
$(document).ready(
		function() {
			prep_modal();
			questions();
				var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
  return new bootstrap.Popover(popoverTriggerEl)
})
		

		})
		
		function prep_modal()
{
	
  $(".register").each(function() {
	

  var element = this;
	var pages = $(this).find('.modal-split');
console.log(pages);
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
		if(!validateForm(page_track)){
     
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
	console.log("prova")
	
          n_button.setAttribute("type","submit");
         
          $("#register_form").submit(function() {
	  $('#modal2').modal('hide');

						$('#modal3').modal('show');
				// Prevent the form from submitting via the browser.
				event.preventDefault();
				
				ajaxPost();
				
				
				
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
	function questions(){
		console.log("prova");
		let id=0;
		//ROOT
		let div = document.getElementById("question");
		console.log(question.length);
		for(let i=0;i<question.length;i++){
		// LINK
		let al = document.createElement('a');
		al.className="link text-decoration-none";
		al.setAttribute("data-toggle","collapse");
		al.setAttribute("href","#collapseExample"+i);
		al.setAttribute("role","button");
		al.setAttribute("style","margin-top:2%;");
		al.setAttribute("aria-controls","collapseExample");
		al.textContent= question[i][0] + " o " +question[i][1] ;
		let span = document.createElement('span');
		al.appendChild(span);
		let icon = document.createElement('i');
		icon.className="fas fa-caret-down";
		span.appendChild(icon);
		
		//Info
		let info = document.createElement('a');
		info.className="btn btn-link";
		info.setAttribute("tabindex","0")
		info.setAttribute("role","button");
		info.setAttribute("data-bs-toggle","popover");
		info.setAttribute("data-bs-trigger","focus");
		info.setAttribute("data-bs-content",question[i][2]);
		let span2 = document.createElement('span');
		let icon2 = document.createElement('i');
		icon2.className="fas fa-info-circle";
		span2.appendChild(icon2);
		info.appendChild(span2);
		
		let links = document.createElement('div')
		links.className="links";
		links.appendChild(al);
		links.appendChild(info);
		div.appendChild(links);
		
		//Collapse
		let collapse = document.createElement('div');
		collapse.className="collapse"
		collapse.setAttribute("id","collapseExample"+i);
		div.appendChild(collapse);
		
		//Testo
		let text = document.createElement('div');
		text.className="testo";
		let right = document.createElement('p');
		right.className="right";
		right.textContent=question[i][0];
		let left = document.createElement('p'); 
		left.className="left";
		left.textContent=question[i][1]
		text.append(right);
		text.append(left);
		
		//Card
		let card = document.createElement('div');
		card.className ="card card-body"
		collapse.appendChild(card);
		card.appendChild(text);
		//radio
		let r = document.createElement('div');
		r.setAttribute("id","radios"+i);
		card.appendChild(r);
		for(let j=0 ;j<15;(j++)){
			let radio = document.createElement('input');
			let label = document.createElement('label');
			radio.setAttribute("id","option" + j+id);
			radio.setAttribute("name","options"+i);
			radio.setAttribute("type","radio");
			radio.setAttribute("value",j-7);
			label.setAttribute("for","option"+j+id);
			label.textContent= (j-7);
			r.appendChild(radio);
			r.appendChild(label);
			id++;
		}
		
		}
		
		var one =     $("#radios0").radioslider({
    fillOrigin: '0',
      size: 'small',
       fit: true,
});
     var two= $("#radios1").radioslider({
    fillOrigin: '0',
      size: 'small',
       fit: true,
});
 var three = $("#radios2").radioslider({
    fillOrigin: '0',
      size: 'small',
       fit: true,
});
  var four =$("#radios3").radioslider({
    fillOrigin: '0',
      size: 'small',
       fit: true,
});
		one.radioslider('setValue', '0');
		two.radioslider('setValue', '0');
		three.radioslider('setValue', '0');
		four.radioslider('setValue', '0');
		
	}		