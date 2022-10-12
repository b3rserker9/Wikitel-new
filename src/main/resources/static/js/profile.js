var arr = [];
let file;

function showPreview() {
    document.getElementById('photo-upload-button').click()
    const chooseFile = document.getElementById("photo-upload-button");
    const imgPreview = document.getElementById("file-ip-1-preview");
    console.log(imgPreview.src);
    chooseFile.addEventListener("change", function() {
        const files = chooseFile.files[0];

        if (files) {
            const fileReader = new FileReader();
            fileReader.readAsDataURL(files);
            fileReader.addEventListener("load", function() {
                imgPreview.style.display = "block";
                document.getElementById('backg').style.background = "#a5a1a100"
                imgPreview.src = this.result;
                file = files;
                console.log(imgPreview.src);
            });
        }
    });
}

function prova() {
    var getSelectedValue = document.querySelector('input[name="drone"]:checked');
    if (getSelectedValue != null) {
        console.log(getSelectedValue.id)
    } else {
        document.write("Nothing has been selected");
    }
}

function uploadFile(e) {
    console.log(file);
    var getSelectedValue = document.querySelector('input[name="drone"]:checked');
    if (getSelectedValue != null) {
        console.log(getSelectedValue.id);

        if (getSelectedValue.id == "plus") {
            var form = $("#photo-upload-button")[0].files[0];
            console.log(form);
            var data = new FormData();
            data.append("uploadfile", form);
            console.log(data);
            $.ajax({
                url: "/uploadFile",
                type: "POST",
                data: data,
                DataType: 'json',
                processData: false,
                contentType: false,
                cache: false,
                success: function(res) {
                    $('#exampleModal').modal('hide');
                    document.getElementById("myImg").src = document.getElementById("file-ip-1-preview").src;
                },
                error: function(err) {
                    console.error(err);
                    toastr.error("Errore durante l'upload dell'immagine");
                }
            });
        } else {
            $.ajax({
                url: "/uploadFileString",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify({
                    src: document.getElementById(getSelectedValue.id.slice(-1)).src
                }),
                DataType: 'json',

                success: function(res) {
                    $('#exampleModal').modal('hide');
                    document.getElementById("myImg").src = document.getElementById(getSelectedValue.id.slice(-1)).src;
                },
                error: function(err) {
                    console.error(err);
                }
            });
        }
    } else {
        document.write("Nothing has been selected");
    }
}

function ajaxGet() {
    console.log("PPPPP3");
    // PREPARE FORM DATA

    console.log("PPPPP4");
    // DO POST
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "/getprofile",
        data: JSON.stringify({id :document.getElementById("nameid").getAttribute('value') }),
        dataType: "json",

        success: function(data) {
            JSON.parse(data.status).forEach(function(element) {
                document.getElementById("int").innerHTML += '<span class="badge badge-primary" style="margin-right:1.5%;" > ' + element.slice(10) + ' </span>'
                arr.push(element.slice(10))
            });


        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}

function NewPassword(){
	
	 var password = {
                password: $("#new_password").val(),
            }
            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "/changePassword",
                data: JSON.stringify(password),
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


$(document).ready(




    function() {
        console.log(document.getElementById("gender").innerText);
        let gender = document.getElementById("gender").innerText;

        $("#edit").click(function() {
            document.getElementById("a_edit").style.display = "none";
            document.getElementById("b_edit").style.display = "block";

        });

        var itemForm = document.getElementById('interests');
        var checkBoxes = itemForm.querySelectorAll('input[type="checkbox"]');
        $("#edit_i").click(function() {
            $('#interests').modal('show');

            checkBoxes.forEach(item => { // loop all the checkbox item
                if (arr.includes(item.name)) { //if the check box is checked
                    item.checked = true;
                    console.log(item.name);
                }
            })
        });
        let result = [];

        function getData() { // this function will get called when the save button is clicked
            result = [];
            checkBoxes.forEach(item => { // loop all the checkbox item
                if (item.checked) { //if the check box is checked

                    result.push(item.value); //stored the objects to result array
                }
            })
            return result;
        }

        $("#edit_form").submit(function() {
            console.log("PPPPP2");
            // Prevent the form from submitting via the browser.
            event.preventDefault();
            ajaxPost();
        });

        $("#upload_img").submit(function(e) {
            console.log("PPPPP2");
            // Prevent the form from submitting via the browser.
            event.preventDefault();
            uploadFile(e);
        });

        $("#edit_interests").submit(function() {
            console.log("PPPPP2");
            // Prevent the form from submitting via the browser.
            event.preventDefault();
            ajaxPost_interest();
        });

        function ajaxPost() {
            console.log("PPPPP3");
            // PREPARE FORM DATA
            var user = {
                first_name: $("#first_name").val(),
                last_name: $("#last_name").val(),
                email: $("#email").val(),
            }
            console.log("PPPPP4");
            // DO POST
            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "edit",
                data: JSON.stringify(user),
                dataType: "json",
                success: function(data) {
                    console.log("SUCCESS : ", data);
                    $("#redirect").modal('show');
                    setTimeout(function() {
                        window.location.href = 'http://localhost:7000/';
                    }, 3000);


                },
                error: function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });



        }


        function ajaxPost_interest() {
            console.log("PPPPP3");
            // PREPARE FORM DATA
            var user = {
                profile: JSON.stringify(getData()),
            }
            console.log("PPPPP4");
            // DO POST
            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "edit_interests",
                data: JSON.stringify(user),
                dataType: "json",
                success: function(data) {
                    console.log("SUCCESS : ", data);

                    $("#interests").modal('hide');
                    setTimeout("location.reload();", 1000);



                },
                error: function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });



        }

    })