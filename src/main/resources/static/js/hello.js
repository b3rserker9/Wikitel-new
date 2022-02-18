
var n, nome;




function pippo(name, id) {
    console.log(id);
    document.getElementById("demo").innerHTML = '<p> Vuoi iscriverti a ' + name + ' (' + id + ') ' + '</p>';
    n = id;
    nome = name;
    console.log(n);
}

function ajaxGet2() {

    document.getElementById("collapse").innerHTML += '<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/lezione/' + n + '">' + nome + '</a></div>';


}

function ajaxGet() {
    $.ajax({

        type: "GET",
        contentType: "application/json",
        url: "Getlessons_student",
        dataType: "json",

        success: function(data) {

            console.log("SUCCESS : ", data);


            data.data5.forEach(function(l) {
                document.getElementById("collapse").innerHTML += '<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/lezione/' + l.id + '">' + l.name + '</a></div>'

            })


        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}
$(document).ready(
    function() {
	var current_user = document.getElementById("nameid").getAttribute('value');
        var accordions = document.getElementsByClassName("accordion");

        for (var i = 0; i < accordions.length; i++) {
            accordions[i].onclick = function() {
                this.classList.toggle('is-open');

                var content = this.nextElementSibling;
                if (content.style.maxHeight) {
                    // accordion is currently open, so close it
                    content.style.maxHeight = null;
                } else {
                    // accordion is currently closed, so open it
                    content.style.maxHeight = content.scrollHeight + "px";
                }
            }
        }



        $("#iscrizione").submit(function() {
            console.log("PPPPP12");
            // Prevent the form from submitting via the browser.
            event.preventDefault();
            console.log(n);
            ajaxPost(n);

        });

        function ajaxPost(n) {
            console.log(n);
            var id = {
                id: n
            }
            // PREPARE FORM DATA
            console.log("PPPPP4");
            // DO POST
            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "iscrizione",
                data: JSON.stringify(n),
                dataType: "json",
                success: function(data) {
                    if (data.status == "exist") {
                        document.getElementById("error").innerHTML = '<p style="color:red;"> Sei Già iscritto a questa lezione</p>';
                    } else {
                        console.log("SUCCESS : ", data);
                        $('#Enrol').modal('hide');
                        ajaxGet2();
                    }

                },
                error: function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });



        }

    })
    