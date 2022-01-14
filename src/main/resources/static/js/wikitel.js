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
 valid = false;
 break;
 }
  console.log(valid);
  return valid; 
}
$(document).ready(
		function() {
			prep_modal();
	






function fixStepIndicator(n) {
  // This function removes the "active" class of all steps...
  var i, x = document.getElementsByClassName("step");
  for (i = 0; i < x.length; i++) {
    x[i].className = x[i].className.replace(" active", "");
  }
  //... and adds the "active" class on the current step:
  x[n].className += " active";
}
	var url=window.location;
	console.log("PPPPP");
	console.log(url);
			// SUBMIT FORM
			$("#register_form").submit(function() {
				console.log("PPPPP2");
				// Prevent the form from submitting via the browser.
				event.preventDefault();
				ajaxPost();
			});
			
			 var itemForm = document.getElementById('interests'); // getting the parent container of all the checkbox inputs
        var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]'); // get all the check box
        let result = [];
function getData() { // this function will get called when the save button is clicked
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
			password : $("#password_r").val()
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
						$('#modal2').modal('hide');
						$('#modal3').modal('show');
						console.log("SUCCESS : ", data);
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
					$("#ok").click(function(){
    			$("#modal1").modal('show');
  });
			
				

			}

		})
		
		function prep_modal()
{
  $(".register").each(function() {

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
		