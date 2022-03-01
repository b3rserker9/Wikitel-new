var max=0;
var value=0;


export function file_upload(){
	var form = $("#formFileLg")[0].files[0];
	var data = new FormData();
            data.append("uploadfile", form);
              $.ajax({
                url: "/uploadFileLesson/"+document.getElementById('lesson_id').getAttribute('value'),
                type: "POST",
                data: data,
                DataType: 'json',
                processData: false,
                contentType: false,
                cache: false,
                success: function(res) {
                   toastr.info("File uploaded succesfully");
                   file_add(res);
                },
                error: function(err) {
                    console.error(err);
                    toastr.error("Errore durante l'upload dell'immagine");
                }
            });
            
         function file_add(res){
			document.getElementById("all_files").innerHTML+='<div class="tile form">  <i class="mdi mdi-file-document"></i> <h3><a href="/file/'+res.id+'">'+ res.name +'</a> </h3></div>'
}
}

export function play() {
	
   $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/play",
        data:  JSON.stringify({id: document.getElementById('lesson_id').getAttribute('value')}) ,
       success: function(data) {
			console.log("SUCCESS : ", data);
        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}

export function pause() {
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "/pause",
        data:  JSON.stringify({id: document.getElementById('lesson_id').getAttribute('value')}) ,
       success: function(data) {
            console.log("SUCCESS : ", data);
        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });

}

export function stop() {
    $.ajax({

        type: "POST",
        contentType: "application/json",
        url: "/stop",
        data:  JSON.stringify({id: document.getElementById('lesson_id').getAttribute('value')}) ,
       success: function(data) {
            console.log("SUCCESS : ", data);
        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });

}
export function lesson_id(){
	var id=document.getElementById("lesson_id");
	if(id==null){
		return null;
	}
	return id.getAttribute("value");
}
export function horizon(horizon){
	max=horizon;
}

export function loading(v){
	console.log("v: "+v)
	value = parseFloat(((v*100)/max).toFixed(1)); 
	if(v=0){value=0;}
		console.log(value);
		document.getElementById("progressbar").style.width= value +"%";
		document.getElementById("progressbar").innerText=value+"%";
}

export function new_stimuli(src){
	document.getElementById("iframe").setAttribute("src",src);
}



