$(document).ready(
		function() {
			
			$( "#edit" ).click(function() {
  		document.getElementById("a_edit").style.display = "none";
  		document.getElementById("b_edit").style.display = "block";
  		
});
$("#edit_form").submit(function() {
				console.log("PPPPP2");
				// Prevent the form from submitting via the browser.
				event.preventDefault();
				ajaxPost();
			});

function ajaxPost() {
console.log("PPPPP3");
				// PREPARE FORM DATA
				var user = {				
			first_name :$("#first_name").val(),
			last_name: $("#last_name").val(),
			email: $("#email").val(),
				}
console.log("PPPPP4");
				// DO POST
				$.ajax({
						
					type : "POST",
					contentType: "application/json",
					url :"edit",
					data : JSON.stringify(user),
					dataType: "json",
					success : function(data) {
						console.log("SUCCESS : ", data);
						$("#redirect").modal('show');
						setTimeout(function(){window.location.href = 'http://localhost:7000/'; }, 3000);
						
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			
				

			}
			
				})