let array;
let rules = [];
var selected = null;
var nodes = null;
var edges = null;
var network = null;
let uniqueChars = null;


function wiki_link(){
	window.open("https://it.wikipedia.org/wiki/"+selected,"_blank");
}

var LENGTH_MAIN = 350,
  LENGTH_SERVER = 150,
  LENGTH_SUB = 50,
  WIDTH_SCALE = 2,
  GREEN = "green",
  RED = "#C5000B",
  ORANGE = "orange",
  //GRAY = '#666666',
  GRAY = "gray",
  BLACK = "#2B1B17";

function draw() {
  // Create a data table with nodes.
  nodes = [];

  // Create a data table with links.
  edges = [];

      rules.forEach(function(l){	
	const rule = new Object();
	rule.label = l.name;
	rule.id = l.name;
	rule.group = "switch"
	nodes.push(rule);
	console.log(rule);
	l.suggestions.forEach(function(s){	
		const pippo = new Object();
		const edge = new Object();
		
		pippo.label = s.suggestion.page
		pippo.id = pippo.label;
		pippo.group = "desktop";
		edge.from = l.name;
		edge.to = pippo.id;
		edge.length = LENGTH_MAIN;
		edge.width = WIDTH_SCALE * 4;
		console.log(pippo);
		nodes.push(pippo);
		edges.push(edge);
		
		})
		console.log(nodes);
	})
 
 for(let i = 0;i<nodes.length;i++){
	if(rules.some(e => e.name === nodes[i].label) && nodes[i].group == "desktop" ){
		console.log(nodes[i]);
		nodes.splice(i,1);
		
	}	
	
}
nodes = nodes.filter((value, index, self) =>
  index === self.findIndex((t) => (
    t.id === value.id
  ))
)
console.log(nodes);

  // legend
  var mynetwork = document.getElementById("mynetwork");
  var x = -mynetwork.clientWidth / 2 + 50;
  var y = -mynetwork.clientHeight / 2 + 50;
  var step = 70;
  

  // create a network
  var container = document.getElementById("mynetwork");
  var data = {
    nodes: nodes,
    edges: edges,
  };
  var options = {
    nodes: {
      scaling: {
        min: 16,
        max: 32,
      },
    },
    edges: {
      color: GRAY,
      smooth: false,
    },
    physics: {
      barnesHut: { gravitationalConstant: -30000 },
      stabilization: { iterations: 2500 },
    },
    groups: {
      switch: {
        shape: "triangle",
        color: "#FF9900", // orange
      },
      desktop: {
        shape: "dot",
        color: "#2B7CE9", // blue
      },
      mobile: {
        shape: "dot",
        color: "#5A1E5C", // purple
      },
      server: {
        shape: "square",
        color: "#C5000B", // red
      },
      internet: {
        shape: "square",
        color: "#109618", // green
      },
    },
  };
  network = new vis.Network(container, data, options);
var myModal = new bootstrap.Modal(document.getElementById('myModal'), {
  keyboard: false
})
  network.on("click", function (params) {
  params.event = "[original event]";
  console.log(params.nodes[0]);
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
						 draw();
					
},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}

$(document).ready(
		function() {
			ajaxGet()
})