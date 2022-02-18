import * as hello from "./lesson.js";
import {timeline} from "./timeline.js";


$(document).ready(
    function() {
	
	var current_user = document.getElementById("nameid").getAttribute('value');
	console.log(current_user);
        // defined a connection to a new socket endpoint
        var socket = new SockJS('/comunication');
        var stompClient = Stomp.over(socket);
        stompClient.connect({ }, function(frame) {
	var url = socket._transport.url.split('/');
	var index = url.length -2;
	var session = url[index];
	
            // subscribe to the /topic/message endpoint
            stompClient.subscribe("/queue/notify-user"+session, function(data) {
                var message = data.body;
                console.log(message)
                setup_ws(message);
            });
          
  var message={"session":session,"user_id":current_user};
            stompClient.send("/app/register",{},JSON.stringify(message))
        });
          });

function setup_ws(msg) {

        const c_msg = JSON.parse(msg);
        switch (c_msg.type) {
            case 'online':
             console.log("online");
                break;
            case 'follower':
            console.log("follower");
                break;
            case 'profile-update':
                console.log("profileUpdate");
                break;
            case 'lesson-state-update':
               console.log("lessonupdate")
                break;
            case 'text-stimulus':
            case 'question-stimulus':
            case 'url-stimulus':
               toastr.options = {
  "closeButton": false,
  "debug": true,
  "newestOnTop": false,
  "progressBar": true,
  "positionClass": "toast-top-right",
  "preventDuplicates": false,
  "onclick": null,
  "showDuration": "20000",
  "hideDuration": "30000",
  "timeOut": "30000",
  "extendedTimeOut": "30000",
  "showEasing": "swing",
  "hideEasing": "linear",
  "showMethod": "fadeIn",
  "hideMethod": "fadeOut"
}
 toastr.info("Ha avuto un nuovo stimolo da una lezione");
 let parent =document.getElementById("messages");
 let div = document.createElement("div");
 div.className="message"
 div.innerHTML=c_msg.name;
 parent.appendChild(div);
                break;
            case 'Graph':
              console.log("Graph");
                break;
            case 'StartedSolving':
                console.log("solving the problem..");
                break;
            case 'SolutionFound':
                console.log("hurray!! we have found a solution..");
                break;
            case 'InconsistentProblem':
                console.log("unsolvable problem..");
                break;
            case 'FlawCreated':
             console.log("FlawCreated");
                break;
            case 'FlawStateChanged':
               console.log("FlawStateChanged");
                break;
            case 'FlawCostChanged':
               console.log("FlawCostChanged")
                break;
            case 'FlawPositionChanged':
              console.log("FlawCostChanged2")
                break;
            case 'CurrentFlaw':
            console.log("FlawCostChanged3")
                break;
            case 'ResolverCreated':
                console.log("FlawCostChanged4")
                break;
            case 'ResolverStateChanged':
              console.log("FlawCostChanged5")
                break;
            case 'CurrentResolver':
              console.log("FlawCostChanged6")
                break;
            case 'CausalLinkAdded':
               console.log("FlawCostChanged7");
                break;
            case 'Timelines':
            
            console.log(c_msg.timelines[0].horizon + "horizon");
            localStorage.setItem("horizon",c_msg.timelines[0].horizon);
             hello.horizon(c_msg.timelines[0].horizon);
             timeline(c_msg.timelines[0].values)
       break;
            case 'Tick':
            console.log(msg);
            console.log(localStorage.getItem("horizon"));
            	hello.horizon(localStorage.getItem("horizon"));
               hello.loading(c_msg.current_time.num);
                break;
            case 'StartingAtoms':
                console.log("StartingAtoms")
                break;
            case 'EndingAtoms':
               console.log("EndingAtoms")
                break;
            default:
                console.log(msg);
                break;
        }
}