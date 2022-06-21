let rows=[];
let name;
 
 
 
    export function timeline(array){
	
	
       google.charts.load("current", {packages:["timeline"]});
    google.charts.setOnLoadCallback(drawChart);
    
 function drawChart() {
      var container = document.getElementById('timeline');
      var chart = new google.visualization.Timeline(container);
       var dataTable = new google.visualization.DataTable();
      dataTable.addColumn({ type: 'string', id: 'Room' });
      dataTable.addColumn({ type: 'string', id: 'Name' });
      dataTable.addColumn({ type: 'date', id: 'Start' });
      dataTable.addColumn({ type: 'date', id: 'End' });
      dataTable.addRows(rows);

      var options = {
        timeline: { showRowLabels: false },
        avoidOverlappingGridLines: false,
      };

      chart.draw(dataTable, options);
    }
	
	function getName(link){
		console.log(link.substring(3,link.length));
		 var model = {
        id: link.substring(3,link.length)
    }
		   $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/getname",
        data: JSON.stringify(model),
        dataType: "json",
         async: false,
        success: function(data) {
	console.log(data);
            name=data.status;
        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
            name="error";
        }
    });
	}
	
	
	console.log(array);
	rows =[];
	array.forEach(function(s) {
		getName(s.atom.predicate);
		console.log(name);
	rows.push(['rule', name , new Date(0,0,0,0,0,s.from),new Date(0,0,0,0,0,s.to)  ])
	});
	
	drawChart(rows)
}


  