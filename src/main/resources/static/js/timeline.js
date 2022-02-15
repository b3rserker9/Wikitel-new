

    google.charts.load("current", {packages:["timeline"]});
    google.charts.setOnLoadCallback(drawChart);
    
    function drawChart(rows) {
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
        avoidOverlappingGridLines: false
      };

      chart.draw(dataTable, options);
    }

    
    export function timeline(array){
	console.log(array);
	let rows =[];
	array.forEach(function(s) {
	rows.push(['rule', s.atom.predicate , new Date(0,0,0,0,0,s.from),new Date(0,0,0,0,0,s.to)  ])
	});
	
	drawChart(rows)
}