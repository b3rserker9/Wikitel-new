import * as lesson from "./lesson.js";
import {timeline,type} from "./timeline.js";
import {addNewLesson} from "./lessonadd.js";
let u= null;

$(document).ready(
    function() {
	
	var current_user = document.getElementById("nameid").getAttribute('value');
	
	
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
          
  var message={"session":session,"user_id":current_user ,"lesson_id": lesson.lesson_id()};
            stompClient.send("/app/register",{},JSON.stringify(message))
        });
          });

function setup_ws(msg) {

        const c_msg = JSON.parse(msg);
        switch (c_msg.type) {
			case 'searching':
			if(c_msg.status == 1){
			toastr.success(c_msg.name + " è stato aggiunto");
			var st = c_msg.name.replace(/\s/g, '').toLowerCase();
			console.log(st)
			document.getElementById(st).innerHTML = '<i class="fas fa-check fa-2x"></i>';
			document.getElementById(st).classList.remove('spinner-border')
			document.getElementById(st).classList.remove('text-primary')
			document.getElementById(st).classList.add('ico')
			document.getElementById("ricerca_obiettivi").style.display="none"
			}else if(c_msg.status == -1){
				document.getElementById('r' + st).remove();
			}
			else{
				toastr.info("Avviata la ricerca su " + c_msg.name);
				document.getElementById("ricerca_obiettivi").style.display="block"
			}
			break;
			case 'lesson':
			addNewLesson(c_msg.id_Lesson,c_msg.name_lesson,c_msg.teacher_lesson,c_msg.teacher_id)
			break;
            case 'online':
             console.log("online");
                break;
            case 'user':
             type(c_msg.id)
             u=c_msg.id
                break;
            case 'follower':
            toastr.info("sei stato aggiunto " + c_msg.state);
                break;
                   case 'Subscribe':
            toastr.info("sei stato aggiunto alla lezione " + c_msg.lesson);
            addlesson(c_msg)
                break;
            case 'profile-update':
                console.log("profileUpdate");
                break;
            case 'lesson-state-update':
               toastr.info("lesson-state-update " + c_msg.state);
                break;
            case 'text-stimulus':
            lesson.new_stimulitext(c_msg.name)
            break;
            case 'question-stimulus':
            case 'url-stimulus':
				
				lesson.new_stimuli(c_msg.url)
                setTimeout(lesson.new_stimuli_icon, 800, c_msg.id);
				messages(c_msg);
                break;
            case 'Graph':
              console.log("Graph");
                break;
            case 'StartedSolving':
            toastr.info("solving the problem..");
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
            console.log(c_msg.timelines[0] + "horizon");
            localStorage.setItem("horizon",c_msg.timelines[0].horizon);
             lesson.horizon(c_msg.timelines[0].horizon);
             console.log(c_msg)
             timeline(c_msg.timelines[0].values,u)
       break;
            case 'Tick':
            console.log(msg);
            console.log(localStorage.getItem("horizon"));
            	lesson.horizon(localStorage.getItem("horizon"));
               lesson.loading(c_msg.current_time.num);
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

function messages(c_msg){
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
 toastr.info("Ha avuto un nuovo stimolo dalla lezione " + c_msg.name);
 
 document.getElementById("timeline-container").innerHTML += '<div class="uk-timeline-item">'+
            '<div class="uk-timeline-icon"><span class="uk-badge"><span uk-icon="check"></span></span></div>'+
            '<div class="uk-timeline-content">'+
            '<div class="uk-card uk-card-default uk-margin-medium-bottom uk-overflow-auto">'+
            '<div class="uk-card-header">'+
            '<div class="uk-grid-small uk-flex-middle" uk-grid><h3 class="uk-card-title"><time datetime="2020-07-08" style="font-size: 15px;"> Data </time></h3> <span class="uk-label uk-label-success uk-margin-auto-left">Info</span></div></div>'+
            '<div class="uk-card-body"><p class="uk-text-success">"Ha avuto un nuovo stimolo dalla lezione di nome <a href="/lezione/"'+c_msg.lesson_id +'> '+ c_msg.name +'</a>"</p> </div> </div> </div> </div> ';
            console.log("add")
}
function addlesson(c_msg){
	document.getElementById("collapse").innerHTML += '<div class="card card-body" id="side-bar"><a style="font-size: 19px;"href="/lezione/' + c_msg.lessonId + '">' + c_msg.lesson + '</a></div>'
}
