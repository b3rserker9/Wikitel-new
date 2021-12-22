	function ajaxPost() {
console.log("PPPPP3");
				// PREPARE FORM DATA
				 
		console.log($("#name").val());
				
console.log("PPPPP4");
				// DO POST
				$.ajax({
						
					type : "POST",
					contentType: "application/json",
					url :"Postlessons",
					data : $("#name").val(),
					
					
					success : function(data) {
						ajaxGet2();
						console.log("SUCCESS : ", data);
						
						
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

						
						data.data5.forEach(function(l){		
							if(document.getElementById("rows") == null){
								document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/lezione/'+ l.id + '">'+ l.name + '</a></div>'
							}
							else{
								document.getElementById("rows").innerHTML +='<div class="columns"><div class="cards"><a href="/lezione/'+ l.id + '"> <h3 >' + l.name + '</h3></a><p>Followed by ' + l.followed_by.length + ' Student </p><p>Some text</p> </div></div>'
								document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/lezione/'+ l.id + '">'+ l.name + '</a></div>'
							
							}
						})
						
						
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}
			function ajaxGet2() {
				$.ajax({
						
					type : "GET",
					contentType: "application/json",
					url :"lesson",
					dataType: "json",
					
					success : function(data) {
						if(document.getElementById("rows") == null){
							document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/lezione/'+ l.id + '">'+ l.name + '</a></div>'
							}else{
						document.getElementById("rows").innerHTML +='<div class="columns animate__animated animate__bounce"><div class="cards"><a href="/lezione/'+ data.data4.id + '"> <h3 >'+ data.data4.name + '</h3></a><p>Followed by ' + data.data4.followed_by.length + ' Student </p><p>Some text</p> </div></div>'
						document.getElementById("collapse").innerHTML +='<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/lezione/'+ data.data4.id + '">'+ data.data4.name + '</a></div>'
						}
						console.log("SUCCESS : ", data);	
						$('#new_lesson1').modal('hide');	
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			
				

			}
$(document).ready(
		function() {
				
			$("#new_lesson1").submit(function() {
				console.log("PPPPP2");
				// Prevent the form from submitting via the browser.
				event.preventDefault();
				ajaxPost();
				
				
			});
			
	
			
				
			 
			})