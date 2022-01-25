let array;
let rules = [];
let current_rule = [];
let current_edge = [];
var selected = null;
var nodes = [];
var edges = [];
var network = null;
let uniqueChars = null;
let text="desktop";
let color="green";
let tables = [];
let raw = [];
var nodesDataset;
var edgesDataset;
	function update_add() {
				var select = document.getElementById('graph_rilevanza');
				var option = select.options[select.selectedIndex];
				console.log(option.value);
				switch(option.value){
					case '2':
					color="red"
					break;
					case '1':
					color="green"
					break;
				}
				
}
	function update() {
				var select = document.getElementById('new-rule-type');
				var option = select.options[select.selectedIndex];
				console.log($("#Add_node_name").val());
				switch(option.value){
					case '2':
					document.getElementById('url').style.display="block";
					document.getElementById('textarea').style.display="none";
					document.getElementById('file').style.display="none";
					text="desktop"
					break;
					case '1':
					document.getElementById('url').style.display="none";
					document.getElementById('textarea').style.display="block";
					document.getElementById('file').style.display="none";
					text="desktop"
					break;
					case '3':
					document.getElementById('url').style.display="none";
					document.getElementById('textarea').style.display="none";
					document.getElementById('file').style.display="none";
					text="server"

					break;
					case '4':
					document.getElementById('url').style.display="none";
					document.getElementById('textarea').style.display="none";
					document.getElementById('file').style.display="block";
					text="switch"
					break;
				}
				
}
function wiki_link(){
	window.open("https://it.wikipedia.org/wiki/"+selected,"_blank");
}


var network;
var allNodes;
var highlightActive = false;
 
function redrawAll() {
	rules.forEach(function(l){	
	const rule = new Object();
	rule.label = l.name;
	rule.id = l.name;
	rule.group = 5;
	current_rule.push(rule);
	nodes.push(rule);
	console.log(rule);
	l.suggestions.forEach(function(s){	
		raw = ["",s.suggestion.page,s.score,s.score2];
		const pippo = new Object();
		const edge = new Object();
		
		pippo.label = s.suggestion.page
		pippo.id = pippo.label;
		pippo.group = 6;
		pippo.title =s.score2;
		edge.from = l.name;
		edge.to = pippo.id;

		console.log(pippo);
		nodes.push(pippo);
		edges.push(edge);
		tables.push(raw);
		
		})
		console.log(nodes);
		console.log(tables);
	})
	 for(let i = 0;i<nodes.length;i++){
	if(rules.some(e => e.name === nodes[i].label) && nodes[i].group == 6 ){
		console.log(nodes[i]);
		nodes.splice(i,1);
		
	}	
	
}
nodes = nodes.filter((value, index, self) =>
  index === self.findIndex((t) => (
    t.id === value.id
  ))
)
for(let i = 0;i<nodes.length;i++){
if(nodes[i].title<0.2){
		 nodes[i].color = "rgba(200,200,200,0.5)";
	}
	}
	table = document.getElementById("example");
	for(var i = 0; i < tables.length; i++)
           {
               // create a new row
               tbody = document.getElementById("row_element");
               var newRow = tbody.insertRow(table.length);
               for(var j = 0; j < tables[i].length; j++)
               {
                   // create a new cell
                   var cell = newRow.insertCell(j);
                   
                   // add value to the cell
                   cell.innerHTML = tables[i][j];
               }
           }
           
         var prova  =  $('#example').DataTable( {
        columnDefs: [ {
            orderable: false,
            className: 'select-checkbox',
            targets:   0
        } ],
        select: {
            style:    'multi',
            selector: 'td:first-child'
        },
        order: [[ 1, 'asc' ]]
    
    } );
    
  
console.log(nodes);
nodesDataset = new vis.DataSet(nodes); // these come from WorldCup2014.js
 edgesDataset = new vis.DataSet(edges); // these come from WorldCup2014.js

  var container = document.getElementById("mynetwork");
  var options = {
    nodes: {
      shape: "dot",
      scaling: {
        min: 10,
        max: 30,
        label: {
          min: 8,
          max: 30,
          drawThreshold: 12,
          maxVisible: 20,
        },
      },
      font: {
        size: 12,
        face: "Tahoma",
      },
    },
    edges: {
      width: 0.15,
      color: { inherit: "from" },
      smooth: {
        type: "continuous",
      },
    },
    physics: false,
    interaction: {
      tooltipDelay: 200,
      hideEdgesOnDrag: true,
      hideEdgesOnZoom: true,
    },
  };
  var data = { nodes: nodesDataset, edges: edgesDataset }; // Note: data is coming from ./datasources/WorldCup2014.js

  network = new vis.Network(container, data, options);

  // get a JSON object
  allNodes = nodesDataset.get({ returnType: "Object" });
	console.log(allNodes);
	  var myModal = new bootstrap.Modal(document.getElementById('myModal'), {
  keyboard: false
})
    document.getElementById("try").addEventListener("click", function() {
	nodes=[]
	nodesDataset =[];
  nodes.push(...current_rule);
  console.log(current_rule);
  for(let i=0;i<prova.rows( { selected: true } ).count();i++){
	console.log(prova.rows( { selected: true } ).data()[i][1]);
	const pippo = new Object();
	pippo.label = prova.rows( { selected: true } ).data()[i][1]
		pippo.id = pippo.label;
		pippo.group = 5;
  nodes.push({ id:prova.rows( { selected: true } ).data()[i][1], label: prova.rows( { selected: true } ).data()[i][1] ,group : text});
  }
   nodesDataset= new vis.DataSet(nodes);
     var container = document.getElementById("mynetwork");
  var options = {
    nodes: {
      shape: "dot",
      scaling: {
        min: 10,
        max: 30,
        label: {
          min: 8,
          max: 30,
          drawThreshold: 12,
          maxVisible: 20,
        },
      },
      font: {
        size: 12,
        face: "Tahoma",
      },
    },
    edges: {
      width: 0.15,
      color: { inherit: "from" },
      smooth: {
        type: "continuous",
      },
    },
    physics: false,
   
  };
  var data = { nodes: nodesDataset, edges: edgesDataset }; // Note: data is coming from ./datasources/WorldCup2014.js

   network = new vis.Network(container, data, options);
  console.log(nodes);
       network.on("click", function (params) {
  params.event = "[original event]";
  console.log(params.nodes[0]);
  select=params.nodes[0]
   selected=params.nodes[0].replaceAll(' ', '_');
  console.log(selected.replaceAll(' ', '-'));
   document.getElementById("button_link").textContent ="https://it.wikipedia.org/wiki/"+selected
myModal.show()
});
  });
       network.on("click", function (params) {
  params.event = "[original event]";
  console.log(params.nodes[0]);
  select=params.nodes[0]
   selected=params.nodes[0].replaceAll(' ', '_');
  console.log(selected.replaceAll(' ', '-'));
   document.getElementById("button_link").textContent ="https://it.wikipedia.org/wiki/"+selected
myModal.show()
});
}



function disegna(nodes,edges){
	var mynetwork = document.getElementById("mynetwork");
  var x = -mynetwork.clientWidth / 2 + 50;
  var y = -mynetwork.clientHeight / 2 + 50;
  var step = 70;
  
 

  
      document.getElementById("try").addEventListener("click", function() {
  console.log(prova.rows( { selected: true } ).data()[1][1]);
  nodes = current_rule;
  console.log(nodes);
  for(let i=0;i<prova.rows( { selected: true } ).count();i++){
	console.log(prova.rows( { selected: true } ).data()[i][1]);
	const pippo = new Object();
	pippo.label = prova.rows( { selected: true } ).data()[i][1]
		pippo.id = pippo.label;
		pippo.group = "desktop";
  nodes.push(pippo);
  console.log(nodes);
  disegna(nodes,edges);
  }
});
var myModal = new bootstrap.Modal(document.getElementById('myModal'), {
  keyboard: false
})
  network.on("click", function (params) {
  params.event = "[original event]";
  console.log(params.nodes[0]);
  select=params.nodes[0]
   selected=params.nodes[0].replaceAll(' ', '_');
  console.log(selected.replaceAll(' ', '-'));
   document.getElementById("button_link").textContent ="https://it.wikipedia.org/wiki/"+selected
myModal.show()
});
}
 function ajaxGet() {
				$.ajax({
					type : "POST",
					contentType: "application/json",
					url :"http://localhost:7000/Getmodel",
					dataType: "json",
					data: JSON.stringify({ids : document.getElementById('model_name').getAttribute('value')}),
					success : function(data) {
						console.log("SUCCESS : ", data);
						moedl = data;
						rules = data.rules;
						console.log(rules);
						 redrawAll();;
					
},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}
			
		function addNode() {
  console.log(selected);
  nodesDataset.add({ id: $("#Add_node_name").val(), label: $("#Add_node_name").val(),group : 6});
  edgesDataset.add({from: select, to:$("#Add_node_name").val()});
}



$(document).ready(
		function() {
			ajaxGet()
})