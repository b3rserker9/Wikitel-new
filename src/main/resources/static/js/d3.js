highlightActive = !1;
var nodesDataset, fiternode = [], nodeFilterValue = -1, nodeFilterSelector = document.getElementById("customRange1"),rule_effects,type="Testo",table;


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

function checkAddNode(){
	 send=true;
	
		if(document.getElementById("Add_node_name").value.length == 0){
		document.getElementById("error_name_rule").innerText = "Nome non inserito";
		send=false;
		} else{
		document.getElementById("error_name_rule").innerText = '';
	}
	if (document.getElementById("Add_node_time").value.length == 0){
		document.getElementById("error_time").innerText = "Durata lezione non inserita";
		send=false;
	}else{
		document.getElementById("error_time").innerText = '';
	}
	if(document.getElementById("Pagina Web").value.length == 0 && type == "web"){
		document.getElementById("error_web").innerText = "Url non inserito";
		send=false;
	}else{
		document.getElementById("error_web").innerText = '';
	}
	
	if(document.getElementById("Testo").value.length == 0 && type == "Testo"){
		document.getElementById("error_add_text").innerText = "Testo non inserito";
		send=false;
	}else{
		document.getElementById("error_add_text").innerText = '';
	}
	if(document.getElementById("formFile").files.length == 0 && type == "file"){
		document.getElementById("error_add_file").innerText = "File non inserito";
		send=false;
	}else{
		document.getElementById("error_add_file").innerText = '';
	}
	return send;
}


function sendnode(a){


	console.log(document.getElementById("formFile").files.length)

	 $.ajax({type:"POST", contentType:"application/json", url:"/Newrule", dataType:"json", data:JSON.stringify(a), success:function(a) {
    console.log("SUCCESS : ", a);
    if(a.status == 'NO'){
	document.getElementById("error_name_rule").innerText = "Errore nome dupplicato";
}else{
	document.getElementById("model_name").value = ''
document.getElementById("Testo").value = ''
document.getElementById("Pagina Web").value = ''
document.getElementById("Add_node_name").value = ''
document.getElementById("Add_node_time").value = ''

        table.destroy();
     ajaxGet();
document.getElementById("lesson_goal").innerHTML += '<div style="padding-right:10px" id="'+a.data6+'"> <div class="form-check form-switch col"> <input class="form-check-input" type="checkbox" role="switch" value="'+a.data6+'" name="'+a.rule_name+'"> <label class="form-check-label" for="flexSwitchCheckDefault">'+a.rule_name+'</label> </div> </div>'
   }
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
                        var a = {model_id: document.getElementById("model_name").getAttribute("value") , rule_name:$("#Add_node_name").val(), rule_type :"File",  rule_id: rule_effects.rule_id,rule_length:$("#Add_node_time").val()};
                        sendnode(a)
                    },
                    error: function(err) {
                        console.error(err);
                        
                    }
                });
}

function addNode() {
  var a = {model_id: document.getElementById("model_name").getAttribute("value") ,rule_length:$("#Add_node_time").val(), rule_name:$("#Add_node_name").val(), rule_text: document.getElementById(type).value, rule_type :type,  rule_id: rule_effects.rule_id};

 if(checkAddNode()){
 if(type!="file"){
 	sendnode(a)}
  else{
	New_rule_file()
}
}

}



function ajaxGet() {
  $.ajax({type:"POST", contentType:"application/json", url:"/findsuggestiontot",async:false, dataType:"json", data:JSON.stringify({ids:document.getElementById("model_name").getAttribute("value")}), success:function(a) {
    console.log("SUCCESS : ", a);
    edges = a[0];
    edgesDataset = new vis.DataSet(a[0]);
    nodestable = a[2];
    rules = a[3];
    nodes = a[1];
    halfnodes = a[4];
    
   
    table = $("#example").DataTable({autoWidth:!1, data:nodestable, columns:[{data:"id"}, {data:"parent"}, {data:"type"}, {data:"score2"}]});

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
function MultiSearch(){
	 for (var a = 0; a < table.rows(".selected").data().length; a++) {
		elem = table.rows(".selected").data()[a]

           	if(document.getElementsByClassName("flexing").length == 0)
           		document.getElementById("Search_container").innerHTML= ''
           		
             document.getElementById("Search_container").innerHTML += '<li class="list-group-item flexing" ><div class="fw-bold text-wrap ricerca" style="width: 85%;overflow-wrap: break-word;word-wrap: break-word;hyphens: auto;">'+elem.id+' (<small class="w-100" style="text-align: center;" >'+window.location.pathname.split("/")[2]+'</small>)</div><div id="'+elem.id.replace(/\s/g, '').toLowerCase()+'" class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div> </li>'
          
          var precondition = {
                model_id: window.location.pathname.split("/")[2],
                rule_id: elem.rule_id,
                rule_name: elem.id,
                rule_type: "Pagina Wikipedia",
            }
          
          
            $.ajax({

                type: "POST",
                contentType: "application/json",
                url: "/Newrule",
                data: JSON.stringify(precondition),
                dataType: "json",
                success: function(data) {
                    console.log("SUCCESS : ", data);
                    table.clear();
       				 ajaxGet();
                   

                },
                error: function(e) {
                    alert("Error!")
                    console.log("ERROR: ", e);
                }
            });
  }
}

function aggiorna() {
  node = [];
  console.log(table.rows(".selected").data().length);
  for (var a = 0; a < table.rows(".selected").data().length; a++) {
    node.push(table.rows(".selected").data()[a]), console.log(table.rows(".selected").data()[a].rule_id);
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
  table.on("click", "tr", function() {
      $(this).toggleClass("selected");
      console.log("ciaoo")
      console.log(table.rows(".selected").data().length)
      
    });
});
function redrawAll(a, c) {
	 document.getElementById("loadingBar").style.display = "block";
	 document.getElementById("loadingBar").style.opacity = 1;
  var e = document.getElementById("mynetwork"), b = {nodes:a, edges:c};
  console.log(a.length);
  if (700 > a.length) {
    var d = {nodes:{shape:"dot", scaling:{min:10, max:30}, size: 16,shapeProperties: {
    interpolation: false    // 'true' for intensive zooming
  }}, edges:{width:0.15 , selectionWidth: function (width) {return width*2;}, color:{inherit:"from"}, smooth:{type:"continuous",},}, physics:{enabled:false,stabilization:false
     , solver:"repulsion", repulsion:{nodeDistance:500}}, interaction:{tooltipDelay:200, hideEdgesOnDrag:!0, hideEdgesOnZoom:!0,}, layout:{improvedLayout:false}, groups:{file:{shape:"icon", icon:{face:"'FontAwesome'", code:"\uf15b", size:50, color:"#000000",},}, text:{shape:"icon", icon:{face:"'FontAwesome'", code:"\uf031", size:50, color:"#000000",},},}};
  } else {
     d = {nodes:{shape:"dot", scaling:{min:10, max:30,}, size: 16,}, edges:{smooth:!1}, physics: {
    forceAtlas2Based: {
      gravitationalConstant: -126,
      springLength: 200,
      springConstant: 0.01
    },
    maxVelocity: 50,
    solver: "forceAtlas2Based",
    timestep: 0.35,
    stabilization: true
  },
  interaction: {
    tooltipDelay: 200,
    hideEdgesOnDrag: true,
    hideEdgesOnZoom: true
  }
  , layout:{improvedLayout:!1}};
  }
  network = new vis.Network(e, b, d);

  allNodes =  nodesDataset.get({returnType:"Object"});
    network.fit();
  network.stabilize();
  network.on("click", neighbourhoodHighlight);
  
  network.on("stabilizationProgress", function (params) {
    var maxWidth = 496;
    var minWidth = 20;
    var widthFactor = params.iterations / params.total;
    var width = Math.max(minWidth, maxWidth * widthFactor);

    document.getElementById("bar").style.width = width + "px";
    document.getElementById("text").innerText =
      Math.round(widthFactor * 100) + "%";
  });
  network.once("stabilizationIterationsDone", function () {
    document.getElementById("text").innerText = "100%";
    document.getElementById("bar").style.width = "496px";
    document.getElementById("loadingBar").style.opacity = 0;
    // really clean the dom element
    setTimeout(function () {
      document.getElementById("loadingBar").style.display = "none";
    }, 500);
  });
}

  
  
nodeFilterSelector.addEventListener("change", function(a) {
  fiternode = [];
  nodeFilterValue = a.target.value;

  nodes.forEach(function(c) {
    "rule" == c.type && fiternode.push(c);
    nodeFilterValue * c.max / 100 <= c.score && fiternode.push(c);
  });
document.getElementById("only_rule").checked = false
  console.log(document.getElementById("only_rule").checked)
  nodesDataset = new vis.DataSet(fiternode);
  redrawAll(nodesDataset, edgesDataset);
});



function esplora() {
	 document.getElementById("graph_search").style.display="block";
	 	if(document.getElementsByClassName("flexing").length == 0)
           		document.getElementById("Search_container").innerHTML= ''
           		
	             document.getElementById("Search_container").innerHTML += '<li class="list-group-item flexing" id="'+rule_selected.replace(/\s/g, '').toLowerCase()+'"><div class="fw-bold text-wrap ricerca" style="width: 85%;overflow-wrap: break-word;word-wrap: break-word;hyphens: auto;">'+rule_selected+' (<small class="w-100" style="text-align: center;" >'+window.location.pathname.split("/")[2]+'</small>)</div><div id="'+rule_selected.replace(/\s/g, '').toLowerCase()+'" class="spinner-border text-primary" role="status"><span class="visually-hidden">Loading...</span></div> </li>'

  
  var a = {model_id:window.location.pathname.split("/")[2], rule_id: rule_effects.rule_id, rule_name: rule_selected, rule_type:"Pagina Wikipedia"};
  console.log(a);
  $.ajax({type:"POST", contentType:"application/json", url:"/Newrule", data:JSON.stringify(a), dataType:"json", success:function(b) {
    console.log("SUCCESS : ", b);
 
    
   table.destroy()
    ajaxGet();
  	document.getElementById("graph_search").style.display="none";
  }, error:function(b) {
    alert("Error!");
    console.log("ERROR: ", b);
  }});
}


function wiki_link() {
	if(rule_effects.rule_type=="web"){
		if(!rule_effects.rule_web.match(/^https?:\/\//i)){
		var url = 'http://' + rule_effects.rule_web;
		document.getElementById("iframe").setAttribute("src",url);
		}else{
			document.getElementById("iframe").setAttribute("src",rule_effects.rule_web);
		}
	}else{
		document.getElementById("iframe").setAttribute("src","https://it.wikipedia.org/wiki/" + selected);

  }
}

function deleteRule(){
	
		var d = {model:window.location.pathname.split("/")[2] , rule: rule_effects.rule_id}
	  $.ajax({type:"POST", contentType:"application/json", url:"/deleterule", data:JSON.stringify(d), dataType:"json", success:function(b) {
    console.log("SUCCESS : ", b);
    $('#example').DataTable().destroy();
    ajaxGet();

  }, error:function(b) {
    alert("Error!"+ b);
    console.log("ERROR: ", b);
  }});
	
	
}

function neighbourhoodHighlight(a) {
	 var b = new bootstrap.Modal(document.getElementById("myModal"), {keyboard:!1});
	
	
  if (0 < a.nodes.length) {
	rule_effects = nodesDataset.get(a.nodes)[0], console.log(rule_effects), rule_selected = nodesDataset.get(a.nodes)[0].label, a.event = "[original event]", select = a.nodes[0], selected = a.nodes[0].replaceAll(" ", "_"), b.show(),document.getElementById("right_title").textContent = a.nodes[0];
  
    
	 if(nodesDataset.get(a.nodes)[0].type === "rule"){
	  document.getElementById("Add").style.display= "block";
	  document.getElementById("esploranodo").style.display= "none";
	  document.getElementById("deleteRule").style.display= "block";
	  }else{
		document.getElementById("deleteRule").style.display= "none";
	   document.getElementById("Add").style.display= "none";
	   document.getElementById("esploranodo").style.display= "block";
	   }
	    if(nodesDataset.get(a.nodes)[0].rule_type === "text"){
	  	document.getElementById("textarea1").style.display= "block"
	  	document.getElementById("all_files").style.display="none"
	  	document.getElementById("Testo1").value = nodesDataset.get(a.nodes)[0].rule_text
	  	document.getElementById("button_link").style.display="none"}
	  else if (nodesDataset.get(a.nodes)[0].rule_type === "wiki" || nodesDataset.get(a.nodes)[0].type === "Suggestion"){
		
		document.getElementById("textarea1").style.display= "none"
		document.getElementById("all_files").style.display="none"
		document.getElementById("button_link").style.display="block"
		
	}else if(nodesDataset.get(a.nodes)[0].rule_type === "file"){
	 	document.getElementById("textarea1").style.display= "none"
	 	document.getElementById("button_link").style.display="none"
	 	document.getElementById("all_files").style.display="block"}
	 	else if (nodesDataset.get(a.nodes)[0].rule_type === "web"){
		document.getElementById("textarea1").style.display= "none"
		document.getElementById("all_files").style.display="none"
		document.getElementById("button_link").style.display="block"
		document.getElementById("button_link").textContent = nodesDataset.get(a.nodes)[0].rule_web;
	}
	 	document.getElementById("file_download").textContent=nodesDataset.get(a.nodes)[0].id
	 	document.getElementById("file_download").setAttribute('href',"/fileRule/"+nodesDataset.get(a.nodes)[0].rule_id);
    
    
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
$(document).ready(function() {
	
	 
	
	
	});