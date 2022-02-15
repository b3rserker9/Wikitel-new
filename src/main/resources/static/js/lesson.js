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
export function horizon(horizon){
	max=horizon;
}

export function loading(v){
	value =Math.round( (v*100)/max); 
		console.log(value);
    var left = document.getElementById("left");
    var right = document.getElementById("right");
	 document.getElementById("num").innerHTML = value;
    if (value > 0) {
   
      if (value <= 50) {
	right.style.transform='rotate(' + percentageToDegrees(value) + 'deg)';
        
      } else {
	right.style.transform='rotate(180deg)';
        left.style.transform='rotate(' + percentageToDegrees(value - 50) + 'deg)';
        
      }
     
    }
    
     function percentageToDegrees(percentage) {

    return percentage / 100 * 360

  }
	
}



