var max=0;
var value=0;
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
	console.log(document.getElementById("lesson_id").getAttribute("value"));
	return document.getElementById("lesson_id").getAttribute("value");
}
export function horizon(horizon){
	max=horizon;
}

export function loading(v){
	value = parseFloat(((v*100)/max).toFixed(1)); 
	
		console.log(value);
		document.getElementById("progressbar").style.width= value +"%";
		document.getElementById("progressbar").innerText=value+"%";

    

}



