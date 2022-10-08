var network, allNodes, highlightActive = !1, nodestable, nodes ,halfnodes , table, edges, rules, type = "Testo", AddNode = [], edgesDataset ;
highlightActive = !1;
var nodesDataset, fiternode = [], nodeFilterValue = -1, nodeFilterSelector = document.getElementById("customRange1");


function update() {
  var a = document.getElementById("new-rule-type");
  switch(a.options[a.selectedIndex].value) {
    case "2":
      document.getElementById("url").style.display = "block";
      document.getElementById("textarea").style.display = "none";
      document.getElementById("file").style.display = "none";
      text = "desktop";
      type = "Pagina Web";
      break;
    case "1":
      document.getElementById("url").style.display = "none";
      document.getElementById("textarea").style.display = "block";
      document.getElementById("file").style.display = "none";
      text = "desktop";
      type = "Testo";
      break;
    case "3":
      document.getElementById("url").style.display = "none";
      document.getElementById("textarea").style.display = "none";
      document.getElementById("file").style.display = "none";
      text = "server";
      type = "Pagina Wikipedia";
      break;
    case "4":
      document.getElementById("url").style.display = "none", document.getElementById("textarea").style.display = "none", document.getElementById("file").style.display = "block", text = "switch", type = "file";
  }
}

function sendnode(a){
	 $.ajax({type:"POST", contentType:"application/json", url:"/Newrule", dataType:"json", data:JSON.stringify(a), success:function(a) {
    console.log("SUCCESS : ", a);
        $('#example').DataTable().clear().destroy();
     ajaxGet();
document.getElementById("goal").innerHTML += '<div class="form-check form-switch col"><input class="form-check-input" type="checkbox" role="switch"  th:value="${g.id}" th:name="${g.name}"> <label class="form-check-label" for="flexSwitchCheckDefault" th:text="${g.name}"></label></div>'
   
  }, error:function(a) {
    alert("Error!");
    console.log("ERROR: ", a);
  }});
}

function New_rule_file(){
	    var form = $("#formFile")[0].files[0];
    var data = new FormData();
    data.append("uploadfile", form);
      $.ajax({
                    url: "/uploadFileRulePre",
                    type: "POST",
                    data: data,
                    DataType: 'json',
                    processData: false,
                    contentType: false,
                    cache: false,
                    success: function(res) {
                        console.log("SUCCESS : ", res);
                        var a = {model_id: document.getElementById("model_name").getAttribute("value") , rule_name:$("#Add_node_name").val(), rule_type :"File",  rule_id: rule_effects.rule_id};
                        sendnode(a)
                    },
                    error: function(err) {
                        console.error(err);
                        
                    }
                });
}

function addNode() {
  var a = {model_id: document.getElementById("model_name").getAttribute("value") ,rule_length:$("#Add_node_time").val(), rule_name:$("#Add_node_name").val(), rule_text:document.getElementById(type).value, rule_type :type,  rule_id: rule_effects.rule_id};
 if(type!="file"){
 	sendnode(a)}
  else{
	New_rule_file()
}
document.getElementById("model_name").value = ''
document.getElementById("Testo").value = ''
document.getElementById("Pagina Web").value = ''
}



function ajaxGet() {
  $.ajax({type:"POST", contentType:"application/json", url:"/findsuggestiontot", dataType:"json", data:JSON.stringify({ids:document.getElementById("model_name").getAttribute("value")}), success:function(a) {
    console.log("SUCCESS : ", a);
    edges = a[0];
    edgesDataset = new vis.DataSet(a[0]);
    nodestable = a[2];
    rules = a[3];
    nodes = a[1];
    halfnodes = a[4];
    
    table = $("#example").DataTable({autoWidth:!1, data:nodestable, columns:[{data:"id"}, {data:"parent"}, {data:"type"}, {data:"score2"}]});
    $("#example tbody").on("click", "tr", function() {
      $(this).toggleClass("selected");
      console.log(table.rows(".selected").data());
    });
    $("#button").click(function() {
      alert(table.rows(".selected").data().length + " row(s) selected");
    });
    nodesDataset = new vis.DataSet(halfnodes);
    
    redrawAll(nodesDataset, edgesDataset);
  }, error:function(a) {
    alert("Error!");
    console.log("ERROR: ", a);
  }});
}
function filter_rule() {
  document.getElementById("only_rule").checked ? (nodesDataset = new vis.DataSet(rules), console.log(nodesDataset)) : nodesDataset = new vis.DataSet(halfnodes);
  redrawAll(nodesDataset, edgesDataset);
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
  redrawAll(nodesDataset, edgesDataset);
}
$(document).ready(function() {
  ajaxGet();
});
function redrawAll(a, c) {
  var e = document.getElementById("mynetwork"), b = {nodes:a, edges:c};
  console.log(a.length);
  if (900 > a.length) {
    var d = {nodes:{shape:"dot", scaling:{min:10, max:30}, size: 16,}, edges:{width:0.15, color:{inherit:"from"}, smooth:{type:"continuous",},}, physics:{enabled:!1, solver:"repulsion", repulsion:{nodeDistance:250,springLength:10}}, interaction:{tooltipDelay:200, hideEdgesOnDrag:!0, hideEdgesOnZoom:!0,}, layout:{improvedLayout:!1}};
    console.log("sonoqui");
  } else {
    console.log("slpkd"), d = {nodes:{shape:"dot", scaling:{min:10, max:30,}, size: 16,}, edges:{smooth:!1}, physics: {
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
  allNodes =  nodesDataset.get({returnType:"Object"});
  network.on("click", neighbourhoodHighlight);
}
nodeFilterSelector.addEventListener("change", function(a) {
  fiternode = [];
  nodeFilterValue = a.target.value;

  nodes.forEach(function(c) {
    "rule" == c.type && fiternode.push(c);
    nodeFilterValue * c.max / 100 <= c.score && fiternode.push(c);
  });

  nodesDataset = new vis.DataSet(fiternode);
  redrawAll(nodesDataset, edgesDataset);
});

function esplora() {
  toastr.info("Avviata la ricerca su " + rule_selected);
  var a = {model_id:window.location.pathname.split("/")[2], rule_id: rule_effects.rule_id, rule_name:rule_selected, rule_type:"Pagina Wikipedia"};
  console.log(a);
  $.ajax({type:"POST", contentType:"application/json", url:"/Newrule", data:JSON.stringify(a), dataType:"json", success:function(b) {
    console.log("SUCCESS : ", b);
    toastr.info("La ricerca su " + rule_selected + "\u00e8 stata completata");
    $('#example').DataTable().clear().destroy();
    ajaxGet();
  }, error:function(b) {
    alert("Error!");
    console.log("ERROR: ", b);
  }});
}


function wiki_link() {
  window.open("https://it.wikipedia.org/wiki/" + selected, "_blank");
}

function neighbourhoodHighlight(a) {
	 var b = new bootstrap.Modal(document.getElementById("myModal"), {keyboard:!1});
	 console.log(a.nodes.length)
	 if(0 < a.nodes.length){
	console.log(a)
	 if(nodesDataset.get(a.nodes)[0].type === "rule"){
	  document.getElementById("addnodo").style.display= "block";
	  
	  document.getElementById("esploranodo").style.display= "none";
	  }else{
	   document.getElementById("addnodo").style.display= "none";
	   document.getElementById("Add").style.display= "none";
	   document.getElementById("esploranodo").style.display= "block";
	   }
	    if(nodesDataset.get(a.nodes)[0].rule_type === "text"){
	  	document.getElementById("textarea1").style.display= "block"
	  	document.getElementById("all_files").style.display="none"
	  	document.getElementById("Testo1").value = nodesDataset.get(a.nodes)[0].rule_text
	  	document.getElementById("button_link").style.display="none"}
	  else if (nodesDataset.get(a.nodes)[0].rule_type === "wiki" || nodesDataset.get(a.nodes)[0].type === "Suggestion"){
		console.log("WOOOOE")
		document.getElementById("textarea1").style.display= "none"
		document.getElementById("all_files").style.display="none"
		document.getElementById("button_link").style.display="block"
		
	}else if(nodesDataset.get(a.nodes)[0].rule_type === "file"){
	 	document.getElementById("textarea1").style.display= "none"
	 	document.getElementById("button_link").style.display="none"
	 	document.getElementById("all_files").style.display="block"}
	 	document.getElementById("file_download").textContent=nodesDataset.get(a.nodes)[0].id
	 	document.getElementById("file_download").setAttribute('href',"/fileRule/"+nodesDataset.get(a.nodes)[0].rule_id);
	   }
  if (0 < a.nodes.length) {
	rule_effects = nodesDataset.get(a.nodes)[0], console.log(rule_effects), rule_selected = nodesDataset.get(a.nodes)[0].label, a.event = "[original event]", select = a.nodes[0], selected = a.nodes[0].replaceAll(" ", "_"), b.show(),document.getElementById("button_link").textContent = "https://it.wikipedia.org/wiki/" + selected;
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