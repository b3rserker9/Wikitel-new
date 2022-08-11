var network, allNodes, highlightActive = !1, nodestable, nodes, table, edges, rules;
highlightActive = !1;
var nodesDataset, fiternode = [], nodeFilterValue = -1, nodeFilterSelector = document.getElementById("customRange1");
function ajaxGet() {
  $.ajax({type:"POST", contentType:"application/json", url:"/findsuggestiontot", dataType:"json", data:JSON.stringify({ids:document.getElementById("model_name").getAttribute("value")}), success:function(a) {
    console.log("SUCCESS : ", a);
    edges = a[0];
    nodestable = a[2];
    rules = a[3];
    nodes = a[1];
    table = $("#example").DataTable({autoWidth:!1, data:nodestable, columns:[{data:"id"}, {data:"parent"}, {data:"type"}, {data:"score1"}, {data:"score2"}]});
    $("#example tbody").on("click", "tr", function() {
      $(this).toggleClass("selected");
      console.log(table.rows(".selected").data());
    });
    $("#button").click(function() {
      alert(table.rows(".selected").data().length + " row(s) selected");
    });
    nodesDataset = new vis.DataSet(nodes);
    console.log(nodesDataset);
    redrawAll(nodesDataset, edges);
  }, error:function(a) {
    alert("Error!");
    console.log("ERROR: ", a);
  }});
}
function filter_rule() {
  document.getElementById("only_rule").checked ? (nodesDataset = new vis.DataSet(rules), console.log(nodesDataset)) : nodesDataset = new vis.DataSet(nodes);
  redrawAll(nodesDataset, edges);
}
function aggiorna() {
  node = [];
  console.log(table.rows(".selected").data().length);
  for (var a = 0; a < table.rows(".selected").data().length; a++) {
    node.push(table.rows(".selected").data()[a]), console.log(node);
  }
  rules.forEach(function(c) {
    node.push(c);
  });
  console.log(node);
  nodesDataset = new vis.DataSet(node);
  redrawAll(nodesDataset, edges);
}
$(document).ready(function() {
  ajaxGet();
});
function redrawAll(a, c) {
  var e = document.getElementById("mynetwork"), b = {nodes:a, edges:c,};
  console.log(a.length);
  if (900 > a.length) {
    var d = {nodes:{shape:"dot", scaling:{min:10, max:30, label:{min:8, max:30, drawThreshold:12, maxVisible:20,},}, font:{size:12, face:"Tahoma",},}, edges:{width:0.15, color:{inherit:"from"}, smooth:{type:"continuous",},}, physics:{enabled:!1, solver:"repulsion", repulsion:{nodeDistance:400}}, interaction:{tooltipDelay:200, hideEdgesOnDrag:!0, hideEdgesOnZoom:!0,}, layout:{improvedLayout:!1}};
    console.log("sonoqui");
  } else {
    console.log("slpkd"), d = {nodes:{shape:"dot", scaling:{min:10, max:30,}, font:{size:12, face:"Tahoma",},}, edges:{smooth:!1}, physics: {
    enabled: true,
    solver: "forceAtlas2Based",
    stabilization: {
      enabled: false // This is here just to see what's going on from the very beginning.
    }
  }, layout:{improvedLayout:!1}};
  }
  network = new vis.Network(e, b, d);
  network.fit();
  network.stabilize();
  allNodes = nodesDataset.get({returnType:"Object"});
  network.on("click", neighbourhoodHighlight);
}
nodeFilterSelector.addEventListener("change", function(a) {
  fiternode = [];
  nodeFilterValue = a.target.value;
  console.log(nodeFilterValue);
  nodes.forEach(function(c) {
    "rule" == c.type && fiternode.push(c);
    nodeFilterValue * c.max / 100 <= c.score && fiternode.push(c);
  });
  console.log(fiternode);
  console.log(edges);
  redrawAll(fiternode, edges);
});
function neighbourhoodHighlight(a) {
  if (0 < a.nodes.length) {
    highlightActive = !0;
    var c, e = a.nodes[0], b;
    for (b in allNodes) {
      allNodes[b].color = "rgba(200,200,200,0.5)", void 0 === allNodes[b].hiddenLabel && (allNodes[b].hiddenLabel = allNodes[b].label, allNodes[b].label = void 0);
    }
    var d = network.getConnectedNodes(e), f = [];
    for (a = 1; 2 > a; a++) {
      for (c = 0; c < d.length; c++) {
        f = f.concat(network.getConnectedNodes(d[c]));
      }
    }
    for (a = 0; a < f.length; a++) {
      allNodes[f[a]].color = "rgba(150,150,150,0.75)", void 0 !== allNodes[f[a]].hiddenLabel && (allNodes[f[a]].label = allNodes[f[a]].hiddenLabel, allNodes[f[a]].hiddenLabel = void 0);
    }
    for (a = 0; a < d.length; a++) {
      allNodes[d[a]].color = void 0, void 0 !== allNodes[d[a]].hiddenLabel && (allNodes[d[a]].label = allNodes[d[a]].hiddenLabel, allNodes[d[a]].hiddenLabel = void 0);
    }
    allNodes[e].color = void 0;
    void 0 !== allNodes[e].hiddenLabel && (allNodes[e].label = allNodes[e].hiddenLabel, allNodes[e].hiddenLabel = void 0);
  } else if (!0 === highlightActive) {
    for (b in allNodes) {
      allNodes[b].color = void 0, void 0 !== allNodes[b].hiddenLabel && (allNodes[b].label = allNodes[b].hiddenLabel, allNodes[b].hiddenLabel = void 0);
    }
    highlightActive = !1;
  }
  a = [];
  for (b in allNodes) {
    allNodes.hasOwnProperty(b) && a.push(allNodes[b]);
  }
  nodesDataset.update(a);
}
;