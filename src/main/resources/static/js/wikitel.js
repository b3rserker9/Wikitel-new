
$(document).ready(
		function() {
			
				var currentTab = 0; // Current tab is set to be the first tab (0)
showTab(currentTab); // Display the current tab

function showTab(n) {
  // This function will display the specified tab of the form...
  var x = document.getElementsByClassName("tab");
  x[n].style.display = "block";
  //... and fix the Previous/Next buttons:
  if (n == 0) {
    document.getElementById("prevBtn").style.display = "none";
  } else {
    document.getElementById("prevBtn").style.display = "inline";
  }
  if (n == (x.length-1)) {
    document.getElementById("nextBtn").innerHTML = "Submit";
    document.getElementById("titolo").innerHTML = "Interests:";
  } else {
    document.getElementById("nextBtn").innerHTML = "Next";
  }
  //... and run a function that will display the correct step indicator:
  fixStepIndicator(n)
}
$( "#nextBtn" ).click(function() {
  nextPrev(1);
});

$( "#prevBtn" ).click(function() {
  nextPrev(-1);
});

function nextPrev(n) {
  // This function will figure out which tab to display
  var x = document.getElementsByClassName("tab");
  // Exit the function if any field in the current tab is invalid:
  if (n == 1 && !validateForm()) return false;
  // Hide the current tab:
  x[currentTab].style.display = "none";
  // Increase or decrease the current tab by 1:
  currentTab = currentTab + n;
  // if you have reached the end of the form...
  if (currentTab >= x.length) {
	 document.getElementById("nextBtn").type = "submit";
  
    
  }
  // Otherwise, display the correct tab:
  showTab(currentTab);
}


function validateForm() {
  // This function deals with validation of the form fields
  var x, y, i, valid = true;
  let text;
  var mailformat = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
  
  x = document.getElementsByClassName("tab");
  y = x[currentTab].getElementsByTagName("input");
  // A loop that checks every input field in the current tab:
  for (i = 0; i < y.length; i++) {
    // If a field is empty...
    if (y[i].value == "") {
      // add an "invalid" class to the field:
      y[i].className += " invalid";
      // and set the current valid status to false
      valid = false;
      console.log(y[i].name);
      
    }
    else{
	if(y[i].name =="email_r") {
		if( !(document.getElementById("email_r").value.match(mailformat) ) ){
	console.log("dsds");
			document.getElementById("error").innerHTML="Wrong Format";
			valid = false;
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
						
						if(data.status == "No" ){
							document.getElementById("error").innerHTML="Email already in use";
							valid = false;
							console.log(valid);
						}
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", email);
					}
				});
			
	
}
}
}
  }
  // If the valid status is true, mark the step as finished and valid:
  if (valid) {
    document.getElementsByClassName("step")[currentTab].className += " finish";
  }
  console.log(valid);
  return valid; // return the valid status
}



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
                    let data = {    // create an object
                        id: item.value,
                        name: item.name
                    }
                    result.push(data); //stored the objects to result array
                }
            })
            return result;
        }
			function ajaxPost() {
console.log("PPPPP3");
				// PREPARE FORM DATA
				var user = {
			interests: getData(),					
			first_name :$("#first_name").val(),
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
		
	
		