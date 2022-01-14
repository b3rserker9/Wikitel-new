let array;

let rules = [];

var nodeDataArray = [];
let b=false;
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
					var $ = go.GraphObject.make;

var myDiagram =
  $(go.Diagram, "myDiagramDiv",
    { "undoManager.isEnabled": true,
    layout:
              $(FlatTreeLayout,  // custom Layout, defined below
                {
                  angle: 90,
                  compaction: go.TreeLayout.CompactionNone
                }),
                initialAutoScale: go.Diagram.Uniform });

myDiagram.nodeTemplate =
        $(go.Node, "Vertical",
          { selectionObjectName: "BODY" },
          $(go.Panel, "Auto", { name: "BODY" },
            $(go.Shape, "RoundedRectangle",
            { fill: "white", strokeWidth: 0 },
              new go.Binding("fill","color"),
              new go.Binding("stroke")),
            $(go.TextBlock,
              { font: "bold 12pt Arial, sans-serif", margin: new go.Margin(4, 2, 2, 2) },
              new go.Binding("text","key"))
          ),
          $(go.Panel,  // this is underneath the "BODY"
            { height: 17 },  // always this height, even if the TreeExpanderButton is not visible
            $("TreeExpanderButton")
          )
        );

      myDiagram.linkTemplate =
        $(go.Link,
          $(go.Shape, { strokeWidth: 1.5 }));
          rules.forEach(function(l){	
	const rule = new Object();
	if(l.name == null){l.name="Tufo";}
	rule.name = l.name;
	rule.key = l.name;
	nodeDataArray.push(rule);
	console.log(rule);
	l.suggestions.forEach(function(s){	
		const pippo = new 	Object();
		pippo.name = s.suggestion.page
		pippo.key = pippo.name;
		pippo.parent = l.name;
		console.log(pippo);
		nodeDataArray.push(pippo);
		console.log(nodeDataArray);
		})
	})
          

console.log(nodeDataArray);
	myDiagram.model = new go.TreeModel(nodeDataArray);
							
					},
					error : function(e) {
						alert("Error!")
						console.log("ERROR: ", e);
					}
				});
			}
// Customize the TreeLayout to position all of the leaf nodes at the same vertical Y position.
    function FlatTreeLayout() {
      go.TreeLayout.call(this);  // call base constructor
    }
    go.Diagram.inherit(FlatTreeLayout, go.TreeLayout);

    // This assumes the TreeLayout.angle is 90 -- growing downward
    FlatTreeLayout.prototype.commitLayout = function() {
      go.TreeLayout.prototype.commitLayout.call(this);  // call base method first
      // find maximum Y position of all Nodes
      var y = -Infinity;
      this.network.vertexes.each(function(v) {
        y = Math.max(y, v.node.position.y);
      });
      // move down all leaf nodes to that Y position, but keeping their X position
      this.network.vertexes.each(function(v) {
        if (v.destinationEdges.count === 0) {
          // shift the node down to Y
          v.node.position = new go.Point(v.node.position.x, y);
          // extend the last segment vertically
          v.node.toEndSegmentLength = Math.abs(v.centerY - y);
        } else {  // restore to normal value
          v.node.toEndSegmentLength = 10;
        }
      });
    };
$(document).ready(
		function() {
			ajaxGet()
})