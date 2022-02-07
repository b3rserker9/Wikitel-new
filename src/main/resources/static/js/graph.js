let array;
let rules = [];
let current_rule = [];
let current_edge = [];
var selected = null;
var nodes = [];
var edges = [];
let type = "text";
var network = null;
let uniqueChars = null;
let text = "desktop";
let arrow = "to";
let tables = [];
let c = null;
let raw = [];
let all = [];
var nodesDataset;
var edgesDataset;
var prova;

function update_add() {
    var select = document.getElementById('graph_rilevanza');
    var option = select.options[select.selectedIndex];
    console.log(option.value);
    switch (option.value) {
        case '2':
            arrow = "to, from"
            break;
        case '1':
            arrow = "to"
            break;
    }

}

function update() {
    var select = document.getElementById('new-rule-type');
    var option = select.options[select.selectedIndex];
    switch (option.value) {
        case '2':
            document.getElementById('url').style.display = "block";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "none";
            text = "desktop"

            break;
        case '1':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "block";
            document.getElementById('file').style.display = "none";
            text = "desktop"
            type = "text"
            break;
        case '3':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "none";
            text = "server"
            type = "wikipedia"

            break;
        case '4':
            document.getElementById('url').style.display = "none";
            document.getElementById('textarea').style.display = "none";
            document.getElementById('file').style.display = "block";
            text = "switch"
            type = "file"
            break;
    }

}

function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function wiki_link() {
    window.open("https://it.wikipedia.org/wiki/" + selected, "_blank");
}


var network;
var allNodes;
var highlightActive = false;

function order() {


    let order = [];
    rules.forEach(function(l) {
        order = [];
        l.suggestions.forEach(function(s) {
 
            order.push(s.score2);
        })
        order.sort(function(a, b) {
            return b - a
        });
        order.unshift(l.name);
   
        all.push(order);
    })
}

function start(){
	 rules.forEach(function(l) {
        const rule = new Object();
        rule.label = l.name;
        rule.id = l.name;
        let prova = getRandomColor();
        rule.group = l.name;
        current_rule.push(rule);
        nodes.push(rule);
        let m;
        for (let i = 0; i < all.length; i++) {
            if (all[i][0] == l.name) {
                m = all[i][1];
            }
        }
        l.suggestions.forEach(function(s) {
            raw = ["", s.suggestion.page, s.score, s.score2];
            if (s.score2 == 0) {
                s.score2 = 0.05
            }
            const pippo = new Object();


            pippo.label = s.suggestion.page
            pippo.id = pippo.label;
            pippo.group = l.name;
            pippo.color = prova;
            pippo.title = s.score2;
            edges.push({
                from: l.name,
                to: pippo.id,
                color: {
                    opacity: s.score2 / m
                },
                width: 4
            });

            nodes.push(pippo);
            tables.push(raw);

        })

    })
    
    for (let i = 0; i < nodes.length; i++) {
        if (rules.some(e => e.name === nodes[i].label) && nodes[i].group == 6) {
            nodes.splice(i, 1);

        }

    }
    nodes = nodes.filter((value, index, self) =>
        index === self.findIndex((t) => (
            t.id === value.id
        ))
    )
    for (let i = 0; i < nodes.length; i++) {
        if (nodes[i].title < 0.1) {
            nodes[i].color = "rgba(200,200,200,0.5)";
        }
    }
    table = document.getElementById("example");
    for (var i = 0; i < tables.length; i++) {
        // create a new row
        tbody = document.getElementById("row_element");
        var newRow = tbody.insertRow(table.length);
        for (var j = 0; j < tables[i].length; j++) {
            // create a new cell
            var cell = newRow.insertCell(j);

            // add value to the cell
            cell.innerHTML = tables[i][j];
        }
    }

     prova = $('#example').DataTable({
      dom: 'Blfrtip',
    buttons: [
        'selectAll',
        'selectNone'
    ],
    language: {
        buttons: {
            selectAll: "Select all items",
            selectNone: "Select none"
        }
    },
        columnDefs: [{
            orderable: false,
            className: 'select-checkbox',
            targets: 0
        }],
     
        select: {
            style: 'multi',
            selector: 'td:first-child'
        },
        order: [
            [1, 'asc']
        ]

    });
	 nodesDataset = new vis.DataSet(nodes);
    edgesDataset = new vis.DataSet(edges); 
    redrawAll()
}

function redrawAll() {
    var container = document.getElementById("mynetwork");
    var options = {
	layout: {improvedLayout:false},
        groups: {
            file: {
                shape: "icon",
                icon: {
                    face: "'FontAwesome'",
                    code: "\uf15b",
                    size: 50,
                    color: "#000000",
                },
            },
            wikipedia: {
                shape: "icon",
                icon: {
                    face: "'FontAwesome'",
                    code: "\uf266",
                    size: 50,
                    color: "#000000",
                },
            },
            text: {
                shape: "icon",
                icon: {
                    face: "'FontAwesome'",
                    code: "\uf031",
                    size: 50,
                    color: "#000000",
                },
            },
        },
        nodes: {
            shape: "dot",
            scaling: {
                min: 10,
                max: 30,

            },
            font: {
                size: 12,
                face: "Tahoma",
            },
        },
        edges: {
            width: 0.15,
            color: {
                inherit: "from"
            },
            smooth: {
                type: "continuous",
            },
        },
        physics: {
            // Even though it's disabled the options still apply to network.stabilize().
            enabled: true,
            solver: "repulsion",
            repulsion: {
                nodeDistance: 300 // Put more distance between the nodes.
            }
        }
    };
    var data = {
        nodes: nodesDataset,
        edges: edgesDataset
    }; 

    network = new vis.Network(container, data, options);
    network.fit();
    network.stabilize();
  
    // get a JSON object
    allNodes = nodesDataset.get({
        returnType: "Object"
    });
    
    var myModal = new bootstrap.Modal(document.getElementById('myModal'), {
        keyboard: false
    })
    
    
    network.on("click", function(params) {
        if ((params.nodes.length) > 0) {
            params.event = "[original event]";
            select = params.nodes[0]
            selected = params.nodes[0].replaceAll(' ', '_');
            document.getElementById("button_link").textContent = "https://it.wikipedia.org/wiki/" + selected
            myModal.show()
        }
    });
}



/*function disegna(nodes, edges) {
    var mynetwork = document.getElementById("mynetwork");
    var x = -mynetwork.clientWidth / 2 + 50;
    var y = -mynetwork.clientHeight / 2 + 50;
    var step = 70;


    var myModal = new bootstrap.Modal(document.getElementById('myModal'), {
        keyboard: false
    })
    network.on("click", function(params) {
        params.event = "[original event]";
        console.log(params);
        select = params.nodes[0]
        selected = params.nodes[0].replaceAll(' ', '_');
        console.log(selected.replaceAll(' ', '-'));
        document.getElementById("button_link").textContent = "https://it.wikipedia.org/wiki/" + selected
        myModal.show()
    });
}*/

  function aggiorna(){
	nodes = []
        nodesDataset = [];
        nodes.push(...current_rule);
        console.log(current_rule);
        for (let i = 0; i < prova.rows({
                selected: true
            }).count(); i++) {
            console.log(prova.rows({
                selected: true
            }).data()[i][1]);
            const pippo = new Object();
            pippo.label = prova.rows({
                selected: true
            }).data()[i][1]
            pippo.id = pippo.label;
            pippo.group = 5;
            nodes.push({
                id: prova.rows({
                    selected: true
                }).data()[i][1],
                label: prova.rows({
                    selected: true
                }).data()[i][1],
                group: text
            });
        }
        nodesDataset = new vis.DataSet(nodes);
	redrawAll()
}

function ajaxGet() {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "Getmodel",
        dataType: "json",
        data: JSON.stringify({
            ids: document.getElementById('model_name').getAttribute('value')
        }),
        success: function(data) {
            console.log("SUCCESS : ", data);
            moedl = data;
            rules = data.rules;
            order();
            start();

        },
        error: function(e) {
            alert("Error!")
            console.log("ERROR: ", e);
        }
    });
}

function addNode() {
    nodesDataset.add({
        id: $("#Add_node_name").val(),
        label: $("#Add_node_name").val(),
        group: type
    });
    edgesDataset.add({
        from: select,
        to: $("#Add_node_name").val(),
        arrows: arrow
    });
}



$(document).ready(
    function() {
        ajaxGet()
    })