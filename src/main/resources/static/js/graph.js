var $jscomp = $jscomp || {};
$jscomp.scope = {};
$jscomp.arrayIteratorImpl = function(a) {
  var b = 0;
  return function() {
    return b < a.length ? {done:!1, value:a[b++],} : {done:!0};
  };
};
$jscomp.arrayIterator = function(a) {
  return {next:$jscomp.arrayIteratorImpl(a)};
};
$jscomp.makeIterator = function(a) {
  var b = "undefined" != typeof Symbol && Symbol.iterator && a[Symbol.iterator];
  return b ? b.call(a) : $jscomp.arrayIterator(a);
};
$jscomp.arrayFromIterator = function(a) {
  for (var b, d = []; !(b = a.next()).done;) {
    d.push(b.value);
  }
  return d;
};
$jscomp.arrayFromIterable = function(a) {
  return a instanceof Array ? a : $jscomp.arrayFromIterator($jscomp.makeIterator(a));
};
var array, rules = [], current_rule = [], current_edge = [], AddNode = [], selected = null, nodes = [], edges = [], type = "text", network = null, uniqueChars = null, text = "desktop", arrow = "to", tables = [], c = null, raw = [], all = [], map1 = new Map(), nodesDataset, edgesDataset, prova, rule_selected, rule_effects;
function update_add() {
  var a = document.getElementById("graph_rilevanza");
  a = a.options[a.selectedIndex];
  console.log(a.value);
  switch(a.value) {
    case "2":
      arrow = "to, from";
      break;
    case "1":
      arrow = "to";
  }
}
function filter_rule() {
  document.getElementById("only_rule").checked ? (nodesDataset = new vis.DataSet(current_rule), console.log(nodesDataset)) : nodesDataset = new vis.DataSet(nodes);
  redrawAll();
}
function slider(a) {
  console.log(a);
  nodes = [];
  document.getElementById("range_input").innerHTML = a + "%";
  var b = 0;
  rules.forEach(function(d) {
    var e = {};
    e.label = d.name;
    e.id = d.name;
    var l = getRandomColor();
    e.color = "#1e90ff";
    e.group = "rule";
    nodes.push(e);
    b = map1.get(d.name)[0];
    d.suggestionm.forEach(function(g) {
      raw = ["", g.page, g.score, g.score2];
      0 == g.score2 && (g.score2 = 0.01);
      if (g.score2 >= b * a / 100 && !rules.some(function(f) {
        return f.name === g.page;
      }) && !nodes.some(function(f) {
        return f.label === g.page;
      })) {
        var h = {};
        h.label = g.page;
        h.id = h.label;
        h.group = d.id;
        h.color = l;
        h.name = g.score2;
        nodes.push(h);
        tables.push(raw);
      }
    });
  });
  nodesDataset = new vis.DataSet(nodes);
  redrawAll();
}
function update() {
  var a = document.getElementById("new-rule-type");
  switch(a.options[a.selectedIndex].value) {
    case "2":
      document.getElementById("url").style.display = "block";
      document.getElementById("textarea").style.display = "none";
      document.getElementById("file").style.display = "none";
      text = "desktop";
      break;
    case "1":
      document.getElementById("url").style.display = "none";
      document.getElementById("textarea").style.display = "block";
      document.getElementById("file").style.display = "none";
      text = "desktop";
      type = "text";
      break;
    case "3":
      document.getElementById("url").style.display = "none";
      document.getElementById("textarea").style.display = "none";
      document.getElementById("file").style.display = "none";
      text = "server";
      type = "wikipedia";
      break;
    case "4":
      document.getElementById("url").style.display = "none", document.getElementById("textarea").style.display = "none", document.getElementById("file").style.display = "block", text = "switch", type = "file";
  }
}
function getRandomColor() {
  for (var a = "#", b = 0; 6 > b; b++) {
    a += "0123456789ABCDEF"[Math.floor(16 * Math.random())];
  }
  return a;
}
function wiki_link() {
  window.open("https://it.wikipedia.org/wiki/" + selected, "_blank");
}
var allNodes, highlightActive = !1;
function order() {
  var a = [];
  console.log(rules);
  rules.forEach(function(b) {
    a = [];
    b.suggestionm.forEach(function(d) {
      a.push(d.score2);
    });
    a.sort(function(d, e) {
      return e - d;
    });
    map1.set(b.name, a);
  });
}
function start() {
  rules.forEach(function(e) {
    var l = {};
    l.label = e.name;
    l.id = e.name;
    var g = getRandomColor();
    l.color = "#1e90ff";
    current_rule.push(l);
    nodes.push(l);
    var h = map1.get(e.name)[0];
    e.suggestionm.forEach(function(f) {
      var k = {};
      k.id = f.page;
      raw = ["", f.page, f.score, f.score2];
      0 == f.score2 && (f.score2 = 0.05);
      if (rules.some(function(n) {
        return n.name === f.page;
      })) {
        edges.push({from:e.name, to:k.id, color:{opacity:f.score2 / h}, width:4});
      } else {
        var m = g;
        0.1 > f.score2 && (m = "rgba(200,200,200,0.5)");
        k.label = f.page;
        k.id = k.label;
        k.group = e.id;
        k.color = m;
        k.name = "cliccami!";
        edges.push({from:e.name, to:k.id, color:{opacity:f.score2 / h}, width:4});
        nodes.push(k);
        tables.push(raw);
      }
    });
  });
  nodes = nodes.filter(function(e, l, g) {
    return l === g.findIndex(function(h) {
      return h.id === e.id;
    });
  });
  tables = document.getElementById("example");
  for (var a = 0; a < tables.length; a++) {
    tbody = document.getElementById("row_element");
    for (var b = tbody.insertRow(table.length), d = 0; d < tables[a].length; d++) {
      b.insertCell(d).innerHTML = tables[a][d];
    }
  }
  prova = $("#example").DataTable({dom:"Blfrtip", buttons:["selectAll", "selectNone"], language:{buttons:{selectAll:"Select all items", selectNone:"Select none"}}, columnDefs:[{orderable:!1, className:"select-checkbox", targets:0}], select:{style:"multi", selector:"td:first-child"}, order:[[1, "asc"]]});
  nodesDataset = new vis.DataSet(nodes);
  edgesDataset = new vis.DataSet(edges);
  redrawAll();
}

function newStart(tot){
	nodesDataset = new vis.DataSet(tot[1]);
  	edgesDataset = new vis.DataSet(tot[0]);
  	 prova = $("#example").DataTable({dom:"Blfrtip", buttons:["selectAll", "selectNone"], language:{buttons:{selectAll:"Select all items", selectNone:"Select none"}}, columnDefs:[{orderable:!1, className:"select-checkbox", targets:0}], select:{style:"multi", selector:"td:first-child"}, order:[[1, "asc"]]});
  
  
  table = document.getElementById("example");
  for (var a = 0; a < tables.length; a++) {
    tbody = document.getElementById("row_element");
    for (var b = tbody.insertRow(table.length), d = 0; d < tables[a].length; d++) {
      b.insertCell(d).innerHTML = tables[a][d];
    }
  }
  redrawAll();
 
}

function redrawAll() {
  var a = document.getElementById("mynetwork");
  network = new vis.Network(a, {nodes:nodesDataset, edges:edgesDataset}, {layout:{improvedLayout:!1}, groups:{file:{shape:"icon", icon:{face:"'FontAwesome'", code:"\uf15b", size:50, color:"#000000",},}, wikipedia:{shape:"icon", icon:{face:"'FontAwesome'", code:"\uf266", size:50, color:"#000000",},}, text:{shape:"icon", icon:{face:"'FontAwesome'", code:"\uf031", size:50, color:"#000000",},},}, nodes:{shape:"dot", scaling:{min:10, max:30,}, font:{size:12, face:"Tahoma",},}, edges:{width:0.15, color:{inherit:"from"}, 
  smooth:{type:"continuous",},}, physics:{stabilization:false, solver:"repulsion", repulsion:{nodeDistance:900}}});
  network.fit();
  network.stabilize();
 // allNodes = nodesDataset.get({returnType:"Object"});
//  document.getElementById("loader-page").style.display= "none"
  var b = new bootstrap.Modal(document.getElementById("myModal"), {keyboard:!1});
  network.on("click", function(d) {
    0 < d.nodes.length && (rule_effects = nodesDataset.get(d.nodes)[0].group, console.log(rule_effects), rule_selected = nodesDataset.get(d.nodes)[0].label, d.event = "[original event]", select = d.nodes[0], selected = d.nodes[0].replaceAll(" ", "_"), document.getElementById("button_link").textContent = "https://it.wikipedia.org/wiki/" + selected, b.show());
  });
}
function esplora() {
  toastr.info("Avviata la ricerca su " + rule_selected);
  var a = {model_id:window.location.pathname.split("/")[2], rule_id:rule_effects, rule_name:rule_selected, rule_type:"Pagina Wikipedia"};
  console.log(a);
  $.ajax({type:"POST", contentType:"application/json", url:"/Newrule", data:JSON.stringify(a), dataType:"json", success:function(b) {
    console.log("SUCCESS : ", b);
    toastr.info("La ricerca su " + rule_selected + "\u00e8 stata completata");
    ajaxGet();
  }, error:function(b) {
    alert("Error!");
    console.log("ERROR: ", b);
  }});
}
function aggiorna() {
  nodes = [];
  nodesDataset = [];
  nodes.push.apply(nodes, $jscomp.arrayFromIterable(current_rule));
  for (var a = 0; a < prova.rows({selected:!0}).count(); a++) {
    console.log(prova.rows({selected:!0}).data()[a][1]), prova.rows({selected:!0}).data(), nodes.push({id:prova.rows({selected:!0}).data()[a][1], label:prova.rows({selected:!0}).data()[a][1], group:text});
  }
  nodesDataset = new vis.DataSet(nodes);
  redrawAll();
}
function ajaxGet() {
  $.ajax({type:"POST", contentType:"application/json", url:"/findsuggestiontot", dataType:"json", data:JSON.stringify({ids:document.getElementById("model_name").getAttribute("value")}), success:function(a) {
    console.log("SUCCESS : ",a);
    //order();
    //start();
    newStart(a)
  }, error:function(a) {
    alert("Error!");
    console.log("ERROR: ", a);
  }});
}
function NewNodeManual() {
  $.ajax({type:"POST", contentType:"application/json", url:"/Newrule", dataType:"json", data:JSON.stringify(AddNode), success:function(a) {
    console.log("SUCCESS : ", a);
  }, error:function(a) {
    alert("Error!");
    console.log("ERROR: ", a);
  }});
}
function addNode() {
  var a = {rule_name:$("#Add_node_name").val(), rule_text:document.getElementById("rule_Textarea").value, rule_type :type, rule_id:select, rule_parent:rule_effects};
  AddNode.push(a);
  console.log(AddNode);
  nodesDataset.add({id:$("#Add_node_name").val(), label:$("#Add_node_name").val(), group:type});
  edgesDataset.add({from: select, to:$("#Add_node_name").val(), arrows:arrow});
}
$(document).ready(function() {
  ajaxGet();
  $("#Save").submit(function() {
    event.preventDefault();
    NewNodeManual();
  });
});